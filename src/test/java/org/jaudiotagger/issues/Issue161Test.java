package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.junit.jupiter.api.Test;

public class Issue161Test extends AbstractTestCase {

  @Test
  public void testReadID3() {
    File orig = new File("testdata", "test159.mp3");
    if (!orig.isFile()) {
      System.err.println("Unable to test file - not available");
      return;
    }

    Exception ex = null;
    try {
      File testFile = AbstractTestCase.copyAudioToTmp("test159.mp3");
      AudioFile af = AudioFileIO.read(testFile);
      assertNotNull(af.getTag());
      System.out.println(af.getTag());
    } catch (Exception e) {
      e.printStackTrace();
      ex = e;
    }
    assertNull(ex);
  }
}
