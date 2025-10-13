package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Issue383Test extends AbstractTestCase {

    /**
     * This song is incorrectly shown as 6:08 when should be 3:34 but all apps (Media Monkey, iTunes)
     * also report incorrect length, however think problem is audio does continue until 6:08 but is just quiet sound
     */
    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testIssueIncorrectTrackLength() {
        Exception caught = null;
        try {
            File testFile = copyAudioToTmp("test106.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            assertEquals(af.getAudioHeader().getTrackLength(), 368);
        } catch (Exception e) {
            caught = e;
        }
        assertNull(caught);
    }

    /**
     * This song is incorrectly shown as 01:12:52, but correct length was 2:24. Other applications
     * such as Media Monkey show correct value.
     */
    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testIssue() {
        Exception caught = null;
        try {
            File testFile = copyAudioToTmp("test107.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            assertEquals(af.getTag().getFirst(FieldKey.TRACK), "01");
            assertEquals(af.getAudioHeader().getTrackLength(), 4372);
        } catch (Exception e) {
            caught = e;
        }
        assertNull(caught);
    }
}
