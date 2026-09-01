package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.audio.mp3.MPEGFrameHeader;
import org.jaudiotagger.logging.Hex;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;
import org.jaudiotagger.tag.images.Images;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

public class Issue294Test extends AbstractTestCase {

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testSavingArtworkToMp3File() {

    File testFile = copyAudioToTmp("test70.mp3");
    File testPix = copyAudioToTmp("test70.jpg");

    Exception exceptionCaught = null;
    try {
      TagOptionSingleton.getInstance().setUnsyncTags(false);
      //Read and save changes
      MP3File af = (MP3File) AudioFileIO.read(testFile);

      //File is corrupt
      assertEquals(1, af.getTag().getArtworkList().size());
      Artwork artwork = af.getTag().getFirstArtwork();
      assertEquals("image/jpeg", artwork.getMimeType());
      assertTrue(ImageFormats.isPortableFormat(artwork.getBinaryData()));

      //Delete and commit
      //af.getTag().deleteArtworkField();
      //af.commit();

      //af.getID3v2TagAsv24().removeFrame("APIC");

      final List multiFrames = new ArrayList();
      multiFrames.add(
        af
          .getID3v2Tag()
          .createField(ArtworkFactory.createArtworkFromFile(testPix))
      );
      af.getID3v2Tag().setFrame("APIC", multiFrames);
      af.commit();

      //Can we read this image file
      artwork = ArtworkFactory.createArtworkFromFile(testPix);
      assertEquals(118145, artwork.getBinaryData().length);
      assertEquals(
        0xFF,
        (int) artwork.getBinaryData()[0] & MPEGFrameHeader.SYNC_BYTE1
      );
      assertEquals(
        0xD8,
        (int) artwork.getBinaryData()[1] & MPEGFrameHeader.SYNC_BYTE1
      );
      assertEquals(
        0xFF,
        (int) artwork.getBinaryData()[2] & MPEGFrameHeader.SYNC_BYTE1
      );
      assertEquals(
        0xE0,
        (int) artwork.getBinaryData()[3] & MPEGFrameHeader.SYNC_BYTE1
      );
      assertEquals("image/jpeg", artwork.getMimeType());
      assertNotNull(artwork.getImage());
      assertEquals(500, Images.getImage(artwork).getHeight());
      assertEquals(500, Images.getImage(artwork).getWidth());

      byte[] origData = artwork.getBinaryData();

      af = (MP3File) AudioFileIO.read(testFile);
      assertEquals(1, af.getTag().getArtworkList().size());
      artwork = af.getTag().getArtworkList().get(0);
      assertEquals(118145, artwork.getBinaryData().length);
      assertEquals(
        0xFF,
        (int) artwork.getBinaryData()[0] & MPEGFrameHeader.SYNC_BYTE1
      );
      assertEquals(
        0xD8,
        (int) artwork.getBinaryData()[1] & MPEGFrameHeader.SYNC_BYTE1
      );
      assertEquals(
        0xFF,
        (int) artwork.getBinaryData()[2] & MPEGFrameHeader.SYNC_BYTE1
      );
      assertEquals(
        0xE0,
        (int) artwork.getBinaryData()[3] & MPEGFrameHeader.SYNC_BYTE1
      );
      assertEquals("image/jpeg", artwork.getMimeType());

      for (int i = 0; i < origData.length; i++) {
        if (origData[i] != artwork.getBinaryData()[i]) {
          System.out.println(
            "Index " +
              i +
              ":" +
              Hex.asHex(i & MPEGFrameHeader.SYNC_BYTE1) +
              ":" +
              Hex.asHex(origData[i] & MPEGFrameHeader.SYNC_BYTE1) +
              ":" +
              Hex.asHex(artwork.getBinaryData()[i] & MPEGFrameHeader.SYNC_BYTE1)
          );
          break;
        }
      }
      assertArrayEquals(origData, artwork.getBinaryData());
      assertNotNull(artwork.getImage());
      assertEquals(500, Images.getImage(artwork).getHeight());
      assertEquals(500, Images.getImage(artwork).getWidth());
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }
}
