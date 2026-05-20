package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.ID3v1Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.junit.jupiter.api.Test;

public class Issue443Test extends AbstractTestCase {

  @Test
  public void testID3v2DefaultCreateOrConvertWhenOnlyHasID3v1() {
    try {
      File testFile = copyAudioToTmp("testV1vbrNew0.mp3");
      MP3File test = new MP3File(testFile);
      assertNull(test.getID3v1Tag());
      assertNull(test.getID3v2Tag());

      test.setID3v1Tag(new ID3v1Tag());
      assertNotNull(test.getID3v1Tag());
      assertNull(test.getID3v2Tag());
      test.commit();

      test = new MP3File(testFile);
      test.getTagAndConvertOrCreateAndSetDefault();
      assertNotNull(test.getID3v1Tag());
      assertNotNull(test.getID3v2Tag());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Test
  public void testID3v2DefaultCreatedWhenOnlyHasID3v1() {
    try {
      File testFile = copyAudioToTmp("testV1vbrNew0.mp3");
      MP3File test = new MP3File(testFile);
      assertNull(test.getID3v1Tag());
      assertNull(test.getID3v2Tag());

      test.setID3v1Tag(new ID3v1Tag());
      assertNotNull(test.getID3v1Tag());
      assertNull(test.getID3v2Tag());
      test.commit();

      test = new MP3File(testFile);
      test.getTagOrCreateAndSetDefault();
      assertNotNull(test.getID3v1Tag());
      assertNotNull(test.getID3v2Tag());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Test
  public void testID3v2CreatedWhenOnlyHasID3v1() {
    try {
      File testFile = copyAudioToTmp("testV1vbrNew0.mp3");
      MP3File test = new MP3File(testFile);
      assertNull(test.getID3v1Tag());
      assertNull(test.getID3v2Tag());

      test.setID3v1Tag(new ID3v1Tag());
      assertNotNull(test.getID3v1Tag());
      assertNull(test.getID3v2Tag());
      test.commit();

      test = new MP3File(testFile);
      Tag tag = test.getTagOrCreateDefault();
      assertInstanceOf(ID3v23Tag.class, tag);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
