package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.flac.FlacTag;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class Issue468Test extends AbstractTestCase {

    @Test
    public void testReadFlac() {
        Exception ex = null;
        try {
            File testFile = copyAudioToTmp("test.flac");
            AudioFile af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag());
            FlacTag tag = (FlacTag) af.getTag();
            tag.setField(tag.createArtworkField(null, 1, "", "", 100, 200, 128, 1));
            af.commit();
        } catch (Exception e) {
            ex = e;
        }
        assertNotNull(ex);
        assertInstanceOf(FieldDataInvalidException.class, ex);
        assertEquals("ImageData cannot be null", ex.getMessage());
    }
}
