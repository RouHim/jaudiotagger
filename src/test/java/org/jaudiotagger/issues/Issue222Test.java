package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.junit.jupiter.api.Test;

public class Issue222Test extends AbstractTestCase {

  /**
   * Test read mp4 with meta but not udata atom
   */
  @Test
  public void testreadMp4WithoutUUuidButNoUdta() {

    File testFile = null;
    Exception exceptionCaught = null;
    try {
      testFile = copyAudioToTmp("test4.m4a");

      //Read File okay
      AudioFile af = AudioFileIO.read(testFile);
      assertTrue(af.getTag().isEmpty()); //But empty
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertNull(exceptionCaught);
  }
}
