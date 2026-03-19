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
import org.junit.jupiter.api.condition.EnabledIf;

public class Issue66Test extends AbstractTestCase {

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testIssue() {
    Exception caught = null;
    try {

      File testFile = copyAudioToTmp("test118.m4a");
      AudioFile af = AudioFileIO.read(testFile);
      assertEquals(af.getTag().getFirst(FieldKey.ARTIST), "Shahmen");

      Tag tag = af.getTag();
      if (tag != null) {
        AudioHeader head = af.getAudioHeader();
      }
    } catch (Exception e) {
      caught = e;
    }
    assertNull(caught);
  }
}
