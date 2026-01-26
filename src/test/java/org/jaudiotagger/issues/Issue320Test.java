package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

public class Issue320Test extends AbstractTestCase {

  /*
   * Test File Equality
   * @throws Exception
   */
  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testTagEquality() throws Exception {

    File file1 = fileResource("testdata", "test26.mp3");
    File file2 = fileResource("testdata", "test26.mp3");

    MP3File audioFile1 = (MP3File) AudioFileIO.read(file1);
    Tag tag1 = audioFile1.getTag();

    MP3File audioFile2 = (MP3File) AudioFileIO.read(file2);
    Tag tag2 = audioFile2.getTag();

    assertEquals(tag1, tag2);
  }
}
