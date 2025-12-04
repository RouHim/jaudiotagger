package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

public class Issue404Test extends AbstractTestCase {

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testWritingTooLongTempFile() {
    Exception caught = null;
    try {
      File orig = copyAudioToTmp(
        "test3811111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111...........................................................m4a"
      );
      AudioFile af = AudioFileIO.read(orig);
      af.getTag().setField(FieldKey.ALBUM, "Albumstuff");
      AudioFileIO.write(af);
    } catch (Exception e) {
      caught = e;
    }
    assertNull(caught);
  }
}
