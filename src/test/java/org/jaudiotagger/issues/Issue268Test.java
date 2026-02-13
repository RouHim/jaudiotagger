package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

public class Issue268Test extends AbstractTestCase {

  /**
   * Test read wma with NonArtwork Binary Data
   */
  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testReadWma() {

    File testFile = null;
    Exception exceptionCaught = null;
    try {
      testFile = copyAudioToTmp("test8.wma");

      //Read File okay
      AudioFile af = AudioFileIO.read(testFile);
      System.out.println(af.getTag().toString());

      af.getTag().setField(FieldKey.ALBUM, "FRED");
      af.commit();
      af = AudioFileIO.read(testFile);
      System.out.println(af.getTag().toString());
      assertEquals("FRED", af.getTag().getFirst(FieldKey.ALBUM));
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertNull(exceptionCaught);
  }
}
