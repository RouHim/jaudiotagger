package org.jaudiotagger.tag.mp4;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

public class ReadMp4MovementTagsTest extends AbstractTestCase {

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testReadMovementFieldsFromITunes() {

    Exception ex = null;
    try {
      File testFile = copyAudioToTmp("test161.m4a");
      AudioFile af = AudioFileIO.read(testFile);
      assertNotNull(af.getTag());
      assertEquals(
        "I. Preludium (Pastorale). Allegro moderato",
        af.getTag().getFirst(FieldKey.MOVEMENT)
      );
      assertEquals("2", af.getTag().getFirst(FieldKey.MOVEMENT_NO));
      assertEquals("0", af.getTag().getFirst(FieldKey.MOVEMENT_TOTAL));

      af.getTag().setField(FieldKey.MOVEMENT, "fred");
      af.getTag().setField(FieldKey.MOVEMENT_NO, "1");
      af.getTag().setField(FieldKey.MOVEMENT_TOTAL, "7");

      assertEquals("fred", af.getTag().getFirst(FieldKey.MOVEMENT));
      assertEquals("1", af.getTag().getFirst(FieldKey.MOVEMENT_NO));
      assertEquals("7", af.getTag().getFirst(FieldKey.MOVEMENT_TOTAL));
      af.commit();
      af = AudioFileIO.read(testFile);
      assertEquals("fred", af.getTag().getFirst(FieldKey.MOVEMENT));
      assertEquals("1", af.getTag().getFirst(FieldKey.MOVEMENT_NO));
      assertEquals("7", af.getTag().getFirst(FieldKey.MOVEMENT_TOTAL));
    } catch (Exception e) {
      ex = e;
    }
    assertNull(ex);
  }
}
