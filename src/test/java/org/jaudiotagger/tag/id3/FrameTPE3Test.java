package org.jaudiotagger.tag.id3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTPE3;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.junit.jupiter.api.Test;

public class FrameTPE3Test extends AbstractTestCase {

  @Test
  public void testGeneric() {
    Exception e = null;
    try {
      Tag tag = new ID3v23Tag();
      tag.addField(FieldKey.CONDUCTOR, "testconductor");
      assertEquals("testconductor", tag.getFirst(FieldKey.CONDUCTOR));
      assertEquals("testconductor", tag.getFirst("TPE3"));
    } catch (Exception ex) {
      e = ex;
      ex.printStackTrace();
    }
    assertNull(e);
  }

  @Test
  public void testID3Specific() {
    Exception e = null;
    try {
      ID3v23Tag tag = new ID3v23Tag();
      ID3v23Frame frame = new ID3v23Frame("TPE3");
      frame.setBody(
        new FrameBodyTPE3(TextEncoding.ISO_8859_1, "testconductor")
      );
      tag.addFrame(frame);
      assertEquals("testconductor", tag.getFirst(FieldKey.CONDUCTOR));
      assertEquals("testconductor", tag.getFirst("TPE3"));
    } catch (Exception ex) {
      e = ex;
      ex.printStackTrace();
    }
    assertNull(e);
  }
}
