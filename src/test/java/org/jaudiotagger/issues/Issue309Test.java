package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Issue309Test extends AbstractTestCase {
    public static int countExceptions = 0;

    @Test
    public void testAddingLargeImageToOgg() {
        File orig = new File("testdata", "test73.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception e = null;
        try {
            final File testFile = AbstractTestCase.copyAudioToTmp("test73.m4a");
            AudioFile af = AudioFileIO.read(testFile);

        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        assertNotNull(e);
    }
}