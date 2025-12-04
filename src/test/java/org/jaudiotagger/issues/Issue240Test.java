package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.io.RandomAccessFile;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

public class Issue240Test extends AbstractTestCase {

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testWritelargeDataToFile() {
    Exception exceptionCaught = null;
    try {
      File testFile = copyAudioToTmp("test34.m4a");

      AudioFile af = AudioFileIO.read(testFile);
      assertEquals(0, af.getTag().getFields(FieldKey.COVER_ART).size());

      //Add new image
      RandomAccessFile imageFile = new RandomAccessFile(
        fileResource("testdata", "coverart.png"),
        "r"
      );
      byte[] imagedata = new byte[(int) imageFile.length()];
      imageFile.read(imagedata);
      af
        .getTag()
        .addField(((Mp4Tag) af.getTag()).createArtworkField(imagedata));
      af.commit();

      //Read File back
      af = AudioFileIO.read(testFile);
      assertEquals(1, af.getTag().getFields(FieldKey.COVER_ART).size());
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }
}
