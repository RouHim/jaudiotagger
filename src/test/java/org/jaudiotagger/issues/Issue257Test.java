package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Issue257Test extends AbstractTestCase {
    /**
     * Test Mp4 with crap between free atom and mdat atom, shoud cause immediate failure
     */
    @Test
    public void testReadMp4FileWithPaddingAfterLastAtom() {
        File orig = new File("testdata", "test37.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = AbstractTestCase.copyAudioToTmp("test37.m4a");

            //Read File
            AudioFile af = AudioFileIO.read(testFile);

            //Print Out Tree

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertTrue(exceptionCaught instanceof CannotReadException);
    }
}
