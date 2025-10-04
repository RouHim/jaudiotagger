package org.jaudiotagger.tag.wav;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.wav.WavOptions;
import org.jaudiotagger.audio.wav.WavSaveOptions;
import org.jaudiotagger.audio.wav.WavSaveOrder;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.junit.jupiter.api.Test;

public class WavMetadataNewTagsInfobeforeId3Test extends AbstractTestCase {

  /**
   * Read file with metadata added by MediaMonkey
   */
  @Test
  public void testModifyFileMetadataSaveBoth() {
    TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
    TagOptionSingleton.getInstance().setWavSaveOptions(
      WavSaveOptions.SAVE_BOTH
    );
    TagOptionSingleton.getInstance().setWavSaveOrder(
      WavSaveOrder.INFO_THEN_ID3
    );

    Exception exceptionCaught = null;
    try {
      File testFile = AbstractTestCase.copyAudioToTmp(
        "test123.wav",
        new File("test123ModifyMetadataSaveBothNew.wav")
      );
      AudioFile f = AudioFileIO.read(testFile);
      System.out.println(f.getAudioHeader());
      assertEquals("529", f.getAudioHeader().getBitRate());
      assertEquals("1", f.getAudioHeader().getChannels());
      assertEquals("22050", f.getAudioHeader().getSampleRate());

      assertTrue(f.getTag() instanceof WavTag);
      WavTag tag = (WavTag) f.getTag();

      assertTrue(tag.isExistingInfoTag());

      assertEquals(
        926264L,
        tag.getInfoTag().getStartLocationInFile().longValue()
      );
      assertEquals(
        926560L,
        tag.getInfoTag().getEndLocationInFile().longValue()
      );
      assertEquals(288L, tag.getInfoTag().getSizeOfTag());
      assertEquals(0L, tag.getSizeOfID3TagOnly());
      assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
      assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());
      //Ease of use methods for common fields
      assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

      //Replace Id3tag
      ID3v24Tag id3tag = new ID3v24Tag();
      tag.setID3Tag(id3tag);

      //Modify Value
      tag.setField(FieldKey.ARTIST, "fred");
      f.commit();

      //Read modified metadata now in file
      f = AudioFileIO.read(testFile);
      assertTrue(f.getTag() instanceof WavTag);
      tag = (WavTag) f.getTag();
      System.out.println(tag.getInfoTag());
      assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

      assertEquals(
        926264L,
        tag.getInfoTag().getStartLocationInFile().longValue()
      );
      assertEquals(
        926552L,
        tag.getInfoTag().getEndLocationInFile().longValue()
      );
      assertEquals(280L, tag.getInfoTag().getSizeOfTag());
      assertEquals(10L, tag.getSizeOfID3TagOnly()); //Because have SAVE BOTH option but nothign added to ID3 save empty ID3tag
      assertEquals(926552L, tag.getStartLocationInFileOfId3Chunk());
      assertEquals(18L, tag.getSizeOfID3TagIncludingChunkHeader());
    } catch (Exception e) {
      e.printStackTrace();
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  /**
   * Read file with metadata added by MediaMonkey
   */
  @Test
  public void testModifyFileWithMoreMetadataSaveBoth() {
    TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
    TagOptionSingleton.getInstance().setWavSaveOptions(
      WavSaveOptions.SAVE_BOTH
    );
    TagOptionSingleton.getInstance().setWavSaveOrder(
      WavSaveOrder.INFO_THEN_ID3
    );

    Exception exceptionCaught = null;
    try {
      File testFile = AbstractTestCase.copyAudioToTmp(
        "test123.wav",
        new File("test123ModifyMoreMetadataNew.wav")
      );
      AudioFile f = AudioFileIO.read(testFile);
      System.out.println(f.getAudioHeader());
      assertEquals("529", f.getAudioHeader().getBitRate());
      assertEquals("1", f.getAudioHeader().getChannels());
      assertEquals("22050", f.getAudioHeader().getSampleRate());

      assertTrue(f.getTag() instanceof WavTag);
      WavTag tag = (WavTag) f.getTag();

      assertEquals(
        926264L,
        tag.getInfoTag().getStartLocationInFile().longValue()
      );
      assertEquals(
        926560L,
        tag.getInfoTag().getEndLocationInFile().longValue()
      );
      assertEquals(288L, tag.getInfoTag().getSizeOfTag());
      assertEquals(0L, tag.getSizeOfID3TagOnly());
      assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
      assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

      //Ease of use methods for common fields
      assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

      //Replace Id3tag
      ID3v24Tag id3tag = new ID3v24Tag();
      tag.setID3Tag(id3tag);

      //Modify Value
      tag.setField(
        FieldKey.ARTIST,
        "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"
      );
      tag.setField(
        FieldKey.ALBUM_ARTIST,
        "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"
      );
      f.commit();

      //Read modified metadata now in file
      f = AudioFileIO.read(testFile);
      assertTrue(f.getTag() instanceof WavTag);
      tag = (WavTag) f.getTag();
      System.out.println(tag.getInfoTag());
      assertEquals(
        "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq",
        tag.getFirst(FieldKey.ARTIST)
      );
      assertEquals(
        "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq",
        tag.getFirst(FieldKey.ALBUM_ARTIST)
      );

      assertEquals(
        926264L,
        tag.getInfoTag().getStartLocationInFile().longValue()
      );
      assertEquals(
        926700L,
        tag.getInfoTag().getEndLocationInFile().longValue()
      );
      assertEquals(428L, tag.getInfoTag().getSizeOfTag());
      assertEquals(10L, tag.getSizeOfID3TagOnly()); //Because have SAVE BOTH option but nothign added to ID3 save empty ID3tag
      assertEquals(926700L, tag.getStartLocationInFileOfId3Chunk());
      assertEquals(18L, tag.getSizeOfID3TagIncludingChunkHeader());
    } catch (Exception e) {
      e.printStackTrace();
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  /**
   * Read file with metadata added by MediaMonkey
   */
  @Test
  public void testModifyFileMetadataSaveExistingActiveId3Info() {
    TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
    TagOptionSingleton.getInstance().setWavSaveOptions(
      WavSaveOptions.SAVE_EXISTING_AND_ACTIVE
    );
    TagOptionSingleton.getInstance().setWavSaveOrder(
      WavSaveOrder.INFO_THEN_ID3
    );

    Exception exceptionCaught = null;
    try {
      File testFile = AbstractTestCase.copyAudioToTmp(
        "test123.wav",
        new File("test123ModifyMetadataSaveExistingActiveId3New.wav")
      );
      AudioFile f = AudioFileIO.read(testFile);
      System.out.println(f.getAudioHeader());
      assertEquals("529", f.getAudioHeader().getBitRate());
      assertEquals("1", f.getAudioHeader().getChannels());
      assertEquals("22050", f.getAudioHeader().getSampleRate());

      assertTrue(f.getTag() instanceof WavTag);
      WavTag tag = (WavTag) f.getTag();

      assertTrue(tag.isExistingInfoTag());

      assertEquals(
        926264L,
        tag.getInfoTag().getStartLocationInFile().longValue()
      );
      assertEquals(
        926560L,
        tag.getInfoTag().getEndLocationInFile().longValue()
      );
      assertEquals(288L, tag.getInfoTag().getSizeOfTag());
      assertEquals(0L, tag.getSizeOfID3TagOnly());
      assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
      assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

      //Ease of use methods for common fields
      assertEquals("", tag.getFirst(FieldKey.ARTIST));

      //Replace Id3tag
      ID3v24Tag id3tag = new ID3v24Tag();
      tag.setID3Tag(id3tag);

      //Modify Value
      tag.setField(FieldKey.ARTIST, "fred");
      f.commit();

      //Read modified metadata now in file
      f = AudioFileIO.read(testFile);
      assertTrue(f.getTag() instanceof WavTag);
      tag = (WavTag) f.getTag();
      System.out.println(tag.getInfoTag());
      assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

      assertEquals(
        926264L,
        tag.getInfoTag().getStartLocationInFile().longValue()
      );
      assertEquals(
        926560L,
        tag.getInfoTag().getEndLocationInFile().longValue()
      );
      assertEquals(288L, tag.getInfoTag().getSizeOfTag());
      assertEquals(26L, tag.getSizeOfID3TagOnly());
      assertEquals(926560L, tag.getStartLocationInFileOfId3Chunk());
      assertEquals(34L, tag.getSizeOfID3TagIncludingChunkHeader());
    } catch (Exception e) {
      e.printStackTrace();
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  /**
   * Read file with metadata added by MediaMonkey
   */
  @Test
  public void testModifyFileMetadataSaveActiveId3() {
    TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
    TagOptionSingleton.getInstance().setWavSaveOptions(
      WavSaveOptions.SAVE_ACTIVE
    );
    TagOptionSingleton.getInstance().setWavSaveOrder(
      WavSaveOrder.INFO_THEN_ID3
    );

    Exception exceptionCaught = null;
    try {
      File testFile = AbstractTestCase.copyAudioToTmp(
        "test123.wav",
        new File("test123ModifyMetadataSaveActiveId3New.wav")
      );
      AudioFile f = AudioFileIO.read(testFile);
      System.out.println(f.getAudioHeader());
      assertEquals("529", f.getAudioHeader().getBitRate());
      assertEquals("1", f.getAudioHeader().getChannels());
      assertEquals("22050", f.getAudioHeader().getSampleRate());

      assertTrue(f.getTag() instanceof WavTag);
      WavTag tag = (WavTag) f.getTag();

      assertTrue(tag.isExistingInfoTag());

      assertEquals(
        926264L,
        tag.getInfoTag().getStartLocationInFile().longValue()
      );
      assertEquals(
        926560L,
        tag.getInfoTag().getEndLocationInFile().longValue()
      );
      assertEquals(288L, tag.getInfoTag().getSizeOfTag());
      assertEquals(0L, tag.getSizeOfID3TagOnly());
      assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
      assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

      //Ease of use methods for common fields
      assertEquals("", tag.getFirst(FieldKey.ARTIST));

      //Replace Id3tag
      ID3v24Tag id3tag = new ID3v24Tag();
      tag.setID3Tag(id3tag);

      //Modify Value
      tag.setField(FieldKey.ARTIST, "fred");
      f.commit();

      //Read modified metadata now in file
      f = AudioFileIO.read(testFile);
      assertTrue(f.getTag() instanceof WavTag);
      tag = (WavTag) f.getTag();
      System.out.println(tag.getInfoTag());
      assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

      assertTrue(tag.isInfoTag());
      assertTrue(tag.isID3Tag());
      assertFalse(tag.isExistingInfoTag());
      assertTrue(tag.isExistingId3Tag());

      assertNull(tag.getInfoTag().getStartLocationInFile());
      assertNull(tag.getInfoTag().getEndLocationInFile());
      assertEquals(0L, tag.getInfoTag().getSizeOfTag());
      assertEquals(26L, tag.getSizeOfID3TagOnly());
      assertEquals(926264L, tag.getStartLocationInFileOfId3Chunk());
      assertEquals(34L, tag.getSizeOfID3TagIncludingChunkHeader());
    } catch (Exception e) {
      e.printStackTrace();
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  /**
   * Read file with metadata added by MediaMonkey
   */
  @Test
  public void testModifyFileMetadataSaveActiveId32() {
    TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
    TagOptionSingleton.getInstance().setWavSaveOptions(
      WavSaveOptions.SAVE_ACTIVE
    );
    TagOptionSingleton.getInstance().setWavSaveOrder(
      WavSaveOrder.INFO_THEN_ID3
    );

    Exception exceptionCaught = null;
    try {
      File testFile = AbstractTestCase.copyAudioToTmp(
        "test126.wav",
        new File("test126ModifyMetadataSaveActiveId3New.wav")
      );
      AudioFile f = AudioFileIO.read(testFile);
      System.out.println(f.getAudioHeader());
      assertEquals("529", f.getAudioHeader().getBitRate());
      assertEquals("1", f.getAudioHeader().getChannels());
      assertEquals("22050", f.getAudioHeader().getSampleRate());

      assertTrue(f.getTag() instanceof WavTag);
      WavTag tag = (WavTag) f.getTag();

      assertFalse(tag.isExistingInfoTag());

      assertNull(tag.getInfoTag().getStartLocationInFile());
      assertNull(tag.getInfoTag().getEndLocationInFile());
      assertEquals(0L, tag.getInfoTag().getSizeOfTag());
      assertEquals(25L, tag.getSizeOfID3TagOnly());
      assertEquals(926264L, tag.getStartLocationInFileOfId3Chunk());
      assertEquals(33L, tag.getSizeOfID3TagIncludingChunkHeader());

      //Ease of use methods for common fields
      assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

      //Replace Id3tag
      ID3v24Tag id3tag = new ID3v24Tag();
      tag.setID3Tag(id3tag);

      //Modify Value
      tag.setField(FieldKey.ARTIST, "fred");
      f.commit();

      //Read modified metadata now in file
      f = AudioFileIO.read(testFile);
      assertTrue(f.getTag() instanceof WavTag);
      tag = (WavTag) f.getTag();
      System.out.println(tag.getInfoTag());

      assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

      assertTrue(tag.isInfoTag());
      assertTrue(tag.isID3Tag());
      assertFalse(tag.isExistingInfoTag());
      assertTrue(tag.isExistingId3Tag());

      assertNull(tag.getInfoTag().getStartLocationInFile());
      assertNull(tag.getInfoTag().getEndLocationInFile());
      assertEquals(0L, tag.getInfoTag().getSizeOfTag());
      assertEquals(26L, tag.getSizeOfID3TagOnly());
      assertEquals(926264L, tag.getStartLocationInFileOfId3Chunk());
      assertEquals(34L, tag.getSizeOfID3TagIncludingChunkHeader());
    } catch (Exception e) {
      e.printStackTrace();
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  /**
   * Starts of with Id3chunk which is odd but doesnt have padding byte but at end of file
   * so can still read, then we write to it padding bit added and when read/write again we
   * correctly work out ID3chunk is still at end of file.
   */
  @Test
  public void testFileDeleteWithInfoAndOddLengthData() {
    Exception exceptionCaught = null;

    File orig = new File("testdata", "test129.wav");
    if (!orig.isFile()) {
      System.err.println("Unable to test file - not available");
      return;
    }

    TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
    TagOptionSingleton.getInstance().setWavSaveOptions(
      WavSaveOptions.SAVE_ACTIVE
    );
    TagOptionSingleton.getInstance().setWavSaveOrder(
      WavSaveOrder.INFO_THEN_ID3
    );

    File testFile = AbstractTestCase.copyAudioToTmp(
      "test129.wav",
      new File("test128OddData.wav")
    );
    try {
      AudioFile f = AudioFileIO.read(testFile);
      f.delete();
    } catch (Exception e) {
      e.printStackTrace();
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }
}
