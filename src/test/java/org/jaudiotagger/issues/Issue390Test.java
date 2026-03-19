package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTIPL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

public class Issue390Test extends AbstractTestCase {

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testIssue() {
    Exception caught = null;
    try {
      File testFile = copyAudioToTmp("test101.mp3");
      AudioFile af = AudioFileIO.read(testFile);
      MP3File mp3 = (MP3File) af;
      assertNotNull(mp3.getID3v2Tag());
      assertNotNull(mp3.getID3v2Tag().getFrame("TIPL"));
      FrameBodyTIPL body = ((FrameBodyTIPL) ((AbstractID3v2Frame) (mp3
        .getID3v2Tag()
        .getFrame("TIPL"))).getBody());
      assertEquals(4, body.getNumberOfPairs());
      assertEquals(body.getKeyAtIndex(3), "producer");
      assertEquals(body.getValueAtIndex(3), "producer");

      body = ((FrameBodyTIPL) ((AbstractID3v2Frame) (mp3
        .getID3v2TagAsv24()
        .getFrame("TIPL"))).getBody());
      assertEquals(4, body.getNumberOfPairs());
      assertEquals(body.getKeyAtIndex(3), "producer");
      assertEquals(body.getValueAtIndex(3), "producer");
    } catch (Exception e) {
      caught = e;
    }
    assertNull(caught);
  }
}
