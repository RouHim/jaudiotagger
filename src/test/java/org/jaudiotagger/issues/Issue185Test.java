package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.junit.jupiter.api.Test;

public class Issue185Test extends AbstractTestCase {

  @Test
  public void testDefaultTagMp3() {
    Exception exceptionCaught = null;
    try {
      File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
      AudioFile af = AudioFileIO.read(testFile);

      //No Tag
      assertNull(af.getTag());

      //Tag Created
      Tag tag = af.createDefaultTag();
      assertTrue(tag instanceof ID3v23Tag);

      //but not setField in tag itself
      assertNull(af.getTag());

      //Now setField
      af.setTag(tag);
      assertTrue(af.getTag() instanceof ID3v23Tag);

      //Save changes
      af.commit();

      af = AudioFileIO.read(testFile);
      assertTrue(af.getTag() instanceof ID3v23Tag);
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  @Test
  public void testDefaultTagMp3AndCreate() {
    Exception exceptionCaught = null;
    try {
      File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
      AudioFile af = AudioFileIO.read(testFile);

      //No Tag
      assertNull(af.getTag());

      //Tag Created and setField
      Tag tag = af.getTagOrCreateAndSetDefault();
      assertTrue(tag instanceof ID3v23Tag);
      assertTrue(af.getTag() instanceof ID3v23Tag);

      //Save changes
      af.commit();

      af = AudioFileIO.read(testFile);
      assertTrue(af.getTag() instanceof ID3v23Tag);
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }
}
