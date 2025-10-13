package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Issue466Test extends AbstractTestCase {

    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testReadFlac() {
        Exception ex = null;
        try {
            File testFile = copyAudioToTmp("test115.flac");
            AudioFile af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag());
            assertEquals("", af.getTag().getFirst(FieldKey.ALBUM_ARTIST));
        } catch (Exception e) {
            ex = e;
        }
        assertNotNull(ex);
        assertEquals("Flac file has invalid block type 124", ex.getMessage());
    }
}
