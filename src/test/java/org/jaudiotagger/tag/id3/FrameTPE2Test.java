package org.jaudiotagger.tag.id3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTPE1;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.junit.jupiter.api.Test;

public class FrameTPE2Test extends AbstractTestCase {

  @Test
  public void testID3Specific() {
    Exception e = null;
    try {
      ID3v23Tag tag = new ID3v23Tag();
      ID3v23Frame frame = new ID3v23Frame("TPE2");
      frame.setBody(new FrameBodyTPE1(TextEncoding.ISO_8859_1, "testband"));
      tag.addFrame(frame);
      assertEquals("testband", tag.getFirst("TPE2"));
    } catch (Exception ex) {
      e = ex;
      ex.printStackTrace();
    }
    assertNull(e);
  }
}
