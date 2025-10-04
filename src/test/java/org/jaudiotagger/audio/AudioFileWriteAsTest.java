package org.jaudiotagger.audio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.tag.FieldKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AudioFileWriteAsTest extends AbstractTestCase {

  public static final String EXPECTED_EXTENSION = ".mp3";
  public static final String LANGUAGE = "English";
  private static final String DESTINATION_FILE_NAME = "writeastest";
  private AudioFile af;
  private File sourceFile;

  @BeforeEach
  public void setUp() {
    super.setUp();
    File orig = new File("testdata", "01.mp3");
    try {
      sourceFile = AbstractTestCase.copyAudioToTmp(orig.getName());
      af = AudioFileIO.read(sourceFile);
    } catch (Throwable e) {
      throw new RuntimeException("Can't setUp test.", e);
    }
  }

  @Test
  public void testWriteAs() throws Exception {
    af.getTagOrCreateAndSetDefault().setField(FieldKey.LANGUAGE, LANGUAGE);
    af.commit();

    final String parent = sourceFile.getParent();
    File destinationNoExtension = new File(parent, DESTINATION_FILE_NAME);
    AudioFileIO.writeAs(af, destinationNoExtension.getPath());

    assertEquals(
      destinationNoExtension + EXPECTED_EXTENSION,
      af.getFile().getPath()
    );
    assertEquals(LANGUAGE, af.getTag().getFirst(FieldKey.LANGUAGE));
  }

  @Test
  public void testWriteAsWithNull() {
    try {
      AudioFileIO.writeAs(af, null);
    } catch (CannotWriteException e) {
      // expected
      return;
    }
    fail("Didn't get expected exception " + CannotWriteException.class);
  }
}
