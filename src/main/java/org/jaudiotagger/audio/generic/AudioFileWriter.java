/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Raphaël Slinckx <raphael@slinckx.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jaudiotagger.audio.generic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.ModifyVetoException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This abstract class is the skeleton for tag writers.
 * <p/>
 * <p/>
 * It handles the creation/closing of the randomaccessfile objects and then call
 * the subclass method writeTag or deleteTag. These two method have to be
 * implemented in the subclass.
 *
 * @author Raphael Slinckx
 * @version $Id: AudioFileWriter.java,v 1.21 2009/05/05 15:59:14 paultaylor Exp
 * $
 * @since v0.02
 */
public abstract class AudioFileWriter {

  private static final String TEMP_FILENAME_SUFFIX = ".tmp";
  private static final String WRITE_MODE = "rw";
  protected static final int MINIMUM_FILESIZE = 100;
  private static final Logger logger = LoggerFactory.getLogger(
    "org.jaudiotagger.audio.generic"
  );

  /**
   * If not <code>null</code>, this listener is used to notify the listener
   * about modification events.<br>
   */
  private AudioFileModificationListener modificationListener = null;

  /**
   * Delete the tag (if any) present in the given file
   *
   * @param af The file to process
   * @throws CannotWriteException if anything went wrong
   * @throws CannotReadException
   */
  public void delete(AudioFile af)
    throws CannotReadException, CannotWriteException {
    final File file = af.getFile();

    // Fix the logic: only throw if file is NOT writable (the original had this backwards)
    if (
      TagOptionSingleton.getInstance().isCheckIsWritable() && !file.canWrite()
    ) {
      logger.error("Unable to write file: {}", file);
      throw new CannotWriteException(
        ErrorMessage.GENERAL_DELETE_FAILED.getMsg(file)
      );
    }

    if (file.length() <= MINIMUM_FILESIZE) {
      throw new CannotWriteException(
        ErrorMessage.GENERAL_DELETE_FAILED_BECAUSE_FILE_IS_TOO_SMALL.getMsg(
          file
        )
      );
    }

    File tempFile = createTempFile(file);

    try {
      // Process the tag deletion with modern resource management
      processTagDeletion(af, tempFile);

      // Handle the file replacement if needed
      if (tempFile.length() > 0) {
        replaceOriginalWithTemp(file, tempFile);
      } else {
        Files.deleteIfExists(tempFile.toPath());
      }

      // Notify listener
      if (modificationListener != null) {
        modificationListener.fileOperationFinished(file);
      }
    } catch (ModifyVetoException veto) {
      deleteQuietly(tempFile);
      throw new CannotWriteException(veto);
    } catch (Exception e) {
      deleteQuietly(tempFile);
      throw new CannotWriteException(
        "Failed to delete tag from " + file.getAbsolutePath(),
        e
      );
    }
  }

  private File createTempFile(File originalFile) throws CannotWriteException {
    try {
      // Create temp file in system temp directory with recognizable name
      String baseName = originalFile.getName().replace('.', '_');
      return File.createTempFile(baseName, TEMP_FILENAME_SUFFIX);
    } catch (IOException e) {
      throw new CannotWriteException("Failed to create temporary file", e);
    }
  }

  private void processTagDeletion(AudioFile af, File tempFile)
    throws IOException, ModifyVetoException, CannotReadException, CannotWriteException {
    try (
      var raf = new RandomAccessFile(af.getFile(), WRITE_MODE);
      var rafTemp = new RandomAccessFile(tempFile, WRITE_MODE)
    ) {
      raf.seek(0);
      rafTemp.seek(0);

      if (modificationListener != null) {
        modificationListener.fileWillBeModified(af, true);
      }

      deleteTag(af.getTag(), raf, rafTemp);

      if (modificationListener != null) {
        modificationListener.fileModified(af, tempFile);
      }
    }
  }

