package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

public class Issue270Test extends AbstractTestCase {

  /**
   * Test read mp3 that says it has extended header but doesnt really
   */
  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testReadMp4WithCorruptMdata() {

    File testFile = null;
    Exception exceptionCaught = null;
    try {
      testFile = copyAudioToTmp("test49.m4a");

      //Read FileFails
      AudioFile af = AudioFileIO.read(testFile);
      System.out.println(af.getTag().toString());
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertInstanceOf(CannotReadException.class, exceptionCaught);
  }
}
