package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.junit.jupiter.api.Test;

public class Issue270Test extends AbstractTestCase {

  /**
   * Test read mp3 that says it has extended header but doesnt really
   */
  @Test
  public void testReadMp4WithCorruptMdata() {
    File orig = new File("testdata", "test49.m4a");
    if (!orig.isFile()) {
      System.err.println("Unable to test file - not available");
      return;
    }

    File testFile = null;
    Exception exceptionCaught = null;
    try {
      testFile = AbstractTestCase.copyAudioToTmp("test49.m4a");

      //Read FileFails
      AudioFile af = AudioFileIO.read(testFile);
      System.out.println(af.getTag().toString());
    } catch (Exception e) {
      e.printStackTrace();
      exceptionCaught = e;
    }

    assertTrue(exceptionCaught instanceof CannotReadException);
  }
}
