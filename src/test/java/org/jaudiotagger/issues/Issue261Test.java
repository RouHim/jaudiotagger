package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.junit.jupiter.api.Test;

public class Issue261Test extends AbstractTestCase {

  /**
   * Test write mp4 ok without any udta/meta atoms
   */
  @Test
  public void testWriteMp4() {
    File orig = new File("testdata", "test45.m4a");
    if (!orig.isFile()) {
      System.err.println("Unable to test file - not available");
      return;
    }

    File testFile = null;
    Exception exceptionCaught = null;
    try {
      testFile = AbstractTestCase.copyAudioToTmp("test45.m4a");

      //Read File okay
      AudioFile af = AudioFileIO.read(testFile);

      //Write file
      af.getTag().setField(FieldKey.YEAR, "2007");
      af.commit();

      af = AudioFileIO.read(testFile);
      assertEquals("2007", af.getTag().getFirst(FieldKey.YEAR));
    } catch (Exception e) {
      e.printStackTrace();
      exceptionCaught = e;
    }

    assertNull(exceptionCaught);
  }
}
