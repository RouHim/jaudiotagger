package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

public class Issue292Test extends AbstractTestCase {

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testSavingMp3File() {
    File testFile;

    File originalFileBackup = null;

    Exception exceptionCaught = null;
    try {
      testFile = copyAudioToTmp("testV1Cbr128ID3v2.mp3");
      //Put file in backup location
      originalFileBackup = fileResource(
        testFile.getAbsoluteFile().getParentFile().getPath(),
        AudioFile.getBaseFilename(testFile) + ".old"
      );
      testFile.renameTo(originalFileBackup);

      //Copy over again
      testFile = copyAudioToTmp("testV1Cbr128ID3v2.mp3");

      //Read and save chnages
      AudioFile af = AudioFileIO.read(testFile);
      af
        .getTag()
        .setField(
          af
            .getTag()
            .createField(
              FieldKey.ARTIST,
              "fredqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"
            )
        );
      af
        .getTag()
        .setField(
          af
            .getTag()
            .createField(
              FieldKey.AMAZON_ID,
              "fredqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"
            )
        );

      af.commit();

      af = AudioFileIO.read(testFile);
      assertEquals(
        "fredqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq",
        af.getTag().getFirst(FieldKey.ARTIST)
      );
    } catch (Exception e) {
      exceptionCaught = e;
    } finally {
      originalFileBackup.delete();
    }
    assertNull(exceptionCaught);
  }

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testSavingMp4File() {
    File testFile;

    File originalFileBackup = null;

    Exception exceptionCaught = null;
    try {
      testFile = copyAudioToTmp("test8.m4a");
      //Put file in backup location
      originalFileBackup = fileResource(
        testFile.getAbsoluteFile().getParentFile().getPath(),
        AudioFile.getBaseFilename(testFile) + ".old"
      );
      testFile.renameTo(originalFileBackup);

      //Copy over again
      testFile = copyAudioToTmp("test8.m4a");

      //Read and save chnages
      AudioFile af = AudioFileIO.read(testFile);
      af
        .getTag()
        .setField(
          af
            .getTag()
            .createField(
              FieldKey.ARTIST,
              "fredqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"
            )
        );
      af
        .getTag()
        .setField(
          af
            .getTag()
            .createField(
              FieldKey.AMAZON_ID,
              "fredqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"
            )
        );

      af.commit();

      af = AudioFileIO.read(testFile);
      assertEquals(
        "fredqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq",
        af.getTag().getFirst(FieldKey.ARTIST)
      );
    } catch (Exception e) {
      exceptionCaught = e;
    } finally {
      originalFileBackup.delete();
    }
    assertNull(exceptionCaught);
  }
}
