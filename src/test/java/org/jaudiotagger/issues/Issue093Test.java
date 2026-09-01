package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.aiff.AiffAudioHeader;
import org.jaudiotagger.audio.wav.WavOptions;
import org.jaudiotagger.audio.wav.WavSaveOptions;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

public class Issue093Test extends AbstractTestCase {

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testWriteAiffWithCorruptID3Tag1() {
    Exception exceptionCaught = null;


    File testFile = copyAudioToTmp(
      "test145.aiff",
      "test145CorruptedID3.aiff"
    );
    try {
      AudioFile f = AudioFileIO.read(testFile);
      AudioHeader ah = f.getAudioHeader();
      assertInstanceOf(AiffAudioHeader.class, ah);
      Tag tag = f.getTag();
      System.out.println(tag);
      f.getTag().setField(FieldKey.ARTIST, "Jonathon");
      f.commit();
      f = AudioFileIO.read(testFile);
      tag = f.getTag();
      System.out.println(tag);
      assertEquals("Jonathon", tag.getFirst(FieldKey.ARTIST));
    } catch (Exception ex) {
      ex.printStackTrace();
      exceptionCaught = ex;
    }
    assertNull(exceptionCaught);
  }

  @Test
  public void testWriteAiffWithCorruptID3Tag2() {
    Exception exceptionCaught = null;


    File testFile = copyAudioToTmp(
      "test152.aiff",
      "test152MissingByteId3.aiff"
    );
    try {
      AudioFile f = AudioFileIO.read(testFile);
      AudioHeader ah = f.getAudioHeader();
      assertInstanceOf(AiffAudioHeader.class, ah);
      Tag tag = f.getTag();
      System.out.println(tag);
      f.getTag().setField(FieldKey.ARTIST, "fred");
      f.commit();
      f = AudioFileIO.read(testFile);
      tag = f.getTag();
      System.out.println(tag);
      assertEquals("fred", tag.getFirst(FieldKey.ARTIST));
    } catch (Exception ex) {
      ex.printStackTrace();
      exceptionCaught = ex;
    }
    assertNull(exceptionCaught);
  }

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testNaimRipMultipleTagsFixId3() {

    TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
    TagOptionSingleton.getInstance().setWavSaveOptions(
      WavSaveOptions.SAVE_ACTIVE
    );

    Exception exceptionCaught = null;
    try {
      File testFile = copyAudioToTmp(
        "test152.wav",
        "test152_id3.wav"
      );
      AudioFile f = AudioFileIO.read(testFile);
      System.out.println(f.getAudioHeader());
      System.out.println(f.getTag());
      f.getTag().setField(FieldKey.ARTIST, "fred");
      f.commit();
      f = AudioFileIO.read(testFile);
      Tag tag = f.getTag();
      System.out.println(tag);
      assertEquals("fred", tag.getFirst(FieldKey.ARTIST));
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testNaimRipMultipleFixTagsExistingInfo() {

    TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
    TagOptionSingleton.getInstance().setWavSaveOptions(
      WavSaveOptions.SAVE_EXISTING_AND_ACTIVE
    );

    Exception exceptionCaught = null;
    try {
      File testFile = copyAudioToTmp(
        "test152.wav",
        "test152_existing_info.wav"
      );
      AudioFile f = AudioFileIO.read(testFile);
      System.out.println(f.getAudioHeader());
      System.out.println(f.getTag());
      f.getTag().setField(FieldKey.ARTIST, "fred");
      f.commit();
      f = AudioFileIO.read(testFile);
      Tag tag = f.getTag();
      System.out.println(tag);
      assertEquals("fred", tag.getFirst(FieldKey.ARTIST));
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testNaimRipMultipleTagsFixId3BothSync() {

    TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
    TagOptionSingleton.getInstance().setWavSaveOptions(
      WavSaveOptions.SAVE_BOTH_AND_SYNC
    );

    Exception exceptionCaught = null;
    try {
      File testFile = copyAudioToTmp(
        "test152.wav",
        "test152_existing_id3_both_sync.wav"
      );
      AudioFile f = AudioFileIO.read(testFile);
      System.out.println(f.getAudioHeader());
      System.out.println(f.getTag());
      f.getTag().setField(FieldKey.ARTIST, "fred");
      f.commit();
      f = AudioFileIO.read(testFile);
      Tag tag = f.getTag();
      System.out.println(tag);
      assertEquals("fred", tag.getFirst(FieldKey.ARTIST));
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }
}
