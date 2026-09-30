package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Date;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.junit.jupiter.api.Test;

public class Issue178Test extends AbstractTestCase {

  /**
   * Test Read empty file pretenidng to be an Ogg, should fail quickly
   */
  @Test
  public void testReadBadOgg() {

    File testFile = null;
    Exception exceptionCaught = null;
    Date startDate = new Date();
    System.out.println("start:" + startDate);
    try {
      testFile = copyAudioToTmp("test36.ogg");

      //Read File
      AudioFile af = AudioFileIO.read(testFile);

      //Print Out Tree
    } catch (Exception e) {
      exceptionCaught = e;
    }

    Date endDate = new Date();
    System.out.println("end  :" + endDate);
    assertInstanceOf(CannotReadException.class, exceptionCaught);
    assertTrue(endDate.getTime() - startDate.getTime() < 1000);
  }
}
