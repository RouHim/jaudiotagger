package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

public class Issue484Test extends AbstractTestCase {

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testReadUTF16WithMissingBOM() {

    Exception ex = null;
    try {
      File testFile = copyAudioToTmp("test140.mp3");
      AudioFile af = AudioFileIO.read(testFile);
      assertNotNull(af.getTag());
      System.out.println(af.getTag());
      assertEquals("1968", (af.getTag().getFirst(FieldKey.YEAR)));
    } catch (Exception e) {
      ex = e;
    }
    assertNull(ex);
  }
}
