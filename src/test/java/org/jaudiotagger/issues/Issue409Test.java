package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

public class Issue409Test extends AbstractTestCase {

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testFindAudioHeaderWhenTagSizeIsTooShortAndHasNullPadding()
    throws Exception {
    Exception ex = null;

    File testFile = copyAudioToTmp("test111.mp3");
    MP3File mp3File = new MP3File(testFile);
    System.out.println("AudioHeaderBefore" + mp3File.getMP3AudioHeader());
    assertEquals(44100, mp3File.getMP3AudioHeader().getSampleRateAsNumber());
  }
}
