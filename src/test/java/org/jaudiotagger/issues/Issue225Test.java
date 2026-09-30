package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

public class Issue225Test extends AbstractTestCase {

  /**
   * Reading a file contains genre field but set to invalid value 149, because Mp4genreField always
   * store the value the genre is mapped to we return null. This is correct behaviour.
   */
  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testReadInvalidGenre() {
    String genre = null;

    Exception exceptionCaught = null;
    try {
      File testFile = copyAudioToTmp("test30.m4a");
      AudioFile f = AudioFileIO.read(testFile);
      Tag tag = f.getTag();
      genre = tag.getFirst(FieldKey.GENRE);
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
    assertNull(genre);
  }
}
