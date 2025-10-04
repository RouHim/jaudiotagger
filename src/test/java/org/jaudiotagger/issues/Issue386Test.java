package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.junit.jupiter.api.Test;

public class Issue386Test extends AbstractTestCase {

  @Test
  public void testIssue() {
    Exception caught = null;
    try {
      File orig = new File("testdata", "test99.mp3");
      if (!orig.isFile()) {
        System.err.println("Unable to test file - not available");
        return;
      }

      File testFile = AbstractTestCase.copyAudioToTmp("test99.mp3");
      AudioFile af = AudioFileIO.read(testFile);
      System.out.println(af.getAudioHeader());
    } catch (Exception e) {
      caught = e;
      e.printStackTrace();
    }
    assertNull(caught);
  }
}
