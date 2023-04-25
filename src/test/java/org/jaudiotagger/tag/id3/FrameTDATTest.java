package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTDAT;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTDRC;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTIME;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTYER;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.reference.ID3V2Version;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class FrameTDATTest extends AbstractTestCase {
    @Test
    public void testID3Specific() {
        Exception e = null;
        try {
            ID3v23Tag tag = new ID3v23Tag();
            ID3v23Frame frame = new ID3v23Frame("TDAT");
            frame.setBody(new FrameBodyTDAT(TextEncoding.ISO_8859_1, "3006"));
            tag.addFrame(frame);
            assertEquals("3006", tag.getFirst("TDAT"));

            ID3v24Tag v24tag = new ID3v24Tag(tag);
            assertEquals(1, v24tag.getFieldCount());
            assertNotNull(v24tag.getFirst("TDAT"));
            assertEquals("-06-30", v24tag.getFirst("TDRC"));
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }

    @Test
    public void testID3SpecificWithYearAndTime() {
        Exception e = null;
        try {
            ID3v23Tag tag = new ID3v23Tag();
            ID3v23Frame frame = new ID3v23Frame("TDAT");
            frame.setBody(new FrameBodyTDAT(TextEncoding.ISO_8859_1, "3006"));
            tag.addFrame(frame);
            assertEquals("3006", tag.getFirst("TDAT"));

            ID3v23Frame frameYear = new ID3v23Frame("TYER");
            frameYear.setBody(new FrameBodyTYER(TextEncoding.ISO_8859_1, "1980"));
            tag.addFrame(frameYear);
            assertEquals("1980", tag.getFirst("TYER"));

            ID3v23Frame frameTime = new ID3v23Frame("TIME");
            frameTime.setBody(new FrameBodyTIME(TextEncoding.ISO_8859_1, "1200"));
            tag.addFrame(frameTime);
            assertEquals("1200", tag.getFirst("TIME"));
            assertEquals(3, tag.getFieldCount());

            //Create v24tag from v23, all these time frames shouod be merged into one
            ID3v24Tag v24tag = new ID3v24Tag(tag);
            assertEquals(1, v24tag.getFieldCount());
            assertNotNull(v24tag.getFirst("TDAT"));
            assertNotNull(v24tag.getFirst("TIME"));
            assertNotNull(v24tag.getFirst("TYER"));
            assertEquals("1980-06-30T12:00", v24tag.getFirst("TDRC"));

            //Now create v23tag from v24, the tdrc frame should be split up and the values of the individual
            //values should match the v23 format not simply break up the v24 string
            tag = new ID3v23Tag(v24tag);
            assertEquals(3, tag.getFieldCount());
            assertEquals("3006", tag.getFirst("TDAT"));
            assertEquals("1980", tag.getFirst("TYER"));
            assertEquals("1200", tag.getFirst("TIME"));

            //Do it again to check it works second time around
            v24tag = new ID3v24Tag(tag);
            assertEquals(1, v24tag.getFieldCount());
            assertNotNull(v24tag.getFirst("TDAT"));
            assertNotNull(v24tag.getFirst("TIME"));
            assertNotNull(v24tag.getFirst("TYER"));
            assertEquals("1980-06-30T12:00", v24tag.getFirst("TDRC"));
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }

    @Test
    public void testConvertingPartialDate() {
        Exception e = null;
        try {
            ID3v24Tag tag = new ID3v24Tag();
            ID3v24Frame frame = new ID3v24Frame("TDRC");
            frame.setBody(new FrameBodyTDRC(TextEncoding.ISO_8859_1, "2006-06"));
            tag.addFrame(frame);
            assertEquals("2006-06", tag.getFirst("TDRC"));

            ID3v23Tag v23tag = new ID3v23Tag(tag);
            assertEquals(2, v23tag.getFieldCount());
            assertNotNull(v23tag.getFirst("TYER"));
            assertEquals("2006", v23tag.getFirst("TYER"));
            assertNotNull(v23tag.getFirst("TDAT"));
            //"01" is created because cant just store month in this field in v23
            assertEquals("0106", v23tag.getFirst("TDAT"));

            tag = new ID3v24Tag(v23tag);
            //But because is MonthOnly flag set dd gets lost when convert back to v24
            assertEquals("2006-06", tag.getFirst("TDRC"));

        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }

    @Test
    public void testReadingID3AsV24Generic() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("id3asv24.mp3"));
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
        AudioFile af = AudioFileIO.read(testFile);
        af.getTagAndConvertOrCreateAndSetDefault();
        af.getTag().setField(FieldKey.ARTIST, "fred");
        af.getTag().setField(FieldKey.YEAR, "2003-06-23");
        af.commit();
        assertEquals(af.getTag().getFirst(FieldKey.YEAR), "2003-06-23");
        af = AudioFileIO.read(testFile);
        assertEquals(af.getTag().getFirst(FieldKey.ARTIST), "fred");
        assertEquals(af.getTag().getAll(FieldKey.ARTIST).get(0), "fred");
        assertEquals(af.getTag().getFirst(FieldKey.YEAR), "2003-06-23");
        assertEquals(af.getTag().getAll(FieldKey.YEAR).get(0), "2003-06-23");
        assertEquals(((MP3File) af).getID3v2TagAsv24().getFirst(FieldKey.YEAR), "2003-06-23");
        assertEquals(((MP3File) af).getID3v2TagAsv24().getAll(FieldKey.YEAR).get(0), "2003-06-23");

    }


}