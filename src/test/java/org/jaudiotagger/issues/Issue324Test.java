package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.ID3v11Tag;
import org.junit.jupiter.api.Test;

public class Issue324Test extends AbstractTestCase {

  @Test
  public void testID3v1TagHandling() throws Exception {
    File orig = new File("testdata", "test32.mp3");
    if (!orig.isFile()) {
      System.err.println("Unable to test file - not available");
      return;
    }

    File testFile = AbstractTestCase.copyAudioToTmp("test32.mp3");
    assertEquals(1853744, testFile.length());
    MP3File f = (MP3File) AudioFileIO.read(testFile);
    assertEquals("Iron Maiden", f.getID3v1Tag().getFirst(FieldKey.ARTIST));
    f.setID3v1Tag(new ID3v11Tag());
    f.getID3v1Tag().setField(FieldKey.ARTIST, "Iron Mask");
    f.commit();
    assertEquals(1853744, testFile.length());
    f = (MP3File) AudioFileIO.read(testFile);
    assertEquals("Iron Mask", f.getID3v1Tag().getFirst(FieldKey.ARTIST));
  }
}
