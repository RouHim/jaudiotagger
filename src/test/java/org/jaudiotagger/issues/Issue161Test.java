package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

public class Issue161Test extends AbstractTestCase {

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testReadID3() {

    Exception ex = null;
    try {
      File testFile = copyAudioToTmp("test159.mp3");
      AudioFile af = AudioFileIO.read(testFile);
      assertNotNull(af.getTag());
      System.out.println(af.getTag());
    } catch (Exception e) {
      ex = e;
    }
    assertNull(ex);
  }
}
