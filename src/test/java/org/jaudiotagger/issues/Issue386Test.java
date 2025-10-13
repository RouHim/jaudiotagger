package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNull;

public class Issue386Test extends AbstractTestCase {

    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testIssue() {
        Exception caught = null;
        try {
            File testFile = copyAudioToTmp("test99.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            System.out.println(af.getAudioHeader());
        } catch (Exception e) {
            caught = e;
        }
        assertNull(caught);
    }
}
