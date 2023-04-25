package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class Issue481Test extends AbstractTestCase {
    @Test
    public void testReadYear() {
        File orig = new File("testdata", "test139.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test139.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag());
            System.out.println(af.getTag());
            assertEquals("2005", (af.getTag().getFirst(FieldKey.YEAR)));
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        assertNull(ex);
    }
}
