package org.jaudiotagger.tag.id3;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Iterator;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;
import org.junit.jupiter.api.Test;

public class FrameTPOSTest extends AbstractTestCase {

  @Test
  public void testMergingMultipleFrames() throws Exception {
    ID3v24Tag tag = new ID3v24Tag();
    tag.setField(tag.createField(FieldKey.DISC_NO, "1"));
    tag.setField(tag.createField(FieldKey.DISC_TOTAL, "10"));
    assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
    assertEquals("10", tag.getFirst(FieldKey.DISC_TOTAL));
    assertTrue(tag.getFrame("TPOS") instanceof AbstractID3v2Frame);
  }

  @Test
  public void testDiscNo() {
    Exception exceptionCaught = null;
    File orig = new File("testdata", "test82.mp3");
    if (!orig.isFile()) {
      System.err.println("Unable to test file - not available");
      return;
    }

    try {
      AudioFile af = AudioFileIO.read(orig);
      Tag newTags = af.getTag();
      Iterator<TagField> i = newTags.getFields();
      while (i.hasNext()) {
        System.out.println(i.next().getId());
      }
      //Integer discNo = Integer.parseInt(newTags.get("Disc Number"));
      //tag.setField(FieldKey.DISC_NO,discNo.toString())
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertNull(exceptionCaught);
  }
}
