package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

public class Issue454Test extends AbstractTestCase {

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testMpeg3layer2_64bit() throws Exception {
    Exception ex = null;

    File testFile = copyAudioToTmp("test114.mp3");
    MP3File mp3File = new MP3File(testFile);
    MP3AudioHeader audio = mp3File.getMP3AudioHeader();
    assertEquals("64", audio.getBitRate());
    assertEquals("Layer 3", audio.getMpegLayer());
    assertEquals("MPEG-2", audio.getMpegVersion());
    assertEquals("Joint Stereo", audio.getChannels());
    assertEquals(277, audio.getTrackLength());
  }
}
