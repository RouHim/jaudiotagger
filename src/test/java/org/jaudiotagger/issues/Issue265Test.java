package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.junit.jupiter.api.Test;

public class Issue265Test extends AbstractTestCase {

  /**
   * Try an d write too large a file
   */
  @Test
  public void testWriteTooLargeStringToFile() {

    Exception exceptionCaught = null;
    try {
      TagOptionSingleton.getInstance().setTruncateTextWithoutErrors(false);

      File testFile = copyAudioToTmp("test7.wma");
      AudioFile f = AudioFileIO.read(testFile);
      Tag tag = f.getTag();
      assertEquals(0, tag.getFields(FieldKey.COVER_ART).size());

      //Now createField artwork field
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < 34000; i++) {
        sb.append("x");
      }
      tag.setField(FieldKey.ARTIST, sb.toString());
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNotNull(exceptionCaught);
    assertInstanceOf(IllegalArgumentException.class, exceptionCaught);
  }

  /**
   * Try and write too large a file, automtically truncated if option set
   */
  @Test
  public void testWriteTruncateStringToFile() {

    Exception exceptionCaught = null;
    try {
      File testFile = copyAudioToTmp("test7.wma");
      AudioFile f = AudioFileIO.read(testFile);
      Tag tag = f.getTag();
      assertEquals(0, tag.getFields(FieldKey.COVER_ART).size());

      //Enable value
      TagOptionSingleton.getInstance().setTruncateTextWithoutErrors(true);

      //Now createField artwork field
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < 34000; i++) {
        sb.append("x");
      }
      tag.setField(FieldKey.ARTIST, sb.toString());
      f.commit();
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  /**
   * Try an d write too large a file
   */
  @Test
  public void testWriteTooLargeStringToFileContentDesc() {

    Exception exceptionCaught = null;
    try {
      File testFile = copyAudioToTmp("test7.wma");
      AudioFile f = AudioFileIO.read(testFile);
      Tag tag = f.getTag();

      TagOptionSingleton.getInstance().setTruncateTextWithoutErrors(false);
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < 34000; i++) {
        sb.append("x");
      }
      tag.setField(FieldKey.TITLE, sb.toString());
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNotNull(exceptionCaught);
    assertInstanceOf(IllegalArgumentException.class, exceptionCaught);
  }

  /**
   * Try and write too large a file, automtically truncated if option set
   */
  @Test
  public void testWriteTruncateStringToFileContentDesc() {

    Exception exceptionCaught = null;
    try {
      File testFile = copyAudioToTmp("test7.wma");
      AudioFile f = AudioFileIO.read(testFile);
      Tag tag = f.getTag();

      //Enable value
      TagOptionSingleton.getInstance().setTruncateTextWithoutErrors(true);

      //Now createField artwork field
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < 34000; i++) {
        sb.append("x");
      }
      tag.setField(FieldKey.TITLE, sb.toString());
      f.commit();
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }
}
