package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.junit.jupiter.api.Test;

public class Issue437Test extends AbstractTestCase {

  @Test
  public void testGetBitsPerSampleFlac() {
    Throwable e = null;
    try {
      File testFile = copyAudioToTmp("test.flac");
      AudioFile af = AudioFileIO.read(testFile);
      assertEquals(16, af.getAudioHeader().getBitsPerSample());
    } catch (Exception ex) {
      e = ex;
    }
    assertNull(e);
  }

  @Test
  public void testGetBitsPerSampleMp4() {
    Throwable e = null;
    try {
      File testFile = copyAudioToTmp("test.m4a");
      AudioFile af = AudioFileIO.read(testFile);
      assertEquals(16, af.getAudioHeader().getBitsPerSample());
    } catch (Exception ex) {
      e = ex;
    }
    assertNull(e);
  }

  @Test
  public void testGetBitsPerSampleOgg() {
    Throwable e = null;
    try {
      File testFile = copyAudioToTmp("test.ogg");
      AudioFile af = AudioFileIO.read(testFile);
      assertEquals(16, af.getAudioHeader().getBitsPerSample());
    } catch (Exception ex) {
      e = ex;
    }
    assertNull(e);
  }

  @Test
  public void testGetBitsPerSampleWma() {
    Throwable e = null;
    try {
      File testFile = copyAudioToTmp("test1.wma");
      AudioFile af = AudioFileIO.read(testFile);
      assertEquals(16, af.getAudioHeader().getBitsPerSample());
    } catch (Exception ex) {
      e = ex;
    }
    assertNull(e);
  }

  @Test
  public void testGetBitsPerSampleMp3() {
    Throwable e = null;
    try {
      File testFile = copyAudioToTmp(
        "testV1.mp3",
        "testGetBitsPerSampleMp3.mp3"
      );
      AudioFile af = AudioFileIO.read(testFile);
      assertEquals(16, af.getAudioHeader().getBitsPerSample());
    } catch (Exception ex) {
      e = ex;
    }
    assertNull(e);
  }
}
