package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.junit.jupiter.api.Test;

public class Issue417Test extends AbstractTestCase {

  /**
   * Multiple WOAR frames ARE allowed
   */
  @Test
  public void testWOARMultiples() {
    Exception caught = null;
    try {
      File testFile = copyAudioToTmp("01.mp3");
      AudioFile af = AudioFileIO.read(testFile);
      af
        .getTagOrCreateAndSetDefault()
        .setField(FieldKey.URL_OFFICIAL_ARTIST_SITE, "http://test1.html");
      assertInstanceOf(ID3v23Tag.class, af.getTag());
      af.commit();
      af = AudioFileIO.read(testFile);
      assertEquals(
        "http://test1.html",
        af.getTag().getFirst(FieldKey.URL_OFFICIAL_ARTIST_SITE)
      );
      af
        .getTag()
        .addField(FieldKey.URL_OFFICIAL_ARTIST_SITE, "http://test2.html");
      af
        .getTag()
        .addField(FieldKey.URL_OFFICIAL_ARTIST_SITE, "http://test3.html");
      af
        .getTag()
        .addField(FieldKey.URL_OFFICIAL_ARTIST_SITE, "http://test4.html");
      af.commit();
      af = AudioFileIO.read(testFile);
      assertEquals(
        "http://test1.html",
        af.getTag().getValue(FieldKey.URL_OFFICIAL_ARTIST_SITE, 0)
      );
      assertEquals(
        "http://test1.html",
        af.getTag().getFirst(FieldKey.URL_OFFICIAL_ARTIST_SITE)
      );
      assertEquals(
        "http://test2.html",
        af.getTag().getValue(FieldKey.URL_OFFICIAL_ARTIST_SITE, 1)
      );

      //No of WOAR Values
      assertEquals(
        4,
        af.getTag().getAll(FieldKey.URL_OFFICIAL_ARTIST_SITE).size()
      );

      //Actual No Of Fields used to store WOAR frames
      assertEquals(
        4,
        af.getTag().getFields(FieldKey.URL_OFFICIAL_ARTIST_SITE).size()
      );
    } catch (Exception e) {
      caught = e;
    }
    assertNull(caught);
  }
}
