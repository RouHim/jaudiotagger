package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.junit.jupiter.api.Test;

public class Issue380Test extends AbstractTestCase {

  @Test
  public void testIssue() {
    Exception caught = null;
    try {
      File orig = new File("testdata", "test98.m4a");
      if (!orig.isFile()) {
        System.err.println("Unable to test file - not available");
        return;
      }

      File testFile = AbstractTestCase.copyAudioToTmp("test98.m4a");
      AudioFile af = AudioFileIO.read(testFile);
      assertEquals("", af.getTag().getFirst(FieldKey.TRACK_TOTAL));
    } catch (Exception e) {
      caught = e;
      e.printStackTrace();
    }
    assertNull(caught);
  }
}
