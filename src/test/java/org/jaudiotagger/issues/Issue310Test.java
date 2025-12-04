package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.images.ArtworkFactory;
import org.jcodec.containers.mp4.MP4Util;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

public class Issue310Test extends AbstractTestCase {

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testSavingFile() {

    File testFile = null;
    Exception exceptionCaught = null;
    try {
      testFile = copyAudioToTmp(
        "test85.mp4",
        "test85Test1.mp4"
      );
      AudioFile af = AudioFileIO.read(testFile);
      af
        .getTag()
        .setField(af.getTag().createField(FieldKey.ARTIST, "Kenny Rankin1"));
      af.commit();
      af = AudioFileIO.read(testFile);
      assertEquals("Kenny Rankin1", af.getTag().getFirst(FieldKey.ARTIST));
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertNull(exceptionCaught);
  }

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testSavingFile2() {

    File testFile = null;
    Exception exceptionCaught = null;
    try {
      testFile = copyAudioToTmp(
        "test85.mp4",
        "test85Test2.mp4"
      );
      AudioFile af = AudioFileIO.read(testFile);

      af.getTag().deleteField(FieldKey.ENCODER);
      af.commit();
      af = AudioFileIO.read(testFile);
      assertEquals("", af.getTag().getFirst(FieldKey.ENCODER));
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertNull(exceptionCaught);
  }

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testSavingFile3() {

    File testFile = null;
    Exception exceptionCaught = null;
    try {
      testFile = copyAudioToTmp(
        "test85.mp4",
        "test85Test3.mp4"
      );
      AudioFile af = AudioFileIO.read(testFile);
      af
        .getTag()
        .setField(
          ArtworkFactory.createArtworkFromFile(
            fileResource("testdata", "coverart.png")
          )
        );
      af.commit();
      af = AudioFileIO.read(testFile);
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertNull(exceptionCaught);
  }

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testPrintAtomTree() {

    File testFile = null;
    Exception exceptionCaught = null;
    try {
      testFile = copyAudioToTmp("test85.mp4");
      MP4Util.Movie mp4 = MP4Util.parseFullMovie(testFile);
      String json = new JSONObject(mp4.getMoov().toString()).toString(2);
      System.out.println(json);
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertNull(exceptionCaught);
  }
}
