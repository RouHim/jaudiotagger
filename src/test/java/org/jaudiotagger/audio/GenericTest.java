package org.jaudiotagger.audio;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.jaudiotagger.AbstractBaseTestCase;
import org.junit.jupiter.api.Test;

public class GenericTest extends AbstractBaseTestCase {

  /**
   * Test File filter, postive and negative tests
   */
  @Test
  public void testReadFileUnsupportedFormat() {
    File nonAudioFile = fileResource("testdata", "coverart.bmp");
    AudioFileFilter aff = new AudioFileFilter();
    aff.accept(nonAudioFile);
    assertFalse(aff.accept(nonAudioFile));

    File audioFile = fileResource("testdata", "test.m4a");
    aff.accept(audioFile);
    assertTrue(aff.accept(audioFile));

    audioFile = fileResource("testdata", "test.flac");
    aff.accept(audioFile);
    assertTrue(aff.accept(audioFile));

    audioFile = fileResource("testdata", "test.ogg");
    aff.accept(audioFile);
    assertTrue(aff.accept(audioFile));

    audioFile = fileResource("testdata", "testV1.mp3");
    aff.accept(audioFile);
    assertTrue(aff.accept(audioFile));
  }
}
