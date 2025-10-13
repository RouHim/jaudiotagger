package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class FileClosingTest extends AbstractTestCase {

    /**
     * This tests checks files are closed after reading attempt
     */
    @Test
    public void testClosingFileAfterFailedRead() {
        Exception exception = null;
        File testFile = copyAudioToTmp("corrupt.mp3");

        //Try and Read
        try {
            MP3File mp3File = new MP3File(testFile);
        } catch (Exception e) {
            exception = e;
        }

        //Error Should have occured
        assertNotNull(exception);

        //Should be able to deleteField
        boolean deleted = testFile.delete();
        assertTrue(deleted);
    }

    /**
     * This tests checks files are closed after succesful reading attempt
     */
    @Test
    public void testClosingFileAfterSuccessfulRead() {
        Exception exception = null;
        File testFile = copyAudioToTmp("testV1.mp3");

        //Try and Read
        try {
            MP3File mp3File = new MP3File(testFile);
        } catch (Exception e) {
            exception = e;
        }

        //No Error Should have occured
        assertNull(exception);

        //Should be able to deleteField
        boolean deleted = testFile.delete();
        assertTrue(deleted);
    }

    /**
     * This tests checks files are closed after failed reading attempt (read only)
     */
    @Test
    public void testClosingFileAfterFailedReadOnly() {
        Exception exception = null;
        File testFile = copyAudioToTmp("testV1.mp3");

        boolean readonly = testFile.setReadOnly();
        assertTrue(readonly);

        //Try and Read
        try {
            MP3File mp3File = new MP3File(testFile);
        } catch (Exception e) {
            exception = e;
        }

        //Error Should have occured
        assertNotNull(exception);

        //Should be able to deleteField
        testFile.setWritable(true); // needs to be writeable to be deletable
        boolean deleted = testFile.delete();
        assertTrue(deleted);
    }
}
