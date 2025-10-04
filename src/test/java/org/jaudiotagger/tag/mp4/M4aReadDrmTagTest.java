package org.jaudiotagger.tag.mp4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.util.List;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp4.EncoderType;
import org.jaudiotagger.audio.mp4.Mp4AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jcodec.containers.mp4.boxes.EsdsBox;
import org.junit.jupiter.api.Test;

public class M4aReadDrmTagTest {

  /**
   * Test to read all metadata from an Apple iTunes encoded mp4 file, note also uses fixed genre rather than
   * custom genre
   */
  @Test
  public void testReadFile() {
    Exception exceptionCaught = null;
    try {
      File orig = new File("testdata", "test9.m4p");
      if (!orig.isFile()) {
        return;
      }

      File testFile = AbstractTestCase.copyAudioToTmp("test9.m4p");
      AudioFile f = AudioFileIO.read(testFile);
      Tag tag = f.getTag();

      System.out.println(f.getAudioHeader());
      System.out.println(tag);

      //AudioInfo
      //Time in seconds
      assertEquals(329, f.getAudioHeader().getTrackLength());
      assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());
      assertEquals("2", f.getAudioHeader().getChannels());
      assertEquals(128, f.getAudioHeader().getBitRateAsNumber());
      assertEquals(
        EncoderType.DRM_AAC.getDescription(),
        f.getAudioHeader().getEncodingType()
      );

      //MPEG Specific
      Mp4AudioHeader audioheader = (Mp4AudioHeader) f.getAudioHeader();
      assertEquals(EsdsBox.Kind.MPEG4_AUDIO, audioheader.getKind());
      assertEquals(
        EsdsBox.AudioProfile.LOW_COMPLEXITY,
        audioheader.getProfile()
      );

      //Ease of use methods for common fields
      assertEquals("The King Of The Slums", tag.getFirst(FieldKey.ARTIST));
      assertEquals("Barbarous English Fayre", tag.getFirst(FieldKey.ALBUM));
      assertEquals("Simpering Blonde Bombshell", tag.getFirst(FieldKey.TITLE));
      assertEquals("1990-01-01T08:00:00Z", tag.getFirst(FieldKey.YEAR));
      assertEquals("1", tag.getFirst(FieldKey.TRACK));
      assertEquals("12", tag.getFirst(FieldKey.TRACK_TOTAL));
      assertEquals("Rock", tag.getFirst(FieldKey.GENRE));

      //Cast to format specific tag
      Mp4Tag mp4tag = (Mp4Tag) tag;

      //Lookup by mp4 key
      assertEquals(
        "The King Of The Slums",
        mp4tag.getFirst(Mp4FieldKey.ARTIST)
      );
      assertEquals(
        "Barbarous English Fayre",
        mp4tag.getFirst(Mp4FieldKey.ALBUM)
      );
      assertEquals(
        "Simpering Blonde Bombshell",
        mp4tag.getFirst(Mp4FieldKey.TITLE)
      );
      List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
      //Should be one image
      assertEquals(1, coverart.size());
    } catch (Exception e) {
      e.printStackTrace();
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }
}
