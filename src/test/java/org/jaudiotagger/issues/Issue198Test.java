package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jcodec.containers.mp4.MP4Util;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNull;

public class Issue198Test extends AbstractTestCase {

    @Test
    public void testIssue() {
        Exception caught = null;
        try {
            File testFile = copyAudioToTmp("issue-198.m4a");
            AudioFile af = AudioFileIO.read(testFile);
            System.out.println(af.getAudioHeader());
            af.getTagOrCreateAndSetDefault();
            af.commit();

            MP4Util.Movie mp4 = MP4Util.parseFullMovie(testFile);
            String json = new JSONObject(mp4.moov().toString()).toString(2);
            System.out.println(json);
        } catch (Exception e) {
            caught = e;
        }
        assertNull(caught);
    }
}
