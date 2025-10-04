package org.jaudiotagger.tag.id3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTDLY;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.junit.jupiter.api.Test;

public class FrameTDLYTest extends AbstractTestCase {

  @Test
  public void testID3Specific() {
    Exception e = null;
    try {
      ID3v24Tag tag = new ID3v24Tag();
      ID3v24Frame frame = new ID3v24Frame("TDLY");
      frame.setBody(new FrameBodyTDLY(TextEncoding.ISO_8859_1, "11:10"));
      tag.addFrame(frame);
      assertEquals("11:10", tag.getFirst("TDLY"));
    } catch (Exception ex) {
      e = ex;
      ex.printStackTrace();
    }
    assertNull(e);
  }
}
