package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.junit.jupiter.api.Test;

public class Issue484Test extends AbstractTestCase {

  @Test
  public void testReadUTF16WithMissingBOM() {
    File orig = new File("testdata", "test140.mp3");
    if (!orig.isFile()) {
      System.err.println("Unable to test file - not available");
      return;
    }

    Exception ex = null;
    try {
      File testFile = AbstractTestCase.copyAudioToTmp("test140.mp3");
      AudioFile af = AudioFileIO.read(testFile);
      assertNotNull(af.getTag());
      System.out.println(af.getTag());
      assertEquals("1968", (af.getTag().getFirst(FieldKey.YEAR)));
    } catch (Exception e) {
      e.printStackTrace();
      ex = e;
    }
    assertNull(ex);
  }
}
