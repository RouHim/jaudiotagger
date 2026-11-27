package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

public class Issue421Test extends AbstractTestCase {

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testTrackField() throws Exception {

    File testFile = copyAudioToTmp("Arizona.m4a");
    AudioFile f = AudioFileIO.read(testFile);
    Tag tag = f.getTag();
    assertEquals("13", tag.getFirst(FieldKey.TRACK));
    assertEquals("14", tag.getFirst(FieldKey.TRACK_TOTAL));
    assertEquals("13", tag.getAll(FieldKey.TRACK).get(0));
    assertEquals("14", tag.getAll(FieldKey.TRACK_TOTAL).get(0));
  }
}
