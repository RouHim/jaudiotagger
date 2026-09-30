package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.junit.jupiter.api.Test;

public class Issue248Test extends AbstractTestCase {

  public static int countExceptions = 0;

  @Test
  public void testMultiThreadedMP3HeaderAccess() throws Exception {
    final File testFile = copyAudioToTmp("testV1vbrOld0.mp3");
    final MP3File mp3File = new MP3File(testFile);
    final Thread[] threads = new Thread[1000];
    for (int i = 0; i < 1000; i++) {
      threads[i] = new Thread(
        new Runnable() {
          @Test
          public void run() {
            try {
              //System.out.println("Output is"+mp3File.getMP3AudioHeader().getTrackLengthAsString());
            } catch (RuntimeException e) {
              countExceptions++;
            } catch (Exception e) {
              countExceptions++;
            }
          }
        }
      );
    }

    for (int i = 0; i < 1000; i++) {
      threads[i].start();
    }

    assertEquals(0, countExceptions);
  }
}
