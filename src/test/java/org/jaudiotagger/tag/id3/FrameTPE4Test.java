package org.jaudiotagger.tag.id3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTPE3;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.junit.jupiter.api.Test;

public class FrameTPE4Test extends AbstractTestCase {

  @Test
  public void testGenericv22() {
    Exception e = null;
    try {
      Tag tag = new ID3v22Tag();
      tag.addField(FieldKey.REMIXER, "testREMIXER");
      assertEquals("testREMIXER", tag.getFirst(FieldKey.REMIXER));
      assertEquals("testREMIXER", tag.getFirst("TP4"));
    } catch (Exception ex) {
      e = ex;
      ex.printStackTrace();
    }
    assertNull(e);
  }

  @Test
  public void testID3Specificv22() {
    Exception e = null;
    try {
      ID3v22Tag tag = new ID3v22Tag();
      ID3v22Frame frame = new ID3v22Frame("TP4");
      frame.setBody(new FrameBodyTPE3(TextEncoding.ISO_8859_1, "testRemixer"));
      tag.addFrame(frame);
      assertEquals("testRemixer", tag.getFirst(FieldKey.REMIXER));
      assertEquals("testRemixer", tag.getFirst("TP4"));
    } catch (Exception ex) {
      e = ex;
      ex.printStackTrace();
    }
    assertNull(e);
  }

  @Test
  public void testGenericv23() {
    Exception e = null;
    try {
      Tag tag = new ID3v23Tag();
      tag.addField(FieldKey.REMIXER, "testRemixer");
      assertEquals("testRemixer", tag.getFirst(FieldKey.REMIXER));
      assertEquals("testRemixer", tag.getFirst("TPE4"));
    } catch (Exception ex) {
      e = ex;
      ex.printStackTrace();
    }
    assertNull(e);
  }

  @Test
  public void testID3Specificv23() {
    Exception e = null;
    try {
      ID3v23Tag tag = new ID3v23Tag();
      ID3v23Frame frame = new ID3v23Frame("TPE4");
      frame.setBody(new FrameBodyTPE3(TextEncoding.ISO_8859_1, "testRemixer"));
      tag.addFrame(frame);
      assertEquals("testRemixer", tag.getFirst(FieldKey.REMIXER));
      assertEquals("testRemixer", tag.getFirst("TPE4"));
    } catch (Exception ex) {
      e = ex;
      ex.printStackTrace();
    }
    assertNull(e);
  }

  @Test
  public void testGenericv24() {
    Exception e = null;
    try {
      Tag tag = new ID3v24Tag();
      tag.addField(FieldKey.REMIXER, "testRemixer");
      assertEquals("testRemixer", tag.getFirst(FieldKey.REMIXER));
      assertEquals("testRemixer", tag.getFirst("TPE4"));
    } catch (Exception ex) {
      e = ex;
      ex.printStackTrace();
    }
    assertNull(e);
  }

  @Test
  public void testID3Specificv24() {
    Exception e = null;
    try {
      ID3v24Tag tag = new ID3v24Tag();
      ID3v24Frame frame = new ID3v24Frame("TPE4");
      frame.setBody(new FrameBodyTPE3(TextEncoding.ISO_8859_1, "testRemixer"));
      tag.addFrame(frame);
      assertEquals("testRemixer", tag.getFirst(FieldKey.REMIXER));
      assertEquals("testRemixer", tag.getFirst("TPE4"));
    } catch (Exception ex) {
      e = ex;
      ex.printStackTrace();
    }
    assertNull(e);
  }
}
