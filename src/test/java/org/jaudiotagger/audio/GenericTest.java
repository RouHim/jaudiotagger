package org.jaudiotagger.audio;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.junit.jupiter.api.Test;

public class GenericTest {

  /**
   * Test File filter, postive and negative tests
   */
  @Test
  public void testReadFileUnsupportedFormat() {
    File nonAudioFile = new File("testdata", "coverart.bmp");
    AudioFileFilter aff = new AudioFileFilter();
    aff.accept(nonAudioFile);
    assertFalse(aff.accept(nonAudioFile));

    File audioFile = new File("testdata", "test.m4a");
    aff.accept(audioFile);
    assertTrue(aff.accept(audioFile));

    audioFile = new File("testdata", "test.flac");
    aff.accept(audioFile);
    assertTrue(aff.accept(audioFile));

    audioFile = new File("testdata", "test.ogg");
    aff.accept(audioFile);
    assertTrue(aff.accept(audioFile));

    audioFile = new File("testdata", "testV1.mp3");
    aff.accept(audioFile);
    assertTrue(aff.accept(audioFile));
  }
}
