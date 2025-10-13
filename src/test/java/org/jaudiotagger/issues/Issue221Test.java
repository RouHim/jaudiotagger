package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class Issue221Test extends AbstractTestCase {

    @Test
    public void testCreateNullMp4FrameTitle() {
        Exception exceptionCaught = null;
        try {
            Mp4Tag tag = new Mp4Tag();
            tag.setField(FieldKey.TITLE, (String) null);
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertInstanceOf(IllegalArgumentException.class, exceptionCaught);
    }

    @Test
    public void testCreateNullOggVorbisFrameTitle() {
        Exception exceptionCaught = null;
        try {
            VorbisCommentTag tag = VorbisCommentTag.createNewTag();
            tag.setField(FieldKey.TITLE, (String) null);
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertInstanceOf(IllegalArgumentException.class, exceptionCaught);
    }

    @Test
    public void testCreateNullID3v23FrameTitle() {
        Exception exceptionCaught = null;
        try {
            ID3v23Tag tag = new ID3v23Tag();
            tag.setField(FieldKey.TITLE, (String) null);
            FileOutputStream os = new FileOutputStream(
                    tempFileResource("issue_221_title_v23.mp3")
            );
            tag.write(os.getChannel(), 0);
            os.close();
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertInstanceOf(IllegalArgumentException.class, exceptionCaught);
    }

    @Test
    public void testCreateNullID3v23FrameAlbum() {
        Exception exceptionCaught = null;
        try {
            ID3v23Tag tag = new ID3v23Tag();
            tag.setField(FieldKey.ALBUM, (String) null);
            FileOutputStream os = new FileOutputStream(
                    tempFileResource("issue_221_title_v23.mp3")
            );
            tag.write(os.getChannel(), 0);
            os.close();
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertInstanceOf(IllegalArgumentException.class, exceptionCaught);
    }

    @Test
    public void testCreateNullID3v23FrameArtist() {
        Exception exceptionCaught = null;
        try {
            ID3v23Tag tag = new ID3v23Tag();
            tag.setField(FieldKey.ARTIST, (String) null);
            FileOutputStream os = new FileOutputStream(
                    tempFileResource("issue_221_title_v23.mp3")
            );
            tag.write(os.getChannel(), 0);
            os.close();
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertInstanceOf(IllegalArgumentException.class, exceptionCaught);
    }

    @Test
    public void testCreateNullID3v23FrameComment() {
        Exception exceptionCaught = null;
        try {
            ID3v23Tag tag = new ID3v23Tag();
            tag.setField(FieldKey.COMMENT, (String) null);
            FileOutputStream os = new FileOutputStream(
                    tempFileResource("issue_221_title_v23.mp3")
            );
            tag.write(os.getChannel(), 0);
            os.close();
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertInstanceOf(IllegalArgumentException.class, exceptionCaught);
    }

    @Test
    public void testCreateNullID3v23FrameGenre() {
        Exception exceptionCaught = null;
        try {
            ID3v23Tag tag = new ID3v23Tag();
            tag.setField(FieldKey.GENRE, (String) null);
            FileOutputStream os = new FileOutputStream(
                    tempFileResource("issue_221_title_v23.mp3")
            );
            tag.write(os.getChannel(), 0);
            os.close();
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertInstanceOf(IllegalArgumentException.class, exceptionCaught);
    }

    @Test
    public void testCreateNullID3v23FrameTrack() {
        Exception exceptionCaught = null;
        try {
            ID3v23Tag tag = new ID3v23Tag();
            tag.setField(FieldKey.TRACK, (String) null);
            FileOutputStream os = new FileOutputStream(
                    tempFileResource("issue_221_title_v23.mp3")
            );
            tag.write(os.getChannel(), 0);
            os.close();
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertInstanceOf(IllegalArgumentException.class, exceptionCaught);
    }

    @Test
    public void testCreateNullID3v24Frame() {
        Exception exceptionCaught = null;
        try {
            ID3v24Tag tag = new ID3v24Tag();
            tag.setField(FieldKey.TITLE, (String) null);
            FileOutputStream os = new FileOutputStream(
                    "issue_221_title_v24.mp3"
            );
            tag.write(os.getChannel(), 0);
            os.close();
        } catch (Exception e) {
            exceptionCaught = e;
        }

        assertInstanceOf(IllegalArgumentException.class, exceptionCaught);
    }

    @Test
    public void testCreateNullID3v22Frame() {
        Exception exceptionCaught = null;
        try {
            ID3v22Tag tag = new ID3v22Tag();
            tag.setField(FieldKey.TITLE, (String) null);
            FileOutputStream os = new FileOutputStream(
                    "issue_221_title_v24.mp3"
            );
            tag.write(os.getChannel(), 0);
            os.close();
        } catch (Exception e) {
            exceptionCaught = e;
        }

        assertInstanceOf(IllegalArgumentException.class, exceptionCaught);
    }
}
