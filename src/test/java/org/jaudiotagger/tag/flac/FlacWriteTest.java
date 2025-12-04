package org.jaudiotagger.tag.flac;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;
import org.jaudiotagger.FilePermissionsTest;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.flac.FlacInfoReader;
import org.jaudiotagger.audio.flac.metadatablock.MetadataBlockDataPicture;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;
import org.jaudiotagger.tag.reference.PictureTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

public class FlacWriteTest extends FilePermissionsTest {

  @BeforeEach
  public void setUp() {
    TagOptionSingleton.getInstance().setToDefault();
  }

  /**
   * Write flac info to file
   */
  @Test
  public void testWriteAllFieldsToFile() {
    Exception exceptionCaught = null;
    try {
      //Put artifically low just to test it out
      TagOptionSingleton.getInstance().setWriteChunkSize(40000);
      File testFile = copyAudioToTmp(
        "test2.flac",
        "test2write.flac"
      );
      AudioFile f = AudioFileIO.read(testFile);

      System.out.println("startFileSize:" + f.getFile().length());

      assertEquals("192", f.getAudioHeader().getBitRate());
      assertEquals("FLAC 16 bits", f.getAudioHeader().getEncodingType());
      assertEquals("2", f.getAudioHeader().getChannels());
      assertEquals("44100", f.getAudioHeader().getSampleRate());

      assertInstanceOf(FlacTag.class, f.getTag());
      FlacTag tag = (FlacTag) f.getTag();
      assertEquals(
        "reference libFLAC 1.1.4 20070213",
        tag.getFirst(FieldKey.ENCODER)
      );
      assertEquals(
        "reference libFLAC 1.1.4 20070213",
        tag.getVorbisCommentTag().getVendor()
      );
      //No Images
      assertEquals(0, tag.getImages().size());
      FlacInfoReader infoReader = new FlacInfoReader();
      assertEquals(4, infoReader.countMetaBlocks(f.getFile()));

      tag.addField(FieldKey.ARTIST, "artist\u01ff");
      tag.addField(FieldKey.ALBUM, "album");
      tag.addField(FieldKey.TITLE, "title");
      assertEquals(1, tag.getFields(FieldKey.TITLE.name()).size());
      tag.addField(FieldKey.YEAR, "1971");
      assertEquals(1, tag.getFields(FieldKey.YEAR).size());
      tag.addField(FieldKey.TRACK, "2");
      tag.addField(FieldKey.GENRE, "Rock");

      tag.setField(tag.createField(FieldKey.BPM, "123"));
      tag.setField(
        tag.createField(FieldKey.URL_LYRICS_SITE, "http://www.lyrics.fly.com")
      );
      tag.setField(
        tag.createField(
          FieldKey.URL_DISCOGS_ARTIST_SITE,
          "http://www.discogs1.com"
        )
      );
      tag.setField(
        tag.createField(
          FieldKey.URL_DISCOGS_RELEASE_SITE,
          "http://www.discogs2.com"
        )
      );
      tag.setField(
        tag.createField(
          FieldKey.URL_OFFICIAL_ARTIST_SITE,
          "http://www.discogs3.com"
        )
      );
      tag.setField(
        tag.createField(
          FieldKey.URL_OFFICIAL_RELEASE_SITE,
          "http://www.discogs4.com"
        )
      );
      tag.setField(
        tag.createField(
          FieldKey.URL_WIKIPEDIA_ARTIST_SITE,
          "http://www.discogs5.com"
        )
      );
      tag.setField(
        tag.createField(
          FieldKey.URL_WIKIPEDIA_RELEASE_SITE,
          "http://www.discogs6.com"
        )
      );
      tag.setField(tag.createField(FieldKey.TRACK_TOTAL, "11"));
      tag.setField(tag.createField(FieldKey.DISC_TOTAL, "3"));
      //key not known to jaudiotagger
      tag.setField("VIOLINIST", "Sarah Curtis");

      //Add new image
      RandomAccessFile imageFile = new RandomAccessFile(
        fileResource("testdata", "coverart.png"),
        "r"
      );
      byte[] imagedata = new byte[(int) imageFile.length()];
      imageFile.read(imagedata);
      tag.setField(
        tag.createArtworkField(
          imagedata,
          PictureTypes.DEFAULT_ID,
          ImageFormats.MIME_TYPE_PNG,
          "test",
          200,
          200,
          24,
          0
        )
      );

      assertEquals("11", tag.getFirst(FieldKey.TRACK_TOTAL));
      assertEquals("3", tag.getFirst(FieldKey.DISC_TOTAL));

      f.commit();
      f = AudioFileIO.read(testFile);
      assertEquals(5, infoReader.countMetaBlocks(f.getFile()));
      assertInstanceOf(FlacTag.class, f.getTag());

      assertEquals(
        "reference libFLAC 1.1.4 20070213",
        tag.getFirst(FieldKey.ENCODER)
      );
      assertEquals(
        "reference libFLAC 1.1.4 20070213",
        tag.getVorbisCommentTag().getVendor()
      );
      tag.addField(tag.createField(FieldKey.ENCODER, "encoder"));
      assertEquals("encoder", tag.getFirst(FieldKey.ENCODER));

      tag = (FlacTag) f.getTag();
      assertEquals("artist\u01ff", tag.getFirst(FieldKey.ARTIST));
      assertEquals("album", tag.getFirst(FieldKey.ALBUM));
      assertEquals("title", tag.getFirst(FieldKey.TITLE));
      assertEquals("123", tag.getFirst(FieldKey.BPM));
      assertEquals("1971", tag.getFirst(FieldKey.YEAR));
      assertEquals("2", tag.getFirst(FieldKey.TRACK));
      assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
      assertEquals(1, tag.getFields(FieldKey.GENRE).size());
      assertEquals(1, tag.getFields(FieldKey.ARTIST).size());
      assertEquals(1, tag.getFields(FieldKey.ALBUM).size());
      assertEquals(1, tag.getFields(FieldKey.TITLE).size());
      assertEquals(1, tag.getFields(FieldKey.BPM).size());
      assertEquals(1, tag.getFields(FieldKey.YEAR).size());
      assertEquals(1, tag.getFields(FieldKey.TRACK).size());
      //One Image
      assertEquals(1, tag.getFields(FieldKey.COVER_ART.name()).size());
      assertEquals(1, tag.getImages().size());
      MetadataBlockDataPicture pic = tag.getImages().get(0);
      assertEquals((int) PictureTypes.DEFAULT_ID, pic.getPictureType());
      assertEquals(ImageFormats.MIME_TYPE_PNG, pic.getMimeType());
      assertEquals("test", pic.getDescription());
      assertEquals(200, pic.getWidth());
      assertEquals(200, pic.getHeight());
      assertEquals(24, pic.getColourDepth());
      assertEquals(0, pic.getIndexedColourCount());

      assertEquals(
        "http://www.lyrics.fly.com",
        tag.getFirst(FieldKey.URL_LYRICS_SITE)
      );
      assertEquals(
        "http://www.discogs1.com",
        tag.getFirst(FieldKey.URL_DISCOGS_ARTIST_SITE)
      );
      assertEquals(
        "http://www.discogs2.com",
        tag.getFirst(FieldKey.URL_DISCOGS_RELEASE_SITE)
      );
      assertEquals(
        "http://www.discogs3.com",
        tag.getFirst(FieldKey.URL_OFFICIAL_ARTIST_SITE)
      );
      assertEquals(
        "http://www.discogs4.com",
        tag.getFirst(FieldKey.URL_OFFICIAL_RELEASE_SITE)
      );
      assertEquals(
        "http://www.discogs5.com",
        tag.getFirst(FieldKey.URL_WIKIPEDIA_ARTIST_SITE)
      );
      assertEquals(
        "http://www.discogs6.com",
        tag.getFirst(FieldKey.URL_WIKIPEDIA_RELEASE_SITE)
      );
      assertEquals("11", tag.getFirst(FieldKey.TRACK_TOTAL));
      assertEquals("3", tag.getFirst(FieldKey.DISC_TOTAL));
      assertEquals("Sarah Curtis", tag.getFirst("VIOLINIST"));

      System.out.println("NewFileSize:" + f.getFile().length());
      assertEquals(144202, f.getFile().length());
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  @Test
  public void testWriteAllFieldsToFileSmallChunkSize() {
    Exception exceptionCaught = null;
    try {
      //Put artifically low just to test it out
      TagOptionSingleton.getInstance().setWriteChunkSize(1000);
      File testFile = copyAudioToTmp(
        "test2.flac",
        "test2write.flac"
      );
      AudioFile f = AudioFileIO.read(testFile);

      System.out.println("startFileSize:" + f.getFile().length());

      assertEquals("192", f.getAudioHeader().getBitRate());
      assertEquals("FLAC 16 bits", f.getAudioHeader().getEncodingType());
      assertEquals("2", f.getAudioHeader().getChannels());
      assertEquals("44100", f.getAudioHeader().getSampleRate());

      assertInstanceOf(FlacTag.class, f.getTag());
      FlacTag tag = (FlacTag) f.getTag();
      assertEquals(
        "reference libFLAC 1.1.4 20070213",
        tag.getFirst(FieldKey.ENCODER)
      );
      assertEquals(
        "reference libFLAC 1.1.4 20070213",
        tag.getVorbisCommentTag().getVendor()
      );
      //No Images
      assertEquals(0, tag.getImages().size());
      FlacInfoReader infoReader = new FlacInfoReader();
      assertEquals(4, infoReader.countMetaBlocks(f.getFile()));

      tag.addField(FieldKey.ARTIST, "artist\u01ff");
      tag.addField(FieldKey.ALBUM, "album");
      tag.addField(FieldKey.TITLE, "title");
      assertEquals(1, tag.getFields(FieldKey.TITLE.name()).size());
      tag.addField(FieldKey.YEAR, "1971");
      assertEquals(1, tag.getFields(FieldKey.YEAR).size());
      tag.addField(FieldKey.TRACK, "2");
      tag.addField(FieldKey.GENRE, "Rock");

      tag.setField(tag.createField(FieldKey.BPM, "123"));
      tag.setField(
        tag.createField(FieldKey.URL_LYRICS_SITE, "http://www.lyrics.fly.com")
      );
      tag.setField(
        tag.createField(
          FieldKey.URL_DISCOGS_ARTIST_SITE,
          "http://www.discogs1.com"
        )
      );
      tag.setField(
        tag.createField(
          FieldKey.URL_DISCOGS_RELEASE_SITE,
          "http://www.discogs2.com"
        )
      );
      tag.setField(
        tag.createField(
          FieldKey.URL_OFFICIAL_ARTIST_SITE,
          "http://www.discogs3.com"
        )
      );
      tag.setField(
        tag.createField(
          FieldKey.URL_OFFICIAL_RELEASE_SITE,
          "http://www.discogs4.com"
        )
      );
      tag.setField(
        tag.createField(
          FieldKey.URL_WIKIPEDIA_ARTIST_SITE,
          "http://www.discogs5.com"
        )
      );
      tag.setField(
        tag.createField(
          FieldKey.URL_WIKIPEDIA_RELEASE_SITE,
          "http://www.discogs6.com"
        )
      );
      tag.setField(tag.createField(FieldKey.TRACK_TOTAL, "11"));
      tag.setField(tag.createField(FieldKey.DISC_TOTAL, "3"));
      //key not known to jaudiotagger
      tag.setField("VIOLINIST", "Sarah Curtis");

      //Add new image
      RandomAccessFile imageFile = new RandomAccessFile(
        fileResource("testdata", "coverart.png"),
        "r"
      );
      byte[] imagedata = new byte[(int) imageFile.length()];
      imageFile.read(imagedata);
      tag.setField(
        tag.createArtworkField(
          imagedata,
          PictureTypes.DEFAULT_ID,
          ImageFormats.MIME_TYPE_PNG,
          "test",
          200,
          200,
          24,
          0
        )
      );

      assertEquals("11", tag.getFirst(FieldKey.TRACK_TOTAL));
      assertEquals("3", tag.getFirst(FieldKey.DISC_TOTAL));

      f.commit();
      f = AudioFileIO.read(testFile);
      assertEquals(5, infoReader.countMetaBlocks(f.getFile()));
      assertInstanceOf(FlacTag.class, f.getTag());

      assertEquals(
        "reference libFLAC 1.1.4 20070213",
        tag.getFirst(FieldKey.ENCODER)
      );
      assertEquals(
        "reference libFLAC 1.1.4 20070213",
        tag.getVorbisCommentTag().getVendor()
      );
      tag.addField(tag.createField(FieldKey.ENCODER, "encoder"));
      assertEquals("encoder", tag.getFirst(FieldKey.ENCODER));

      tag = (FlacTag) f.getTag();
      assertEquals("artist\u01ff", tag.getFirst(FieldKey.ARTIST));
      assertEquals("album", tag.getFirst(FieldKey.ALBUM));
      assertEquals("title", tag.getFirst(FieldKey.TITLE));
      assertEquals("123", tag.getFirst(FieldKey.BPM));
      assertEquals("1971", tag.getFirst(FieldKey.YEAR));
      assertEquals("2", tag.getFirst(FieldKey.TRACK));
      assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
      assertEquals(1, tag.getFields(FieldKey.GENRE).size());
      assertEquals(1, tag.getFields(FieldKey.ARTIST).size());
      assertEquals(1, tag.getFields(FieldKey.ALBUM).size());
      assertEquals(1, tag.getFields(FieldKey.TITLE).size());
      assertEquals(1, tag.getFields(FieldKey.BPM).size());
      assertEquals(1, tag.getFields(FieldKey.YEAR).size());
      assertEquals(1, tag.getFields(FieldKey.TRACK).size());
      //One Image
      assertEquals(1, tag.getFields(FieldKey.COVER_ART.name()).size());
      assertEquals(1, tag.getImages().size());
      MetadataBlockDataPicture pic = tag.getImages().get(0);
      assertEquals((int) PictureTypes.DEFAULT_ID, pic.getPictureType());
      assertEquals(ImageFormats.MIME_TYPE_PNG, pic.getMimeType());
      assertEquals("test", pic.getDescription());
      assertEquals(200, pic.getWidth());
      assertEquals(200, pic.getHeight());
      assertEquals(24, pic.getColourDepth());
      assertEquals(0, pic.getIndexedColourCount());

      assertEquals(
        "http://www.lyrics.fly.com",
        tag.getFirst(FieldKey.URL_LYRICS_SITE)
      );
      assertEquals(
        "http://www.discogs1.com",
        tag.getFirst(FieldKey.URL_DISCOGS_ARTIST_SITE)
      );
      assertEquals(
        "http://www.discogs2.com",
        tag.getFirst(FieldKey.URL_DISCOGS_RELEASE_SITE)
      );
      assertEquals(
        "http://www.discogs3.com",
        tag.getFirst(FieldKey.URL_OFFICIAL_ARTIST_SITE)
      );
      assertEquals(
        "http://www.discogs4.com",
        tag.getFirst(FieldKey.URL_OFFICIAL_RELEASE_SITE)
      );
      assertEquals(
        "http://www.discogs5.com",
        tag.getFirst(FieldKey.URL_WIKIPEDIA_ARTIST_SITE)
      );
      assertEquals(
        "http://www.discogs6.com",
        tag.getFirst(FieldKey.URL_WIKIPEDIA_RELEASE_SITE)
      );
      assertEquals("11", tag.getFirst(FieldKey.TRACK_TOTAL));
      assertEquals("3", tag.getFirst(FieldKey.DISC_TOTAL));
      assertEquals("Sarah Curtis", tag.getFirst("VIOLINIST"));

      System.out.println("NewFileSize:" + f.getFile().length());
      assertEquals(144202, f.getFile().length());
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  /**
   * Test deleting tag file
   */
  @Test
  public void testDeleteTagFile() {
    Exception exceptionCaught = null;
    try {
      File testFile = copyAudioToTmp(
        "test.flac",
        "testdeletetag.flac"
      );
      AudioFile f = AudioFileIO.read(testFile);

      assertEquals("192", f.getAudioHeader().getBitRate());
      assertEquals("FLAC 16 bits", f.getAudioHeader().getEncodingType());
      assertEquals("2", f.getAudioHeader().getChannels());
      assertEquals("44100", f.getAudioHeader().getSampleRate());
      assertEquals(2, ((FlacTag) f.getTag()).getImages().size());
      assertInstanceOf(FlacTag.class, f.getTag());
      assertFalse(f.getTag().isEmpty());

      AudioFileIO.delete(f);
      f = AudioFileIO.read(testFile);
      assertTrue(f.getTag().isEmpty());
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  /**
   * Test Writing file that contains cuesheet
   */
  @Test
  public void testWriteFileWithCueSheet() {
    Exception exceptionCaught = null;
    try {
      File testFile = copyAudioToTmp(
        "test3.flac",
        "testWriteWithCueSheet.flac"
      );
      AudioFile f = AudioFileIO.read(testFile);
      FlacInfoReader infoReader = new FlacInfoReader();
      assertEquals(5, infoReader.countMetaBlocks(f.getFile()));
      f.getTag().setField(FieldKey.ALBUM, "BLOCK");
      f.commit();
      f = AudioFileIO.read(testFile);
      infoReader = new FlacInfoReader();
      assertEquals("BLOCK", f.getTag().getFirst(FieldKey.ALBUM));
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  /**
   * Test writing to file that contains an ID3 header
   */
  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testWriteFileWithId3Header() {
    Exception exceptionCaught = null;
    try {
      File testFile = copyAudioToTmp(
        "test22.flac",
        "testWriteFlacWithId3.flac"
      );
      AudioFile f = AudioFileIO.read(testFile);
      FlacInfoReader infoReader = new FlacInfoReader();
      assertEquals(4, infoReader.countMetaBlocks(f.getFile()));
      f.getTag().setField(FieldKey.ALBUM, "BLOCK");
      f.commit();
      f = AudioFileIO.read(testFile);
      infoReader = new FlacInfoReader();
      assertEquals(4, infoReader.countMetaBlocks(f.getFile()));
      assertEquals("BLOCK", f.getTag().getFirst(FieldKey.ALBUM));
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  /**
   * Metadata size has increased so that shift required
   */
  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testWriteFileWithId3HeaderAudioShifted() {
    Exception exceptionCaught = null;
    try {
      File testFile = copyAudioToTmp(
        "test22.flac",
        "testWriteFlacWithId3Shifted.flac"
      );
      AudioFile f = AudioFileIO.read(testFile);

      assertEquals("825", f.getAudioHeader().getBitRate());
      assertEquals("FLAC 16 bits", f.getAudioHeader().getEncodingType());
      assertEquals("2", f.getAudioHeader().getChannels());
      assertEquals("44100", f.getAudioHeader().getSampleRate());

      assertInstanceOf(FlacTag.class, f.getTag());
      FlacTag tag = (FlacTag) f.getTag();
      assertEquals(
        "reference libFLAC 1.1.4 20070213",
        tag.getFirst(FieldKey.ENCODER)
      );
      assertEquals(
        "reference libFLAC 1.1.4 20070213",
        tag.getVorbisCommentTag().getVendor()
      );

      //No Images
      assertEquals(0, tag.getImages().size());
      FlacInfoReader infoReader = new FlacInfoReader();
      assertEquals(4, infoReader.countMetaBlocks(f.getFile()));

      tag.setField(FieldKey.ARTIST, "BLOCK");
      tag.addField(FieldKey.ALBUM, "album");
      tag.addField(FieldKey.TITLE, "title");
      tag.addField(FieldKey.YEAR, "1971");
      tag.addField(FieldKey.TRACK, "2");
      tag.addField(FieldKey.GENRE, "Rock");
      tag.setField(tag.createField(FieldKey.BPM, "123"));

      //Add new image
      RandomAccessFile imageFile = new RandomAccessFile(
        fileResource("testdata", "coverart.png"),
        "r"
      );
      byte[] imagedata = new byte[(int) imageFile.length()];
      imageFile.read(imagedata);
      tag.setField(
        tag.createArtworkField(
          imagedata,
          PictureTypes.DEFAULT_ID,
          ImageFormats.MIME_TYPE_PNG,
          "test",
          200,
          200,
          24,
          0
        )
      );
      f.commit();
      f = AudioFileIO.read(testFile);
      assertEquals(5, infoReader.countMetaBlocks(f.getFile()));
      assertInstanceOf(FlacTag.class, f.getTag());
      assertEquals(
        "reference libFLAC 1.1.4 20070213",
        tag.getFirst(FieldKey.ENCODER)
      );
      assertEquals(
        "reference libFLAC 1.1.4 20070213",
        tag.getVorbisCommentTag().getVendor()
      );
      tag = (FlacTag) f.getTag();
      assertEquals("BLOCK", tag.getFirst(FieldKey.ARTIST));
      assertEquals(1, tag.getArtworkList().size());
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  @Test
  public void testDeleteTag() throws Exception {
    File testFile = copyAudioToTmp(
      "test2.flac",
      "testDelete.flac"
    );
    AudioFile f = AudioFileIO.read(testFile);
    AudioFileIO.delete(f);

    f = AudioFileIO.read(testFile);
    assertTrue(f.getTag().isEmpty());
  }

  @Test
  public void testWriteMultipleFields() throws Exception {
    File testFile = copyAudioToTmp(
      "test.flac",
      "testWriteMultiple.flac"
    );
    AudioFile f = AudioFileIO.read(testFile);
    List<TagField> tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
    assertEquals(0, tagFields.size());
    f.getTag().addField(FieldKey.ALBUM_ARTIST_SORT, "artist1");
    f.getTag().addField(FieldKey.ALBUM_ARTIST_SORT, "artist2");
    tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
    assertEquals(2, tagFields.size());
    f.commit();
    f = AudioFileIO.read(testFile);
    tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
    assertEquals(2, tagFields.size());
  }

  @Test
  public void testDeleteFields() throws Exception {
    //Delete using generic key
    File testFile = copyAudioToTmp(
      "test.flac",
      "testWriteMultiple.flac"
    );
    AudioFile f = AudioFileIO.read(testFile);
    List<TagField> tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
    assertEquals(0, tagFields.size());
    f.getTag().addField(FieldKey.ALBUM_ARTIST_SORT, "artist1");
    f.getTag().addField(FieldKey.ALBUM_ARTIST_SORT, "artist2");
    tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
    assertEquals(2, tagFields.size());
    f.getTag().deleteField(FieldKey.ALBUM_ARTIST_SORT);
    f.commit();

    //Delete using flac id
    f = AudioFileIO.read(testFile);
    tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
    assertEquals(0, tagFields.size());
    f.getTag().addField(FieldKey.ALBUM_ARTIST_SORT, "artist1");
    f.getTag().addField(FieldKey.ALBUM_ARTIST_SORT, "artist2");
    tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
    assertEquals(2, tagFields.size());
    f.getTag().deleteField("ALBUMARTISTSORT");
    tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
    assertEquals(0, tagFields.size());
    f.commit();

    f = AudioFileIO.read(testFile);
    tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
    assertEquals(0, tagFields.size());
  }

  /**
   * test read flac file with just streaminfo and padding header
   */
  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testWriteFileThatOnlyHadStreamAndPaddingInfoHeader() {
    Exception exceptionCaught = null;
    try {
      File testFile = copyAudioToTmp(
        "test102.flac",
        "test102.flac"
      );
      AudioFile f = AudioFileIO.read(testFile);
      FlacInfoReader infoReader = new FlacInfoReader();
      assertEquals(2, infoReader.countMetaBlocks(f.getFile()));
      f.getTag().setField(FieldKey.ARTIST, "fred");
      f.commit();

      f = AudioFileIO.read(testFile);

      infoReader = new FlacInfoReader();
      assertEquals(3, infoReader.countMetaBlocks(f.getFile()));
      assertEquals("fred", f.getTag().getFirst(FieldKey.ARTIST));
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  @Test
  public void testWriteWriteProtectedFileWithCheckDisabled() throws Exception {
    runWriteWriteProtectedFileWithCheckDisabled(
      "test2.flac"
    );
  }

  @Test
  public void testWriteWriteProtectedFileWithCheckEnabled() throws Exception {
    runWriteWriteProtectedFileWithCheckEnabled(
      "test2.flac"
    );
  }

  @Test
  public void testWriteReadOnlyFileWithCheckDisabled() throws Exception {
    runWriteReadOnlyFileWithCheckDisabled("test2.flac");
  }
}
