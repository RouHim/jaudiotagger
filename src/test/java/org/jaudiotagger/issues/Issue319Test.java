package org.jaudiotagger.issues;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

public class Issue319Test extends AbstractTestCase {

  /*
   * Test File Equality
   * @throws Exception
   */
  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testTagEquality() throws Exception {

    File file1 = fileResource("testdata", "test26.mp3");

    MP3File audioFile = (MP3File) AudioFileIO.read(file1);
    Tag tag = audioFile.getTag();

    FieldKey key = FieldKey.DISC_NO;
    try {
      String fieldValue = tag.getFirst(key);
      System.out.println("Fieldvalue is" + fieldValue);
    } catch (KeyNotFoundException e) {
    }
  }
}
