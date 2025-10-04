package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;
import org.jcodec.containers.mp4.MP4Util;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class Issue451Test extends AbstractTestCase {

  @Test
  public void testCovrAtom() throws Exception {
    Exception ex = null;
    File orig = new File("testdata", "test109.m4a");
    if (!orig.isFile()) {
      System.err.println("Unable to test file - not available");
      return;
    }

    File testFile = AbstractTestCase.copyAudioToTmp("test109.m4a");
    try {
      //Now just createField tree
      MP4Util.Movie mp4 = MP4Util.parseFullMovie(testFile);
      String json = new JSONObject(mp4.getMoov().toString()).toString(2);
      System.out.println(json);
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      AudioFile af = AudioFileIO.read(testFile);
      ImageFormats.getMimeTypeForBinarySignature(
        af.getTag().getArtworkList().get(0).getBinaryData()
      );
    } catch (ArrayIndexOutOfBoundsException aex) {
      ex = aex;
    }
    assertNull(ex);
  }
}
