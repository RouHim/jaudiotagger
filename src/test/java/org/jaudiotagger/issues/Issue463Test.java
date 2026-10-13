package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.RandomAccessFile;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jcodec.containers.mp4.MP4Util;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

public class Issue463Test extends AbstractTestCase {

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testReadMp4() {
    Exception ex = null;
    try {
      File testFile = copyAudioToTmp("test116.m4a");
      RandomAccessFile raf = new RandomAccessFile(testFile, "r");
      MP4Util.Movie mp4 = MP4Util.parseFullMovie(testFile);
      String json = new JSONObject(mp4.getMoov().toString()).toString(2);
      System.out.println(json);
      raf.close();

      AudioFile af = AudioFileIO.read(testFile);
      assertNotNull(af.getTag());
      assertEquals("Zbigniew Preisner", af.getTag().getFirst(FieldKey.ARTIST));

      af.getTag().setField(FieldKey.ARTIST, "fred");
      assertEquals("fred", af.getTag().getFirst(FieldKey.ARTIST));
      af.commit();

      raf = new RandomAccessFile(testFile, "r");
      MP4Util.Movie mp42 = MP4Util.parseFullMovie(testFile);
      String json2 = new JSONObject(mp42.getMoov().toString()).toString(2);
      System.out.println(json2);
      raf.close();

      af = AudioFileIO.read(testFile);
      assertNotNull(af.getTag());
      assertEquals("fred", af.getTag().getFirst(FieldKey.ARTIST));
    } catch (Exception e) {
      ex = e;
    }
    assertNull(ex);
  }
}
