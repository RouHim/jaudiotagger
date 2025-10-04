package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.junit.jupiter.api.Test;

public class Issue421Test extends AbstractTestCase {

  @Test
  public void testTrackField() throws Exception {
    File orig = new File("testdata", "Arizona.m4a");
    if (!orig.isFile()) {
      System.err.println("Unable to test file - not available");
      return;
    }

    File testFile = AbstractTestCase.copyAudioToTmp("Arizona.m4a");
    AudioFile f = AudioFileIO.read(testFile);
    Tag tag = f.getTag();
    assertEquals("13", tag.getFirst(FieldKey.TRACK));
    assertEquals("14", tag.getFirst(FieldKey.TRACK_TOTAL));
    assertEquals("13", tag.getAll(FieldKey.TRACK).get(0));
    assertEquals("14", tag.getAll(FieldKey.TRACK_TOTAL).get(0));
  }
}
