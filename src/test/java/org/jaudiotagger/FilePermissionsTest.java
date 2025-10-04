package org.jaudiotagger;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.*;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagOptionSingleton;

public class FilePermissionsTest {

  public static void runWriteWriteProtectedFileWithCheckDisabled(
    String sourceFile
  ) throws Exception {
    File testFile = createFile(sourceFile);
    try {
      testFile.setWritable(false);
      boolean threwException = false;
      try {
        setFieldAndCommit(testFile, false);
      } catch (NoWritePermissionsException success) {
        threwException = true;
      }
      assertTrue(
        threwException,
        "Expected to throw " +
          NoWritePermissionsException.class.getSimpleName() +
          " but didn't"
      );
    } finally {
      testFile.delete();
    }
  }

  public static void runWriteWriteProtectedFileWithCheckEnabled(
    String sourceFile
  ) throws Exception {
    File testFile = createFile(sourceFile);
    try {
      testFile.setWritable(false);
      boolean threwException = false;
      try {
        setFieldAndCommit(testFile, true);
      } catch (CannotWriteException success) {
        threwException = true;
      }
      assertTrue(
        threwException,
        "Expected to throw " +
          CannotWriteException.class.getSimpleName() +
          " but didn't"
      );
    } finally {
      testFile.delete();
    }
  }

  public static void runWriteReadOnlyFileWithCheckDisabled(String sourceFile)
    throws Exception {
    File testFile = createFile(sourceFile);
    assertTrue(testFile.exists(), "Test file must exist");
    try {
      testFile.setReadOnly();
      boolean threwException = false;
      try {
        setFieldAndCommit(testFile, false);
      } catch (NoWritePermissionsException success) {
        threwException = true;
      }
      assertTrue(
        threwException,
        "Expected to throw " +
          NoWritePermissionsException.class.getSimpleName() +
          " but didn't"
      );
    } finally {
      testFile.delete();
    }
  }

  private static File createFile(String sourceFile) {
    String[] baseNameAndExt = sourceFile.split("\\.(?=[^\\.]+$)");
    File testFile = AbstractTestCase.copyAudioToTmp(
      sourceFile,
      new File(baseNameAndExt[0] + "WriteProtected." + baseNameAndExt[1])
    );
    return testFile;
  }

  private static void setFieldAndCommit(File testFile, boolean performPreCheck)
    throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException, CannotWriteException {
    TagOptionSingleton.getInstance().setCheckIsWritable(performPreCheck);
    try {
      AudioFile aFile = AudioFileIO.read(testFile);
      Tag tag = aFile.getTag();
      tag.setField(FieldKey.ALBUM, "album");
      aFile.commit();
    } finally {
      testFile.setWritable(true);
      TagOptionSingleton.getInstance().setToDefault();
    }
  }
}
