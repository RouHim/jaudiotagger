package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;
import org.jaudiotagger.tag.images.Images;
import org.junit.jupiter.api.Test;

public class Issue286Test extends AbstractTestCase {

  /*
   * TestRead Vorbis COverArt One
   * @throws Exception
   */
  @Test
  public void testReadVorbisCoverartOne() throws Exception {
    File file = fileResource("testdata", "test76.ogg");
    AudioFile af = AudioFileIO.read(file);
    assertEquals(1, af.getTag().getArtworkList().size());
    Artwork artwork = af.getTag().getFirstArtwork();
    System.out.println(artwork);
    assertEquals(600, Images.getImage(artwork).getWidth());
    assertEquals(800, Images.getImage(artwork).getHeight());
    assertEquals("image/jpeg", artwork.getMimeType());
    assertEquals(3, artwork.getPictureType());
  }

  /*
   * TestRead Vorbis CoverArt Two
   * @throws Exception
   */
  @Test
  public void testReadVorbisCoverartTwo() throws Exception {
    File file = fileResource("testdata", "test77.ogg");
    AudioFile af = AudioFileIO.read(file);
    assertEquals(1, af.getTag().getArtworkList().size());
    Artwork artwork = af.getTag().getFirstArtwork();
    System.out.println(artwork);
    assertEquals(600, Images.getImage(artwork).getWidth());
    assertEquals(800, Images.getImage(artwork).getHeight());
    assertEquals("image/jpeg", artwork.getMimeType());
    assertEquals(3, artwork.getPictureType());
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
      assertEquals("", artwork.getDescription());
      assertEquals(200, Images.getImage(artwork).getWidth());

      //Now add new image
      Artwork newartwork = ArtworkFactory.createArtworkFromFile(
        fileResource("testdata", "coverart.png")
      );
      newartwork.setDescription("A new file");
      assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));
      tag.addField(newartwork);
      af.commit();
      af = AudioFileIO.read(testFile);
      tag = af.getTag();
      assertEquals(2, tag.getArtworkList().size());

      assertInstanceOf(Artwork.class, tag.getArtworkList().get(0));
      artwork = tag.getFirstArtwork();
      assertEquals("image/png", artwork.getMimeType());
      assertNotNull(artwork.getImage());
      assertEquals("", artwork.getDescription());
      assertEquals(200, Images.getImage(artwork).getWidth());

      assertInstanceOf(Artwork.class, tag.getArtworkList().get(1));
      artwork = tag.getArtworkList().get(1);
      assertEquals("image/png", artwork.getMimeType());
      assertNotNull(artwork.getImage());
      assertEquals("A new file", artwork.getDescription());
      assertEquals(200, Images.getImage(artwork).getWidth());
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertNull(exceptionCaught);
  }
}
