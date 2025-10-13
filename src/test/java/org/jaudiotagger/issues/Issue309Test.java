package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Issue309Test extends AbstractTestCase {

    public static int countExceptions = 0;

    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testAddingLargeImageToOgg() {

        Exception e = null;
        try {
            final File testFile = copyAudioToTmp("test73.m4a");
            AudioFile af = AudioFileIO.read(testFile);
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        assertNotNull(e);
    }
}
