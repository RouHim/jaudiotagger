package org.jaudiotagger.audio.dsf;

import org.jaudiotagger.AbstractBaseTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.reference.ID3V2Version;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class DsfAudioFileTest extends AbstractBaseTestCase {

    @Test
    public void testReadDsfTag() {
        Exception exceptionCaught = null;


        File testFile = copyAudioToTmp(
                "test122.dsf",
                "test122read.dsf"
        );
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertEquals("DSF", ah.getEncodingType());
            assertEquals("5644800", ah.getBitRate());
            assertEquals(5644800, ah.getBitRateAsNumber());
            assertEquals("2", ah.getChannels());
            assertEquals("2822400", ah.getSampleRate());
            assertEquals(5, ah.getTrackLength());
            assertFalse(ah.isLossless());
            Tag tag = f.getTag();
            System.out.println(tag);
            assertEquals("Artist", tag.getFirst(FieldKey.ARTIST));
            assertEquals("test3", tag.getFirst(FieldKey.TITLE));
            assertEquals("Album", tag.getFirst(FieldKey.ALBUM));
            assertEquals("Album Artist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            assertEquals("Crossover", tag.getFirst(FieldKey.GENRE));
            assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            assertEquals("Publisher", tag.getFirst(FieldKey.RECORD_LABEL));
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    @Test
    public void testWriteDsfTag() {
        Exception exceptionCaught = null;


        File testFile = copyAudioToTmp(
                "test122.dsf",
                "test122write.dsf"
        );
        try {
            AudioFile f = AudioFileIO.read(testFile);
            f.getTag().addField(FieldKey.ARTIST, "fred");
            Tag tag = f.getTag();
            System.out.println(tag);
            tag.setField(FieldKey.ARTIST, "fred");
            f.commit();

            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            System.out.println(tag);
            assertEquals("fred", tag.getFirst(FieldKey.ARTIST));
            assertEquals("test3", tag.getFirst(FieldKey.TITLE));
            assertEquals("Album", tag.getFirst(FieldKey.ALBUM));
            assertEquals("Album Artist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            assertEquals("Crossover", tag.getFirst(FieldKey.GENRE));
            assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            assertEquals("Publisher", tag.getFirst(FieldKey.RECORD_LABEL));
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    @Test
    public void testDeleteDsfTag() {
        Exception exceptionCaught = null;


        File testFile = copyAudioToTmp(
                "test122.dsf",
                "test122delete.dsf"
        );
        try {
            AudioFile f = AudioFileIO.read(testFile);
            f.getTag().addField(FieldKey.ARTIST, "fred");
            Tag tag = f.getTag();
            System.out.println(tag);
            f.delete();

            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            System.out.println(tag);
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    @Test
    public void testReadDsfNoTag() {
        Exception exceptionCaught = null;


        File testFile = copyAudioToTmp(
                "test156.dsf",
                "test156read.dsf"
        );
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            System.out.println(ah);
            assertEquals("5644800", ah.getBitRate());
            assertEquals(5644800, ah.getBitRateAsNumber());
            assertEquals("2", ah.getChannels());
            assertEquals("2822400", ah.getSampleRate());
            assertEquals(5, ah.getTrackLength());
            assertFalse(ah.isLossless());
            Tag tag = f.getTag();
            assertNull(tag);
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    @Test
    public void testWriteDsfNoTag() {
        Exception exceptionCaught = null;


        File testFile = copyAudioToTmp(
                "test156.dsf",
                "test156write.dsf"
        );
        try {
            AudioFile f = AudioFileIO.read(testFile);
            assertNull(f.getTag());
            f.getTagOrCreateAndSetDefault().addField(FieldKey.ARTIST, "fred");
            Tag tag = f.getTag();
            System.out.println(tag);
            tag.setField(FieldKey.ARTIST, "fred");
            f.commit();

            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            System.out.println(tag);
            assertEquals("fred", tag.getFirst(FieldKey.ARTIST));
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    @Test
    public void testDeleteDsfNoTag() {
        Exception exceptionCaught = null;


        File testFile = copyAudioToTmp(
                "test156.dsf",
                "test156delete.dsf"
        );
        try {
            AudioFile f = AudioFileIO.read(testFile);
            assertNull(f.getTag());
            f.getTagOrCreateAndSetDefault().addField(FieldKey.ARTIST, "fred");
            Tag tag = f.getTag();
            System.out.println(tag);
            f.delete();

            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            System.out.println(tag);
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    @Test
    public void testCreateDefaultTag() throws Exception {

        {
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
            File testFile = copyAudioToTmp(
                    "test122.dsf",
                    "test122read.dsf"
            );
            assertInstanceOf(ID3v24Tag.class, AudioFileIO.read(testFile).createDefaultTag());
        }

        {
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
            File testFile = copyAudioToTmp(
                    "test122.dsf",
                    "test122read.dsf"
            );
            assertInstanceOf(ID3v23Tag.class, AudioFileIO.read(testFile).createDefaultTag());
        }

        {
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V22);
            File testFile = copyAudioToTmp(
                    "test122.dsf",
                    "test122read.dsf"
            );
            assertInstanceOf(ID3v22Tag.class, AudioFileIO.read(testFile).createDefaultTag());
        }

        TagOptionSingleton.getInstance().setToDefault();
    }

  /*
    @Test
    public void testRemoveTagData() throws Exception
    {
        File dir = "C:\\Users\\Paul\\Music\\1983 - David Bowie - Let's Dance [SACD DSF][2003]";
        for(File file:dir.listFiles())
        {
            AudioFile af = AudioFileIO.read(file);
            af.delete();
        }
    }
*/
}
