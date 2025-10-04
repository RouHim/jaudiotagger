package org.jaudiotagger.tag.vorbiscomment;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.Base64;
import java.util.List;
import javax.imageio.ImageIO;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.images.Artwork;
import org.junit.jupiter.api.Test;

public class VorbisImageTest extends AbstractTestCase {

  /**
   * Test can read file with base64 encoded image from ogg
   * <p/>
   * Works
   */
  @Test
  public void testReadFileWithSmallImageTag() {
    Exception exceptionCaught = null;
    try {
      File testFile = AbstractTestCase.copyAudioToTmp("testsmallimage.ogg");
      AudioFile f = AudioFileIO.read(testFile);
      String mimeType = ((VorbisCommentTag) f.getTag()).getFirst(
        VorbisCommentFieldKey.COVERARTMIME
      );
      assertEquals("image/jpeg", mimeType);
      if ((mimeType != null) & (mimeType.length() > 0)) {
        String imageRawData = ((VorbisCommentTag) f.getTag()).getFirst(
          VorbisCommentFieldKey.COVERART
        );
        assertEquals(22972, imageRawData.length());
      }
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  /**
   * Test can read file with base64 encoded image thats spans multiple ogg pages
   * <p/>
   * Fails:Doesnt give error but doesnt read image
   */
  @Test
  public void testReadFileWithLargeImageTag() {
    Exception exceptionCaught = null;
    try {
      File testFile = AbstractTestCase.copyAudioToTmp("testlargeimage.ogg");
      AudioFile f = AudioFileIO.read(testFile);
      String mimeType = ((VorbisCommentTag) f.getTag()).getFirst(
        VorbisCommentFieldKey.COVERARTMIME
      );
      assertEquals("image/jpeg", mimeType);
      if ((mimeType != null) & (mimeType.length() > 0)) {
        String imageRawData = ((VorbisCommentTag) f.getTag()).getFirst(
          VorbisCommentFieldKey.COVERART
        );
        assertEquals(1013576, imageRawData.length());
      }
    } catch (Exception e) {
      e.printStackTrace();
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  /**
   * Write and read image using lowest level methods
   */
  @Test
  public void testWriteImage1() {
    try {
      File testFile = AbstractTestCase.copyAudioToTmp(
        "test.ogg",
        new File("testWriteImage1.ogg")
      );
      AudioFile f = AudioFileIO.read(testFile);
      VorbisCommentTag tag = (VorbisCommentTag) f.getTag();

      //Add new image, requires two fields in oggVorbis with data in  base64 encoded form
      RandomAccessFile imageFile = new RandomAccessFile(
        new File("testdata", "coverart.png"),
        "r"
      );
      byte[] imagedata = new byte[(int) imageFile.length()];
      imageFile.read(imagedata);
      String base64image = Base64.getEncoder().encodeToString(imagedata);
      tag.setField(
        tag.createField(VorbisCommentFieldKey.COVERART, base64image)
      );
      tag.setField(
        tag.createField(VorbisCommentFieldKey.COVERARTMIME, "image/png")
      );
      f.commit();

      f = AudioFileIO.read(testFile);
      tag = (VorbisCommentTag) f.getTag();

      //VorbisImage base64 image, and reconstruct
      assertEquals(base64image, tag.getFirst(VorbisCommentFieldKey.COVERART));
      assertEquals(
        "image/png",
        tag.getFirst(VorbisCommentFieldKey.COVERARTMIME)
      );
      BufferedImage bi = ImageIO.read(
        ImageIO.createImageInputStream(
          new ByteArrayInputStream(
            Base64.getDecoder().decode(
              tag.getFirst(VorbisCommentFieldKey.COVERART)
            )
          )
        )
      );
      assertNotNull(bi);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Write Image using new method, read using lowlevel
   */
  @Test
  public void testWriteImage2() {
    try {
      File testFile = AbstractTestCase.copyAudioToTmp(
        "test.ogg",
        new File("testWriteImage2.ogg")
      );
      AudioFile f = AudioFileIO.read(testFile);
      VorbisCommentTag tag = (VorbisCommentTag) f.getTag();

      //Add new image using purpose built method
      RandomAccessFile imageFile = new RandomAccessFile(
        new File("testdata", "coverart.png"),
        "r"
      );
      byte[] imagedata = new byte[(int) imageFile.length()];
      imageFile.read(imagedata);

      tag.setArtworkField(imagedata, "image/png");
      f.commit();

      f = AudioFileIO.read(testFile);
      tag = (VorbisCommentTag) f.getTag();

      //VorbisImage base64 image, and reconstruct
      String base64image = Base64.getEncoder().encodeToString(imagedata);
      assertEquals(base64image, tag.getFirst(VorbisCommentFieldKey.COVERART));
      assertEquals(
        "image/png",
        tag.getFirst(VorbisCommentFieldKey.COVERARTMIME)
      );
      BufferedImage bi = ImageIO.read(
        ImageIO.createImageInputStream(
          new ByteArrayInputStream(
            Base64.getDecoder().decode(
              tag.getFirst(VorbisCommentFieldKey.COVERART)
            )
          )
        )
      );
      assertNotNull(bi);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Write Image using lowlevel , read using new method
   */
  @Test
  public void testWriteImage3() {
    try {
      File testFile = AbstractTestCase.copyAudioToTmp(
        "test.ogg",
        new File("testWriteImage3.ogg")
      );
      AudioFile f = AudioFileIO.read(testFile);
      VorbisCommentTag tag = (VorbisCommentTag) f.getTag();

      //Add new image, requires two fields in oggVorbis with data in  base64 encoded form
      RandomAccessFile imageFile = new RandomAccessFile(
        new File("testdata", "coverart.png"),
        "r"
      );
      byte[] imagedata = new byte[(int) imageFile.length()];
      imageFile.read(imagedata);
      String base64image = Base64.getEncoder().encodeToString(imagedata);
      tag.setField(
        tag.createField(VorbisCommentFieldKey.COVERART, base64image)
      );
      tag.setField(
        tag.createField(VorbisCommentFieldKey.COVERARTMIME, "image/png")
      );
      f.commit();

      f = AudioFileIO.read(testFile);
      tag = (VorbisCommentTag) f.getTag();

      //VorbisImage base64 image, and reconstruct
      assertEquals("image/png", tag.getArtworkMimeType());
      byte[] newImageData = tag.getArtworkBinaryData();
      BufferedImage bi = ImageIO.read(
        ImageIO.createImageInputStream(new ByteArrayInputStream(newImageData))
      );
      assertNotNull(bi);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Test can read file with base64 encoded image which has newlines in it
   */
  @Test
  public void testReadFileWithNewlinesInBase64() {
    File orig = new File("testdata", "testnewlineimage.small.ogg");
    if (!orig.isFile()) {
      System.err.println("Unable to test file - not available");
      return;
    }

    Exception exceptionCaught = null;
    try {
      File testFile = AbstractTestCase.copyAudioToTmp(
        "testnewlineimage.small.ogg"
      );
      AudioFile f = AudioFileIO.read(testFile);
      List<Artwork> artwork = f.getTag().getArtworkList();
      assertEquals(1, artwork.size());
      final Artwork next = artwork.iterator().next();
      final FileOutputStream fos = new FileOutputStream(new File("test.jpg"));
      for (byte b : next.getBinaryData()) {
        fos.write(b);
      }
      fos.close();
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }
}
