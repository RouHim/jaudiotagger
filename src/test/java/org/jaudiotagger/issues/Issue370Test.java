package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.junit.jupiter.api.Test;

public class Issue370Test extends AbstractTestCase {

  @Test
  public void testIssue() {
    Exception caught = null;
    try {
      File orig = new File("testdata", "test96.m4a");
      if (!orig.isFile()) {
        System.err.println("Unable to test file - not available");
        return;
      }
      //ToDO Fix Issue
      //File testFile = AbstractTestCase.copyAudioToTmp("test96.m4a");
      //AudioFile af = AudioFileIO.read(testFile);
    } catch (Exception e) {
      caught = e;
      e.printStackTrace();
    }
    assertNull(caught);
  }
}
