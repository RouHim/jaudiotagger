package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.ID3v23Frame;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTCON;
import org.jaudiotagger.tag.reference.ID3V2Version;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class Issue055Test extends AbstractTestCase {
    @Test
    public void testId3v23GenreWritingDefault() {
        Exception ex = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("01.mp3", new File("issue55.mp3"));
            MP3File mp3File = new MP3File(testFile);
            TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);

            //Create and Save Genre passing text value
            mp3File.getTagOrCreateAndSetDefault().addField(FieldKey.GENRE, "Rock");
            assertEquals("Rock", mp3File.getID3v2Tag().getFirst(FieldKey.GENRE));
            assertTrue(mp3File.getID3v2Tag() instanceof ID3v23Tag);
            FrameBodyTCON tconbody = (FrameBodyTCON) ((ID3v23Frame) mp3File.getID3v2Tag().getFrame("TCON")).getBody();
            assertEquals("(17)", tconbody.getFirstTextValue());
            mp3File.save();
            assertEquals("Rock", mp3File.getID3v2Tag().getFirst(FieldKey.GENRE));
            assertTrue(mp3File.getID3v2Tag() instanceof ID3v23Tag);
            tconbody = (FrameBodyTCON) ((ID3v23Frame) mp3File.getID3v2Tag().getFrame("TCON")).getBody();
            assertEquals("(17)", tconbody.getFirstTextValue());
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        assertNull(ex);
    }

    @Test
    public void testId3v23GenreWritingDefaultcaseInsensitive() {
        Exception ex = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("01.mp3", new File("issue55.mp3"));
            MP3File mp3File = new MP3File(testFile);
            TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);

            //Create and Save Genre passing text value
            mp3File.getTagOrCreateAndSetDefault().addField(FieldKey.GENRE, "rock");
            assertEquals("Rock", mp3File.getID3v2Tag().getFirst(FieldKey.GENRE));
            assertTrue(mp3File.getID3v2Tag() instanceof ID3v23Tag);
            FrameBodyTCON tconbody = (FrameBodyTCON) ((ID3v23Frame) mp3File.getID3v2Tag().getFrame("TCON")).getBody();
            assertEquals("(17)", tconbody.getFirstTextValue());
            mp3File.save();
            assertEquals("Rock", mp3File.getID3v2Tag().getFirst(FieldKey.GENRE));
            assertTrue(mp3File.getID3v2Tag() instanceof ID3v23Tag);
            tconbody = (FrameBodyTCON) ((ID3v23Frame) mp3File.getID3v2Tag().getFrame("TCON")).getBody();
            assertEquals("(17)", tconbody.getFirstTextValue());
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        assertNull(ex);
    }

    @Test
    public void testId3v23GenreWritingTextAlways() {
        Exception ex = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("01.mp3", new File("issue55.mp3"));
            MP3File mp3File = new MP3File(testFile);
            TagOptionSingleton.getInstance().setWriteMp3GenresAsText(true);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);

            //Create and Save Genre passing text value
            mp3File.getTagOrCreateAndSetDefault().addField(FieldKey.GENRE, "Rock");
            assertEquals("Rock", mp3File.getID3v2Tag().getFirst(FieldKey.GENRE));
            assertTrue(mp3File.getID3v2Tag() instanceof ID3v23Tag);
            FrameBodyTCON tconbody = (FrameBodyTCON) ((ID3v23Frame) mp3File.getID3v2Tag().getFrame("TCON")).getBody();
            assertEquals("Rock", tconbody.getFirstTextValue());
            mp3File.save();
            assertEquals("Rock", mp3File.getID3v2Tag().getFirst(FieldKey.GENRE));
            assertTrue(mp3File.getID3v2Tag() instanceof ID3v23Tag);
            tconbody = (FrameBodyTCON) ((ID3v23Frame) mp3File.getID3v2Tag().getFrame("TCON")).getBody();
            assertEquals("Rock", tconbody.getFirstTextValue());
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        assertNull(ex);
    }

    @Test
    public void testId3v24GenreWritingDefault() {
        Exception ex = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("01.mp3", new File("issue55.mp3"));
            MP3File mp3File = new MP3File(testFile);
            TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);

            //Create and Save Genre passing text value
            mp3File.getTagOrCreateAndSetDefault().addField(FieldKey.GENRE, "Rock");
            assertEquals("Rock", mp3File.getID3v2Tag().getFirst(FieldKey.GENRE));
            assertTrue(mp3File.getID3v2Tag() instanceof ID3v24Tag);
            FrameBodyTCON tconbody = (FrameBodyTCON) ((ID3v24Frame) mp3File.getID3v2Tag().getFrame("TCON")).getBody();
            assertEquals("17", tconbody.getFirstTextValue());
            mp3File.save();
            assertEquals("Rock", mp3File.getID3v2Tag().getFirst(FieldKey.GENRE));
            assertTrue(mp3File.getID3v2Tag() instanceof ID3v24Tag);
            tconbody = (FrameBodyTCON) ((ID3v24Frame) mp3File.getID3v2Tag().getFrame("TCON")).getBody();
            assertEquals("17", tconbody.getFirstTextValue());
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        assertNull(ex);
    }

    @Test
    public void testId3v24GenreWritingDefaultcaseInsensitive() {
        Exception ex = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("01.mp3", new File("issue55.mp3"));
            MP3File mp3File = new MP3File(testFile);
            TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);

            //Create and Save Genre passing text value
            mp3File.getTagOrCreateAndSetDefault().addField(FieldKey.GENRE, "rock");
            assertEquals("Rock", mp3File.getID3v2Tag().getFirst(FieldKey.GENRE));
            assertTrue(mp3File.getID3v2Tag() instanceof ID3v24Tag);
            FrameBodyTCON tconbody = (FrameBodyTCON) ((ID3v24Frame) mp3File.getID3v2Tag().getFrame("TCON")).getBody();
            assertEquals("17", tconbody.getFirstTextValue());
            mp3File.save();
            assertEquals("Rock", mp3File.getID3v2Tag().getFirst(FieldKey.GENRE));
            assertTrue(mp3File.getID3v2Tag() instanceof ID3v24Tag);
            tconbody = (FrameBodyTCON) ((ID3v24Frame) mp3File.getID3v2Tag().getFrame("TCON")).getBody();
            assertEquals("17", tconbody.getFirstTextValue());
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        assertNull(ex);
    }

    @Test
    public void testId3v24GenreWritingTextAlways() {
        Exception ex = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("01.mp3", new File("issue55.mp3"));
            MP3File mp3File = new MP3File(testFile);
            TagOptionSingleton.getInstance().setWriteMp3GenresAsText(true);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);

            //Create and Save Genre passing text value
            mp3File.getTagOrCreateAndSetDefault().addField(FieldKey.GENRE, "Rock");
            assertEquals("Rock", mp3File.getID3v2Tag().getFirst(FieldKey.GENRE));
            assertTrue(mp3File.getID3v2Tag() instanceof ID3v24Tag);
            FrameBodyTCON tconbody = (FrameBodyTCON) ((ID3v24Frame) mp3File.getID3v2Tag().getFrame("TCON")).getBody();
            assertEquals("Rock", tconbody.getFirstTextValue());
            mp3File.save();
            assertEquals("Rock", mp3File.getID3v2Tag().getFirst(FieldKey.GENRE));
            assertTrue(mp3File.getID3v2Tag() instanceof ID3v24Tag);
            tconbody = (FrameBodyTCON) ((ID3v24Frame) mp3File.getID3v2Tag().getFrame("TCON")).getBody();
            assertEquals("Rock", tconbody.getFirstTextValue());
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        assertNull(ex);
    }
}