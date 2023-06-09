package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Issue406Test extends AbstractTestCase {
    @Test
    public void testIssue() {
        Exception caught = null;
        try {
            File orig = new File("testdata", "test103.m4a");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = AbstractTestCase.copyAudioToTmp("test103.m4a");
            AudioFile af = AudioFileIO.read(testFile);
            assertEquals(af.getTag().getFirst(FieldKey.TITLE), "London Calling");
            assertEquals(af.getTag().getFirst(FieldKey.ARTIST), "The Clash");
            assertEquals(af.getTag().getFirst(FieldKey.YEAR), "1979");
            af.getTag().setField(FieldKey.TITLE, "Bridport Calling");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals(af.getTag().getFirst(FieldKey.TITLE), "Bridport Calling");
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        assertNull(caught);
    }
}