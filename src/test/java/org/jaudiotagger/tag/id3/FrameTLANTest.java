package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FrameTLANTest extends AbstractTestCase {
    @Test
    public void testWriteFileContainingTLANFrame() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("Issue116.id3", "testV1.mp3");

        MP3File mp3File = new MP3File(testFile);
        mp3File.save();
    }
}
