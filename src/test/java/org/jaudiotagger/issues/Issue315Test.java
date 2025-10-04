package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.junit.jupiter.api.Test;

public class Issue315Test extends AbstractTestCase {

  /*
   *
   * @throws Exception
   */
  @Test
  public void testReadWriteTagWithPictureBlockAtStart() {
    File orig = new File("testdata", "test54.flac");
    if (!orig.isFile()) {
      System.err.println("Unable to test file - not available");
      return;
    }

    Exception e = null;
    try {
      final File testFile = AbstractTestCase.copyAudioToTmp("test54.flac");
      AudioFile af = AudioFileIO.read(testFile);

      //Modify File
      af.getTag().setField(FieldKey.TITLE, "newtitle");
      af.commit();

      //Reread File
      af = AudioFileIO.read(testFile);
    } catch (Exception ex) {
      e = ex;
      ex.printStackTrace();
    }
    assertNull(e);
  }
}
