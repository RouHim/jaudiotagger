package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTPOS;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class Issue412Test extends AbstractTestCase {

    @Test
    public void testTXXXSameDescription() {
        Exception caught = null;
        try {
            File testFile = copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrCreateAndSetDefault().setField(FieldKey.BARCODE, "BARCODE1");
            assertInstanceOf(ID3v23Tag.class, af.getTag());
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("BARCODE1", af.getTag().getFirst(FieldKey.BARCODE));
            af.getTag().addField(FieldKey.BARCODE, "BARCODE2");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("BARCODE1", af.getTag().getValue(FieldKey.BARCODE, 0));
            assertEquals("BARCODE1", af.getTag().getFirst(FieldKey.BARCODE));
            assertEquals("BARCODE2", af.getTag().getValue(FieldKey.BARCODE, 1));

            //No of Barcode Values
            assertEquals(2, af.getTag().getAll(FieldKey.BARCODE).size());

            //Actual No Of Fields used to store barcode, Should be only one
            assertEquals(1, af.getTag().getFields(FieldKey.BARCODE).size());
        } catch (Exception e) {
            caught = e;
        }
        assertNull(caught);
    }

    @Test
    public void testTXXXDifferentDescription() {
        Exception caught = null;
        try {
            File testFile = copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrCreateAndSetDefault().setField(FieldKey.BARCODE, "BARCODE1");
            assertInstanceOf(ID3v23Tag.class, af.getTag());
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("BARCODE1", af.getTag().getFirst(FieldKey.BARCODE));
            af.getTag().addField(FieldKey.CATALOG_NO, "CATALOGNO");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("BARCODE1", af.getTag().getValue(FieldKey.BARCODE, 0));
            assertEquals("BARCODE1", af.getTag().getFirst(FieldKey.BARCODE));
            assertEquals("CATALOGNO", af.getTag().getValue(FieldKey.CATALOG_NO, 0));

            //No of Barcode Values
            assertEquals(1, af.getTag().getAll(FieldKey.BARCODE).size());

            //Actual No Of Fields used to store barcode, Should be only one
            assertEquals(1, af.getTag().getFields(FieldKey.BARCODE).size());

            //No of Catalog Values
            assertEquals(1, af.getTag().getAll(FieldKey.CATALOG_NO).size());

            //Actual No Of Fields used to store barcode, Should be only one
            assertEquals(1, af.getTag().getFields(FieldKey.CATALOG_NO).size());
        } catch (Exception e) {
            caught = e;
        }
        assertNull(caught);
    }

    @Test
    public void testWXXXSameDescription() {
        Exception caught = null;
        try {
            File testFile = copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af
                    .getTagOrCreateAndSetDefault()
                    .setField(
                            FieldKey.URL_DISCOGS_ARTIST_SITE,
                            "http://www.wrathrecords.co.uk/afarm.htm"
                    );
            assertInstanceOf(ID3v23Tag.class, af.getTag());
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals(
                    "http://www.wrathrecords.co.uk/afarm.htm",
                    af.getTag().getFirst(FieldKey.URL_DISCOGS_ARTIST_SITE)
            );
            af
                    .getTag()
                    .addField(
                            FieldKey.URL_DISCOGS_ARTIST_SITE,
                            "http://www.wrathrecords.co.uk/bfarm.htm"
                    );
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals(
                    "http://www.wrathrecords.co.uk/afarm.htm",
                    af.getTag().getValue(FieldKey.URL_DISCOGS_ARTIST_SITE, 0)
            );
            assertEquals(
                    "http://www.wrathrecords.co.uk/afarm.htm",
                    af.getTag().getFirst(FieldKey.URL_DISCOGS_ARTIST_SITE)
            );
            assertEquals(
                    "http://www.wrathrecords.co.uk/bfarm.htm",
                    af.getTag().getValue(FieldKey.URL_DISCOGS_ARTIST_SITE, 1)
            );

            //No of Url Values
            assertEquals(
                    2,
                    af.getTag().getAll(FieldKey.URL_DISCOGS_ARTIST_SITE).size()
            );

            //Actual No Of Fields used to store urls, Should be only one
            assertEquals(
                    1,
                    af.getTag().getFields(FieldKey.URL_DISCOGS_ARTIST_SITE).size()
            );
        } catch (Exception e) {
            caught = e;
        }
        assertNull(caught);
    }

    @Test
    public void testTXXXSameDescriptionMultiples() {
        Exception caught = null;
        try {
            File testFile = copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrCreateAndSetDefault().setField(FieldKey.BARCODE, "BARCODE1");
            assertInstanceOf(ID3v23Tag.class, af.getTag());
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("BARCODE1", af.getTag().getFirst(FieldKey.BARCODE));
            af.getTag().addField(FieldKey.BARCODE, "BARCODE2");
            af.getTag().addField(FieldKey.CATALOG_NO, "CATNO");
            af.getTag().addField(FieldKey.ARTISTS, "ARTISTS");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("BARCODE1", af.getTag().getValue(FieldKey.BARCODE, 0));
            assertEquals("BARCODE1", af.getTag().getFirst(FieldKey.BARCODE));
            assertEquals("BARCODE2", af.getTag().getValue(FieldKey.BARCODE, 1));

            //No of Barcode Values
            assertEquals(2, af.getTag().getAll(FieldKey.BARCODE).size());

            //Actual No Of Fields used to store barcode, Should be only one
            assertEquals(1, af.getTag().getFields(FieldKey.BARCODE).size());
        } catch (Exception e) {
            caught = e;
        }
        assertNull(caught);
    }

    @Test
    public void testWXXXSameDescriptionMultiples() {
        Exception caught = null;
        try {
            File testFile = copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af
                    .getTagOrCreateAndSetDefault()
                    .setField(FieldKey.URL_DISCOGS_ARTIST_SITE, "BARCODE1");
            assertInstanceOf(ID3v23Tag.class, af.getTag());
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals(
                    "BARCODE1",
                    af.getTag().getFirst(FieldKey.URL_DISCOGS_ARTIST_SITE)
            );
            af.getTag().addField(FieldKey.URL_DISCOGS_ARTIST_SITE, "BARCODE2");
            af.getTag().addField(FieldKey.URL_WIKIPEDIA_ARTIST_SITE, "CATNO");
            af.getTag().addField(FieldKey.URL_WIKIPEDIA_RELEASE_SITE, "ARTISTS");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals(
                    "BARCODE1",
                    af.getTag().getValue(FieldKey.URL_DISCOGS_ARTIST_SITE, 0)
            );
            assertEquals(
                    "BARCODE1",
                    af.getTag().getFirst(FieldKey.URL_DISCOGS_ARTIST_SITE)
            );
            assertEquals(
                    "BARCODE2",
                    af.getTag().getValue(FieldKey.URL_DISCOGS_ARTIST_SITE, 1)
            );

            //No of Barcode Values
            assertEquals(
                    2,
                    af.getTag().getAll(FieldKey.URL_DISCOGS_ARTIST_SITE).size()
            );

            //Actual No Of Fields used to store barcode, Should be only one
            assertEquals(
                    1,
                    af.getTag().getFields(FieldKey.URL_DISCOGS_ARTIST_SITE).size()
            );
        } catch (Exception e) {
            caught = e;
        }
        assertNull(caught);
    }

    @Test
    public void testTCOMMultiples() {
        Exception caught = null;
        try {
            File testFile = copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrCreateAndSetDefault().setField(FieldKey.COMPOSER, "composer1");
            assertInstanceOf(ID3v23Tag.class, af.getTag());
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("composer1", af.getTag().getFirst(FieldKey.COMPOSER));
            af.getTag().addField(FieldKey.COMPOSER, "composer2");
            af.getTag().addField(FieldKey.COMPOSER, "composer3");
            af.getTag().addField(FieldKey.COMPOSER, "composer4");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("composer1", af.getTag().getValue(FieldKey.COMPOSER, 0));
            assertEquals("composer1", af.getTag().getFirst(FieldKey.COMPOSER));
            assertEquals("composer2", af.getTag().getValue(FieldKey.COMPOSER, 1));

            //No of Composer Values
            assertEquals(4, af.getTag().getAll(FieldKey.COMPOSER).size());

            //Actual No Of Fields used to store barcode, Should be only one
            assertEquals(1, af.getTag().getFields(FieldKey.COMPOSER).size());
        } catch (Exception e) {
            caught = e;
        }
        assertNull(caught);
    }

    @Test
    public void testTrackNoTotalCombinations() {
        Exception caught = null;
        try {
            File orig = fileResource("testdata", "01.mp3");

            File testFile = copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrCreateAndSetDefault();
            af.commit();
            af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();

            tag.deleteField(FieldKey.TRACK);
            tag.setField(FieldKey.TRACK, "1");
            tag.deleteField(FieldKey.TRACK_TOTAL);
            tag.setField(FieldKey.TRACK_TOTAL, "11");
            af.commit();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals("1", tag.getFirst(FieldKey.TRACK));
            assertEquals("11", tag.getFirst(FieldKey.TRACK_TOTAL));
        } catch (Exception e) {
            caught = e;
        }
        assertNull(caught);
    }

    @Test
    public void testTrackNoTotalAddCombinations() {
        Exception caught = null;
        try {
            File testFile = copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrCreateAndSetDefault();
            af.commit();
            af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();

            tag.deleteField(FieldKey.TRACK);
            tag.addField(FieldKey.TRACK, "1");
            tag.deleteField(FieldKey.TRACK_TOTAL);
            tag.addField(FieldKey.TRACK_TOTAL, "11");
            af.commit();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals("1", tag.getFirst(FieldKey.TRACK));
            assertEquals("11", tag.getFirst(FieldKey.TRACK_TOTAL));
        } catch (Exception e) {
            caught = e;
        }
        assertNull(caught);
    }

    @Test
    public void testDiscNoTotalCombinations() {
        Exception caught = null;
        try {
            File testFile = copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrCreateAndSetDefault();
            af.commit();
            af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();

            tag.deleteField(FieldKey.DISC_NO);
            tag.setField(FieldKey.DISC_NO, "1");
            tag.deleteField(FieldKey.DISC_TOTAL);
            tag.setField(FieldKey.DISC_TOTAL, "11");
            af.commit();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            assertEquals("11", tag.getFirst(FieldKey.DISC_TOTAL));
        } catch (Exception e) {
            caught = e;
        }
        assertNull(caught);
    }

    @Test
    public void testDiscNoTotalAddCombinations() {
        Exception caught = null;
        try {
            File testFile = copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrCreateAndSetDefault();
            af.commit();
            af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();

            tag.deleteField(FieldKey.DISC_NO);
            tag.addField(FieldKey.DISC_NO, "1");
            tag.deleteField(FieldKey.DISC_TOTAL);
            tag.addField(FieldKey.DISC_TOTAL, "11");
            af.commit();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            ID3v23Tag v23tag = (ID3v23Tag) tag;
            AbstractID3v2Frame frame = (AbstractID3v2Frame) v23tag.getFrame("TPOS");
            FrameBodyTPOS fbBody = (FrameBodyTPOS) frame.getBody();
            assertEquals(1, fbBody.getDiscNo().intValue());
            assertEquals(11, fbBody.getDiscTotal().intValue());

            assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            assertEquals("11", tag.getFirst(FieldKey.DISC_TOTAL));
        } catch (Exception e) {
            caught = e;
        }
        assertNull(caught);
    }

    @Test
    public void testDiscNoTotalAddCombinationsWithPadding() {
        Exception caught = null;
        try {
            TagOptionSingleton.getInstance().setPadNumbers(true);
            File testFile = copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrCreateAndSetDefault();
            af.commit();
            af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();
            ID3v23Tag v23tag = (ID3v23Tag) tag;

            tag.deleteField(FieldKey.DISC_NO);
            tag.addField(FieldKey.DISC_NO, "1");
            tag.deleteField(FieldKey.DISC_TOTAL);
            tag.addField(FieldKey.DISC_TOTAL, "11");

            af.commit();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            v23tag = (ID3v23Tag) tag;
            AbstractID3v2Frame frame = (AbstractID3v2Frame) v23tag.getFrame("TPOS");
            FrameBodyTPOS fbBody = (FrameBodyTPOS) frame.getBody();
            assertEquals(1, fbBody.getDiscNo().intValue());
            assertEquals(11, fbBody.getDiscTotal().intValue());
            assertEquals("01/11", fbBody.getText());

            assertEquals("01", tag.getFirst(FieldKey.DISC_NO));
            assertEquals("11", tag.getFirst(FieldKey.DISC_TOTAL));
        } catch (Exception e) {
            caught = e;
        }
        assertNull(caught);
    }

    @Test
    public void testDiscNoTotalAddCombinationsWithPaddingFlac() {
        Exception caught = null;
        try {
            TagOptionSingleton.getInstance().setPadNumbers(true);
            File testFile = copyAudioToTmp("test.flac");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrCreateAndSetDefault();
            af.commit();
            af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();

            tag.deleteField(FieldKey.DISC_NO);
            tag.addField(FieldKey.DISC_NO, "1");
            tag.deleteField(FieldKey.DISC_TOTAL);
            tag.addField(FieldKey.DISC_TOTAL, "11");
            af.commit();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            assertEquals("11", tag.getFirst(FieldKey.DISC_TOTAL));
        } catch (Exception e) {
            caught = e;
        }
        assertNull(caught);
    }
}
