package org.jaudiotagger.audio.generic;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.NoReadPermissionsException;
import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * Replacement for AudioFileReader class
 */
public abstract class AudioFileReader2 extends AudioFileReader {
    private static final Logger logger = LoggerFactory.getLogger(AudioFileReader2.class);

    /*
     * Reads the given file, and return an AudioFile object containing the Tag
     * and the encoding infos present in the file. If the file has no tag, an
     * empty one is returned. If the encodinginfo is not valid , an exception is thrown.
     *
     * @param f The file to read
     * @exception NoReadPermissionsException if permissions prevent reading of file
     * @exception CannotReadException If anything went bad during the read of this file
     */
    public AudioFile read(File f) throws CannotReadException, IOException {
        if (logger.isDebugEnabled()) {
            logger.debug(ErrorMessage.GENERAL_READ.getMsg(f));
        }

        // Shouldn't be doing these redundant checks, just try to read
        if (!f.canRead()) {
            logger.warn(ErrorMessage.GENERAL_READ_FAILED_DO_NOT_HAVE_PERMISSION_TO_READ_FILE.getMsg(f));
            throw new NoReadPermissionsException(ErrorMessage.GENERAL_READ_FAILED_DO_NOT_HAVE_PERMISSION_TO_READ_FILE.getMsg(f));
        }

        if (f.length() <= MINIMUM_SIZE_FOR_VALID_AUDIO_FILE) {
            throw new CannotReadException(ErrorMessage.GENERAL_READ_FAILED_FILE_TOO_SMALL.getMsg(f));
        }

        try (FileChannel channel = new RandomAccessFile(f, "r").getChannel()) {
            final String absolutePath = f.getAbsolutePath();
            GenericAudioHeader info = getEncodingInfo(channel, absolutePath);
            channel.position(0);
            Tag tag = getTag(channel, absolutePath);
            return new AudioFile(f, info, tag);
        } catch (IllegalArgumentException e) {
            logger.warn(ErrorMessage.GENERAL_READ_FAILED_DO_NOT_HAVE_PERMISSION_TO_READ_FILE.getMsg(f));
            throw new CannotReadException(ErrorMessage.GENERAL_READ_FAILED_DO_NOT_HAVE_PERMISSION_TO_READ_FILE.getMsg(f));
        } catch (FileNotFoundException e) {
            logger.warn("Unable to read file: " + f + " " + e.getMessage());
            throw e;
        }
    }

    /**
     * Read Encoding Information
     *
     * @param channel
     * @param fileName
     * @return
     * @throws CannotReadException
     * @throws IOException
     */
    protected abstract GenericAudioHeader getEncodingInfo(FileChannel channel, final String fileName) throws CannotReadException, IOException;

    protected GenericAudioHeader getEncodingInfo(RandomAccessFile raf) {
        throw new UnsupportedOperationException("Old method not used in version 2");
    }

    /**
     * Read tag Information
     *
     * @param channel
     * @param fileName
     * @return
     * @throws CannotReadException
     * @throws IOException
     */
    protected abstract Tag getTag(FileChannel channel, final String fileName) throws CannotReadException, IOException;

    protected Tag getTag(RandomAccessFile file) {
        throw new UnsupportedOperationException("Old method not used in version 2");
    }
}
