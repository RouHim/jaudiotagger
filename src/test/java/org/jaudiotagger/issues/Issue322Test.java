package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class Issue322Test extends AbstractTestCase {

    /*
     * Test exception thrown
     * @throws Exception
     */

    @Test
    public void testNumberFieldHandling() throws Exception {
        File testFile = copyAudioToTmp("test.m4a");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag();
        Exception expected = null;
        try {
            tag.createField(FieldKey.TRACK_TOTAL, "");
        } catch (Exception e) {
            expected = e;
        }

        assertNotNull(expected);
        assertInstanceOf(FieldDataInvalidException.class, expected);

        expected = null;
        try {
            tag.createField(FieldKey.TRACK_TOTAL, "1");
        } catch (Exception e) {
            expected = e;
        }
        assertNull(expected);
    }
}
