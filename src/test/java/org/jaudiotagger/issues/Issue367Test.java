package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

public class Issue367Test extends AbstractTestCase {

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testIssue() {
    Exception caught = null;
    try {
      File testFile = copyAudioToTmp("test93.mp3");
      long startTime = System.nanoTime();
      AudioFile af = AudioFileIO.read(testFile);
      long endTime = System.nanoTime();
      double totalTime = (endTime - startTime) / 1000000.0;
      System.out.println("Time:" + totalTime + ":ms");
    } catch (Exception e) {
      caught = e;
    }
    assertNull(caught);
  }
}
