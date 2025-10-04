package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.wav.WavOptions;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.aiff.AiffTag;
import org.jaudiotagger.tag.asf.AsfTag;
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;
import org.jaudiotagger.tag.wav.WavInfoTag;
import org.jaudiotagger.tag.wav.WavTag;
import org.junit.jupiter.api.Test;

public class Issue061Test extends AbstractTestCase {

  @Test
  public void testMp3SetNull1() {
    Exception ex = null;
    try {
      Tag tag = new ID3v23Tag();
      tag.setField(FieldKey.ARTIST, (String) null);
    } catch (Exception e) {
      e.printStackTrace();
      ex = e;
    }
    assertTrue(ex instanceof IllegalArgumentException);
  }

  @Test
  public void testMp3SetNull2() {
    Exception ex = null;
    try {
      Tag tag = new ID3v23Tag();
      tag.setField(FieldKey.GENRE, (String) null);
    } catch (Exception e) {
      e.printStackTrace();
      ex = e;
    }
    assertTrue(ex instanceof IllegalArgumentException);
  }

  @Test
  public void testSetMp4Null() {
    Exception ex = null;
    try {
      Tag tag = new Mp4Tag();
      tag.setField(FieldKey.ARTIST, (String) null);
    } catch (Exception e) {
      e.printStackTrace();
      ex = e;
    }
    assertTrue(ex instanceof IllegalArgumentException);
  }

  @Test
  public void testSetFlacNull() {
    Exception ex = null;
    try {
      Tag tag = new FlacTag();
      tag.setField(FieldKey.ARTIST, (String) null);
    } catch (Exception e) {
      e.printStackTrace();
      ex = e;
    }
    assertTrue(ex instanceof IllegalArgumentException);
  }

  @Test
  public void testSetOggNull() {
    Exception ex = null;
    try {
      Tag tag = new VorbisCommentTag();
      tag.setField(FieldKey.ARTIST, (String) null);
    } catch (Exception e) {
      e.printStackTrace();
      ex = e;
    }
    assertTrue(ex instanceof IllegalArgumentException);
  }

  @Test
  public void testSetAifNull() {
    Exception ex = null;
    try {
      Tag tag = new AiffTag();
      ((AiffTag) tag).setID3Tag(new ID3v23Tag());
      tag.setField(FieldKey.ARTIST, (String) null);
    } catch (Exception e) {
      e.printStackTrace();
      ex = e;
    }
    assertTrue(ex instanceof IllegalArgumentException);
  }

  @Test
  public void testSetWavNull() {
    Exception ex = null;
    try {
      Tag tag = new WavTag(WavOptions.READ_ID3_ONLY);
      ((WavTag) tag).setID3Tag(new ID3v23Tag());
      tag.setField(FieldKey.ARTIST, (String) null);
    } catch (Exception e) {
      e.printStackTrace();
      ex = e;
    }
    assertTrue(ex instanceof IllegalArgumentException);
  }

  @Test
  public void testSetWavInfoNull() {
    Exception ex = null;
    try {
      Tag tag = new WavTag(WavOptions.READ_INFO_ONLY);
      ((WavTag) tag).setInfoTag(new WavInfoTag());
      tag.setField(FieldKey.ARTIST, (String) null);
    } catch (Exception e) {
      e.printStackTrace();
      ex = e;
    }
    assertNotNull(ex);
    assertTrue(ex instanceof IllegalArgumentException);
  }

  @Test
  public void testSetWmaNull() {
    Exception ex = null;
    try {
      Tag tag = new AsfTag();
      tag.setField(FieldKey.ARTIST, (String) null);
    } catch (Exception e) {
      e.printStackTrace();
      ex = e;
    }
    assertTrue(ex instanceof IllegalArgumentException);
  }
}
