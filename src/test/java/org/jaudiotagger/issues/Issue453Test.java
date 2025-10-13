package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue453Test extends AbstractTestCase {

    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testMpeg3layer3_32bit() throws Exception {
        Exception ex = null;

        File testFile = copyAudioToTmp("test113.mp3");
        MP3File mp3File = new MP3File(testFile);
        MP3AudioHeader audio = mp3File.getMP3AudioHeader();
        assertEquals("32", audio.getBitRate());
        assertEquals("Layer 3", audio.getMpegLayer());
        assertEquals("MPEG-1", audio.getMpegVersion());
        assertEquals("Joint Stereo", audio.getChannels());
        assertEquals(1451, audio.getTrackLength()); //This is wrong
    }
}
