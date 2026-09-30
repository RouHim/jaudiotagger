package org.jaudiotagger.issues;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Issue185Test extends AbstractTestCase {

  @Test
  public void testDefaultTagMp3() {
    Exception exceptionCaught = null;
    try {
      File testFile = copyAudioToTmp("testV1.mp3");
      AudioFile af = AudioFileIO.read(testFile);

      //No Tag
      assertNull(af.getTag());

      //Tag Created
      Tag tag = af.createDefaultTag();
      assertInstanceOf(ID3v23Tag.class, tag);

      //but not setField in tag itself
      assertNull(af.getTag());

      //Now setField
      af.setTag(tag);
      assertInstanceOf(ID3v23Tag.class, af.getTag());

      //Save changes
      af.commit();

      af = AudioFileIO.read(testFile);
      assertInstanceOf(ID3v23Tag.class, af.getTag());
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  @Test
  public void testDefaultTagMp3AndCreate() {
    Exception exceptionCaught = null;
    try {
      File testFile = copyAudioToTmp("testV1.mp3");
      AudioFile af = AudioFileIO.read(testFile);

      //No Tag
      assertNull(af.getTag());

      //Tag Created and setField
      Tag tag = af.getTagOrCreateAndSetDefault();
      assertInstanceOf(ID3v23Tag.class, tag);
      assertInstanceOf(ID3v23Tag.class, af.getTag());

      //Save changes
      af.commit();

      af = AudioFileIO.read(testFile);
      assertInstanceOf(ID3v23Tag.class, af.getTag());
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }
}
