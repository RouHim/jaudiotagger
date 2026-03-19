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

public class Issue257Test extends AbstractTestCase {

  /**
   * Test Mp4 with crap between free atom and mdat atom, shoud cause immediate failure
   */
  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testReadMp4FileWithPaddingAfterLastAtom() {

    File testFile = null;
    Exception exceptionCaught = null;
    try {
      testFile = copyAudioToTmp("test37.m4a");

      //Read File
      AudioFile af = AudioFileIO.read(testFile);

      //Print Out Tree
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertInstanceOf(CannotReadException.class, exceptionCaught);
  }
}
