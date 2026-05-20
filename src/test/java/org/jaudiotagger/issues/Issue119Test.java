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

public class Issue119Test extends AbstractTestCase {

  @Test
  public void testWriteAiffWithOddLengthDataChunk() {
    Exception exceptionCaught = null;


    File testFile = copyAudioToTmp(
      "test151.aif",
      "test151MissingByte.aiff"
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
  public void testWriteFileWithOddLengthLastDataChunkInfo() {
    TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
    TagOptionSingleton.getInstance().setWavSaveOptions(
      WavSaveOptions.SAVE_ACTIVE
    );


    Exception exceptionCaught = null;
    try {
      File testFile = copyAudioToTmp(
        "test153.wav",
        "test153_odd_data_length_info.wav"
      );
      AudioFile f = AudioFileIO.read(testFile);
      System.out.println(f.getAudioHeader());
      System.out.println(f.getTag());

      f.getTag().setField(FieldKey.ARTIST, "freddy");
      f.commit();
      f = AudioFileIO.read(testFile);
      Tag tag = f.getTag();
      System.out.println(tag);
      assertEquals("freddy", tag.getFirst(FieldKey.ARTIST));
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  @Test
  public void testWriteFileWithOddLengthLastDataChunkId3() {
    TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
    TagOptionSingleton.getInstance().setWavSaveOptions(
      WavSaveOptions.SAVE_ACTIVE
    );


    Exception exceptionCaught = null;
    try {
      File testFile = copyAudioToTmp(
        "test153.wav",
        "test153_odd_data_length_id3.wav"
      );
      AudioFile f = AudioFileIO.read(testFile);
      System.out.println(f.getAudioHeader());
      System.out.println(f.getTag());

      f.getTag().setField(FieldKey.ARTIST, "freddy");
      f.commit();
      f = AudioFileIO.read(testFile);
      Tag tag = f.getTag();
      System.out.println(tag);
      assertEquals("freddy", tag.getFirst(FieldKey.ARTIST));
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  @Test
  public void testWriteFileWithOddLengthLastDataChunkId3AndInfo() {
    TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
    TagOptionSingleton.getInstance().setWavSaveOptions(
      WavSaveOptions.SAVE_BOTH_AND_SYNC
    );


    Exception exceptionCaught = null;
    try {
      File testFile = copyAudioToTmp(
        "test153.wav",
        "test153_odd_data_length_id3_and_info.wav"
      );
      AudioFile f = AudioFileIO.read(testFile);
      System.out.println(f.getAudioHeader());
      System.out.println(f.getTag());

      f.getTag().setField(FieldKey.ARTIST, "freddy");
      f.commit();
      f = AudioFileIO.read(testFile);
      Tag tag = f.getTag();
      System.out.println(tag);
      assertEquals("freddy", tag.getFirst(FieldKey.ARTIST));
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }
}
