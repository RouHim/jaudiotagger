package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import javax.imageio.ImageIO;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.id3.ID3v23Frame;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

public class Issue224Test extends AbstractTestCase {

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testReadInvalidPicture() {
    String genre = null;

    Exception exceptionCaught = null;
    try {
      File testFile = copyAudioToTmp("test31.mp3");
      AudioFile f = AudioFileIO.read(testFile);
      Tag tag = f.getTag();
      assertEquals(11, tag.getFieldCount());
      assertInstanceOf(ID3v23Tag.class, tag);
      ID3v23Tag id3v23Tag = (ID3v23Tag) tag;
      TagField coverArtField = id3v23Tag.getFirstField(
        org.jaudiotagger.tag.id3.ID3v23FieldKey.COVER_ART.getFieldName()
      );
      assertInstanceOf(ID3v23Frame.class, coverArtField);
      assertInstanceOf(FrameBodyAPIC.class, ((ID3v23Frame) coverArtField).getBody());
      FrameBodyAPIC body =
        (FrameBodyAPIC) ((ID3v23Frame) coverArtField).getBody();
      byte[] imageRawData = body.getImageData();
      BufferedImage bi = ImageIO.read(
        ImageIO.createImageInputStream(new ByteArrayInputStream(imageRawData))
      );
      assertEquals(953, bi.getWidth());
      assertEquals(953, bi.getHeight());

      assertEquals("image/png", body.getMimeType());
      assertEquals("", body.getDescription());
      assertEquals("", body.getImageUrl());

      //This is an invalid value (probably first value of PictureType)
      assertEquals(3, body.getPictureType());

      assertFalse(body.isImageUrl());

      //SetDescription
      body.setDescription("FREDDY");
      assertEquals("FREDDY", body.getDescription());
      f.commit();
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
    assertNull(genre);
  }
}
