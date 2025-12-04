package org.jaudiotagger.issues;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Issue184Test extends AbstractTestCase {

  @Test
  public void testReadCorruptWma() {

    Exception ex = null;
    try {
      File testFile = copyAudioToTmp("test509.wma");
      AudioFileIO.read(testFile);
    } catch (Exception e) {
      assertInstanceOf(CannotReadException.class, e);
      ex = e;
    }
    assertNotNull(ex);
  }
}
