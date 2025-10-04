package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.junit.jupiter.api.Test;

public class Issue66Test extends AbstractTestCase {

  @Test
  public void testIssue() {
    Exception caught = null;
    File orig = new File("testdata", "test118.m4a");

    try {
      if (!orig.isFile()) {
        System.err.println("Unable to test file - not available");
        return;
      }

      File testFile = AbstractTestCase.copyAudioToTmp("test118.m4a");
      AudioFile af = AudioFileIO.read(testFile);
      assertEquals(af.getTag().getFirst(FieldKey.ARTIST), "Shahmen");

      Tag tag = af.getTag();
      if (tag != null) {
        AudioHeader head = af.getAudioHeader();
      }
    } catch (Exception e) {
      caught = e;
      e.printStackTrace();
    }
    assertNull(caught);
  }
}
