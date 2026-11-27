package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.junit.jupiter.api.Test;

public class Issue423Test extends AbstractTestCase {

  @Test
  public void testGetAllMp4() throws Exception {
    File testFile = copyAudioToTmp("test2.m4a");
    AudioFile f = AudioFileIO.read(testFile);
    Tag tag = f.getTag();
    assertTrue(tag.hasField(FieldKey.TRACK));
    assertEquals("1", tag.getFirst(FieldKey.TRACK));
    assertEquals(1, tag.getAll(FieldKey.TRACK).size());
    assertTrue(tag.hasField(FieldKey.TITLE));
    assertEquals("title", tag.getFirst(FieldKey.TITLE));
    assertEquals(1, tag.getAll(FieldKey.TITLE).size());
    assertEquals("title", tag.getAll(FieldKey.TITLE).get(0));
  }

  @Test
  public void testGetAllMp3() throws Exception {
    File testFile = copyAudioToTmp(
      "testV1.mp3",
      "testGetAllMp3.mp3"
    );
    AudioFile f = AudioFileIO.read(testFile);
    Tag tag = f.getTagOrCreateDefault();
    tag.setField(FieldKey.TRACK, "1");
    tag.setField(FieldKey.TITLE, "title");
    assertTrue(tag.hasField(FieldKey.TRACK));
    assertEquals("1", tag.getFirst(FieldKey.TRACK));
    assertEquals(1, tag.getAll(FieldKey.TRACK).size());
    assertTrue(tag.hasField(FieldKey.TITLE));
    assertEquals("title", tag.getFirst(FieldKey.TITLE));
    assertEquals(1, tag.getAll(FieldKey.TITLE).size());
    assertEquals("title", tag.getAll(FieldKey.TITLE).get(0));
  }

  @Test
  public void testGetAllOgg() throws Exception {
    File testFile = copyAudioToTmp("test.ogg");
    AudioFile f = AudioFileIO.read(testFile);
    Tag tag = f.getTagOrCreateDefault();
    tag.setField(FieldKey.TRACK, "1");
    tag.setField(FieldKey.TITLE, "title");
    assertTrue(tag.hasField(FieldKey.TRACK));
    assertEquals("1", tag.getFirst(FieldKey.TRACK));
    assertEquals(1, tag.getAll(FieldKey.TRACK).size());
    assertTrue(tag.hasField(FieldKey.TITLE));
    assertEquals("title", tag.getFirst(FieldKey.TITLE));
    assertEquals(1, tag.getAll(FieldKey.TITLE).size());
    assertEquals("title", tag.getAll(FieldKey.TITLE).get(0));
  }

  @Test
  public void testGetAllFlac() throws Exception {
    File testFile = copyAudioToTmp("test.flac");
    AudioFile f = AudioFileIO.read(testFile);
    Tag tag = f.getTagOrCreateDefault();
    tag.setField(FieldKey.TRACK, "1");
    tag.setField(FieldKey.TITLE, "title");
    assertTrue(tag.hasField(FieldKey.TRACK));
    assertEquals("1", tag.getFirst(FieldKey.TRACK));
    assertEquals(1, tag.getAll(FieldKey.TRACK).size());
    assertTrue(tag.hasField(FieldKey.TITLE));
    assertEquals("title", tag.getFirst(FieldKey.TITLE));
    assertEquals(1, tag.getAll(FieldKey.TITLE).size());
    assertEquals("title", tag.getAll(FieldKey.TITLE).get(0));
  }

  @Test
  public void testGetAllWma() throws Exception {
    File testFile = copyAudioToTmp("test1.wma");
    AudioFile f = AudioFileIO.read(testFile);
    Tag tag = f.getTagOrCreateDefault();
    tag.setField(FieldKey.TRACK, "1");
    tag.setField(FieldKey.TITLE, "title");
    assertTrue(tag.hasField(FieldKey.TRACK));
    assertEquals("1", tag.getFirst(FieldKey.TRACK));
    assertEquals(1, tag.getAll(FieldKey.TRACK).size());
    assertTrue(tag.hasField(FieldKey.TITLE));
    assertEquals("title", tag.getFirst(FieldKey.TITLE));
    assertEquals(1, tag.getAll(FieldKey.TITLE).size());
    assertEquals("title", tag.getAll(FieldKey.TITLE).get(0));
  }
}
