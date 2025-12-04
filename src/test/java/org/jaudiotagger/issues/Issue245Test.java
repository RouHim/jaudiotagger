package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.wav.WavOptions;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.asf.AsfTagCoverField;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;
import org.jaudiotagger.tag.images.Images;
import org.junit.jupiter.api.Test;

public class Issue245Test extends AbstractTestCase {

  /**
   * Test writing Artwork  to Mp3 ID3v24
   */
  @Test
  public void testWriteArtworkFieldsToMp3ID3v24() {
    File testFile = null;
    Exception exceptionCaught = null;
    try {
      testFile = copyAudioToTmp("testV1.mp3");

      //Read File okay
      AudioFile af = AudioFileIO.read(testFile);
      af.setTag(new ID3v24Tag());
      Tag tag = af.getTag();

      assertEquals(0, tag.getArtworkList().size());

      //Now addField the image
      Artwork newartwork = ArtworkFactory.createArtworkFromFile(
        fileResource("testdata", "coverart.png")
      );
      assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

      newartwork.setPictureType(5);
      tag.setField(newartwork);
      af.commit();
      af = AudioFileIO.read(testFile);
      tag = af.getTag();
      assertEquals(1, tag.getArtworkList().size());
      assertInstanceOf(Artwork.class, tag.getArtworkList().get(0));
      Artwork artwork = tag.getFirstArtwork();
      assertEquals("image/png", artwork.getMimeType());
      assertNotNull(artwork.getImage());
      assertEquals(200, Images.getImage(artwork).getWidth());
      assertEquals(5, artwork.getPictureType());

      tag.deleteArtworkField();
      assertEquals(0, tag.getArtworkList().size());
      af.commit();
      af = AudioFileIO.read(testFile);
      tag = af.getTag();
      assertEquals(0, tag.getArtworkList().size());
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertNull(exceptionCaught);
  }

  /**
   * Test writing Artwork  to Mp3 ID3v23
   */
  @Test
  public void testWriteArtworkFieldsToMp3ID3v23() {
    File testFile = null;
    Exception exceptionCaught = null;
    try {
      testFile = copyAudioToTmp("testV1.mp3");

      //Read File okay
      AudioFile af = AudioFileIO.read(testFile);
      af.getTagOrCreateAndSetDefault();
      Tag tag = af.getTag();

      assertEquals(0, tag.getArtworkList().size());

      //Now addField the image
      Artwork newartwork = ArtworkFactory.createArtworkFromFile(
        fileResource("testdata", "coverart.png")
      );
      assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

      newartwork.setPictureType(11);
      tag.setField(newartwork);
      af.commit();
      af = AudioFileIO.read(testFile);
      tag = af.getTag();
      assertEquals(1, tag.getArtworkList().size());
      assertInstanceOf(Artwork.class, tag.getArtworkList().get(0));
      Artwork artwork = tag.getFirstArtwork();
      assertEquals("image/png", artwork.getMimeType());
      assertNotNull(artwork.getImage());
      assertEquals(200, Images.getImage(artwork).getWidth());
      assertEquals(11, artwork.getPictureType());

      tag.deleteArtworkField();
      assertEquals(0, tag.getArtworkList().size());
      af.commit();
      af = AudioFileIO.read(testFile);
      tag = af.getTag();
      assertEquals(0, tag.getArtworkList().size());
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertNull(exceptionCaught);
  }

  /**
   * Test writing Artwork  to Mp3 ID3v22
   */
  @Test
  public void testWriteArtworkFieldsToMp3ID3v22() {
    File testFile = null;
    Exception exceptionCaught = null;
    try {
      testFile = copyAudioToTmp("testV1.mp3");

      //Read File okay
      AudioFile af = AudioFileIO.read(testFile);
      af.setTag(new ID3v22Tag());
      Tag tag = af.getTag();

      assertEquals(0, tag.getArtworkList().size());

      //Now addField the image
      Artwork newartwork = ArtworkFactory.createArtworkFromFile(
        fileResource("testdata", "coverart.png")
      );
      assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

      newartwork.setPictureType(5);
      tag.setField(newartwork);
      af.commit();
      af = AudioFileIO.read(testFile);
      tag = af.getTag();
      assertEquals(1, tag.getArtworkList().size());
      assertInstanceOf(Artwork.class, tag.getArtworkList().get(0));
      Artwork artwork = tag.getFirstArtwork();
      assertEquals("image/png", artwork.getMimeType());
      assertNotNull(artwork.getImage());
      assertEquals(200, Images.getImage(artwork).getWidth());
      assertEquals(5, artwork.getPictureType());

      tag.deleteArtworkField();
      assertEquals(0, tag.getArtworkList().size());
      af.commit();
      af = AudioFileIO.read(testFile);
      tag = af.getTag();
      assertEquals(0, tag.getArtworkList().size());
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertNull(exceptionCaught);
  }

  /**
   * Test reading/writing artwork to Ogg
   */
  @Test
  public void testReadWriteArtworkFieldsToOggVorbis() {
    File testFile = null;
    Exception exceptionCaught = null;
    try {
      testFile = copyAudioToTmp("test3.ogg");

      //Read File okay
      AudioFile af = AudioFileIO.read(testFile);
      Tag tag = af.getTag();

      assertEquals(1, tag.getArtworkList().size());
      assertInstanceOf(Artwork.class, tag.getArtworkList().get(0));
      Artwork artwork = tag.getFirstArtwork();
      assertEquals("image/png", artwork.getMimeType());
      assertNotNull(artwork.getImage());
      assertEquals(200, Images.getImage(artwork).getWidth());

      //Now replace the image
      Artwork newartwork = ArtworkFactory.createArtworkFromFile(
        fileResource("testdata", "coverart.png")
      );
      assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));
      tag.setField(newartwork);
      af.commit();
      af = AudioFileIO.read(testFile);
      tag = af.getTag();
      assertEquals(1, tag.getArtworkList().size());
      assertInstanceOf(Artwork.class, tag.getArtworkList().get(0));
      artwork = tag.getFirstArtwork();
      assertEquals("image/png", artwork.getMimeType());
      assertNotNull(artwork.getImage());
      assertEquals(200, Images.getImage(artwork).getWidth());

      tag.deleteArtworkField();
      assertEquals(0, tag.getArtworkList().size());
      af.commit();
      af = AudioFileIO.read(testFile);
      tag = af.getTag();
      assertEquals(0, tag.getArtworkList().size());
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertNull(exceptionCaught);
  }

  /**
   * Test reading/writing artwork to Flac
   */
  @Test
  public void testReadWriteArtworkFieldsToFlac() {
    File testFile = null;
    Exception exceptionCaught = null;
    try {
      testFile = copyAudioToTmp(
        "test.flac",
        "testwriteartwork.flac"
      );

      //Read File okay
      AudioFile af = AudioFileIO.read(testFile);
      Tag tag = af.getTag();

      assertEquals(2, tag.getArtworkList().size());
      assertInstanceOf(Artwork.class, tag.getArtworkList().get(0));
      Artwork artwork = tag.getFirstArtwork();
      assertEquals("image/png", artwork.getMimeType());
      assertNotNull(artwork.getImage());
      assertEquals(200, Images.getImage(artwork).getWidth());
      assertEquals(3, artwork.getPictureType());
      //Now replace the image
      Artwork newartwork = ArtworkFactory.createArtworkFromFile(
        fileResource("testdata", "coverart.png")
      );
      assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

      newartwork.setDescription("freddy");
      newartwork.setPictureType(7);
      tag.setField(newartwork);
      af.commit();
      af = AudioFileIO.read(testFile);
      tag = af.getTag();
      assertEquals(2, tag.getArtworkList().size());
      assertInstanceOf(Artwork.class, tag.getArtworkList().get(0));
      artwork = tag.getFirstArtwork();
      assertEquals("image/png", artwork.getMimeType());
      assertNotNull(artwork.getImage());
      assertEquals(200, Images.getImage(artwork).getWidth());
      assertEquals(7, artwork.getPictureType());

      tag.deleteArtworkField();
      assertEquals(0, tag.getArtworkList().size());
      af.commit();
      af = AudioFileIO.read(testFile);
      tag = af.getTag();
      assertEquals(0, tag.getArtworkList().size());
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertNull(exceptionCaught);
  }

  /**
   * Test reading/writing artwork to Wma
   */
  @Test
  public void testReadWriteArtworkFieldsToWma() {
    File testFile = null;
    Exception exceptionCaught = null;
    try {
      testFile = copyAudioToTmp("test5.wma");

      //Read File okay
      AudioFile af = AudioFileIO.read(testFile);
      Tag tag = af.getTag();

      assertEquals(1, tag.getArtworkList().size());
      assertInstanceOf(Artwork.class, tag.getArtworkList().get(0));
      Artwork artwork = tag.getFirstArtwork();
      assertEquals("image/png", artwork.getMimeType());
      assertNotNull(artwork.getImage());
      assertEquals(200, Images.getImage(artwork).getWidth());
      assertEquals(3, artwork.getPictureType());
      //Now replace the image
      Artwork newartwork = ArtworkFactory.createArtworkFromFile(
        fileResource("testdata", "coverart.png")
      );
      assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

      newartwork.setDescription("freddy");
      newartwork.setPictureType(8);
      tag.setField(newartwork);
      af.commit();
      af = AudioFileIO.read(testFile);
      tag = af.getTag();
      assertInstanceOf(AsfTagCoverField.class, tag.getFirstField(FieldKey.COVER_ART));
      assertEquals(1, tag.getArtworkList().size());
      assertInstanceOf(Artwork.class, tag.getArtworkList().get(0));
      artwork = tag.getFirstArtwork();
      assertEquals("image/png", artwork.getMimeType());
      assertNotNull(artwork.getImage());
      assertEquals(200, Images.getImage(artwork).getWidth());
      assertEquals(8, artwork.getPictureType());

      tag.deleteArtworkField();
      assertEquals(0, tag.getArtworkList().size());
      af.commit();
      af = AudioFileIO.read(testFile);
      tag = af.getTag();
      assertEquals(0, tag.getArtworkList().size());
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertNull(exceptionCaught);
  }

  /**
   * Test reading/writing artwork to Mp4
   */
  @Test
  public void testReadWriteArtworkFieldsToMp4() {
    File testFile = null;
    Exception exceptionCaught = null;
    try {
      testFile = copyAudioToTmp("test.m4a");

      //Read File okay
      AudioFile af = AudioFileIO.read(testFile);
      Tag tag = af.getTag();

      assertEquals(1, tag.getArtworkList().size());
      assertInstanceOf(Artwork.class, tag.getArtworkList().get(0));
      Artwork artwork = tag.getFirstArtwork();
      assertEquals("image/jpeg", artwork.getMimeType());
      assertNotNull(artwork.getImage());
      assertEquals(159, Images.getImage(artwork).getWidth());

      //Now replace the image
      Artwork newartwork = ArtworkFactory.createArtworkFromFile(
        fileResource("testdata", "coverart.png")
      );
      assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

      tag.setField(newartwork);
      af.commit();
      af = AudioFileIO.read(testFile);
      tag = af.getTag();
      assertEquals(1, tag.getArtworkList().size());
      assertInstanceOf(Artwork.class, tag.getArtworkList().get(0));
      artwork = tag.getFirstArtwork();
      assertEquals("image/png", artwork.getMimeType());
      assertNotNull(artwork.getImage());
      assertEquals(200, Images.getImage(artwork).getWidth());

      tag.deleteArtworkField();
      assertEquals(0, tag.getArtworkList().size());
      af.commit();
      af = AudioFileIO.read(testFile);
      tag = af.getTag();
      assertEquals(0, tag.getArtworkList().size());
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertNull(exceptionCaught);
  }

  /**
   * Test Artwork cannot be written to Wav Info Chunk
   */
  @Test
  public void testReadWriteArtworkFieldsToWav() {
    TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
    File testFile = null;
    Exception exceptionCaught = null;
    try {
      testFile = copyAudioToTmp("test.wav");

      //Read File okay
      AudioFile af = AudioFileIO.read(testFile);
      Tag tag = af.getTag();

      assertEquals(0, tag.getArtworkList().size());
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertNull(exceptionCaught);

    try {
      //Now try and addField image
      AudioFile af = AudioFileIO.read(testFile);
      Artwork newartwork = ArtworkFactory.createArtworkFromFile(
        fileResource("testdata", "coverart.png")
      );
      assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

      Tag tag = af.getTag();
      tag.setField(newartwork);
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNotNull(exceptionCaught);
    assertInstanceOf(UnsupportedOperationException.class, exceptionCaught);

    //Not Supported
    try {
      //Now try and addField image
      AudioFile af = AudioFileIO.read(testFile);
      Tag tag = af.getTag();
      tag.deleteArtworkField();
      assertEquals(0, tag.getArtworkList().size());
      af.commit();
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNotNull(exceptionCaught);
    assertInstanceOf(UnsupportedOperationException.class, exceptionCaught);
  }

  /**
   * Test Artwork cannot be written to Real
   */
  @Test
  public void testReadWriteArtworkFieldsToReal() {
    File testFile = null;
    Exception exceptionCaught = null;
    try {
      testFile = copyAudioToTmp("test01.ra");

      //Read File okay
      AudioFile af = AudioFileIO.read(testFile);
      Tag tag = af.getTag();

      assertEquals(0, tag.getArtworkList().size());
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertNull(exceptionCaught);

    try {
      //Now try and addField image
      AudioFile af = AudioFileIO.read(testFile);
      Artwork newartwork = ArtworkFactory.createArtworkFromFile(
        fileResource("testdata", "coverart.png")
      );
      assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

      Tag tag = af.getTag();
      tag.setField(newartwork);
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNotNull(exceptionCaught);
    assertInstanceOf(UnsupportedOperationException.class, exceptionCaught);

    //Not supported
    try {
      AudioFile af = AudioFileIO.read(testFile);
      Tag tag = af.getTag();
      tag.deleteArtworkField();
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNotNull(exceptionCaught);
    assertInstanceOf(UnsupportedOperationException.class, exceptionCaught);
  }
}
