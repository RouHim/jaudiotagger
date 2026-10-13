package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

public class Issue386Test extends AbstractTestCase {

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testIssue() {
    Exception caught = null;
    try {
      File testFile = copyAudioToTmp("test99.mp3");
      AudioFile af = AudioFileIO.read(testFile);
      System.out.println(af.getAudioHeader());
    } catch (Exception e) {
      caught = e;
    }
    assertNull(caught);
  }
}
