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

public class Issue290Test extends AbstractTestCase {

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testSavingFile() {

    File testFile = null;
    Exception exceptionCaught = null;
    try {
      testFile = copyAudioToTmp("test59.mp4");
      AudioFile af = AudioFileIO.read(testFile);
      System.out.println("Tag is" + af.getTag().toString());
      af.getTag().setField(af.getTag().createField(FieldKey.ARTIST, "fred"));
      af.commit();

      af = AudioFileIO.read(testFile);
      assertEquals("fred", af.getTag().getFirst(FieldKey.ARTIST));
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertNull(exceptionCaught);
  }
}