  private void replaceOriginalWithTemp(File originalFile, File tempFile)
    throws CannotWriteException {
    Path originalPath = originalFile.toPath();
    Path tempPath = tempFile.toPath();

    // Force a GC run to help release any file handles
    System.gc();

    // Try to replace with retries
    final int maxAttempts = 3;
    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
      try {
        // Atomically replace original with temp file
        Files.move(tempPath, originalPath, StandardCopyOption.REPLACE_EXISTING);
        return; // Success
      } catch (IOException e) {
        if (attempt == maxAttempts) {
          // Last attempt failed, try fallback approach if it's a locked file
          if (
            e instanceof FileSystemException &&
            e.getMessage().contains("used by another process")
          ) {
            tryFallbackCopy(tempPath, originalPath);
            return; // Success with fallback
          }
          throw new CannotWriteException(
            "Failed to replace original file with modified version",
            e
          );
        }

        // Wait briefly before retrying
        try {
          Thread.sleep(100 * attempt);
        } catch (InterruptedException ignored) {
          Thread.currentThread().interrupt();
        }
      }
    }
  }

  private void tryFallbackCopy(Path tempPath, Path originalPath)
    throws CannotWriteException {
    try {
      // Fallback: copy content and delete temp
      Files.copy(tempPath, originalPath, StandardCopyOption.REPLACE_EXISTING);
      Files.deleteIfExists(tempPath);
    } catch (IOException fallbackEx) {
      throw new CannotWriteException(
        "Failed even with fallback copy approach",
        fallbackEx
      );
    }
  }

  /**
   * Delete the tag (if any) present in the given randomaccessfile, and do not
   * close it at the end.
   *
   * @param tag
   * @param raf     The source file, already opened in r-write mode
   * @param tempRaf The temporary file opened in r-write mode
   * @throws CannotWriteException                                  if anything went wrong
   * @throws org.jaudiotagger.audio.exceptions.CannotReadException
   * @throws java.io.IOException
   */
  public void delete(Tag tag, RandomAccessFile raf, RandomAccessFile tempRaf)
    throws CannotReadException, CannotWriteException, IOException {
    raf.seek(0);
    tempRaf.seek(0);
    deleteTag(tag, raf, tempRaf);
  }

  /**
   * Same as above, but delete tag in the file.
   *
   * @param tag
   * @param raf
   * @throws IOException                                           is thrown when the RandomAccessFile operations throw it (you
   *                                                               should never throw them manually)
   * @throws CannotWriteException                                  when an error occured during the deletion of the tag
   * @throws org.jaudiotagger.audio.exceptions.CannotReadException
   */
  protected abstract void deleteTag(
    Tag tag,
    RandomAccessFile raf,
    RandomAccessFile tempRaf
  ) throws CannotReadException, CannotWriteException, IOException;

  /**
   * This method sets the {@link AudioFileModificationListener}.<br>
   * There is only one listener allowed, if you want more instances to be
   * supported, use the {@link ModificationHandler} to broadcast those events.<br>
   *
   * @param listener The listener. <code>null</code> allowed to deregister.
   */
  public void setAudioFileModificationListener(
    AudioFileModificationListener listener
  ) {
    this.modificationListener = listener;
  }

  /**
   * Write the tag (if not empty) present in the AudioFile in the associated
   * File
   *
   * @param audioFile The file we want to process
   * @throws CannotWriteException if anything went wrong
   */
  public void write(AudioFile audioFile) throws CannotWriteException {
    // MP3 files use a different mechanism
    if (audioFile instanceof MP3File) {
      audioFile.commit();
      return;
    }

    File originalFile = audioFile.getFile();
    File tempFile = createTemporaryFile(originalFile);

    // Write data to files using try-with-resources
    try (
      RandomAccessFile raf = new RandomAccessFile(originalFile, WRITE_MODE);
      RandomAccessFile rafTemp = new RandomAccessFile(tempFile, WRITE_MODE)
    ) {
      raf.seek(0);
      rafTemp.seek(0);

      if (modificationListener != null) {
        modificationListener.fileWillBeModified(audioFile, false);
      }

      writeTag(audioFile, audioFile.getTag(), raf, rafTemp);

      if (modificationListener != null) {
        modificationListener.fileModified(audioFile, tempFile);
      }
    } catch (ModifyVetoException veto) {
      deleteQuietly(tempFile);
      throw new CannotWriteException(veto);
    } catch (IOException e) {
      deleteQuietly(tempFile);
      logger.error(
        ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(
          originalFile.getAbsolutePath()
        ),
        e
      );
      throw new CannotWriteException(
        ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(
          originalFile.getAbsolutePath()
        )
      );
    } catch (Exception e) {
      deleteQuietly(tempFile);
      logger.error(
        ErrorMessage.GENERAL_WRITE_FAILED_BECAUSE.getMsg(
          originalFile,
          e.getMessage()
        ),
        e
      );
      throw new CannotWriteException(
        ErrorMessage.GENERAL_WRITE_FAILED_BECAUSE.getMsg(
          originalFile,
          e.getMessage()
        ),
        e
      );
    }

    // Check if the temporary file is empty
    // If it is not empty, transfer the new file to the original file
    if (tempFile.length() > 0) {
      transferNewFileToOriginalFile(
        tempFile,
        originalFile,
        TagOptionSingleton.getInstance().isPreserveFileIdentity()
      );
    } else {
      deleteQuietly(tempFile);
    }

    if (modificationListener != null) {
      modificationListener.fileOperationFinished(originalFile);
    }
  }

  /**
   * Creates a temporary file for writing.
   */
  private File createTemporaryFile(File originalFile)
    throws CannotWriteException {
    try {
      String baseName = originalFile.getName().replace('.', '_');
      return File.createTempFile(baseName, TEMP_FILENAME_SUFFIX);
    } catch (IOException e) {
      String errorMsg =
        ErrorMessage.GENERAL_WRITE_FAILED_TO_CREATE_TEMPORARY_FILE_IN_FOLDER.getMsg(
          originalFile.getName(),
          System.getProperty("java.io.tmpdir")
        );
      logger.error(errorMsg, e);
      throw new CannotWriteException(errorMsg);
    }
  }

  /**
   * Quietly deletes a file, logging a warning if deletion fails.
   */
  private void deleteQuietly(File file) {
    try {
      if (file != null && file.exists()) {
        Files.deleteIfExists(file.toPath());
      }
    } catch (IOException e) {
      logger.warn(
        ErrorMessage.GENERAL_WRITE_FAILED_TO_DELETE_TEMPORARY_FILE.getMsg(
          file.getAbsolutePath()
        )
      );
    }
  }

  /**
   * <p>
   * Transfers the content from {@code newFile} to a file named {@code originalFile}.
   * With regards to file identity (inode/<a href="https://msdn.microsoft.com/en-us/library/aa363788(v=vs.85).aspx">fileIndex</a>),
   * after execution, {@code originalFile} may be a completely new file or the same file as before execution, depending
   * on {@code reuseExistingOriginalFile}.
   * </p>
   * <p>
   * Reusing the existing file may be slower, if both the temp file and the original file are located
   * in the same filesystem, because an actual copy is created instead of just a file rename.
   * If both files are on different filesystems, a copy is always needed — regardless of which method is used.
   * </p>
   *
   * @param newFile                   new file
   * @param originalFile              original file
   * @param reuseExistingOriginalFile {@code true} or {@code false}
   * @throws CannotWriteException If the file cannot be written
   */
  private void transferNewFileToOriginalFile(
    final File newFile,
    final File originalFile,
    final boolean reuseExistingOriginalFile
  ) throws CannotWriteException {
    if (reuseExistingOriginalFile) {
      transferNewFileContentToOriginalFile(newFile, originalFile);
    } else {
      transferNewFileToNewOriginalFile(newFile, originalFile);
    }
  }

  /**
   * <p>
   * Writes the contents of the given {@code newFile} to the given {@code originalFile},
   * overwriting the already existing content in {@code originalFile}.
   * This ensures that the file denoted by the abstract pathname {@code originalFile}
   * keeps the same Unix inode or Windows
   * <a href="https://msdn.microsoft.com/en-us/library/aa363788(v=vs.85).aspx">fileIndex</a>.
   * </p>
   * <p>
   * If no errors occur, the method follows this approach:
   * </p>
   * <ol>
   * <li>Rename <code>originalFile</code> to <code>originalFile.old</code></li>
   * <li>Rename <code>newFile</code> to <code>originalFile</code> (this implies a file identity change for <code>originalFile</code>)</li>
   * <li>Delete <code>originalFile.old</code></li>
   * <li>Delete <code>newFile</code></li>
   * </ol>
   *
   * @param newFile      File containing the data we want in the {@code originalFile}
   * @param originalFile Before execution this denotes the original, unmodified file.
   *                     After execution it denotes the name of the file with the modified content and new inode/fileIndex.
   * @throws CannotWriteException if the file cannot be written
   */
  private void transferNewFileContentToOriginalFile(
    final File newFile,
    final File originalFile
  ) throws CannotWriteException {
    try (
      var raf = new RandomAccessFile(originalFile, "rw");
      var outChannel = raf.getChannel()
    ) {
      // Try to obtain lock with try-with-resources
      try (var lock = outChannel.tryLock()) {
        if (lock != null) {
          // We have a lock, transfer the file content
          copyFileContent(newFile, raf, outChannel);
        } else {
          // Failed to get lock
          logger.warn(
            ErrorMessage.GENERAL_WRITE_FAILED_FILE_LOCKED.getMsg(
              originalFile.getPath()
            )
          );
          throw new CannotWriteException(
            ErrorMessage.GENERAL_WRITE_FAILED_FILE_LOCKED.getMsg(
              originalFile.getPath()
            )
          );
        }
      } catch (IOException e) {
        // Special case for network shares where locking isn't supported
        if ("Operation not supported".equals(e.getMessage())) {
          logger.info(
            "File locking not supported for {}, proceeding without lock",
            originalFile.getPath()
          );
          copyFileContent(newFile, raf, outChannel);
        } else {
          throw new CannotWriteException(
            ErrorMessage.GENERAL_WRITE_FAILED_FILE_LOCKED.getMsg(
              originalFile.getPath()
            ),
            e
          );
        }
      }

      // Clean up temporary file as it's no longer needed
      if (newFile.exists() && !newFile.delete()) {
        logger.warn(
          ErrorMessage.GENERAL_WRITE_FAILED_TO_DELETE_TEMPORARY_FILE.getMsg(
            newFile.getPath()
          )
        );
      }
    } catch (FileNotFoundException e) {
      throw new CannotWriteException(
        ErrorMessage.GENERAL_WRITE_FAILED_BECAUSE_FILE_NOT_FOUND.getMsg(
          originalFile.getPath()
        ),
        e
      );
    } catch (IOException e) {
      throw new CannotWriteException(
        ErrorMessage.GENERAL_WRITE_FAILED.getMsg(originalFile.getPath()),
        e
      );
    }
  }

  /**
   * Copies content from temporary file to the destination file
   */
  private void copyFileContent(
    File sourceFile,
    RandomAccessFile destRaf,
    FileChannel destChannel
  ) throws IOException {
    try (
      var inChannel = FileChannel.open(
        sourceFile.toPath(),
        StandardOpenOption.READ
      )
    ) {
      // Transfer content in chunks
      long position = 0;
      long size = inChannel.size();

      while (position < size) {
        position += inChannel.transferTo(position, 1024L * 1024L, destChannel);
      }

      // Truncate if original was longer
      destRaf.setLength(size);
    }
  }

  /**
   * <p>
   * Replaces the original file with the new file in a way that changes the file identity.
   * In other words, the Unix inode or the Windows
   * <a href="https://msdn.microsoft.com/en-us/library/aa363788(v=vs.85).aspx">fileIndex</a>
   * of the resulting file with the name {@code originalFile} is not identical to the inode/fileIndex
   * of the file named {@code originalFile} before this method was called.
   * </p>
   * <p>
   * If no errors occur, the method follows this approach:
   * </p>
   * <ol>
   * <li>Rename <code>originalFile</code> to <code>originalFile.old</code></li>
   * <li>Rename <code>newFile</code> to <code>originalFile</code> (this implies a file identity change for <code>originalFile</code>)</li>
   * <li>Delete <code>originalFile.old</code></li>
   * <li>Delete <code>newFile</code></li>
   * </ol>
   *
   * @param newFile      File containing the data we want in the {@code originalFile}
   * @param originalFile Before execution this denotes the original, unmodified file.
   *                     After execution it denotes the name of the file with the modified content and new inode/fileIndex.
   * @throws CannotWriteException if the file cannot be written
   */
  /**
   * Replaces the original file with the new file, changing the file identity.
   *
   * @param newFile      File containing the new data
   * @param originalFile Original file to be replaced
   * @throws CannotWriteException if the file cannot be written
   */
  private void transferNewFileToNewOriginalFile(
    final File newFile,
    final File originalFile
  ) throws CannotWriteException {
    // Create paths
    Path originalPath = originalFile.toPath();
    Path newPath = newFile.toPath();
    Path parentPath = originalPath.getParent();
    String baseFilename = AudioFile.getBaseFilename(originalFile);

    // Create unique backup file path
    Path backupPath = findUniqueBackupPath(parentPath, baseFilename);

    try {
      // Move original file to backup
      Files.move(originalPath, backupPath);

      // Move new file to original location
      Files.move(newPath, originalPath);

      // Delete backup file (not critical if it fails)
      try {
        Files.deleteIfExists(backupPath);
      } catch (IOException e) {
        logger.warn(
          ErrorMessage.GENERAL_WRITE_WARNING_UNABLE_TO_DELETE_BACKUP_FILE.getMsg(
            backupPath.toString()
          )
        );
      }
    } catch (IOException e) {
      // If rename failed, try to restore original state
      restoreOriginalState(originalPath, newPath, backupPath);
      throw new CannotWriteException(
        ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_TO_ORIGINAL_FILE.getMsg(
          originalPath.toString(),
          newPath.toString()
        ),
        e
      );
    }
  }

  /**
   * Creates a unique backup file path
   */
  private Path findUniqueBackupPath(Path parentPath, String baseFilename) {
    Path backupPath = parentPath.resolve(baseFilename + ".old");
    int count = 1;

    while (Files.exists(backupPath)) {
      backupPath = parentPath.resolve(baseFilename + ".old" + count++);
    }

    return backupPath;
  }

  /**
   * Attempts to restore original state after a failure
   */
  private void restoreOriginalState(
    Path originalPath,
    Path newPath,
    Path backupPath
  ) {
    try {
      // Try to restore original file from backup
      if (Files.exists(backupPath)) {
        Files.move(
          backupPath,
          originalPath,
          StandardCopyOption.REPLACE_EXISTING
        );
      }

      // Clean up the new file
      Files.deleteIfExists(newPath);
    } catch (IOException e) {
      logger.warn(
        ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_ORIGINAL_BACKUP_TO_ORIGINAL.getMsg(
          backupPath.toString(),
          originalPath.toString()
        )
      );
    }
  }

  /**
   * This is called when a tag has to be written in a file. Three parameters
   * are provided, the tag to write (not empty) Two randomaccessfiles, the
   * first points to the file where we want to write the given tag, and the
   * second is an empty temporary file that can be used if e.g. the file has
   * to be bigger than the original.
   * <p/>
   * If something has been written in the temporary file, when this method
   * returns, the original file is deleted, and the temporary file is renamed
   * the the original name
   * <p/>
   * If nothing has been written to it, it is simply deleted.
   * <p/>
   * This method can assume the raf, rafTemp are pointing to the first byte of
   * the file. The subclass must not close these two files when the method
   * returns.
   *
   * @param audioFile The file we want to process
   * @param tag
   * @param raf
   * @throws IOException                                           is thrown when the RandomAccessFile operations throw it (you
   *                                                               should never throw them manually)
   * @throws CannotWriteException                                  when an error occured during the generation of the tag
   * @throws org.jaudiotagger.audio.exceptions.CannotReadException
   */
  protected abstract void writeTag(
    AudioFile audioFile,
    Tag tag,
    RandomAccessFile raf,
    RandomAccessFile rafTemp
  ) throws CannotReadException, CannotWriteException, IOException;
}
