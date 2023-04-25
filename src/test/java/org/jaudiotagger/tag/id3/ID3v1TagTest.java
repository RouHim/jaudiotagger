package org.jaudiotagger.tag.id3;


import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.TagTextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class ID3v1TagTest {

    public static final String ARTIST = "artist";
    public static final String ALBUM = "album";
    public static final String COMMENT = "comment";
    public static final String TITLE = "title";
    public static final String TRACK_VALUE = "10";
    public static final String GENRE_VAL = "Country";
    public static final String YEAR = "1971";

    /**
     * Provides an initialised object to be used in other tests
     * to prevent code duplication
     *
     * @return ID3v1Tag
     */
    public static ID3v1Tag getInitialisedTag() {
        ID3v1Tag v1Tag = new ID3v1Tag();
        v1Tag.setArtist(ID3v1TagTest.ARTIST);
        v1Tag.setAlbum(ID3v1TagTest.ALBUM);
        v1Tag.setComment(ID3v1TagTest.COMMENT);
        v1Tag.setTitle(ID3v1TagTest.TITLE);
        v1Tag.setGenre(ID3v1TagTest.GENRE_VAL);
        v1Tag.setYear(ID3v1TagTest.YEAR);
        return v1Tag;
    }


    /////////////////////////////////////////////////////////////////////////
    // TestCase classes to override
    /////////////////////////////////////////////////////////////////////////

    /**
     *
     */
    @BeforeEach
    protected void setUp() {
        TagOptionSingleton.getInstance().setToDefault();
    }

    /**
     *
     */
    protected void tearDown() {
    }


    /**
     *
     */
//    protected void runTest()
//    {
//    }

    /////////////////////////////////////////////////////////////////////////
    // Tests
    /////////////////////////////////////////////////////////////////////////
    @Test
    public void testCreateID3v1Tag() {
        ID3v1Tag v1Tag = new ID3v1Tag();
        v1Tag.setArtist(ID3v1TagTest.ARTIST);
        v1Tag.setAlbum(ID3v1TagTest.ALBUM);
        v1Tag.setComment(ID3v1TagTest.COMMENT);
        v1Tag.setTitle(ID3v1TagTest.TITLE);
        v1Tag.setGenre(ID3v1TagTest.GENRE_VAL);
        v1Tag.setYear(ID3v1TagTest.YEAR);

        assertEquals((byte) 1, v1Tag.getRelease());
        assertEquals((byte) 0, v1Tag.getMajorVersion());
        assertEquals((byte) 0, v1Tag.getRevision());

        //Check with old interface
        assertEquals(ID3v1TagTest.ARTIST, v1Tag.getFirst(FieldKey.ARTIST));
        assertEquals(ID3v1TagTest.ALBUM, v1Tag.getFirst(FieldKey.ALBUM));
        assertEquals(ID3v1TagTest.COMMENT, v1Tag.getFirstComment());
        assertEquals(ID3v1TagTest.TITLE, v1Tag.getFirst(FieldKey.TITLE));
        assertEquals(ID3v1TagTest.GENRE_VAL, v1Tag.getFirst(FieldKey.GENRE));
        assertEquals(ID3v1TagTest.YEAR, v1Tag.getFirst(FieldKey.YEAR));

        //Check with entagged interface
        assertEquals(ID3v1TagTest.ARTIST, ((TagTextField) v1Tag.getArtist().get(0)).getContent());
        assertEquals(ID3v1TagTest.ALBUM, ((TagTextField) v1Tag.getAlbum().get(0)).getContent());
        assertEquals(ID3v1TagTest.COMMENT, ((TagTextField) v1Tag.getComment().get(0)).getContent());
        assertEquals(ID3v1TagTest.TITLE, ((TagTextField) v1Tag.getTitle().get(0)).getContent());
        assertEquals(ID3v1TagTest.GENRE_VAL, ((TagTextField) v1Tag.getGenre().get(0)).getContent());
        assertEquals(ID3v1TagTest.YEAR, ((TagTextField) v1Tag.getYear().get(0)).getContent());
    }

    @Test
    public void testSaveID3v1TagToFile() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create v1 Tag
        ID3v1Tag tag = new ID3v1Tag();
        tag.setArtist(ID3v1TagTest.ARTIST);
        tag.setAlbum(ID3v1TagTest.ALBUM);
        tag.setComment(ID3v1TagTest.COMMENT);
        tag.setTitle(ID3v1TagTest.TITLE);
        tag.setGenre(ID3v1TagTest.GENRE_VAL);
        tag.setYear(ID3v1TagTest.YEAR);

        //Save tag to file
        mp3File.setID3v1Tag(tag);
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        tag = mp3File.getID3v1Tag();
        assertEquals(ID3v1TagTest.ARTIST, tag.getFirst(FieldKey.ARTIST));
        assertEquals(ID3v1TagTest.ALBUM, tag.getFirst(FieldKey.ALBUM));
        assertEquals(ID3v1TagTest.COMMENT, tag.getFirstComment());
        assertEquals(ID3v1TagTest.TITLE, tag.getFirst(FieldKey.TITLE));
        assertEquals(ID3v1TagTest.GENRE_VAL, tag.getFirst(FieldKey.GENRE));
        assertEquals(ID3v1TagTest.YEAR, tag.getFirst(FieldKey.YEAR));

        tag.setField(FieldKey.TRACK, "3");
        mp3File.save();
        mp3File = new MP3File(testFile);
        tag = mp3File.getID3v1Tag();
        assertEquals("", tag.getFirst(FieldKey.TRACK));


    }


    @Test
    public void testSaveID3v1TagToFileUsingTagInterface() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        AudioFile file = AudioFileIO.read(testFile);

        //Create v1 Tag
        Tag tag = file.getTag();
        if (tag == null) {
            file.setTag(new ID3v1Tag());
            tag = file.getTag();
        }
        tag.setField(FieldKey.ARTIST, ID3v1TagTest.ARTIST);
        tag.setField(FieldKey.ALBUM, ID3v1TagTest.ALBUM);
        tag.setField(FieldKey.COMMENT, ID3v1TagTest.COMMENT);
        tag.setField(FieldKey.TITLE, ID3v1TagTest.TITLE);
        tag.setField(FieldKey.GENRE, ID3v1TagTest.GENRE_VAL);
        tag.setField(FieldKey.YEAR, ID3v1TagTest.YEAR);

        //Save tag changes to file
        file.setTag(tag);
        file.commit();

        //Reload
        file = AudioFileIO.read(testFile);
        tag = file.getTag();
        assertEquals(ID3v1TagTest.ARTIST, tag.getFirst(FieldKey.ARTIST));
        assertEquals(ID3v1TagTest.ALBUM, tag.getFirst(FieldKey.ALBUM));
        assertEquals(ID3v1TagTest.COMMENT, tag.getFirst(FieldKey.COMMENT));
        assertEquals(ID3v1TagTest.TITLE, tag.getFirst(FieldKey.TITLE));
        assertEquals(ID3v1TagTest.GENRE_VAL, tag.getFirst(FieldKey.GENRE));
        assertEquals(ID3v1TagTest.YEAR, tag.getFirst(FieldKey.YEAR));
    }

    @Test
    public void testCreateID3v1FromID3v24() {
        ID3v24Tag v2Tag = new ID3v24Tag();
        ID3v1Tag v1Tag = new ID3v1Tag(v2Tag);
        assertNotNull(v1Tag);
        assertEquals((byte) 1, v1Tag.getRelease());
        assertEquals((byte) 0, v1Tag.getMajorVersion());
        assertEquals((byte) 0, v1Tag.getRevision());


    }

    @Test
    public void testCreateID3v1FromID3v23() {
        ID3v23Tag v2Tag = new ID3v23Tag();
        ID3v1Tag v1Tag = new ID3v1Tag(v2Tag);
        assertNotNull(v1Tag);
        assertEquals((byte) 1, v1Tag.getRelease());
        assertEquals((byte) 0, v1Tag.getMajorVersion());
        assertEquals((byte) 0, v1Tag.getRevision());

    }

    @Test
    public void testCreateID3v1FromID3v22() {
        ID3v22Tag v2Tag = new ID3v22Tag();
        ID3v1Tag v1Tag = new ID3v1Tag(v2Tag);
        assertNotNull(v1Tag);
        assertEquals((byte) 1, v1Tag.getRelease());
        assertEquals((byte) 0, v1Tag.getMajorVersion());
        assertEquals((byte) 0, v1Tag.getRevision());
    }

    @Test
    public void testNewInterface() {
        Exception exceptionCaught = null;
        ID3v1Tag v1Tag = new ID3v1Tag();
        assertTrue(v1Tag.isEmpty());

        v1Tag.setField(new ID3v1TagField(FieldKey.ARTIST.name(), "artist"));
        assertEquals("artist", ((TagTextField) v1Tag.getFields(FieldKey.ARTIST).get(0)).getContent());
        assertEquals("artist", ((TagTextField) v1Tag.getFirstField(FieldKey.ARTIST.name())).getContent());
        assertEquals("artist", ((TagTextField) (v1Tag.getFields(FieldKey.ARTIST.name()).get(0))).getContent());

        v1Tag.setField(new ID3v1TagField(FieldKey.ALBUM.name(), "album"));
        assertEquals("album", ((TagTextField) v1Tag.getFields(FieldKey.ALBUM).get(0)).getContent());
        assertEquals("album", ((TagTextField) v1Tag.getFirstField(FieldKey.ALBUM.name())).getContent());

        v1Tag.setField(new ID3v1TagField(FieldKey.TITLE.name(), "title"));
        assertEquals("title", ((TagTextField) v1Tag.getFields(FieldKey.TITLE).get(0)).getContent());
        assertEquals("title", ((TagTextField) v1Tag.getFirstField(FieldKey.TITLE.name())).getContent());

        v1Tag.setField(new ID3v1TagField(FieldKey.YEAR.name(), "year"));
        assertEquals("year", ((TagTextField) v1Tag.getFields(FieldKey.YEAR).get(0)).getContent());
        assertEquals("year", ((TagTextField) v1Tag.getFirstField(FieldKey.YEAR.name())).getContent());

        v1Tag.setField(new ID3v1TagField(FieldKey.GENRE.name(), "Country"));
        assertEquals("Country", ((TagTextField) v1Tag.getFields(FieldKey.GENRE).get(0)).getContent());
        assertEquals("Country", ((TagTextField) v1Tag.getFirstField(FieldKey.GENRE.name())).getContent());

        v1Tag.setField(new ID3v1TagField(FieldKey.COMMENT.name(), "comment"));
        assertEquals("comment", ((TagTextField) v1Tag.getFields(FieldKey.COMMENT).get(0)).getContent());
        assertEquals("comment", ((TagTextField) v1Tag.getFirstField(FieldKey.COMMENT.name())).getContent());

        //Check nothing been overwritten
        assertEquals("year", v1Tag.getFirst(FieldKey.YEAR));
        assertEquals("Country", v1Tag.getFirst(FieldKey.GENRE));
        assertEquals("title", v1Tag.getFirst(FieldKey.TITLE));
        assertEquals("album", v1Tag.getFirst(FieldKey.ALBUM));
        assertEquals("artist", v1Tag.getFirst(FieldKey.ARTIST));

        //Delete artist field
        v1Tag.deleteField(FieldKey.ARTIST);
        assertEquals("", v1Tag.getFirst(FieldKey.ARTIST));
        assertEquals("year", v1Tag.getFirst(FieldKey.YEAR));
        assertEquals("Country", v1Tag.getFirst(FieldKey.GENRE));
        assertEquals("title", v1Tag.getFirst(FieldKey.TITLE));
        assertEquals("album", v1Tag.getFirst(FieldKey.ALBUM));

        //Not Empty
        assertFalse(v1Tag.isEmpty());

        v1Tag.deleteField(FieldKey.ALBUM);
        v1Tag.deleteField(FieldKey.YEAR);
        v1Tag.deleteField(FieldKey.GENRE);
        v1Tag.deleteField(FieldKey.TITLE);
        v1Tag.deleteField(FieldKey.COMMENT);
        v1Tag.setField(new ID3v1TagField(FieldKey.COMMENT.name(), ""));
        //Empty
        assertTrue(v1Tag.isEmpty());

        //Null Handling
        try {
            v1Tag.setField(new ID3v1TagField(FieldKey.COMMENT.name(), null));
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertTrue(exceptionCaught instanceof IllegalArgumentException);
    }
}
