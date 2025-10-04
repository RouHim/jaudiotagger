package org.jaudiotagger.tag.id3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.Tag;
import org.junit.jupiter.api.Test;

public class DuplicateFrameTest extends AbstractTestCase {

  @Test
  public void testReadingFileWithCorruptFirstFrame() throws Exception {
    File orig = new File("testdata", "test78.mp3");
    if (!orig.isFile()) {
      System.err.println("Unable to test file - not available");
      return;
    }
    File testFile = AbstractTestCase.copyAudioToTmp("test78.mp3");

    MP3File f = (MP3File) AudioFileIO.read(testFile);
    Tag tag = f.getTag();
    assertTrue(f.getTag() instanceof ID3v23Tag);
    ID3v23Tag id3v23tag = (ID3v23Tag) tag;
    //Frame contains two TYER frames
    assertEquals(21, id3v23tag.getDuplicateBytes());
    assertEquals("*TYER*", "*" + id3v23tag.getDuplicateFrameId() + "*");
    f.commit();
    f = (MP3File) AudioFileIO.read(testFile);
    tag = f.getTag();
    id3v23tag = (ID3v23Tag) tag;
    //After save the duplicate frame has been discarded
    assertEquals(0, id3v23tag.getDuplicateBytes());
    assertEquals("", id3v23tag.getDuplicateFrameId());
  }
}
