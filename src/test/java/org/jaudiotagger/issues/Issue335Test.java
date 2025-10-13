package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.AbstractTagFrameBody;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.framebody.AbstractFrameBodyTextInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue335Test extends AbstractTestCase {

    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testConvertv24Tov23ConvertsUTF8ToISO8859IfItCan()
            throws Exception {
        //TagOptionSingleton.getInstance().setResetTextEncodingForExistingFrames(false);
        File testFile = copyAudioToTmp("test79.mp3");
        MP3File f = (MP3File) AudioFileIO.read(testFile);
        assertEquals("Familial", f.getID3v2Tag().getFirst("TALB"));
        AbstractID3v2Frame frame = (AbstractID3v2Frame) f
                .getID3v2Tag()
                .getFrame("TALB");
        AbstractTagFrameBody body = frame.getBody();
        assertEquals(3, body.getTextEncoding());

        ID3v23Tag tag = new ID3v23Tag(f.getID3v2Tag());
        assertEquals(3, body.getTextEncoding());
        f.setID3v2Tag(tag);
        f.commit();

        f = (MP3File) AudioFileIO.read(testFile);
        assertEquals("Familial", f.getID3v2Tag().getFirst("TALB"));
        frame = (AbstractID3v2Frame) f.getID3v2Tag().getFrame("TALB");
        body = frame.getBody();
        assertEquals(0, body.getTextEncoding());
    }

    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testConvertv24Tov23OnlyConvertsUTF8ToISO8859IfItCan()
            throws Exception {

        //TagOptionSingleton.getInstance().setResetTextEncodingForExistingFrames(false);
        File testFile = copyAudioToTmp("test79.mp3");
        MP3File f = (MP3File) AudioFileIO.read(testFile);
        assertEquals("Familial", f.getID3v2Tag().getFirst("TALB"));
        assertEquals(4, f.getID3v2Tag().getMajorVersion());
        AbstractID3v2Frame frame = (AbstractID3v2Frame) f
                .getID3v2Tag()
                .getFrame("TALB");
        AbstractFrameBodyTextInfo body =
                (AbstractFrameBodyTextInfo) frame.getBody();
        body.setText("ǿ");
        //It was UTF8
        assertEquals(3, body.getTextEncoding());

        ID3v23Tag tag = new ID3v23Tag(f.getID3v2Tag());
        frame = (AbstractID3v2Frame) tag.getFrame("TALB");
        body = (AbstractFrameBodyTextInfo) frame.getBody();
        //We default to 0
        assertEquals(0, body.getTextEncoding());
        f.setID3v2Tag(tag);
        f.commit();

        f = (MP3File) AudioFileIO.read(testFile);
        assertEquals("ǿ", f.getID3v2Tag().getFirst("TALB"));
        frame = (AbstractID3v2Frame) f.getID3v2Tag().getFrame("TALB");
        body = (AbstractFrameBodyTextInfo) frame.getBody();
        //But need UTF16 to store this value
        assertEquals(1, body.getTextEncoding());
    }

    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testConvertv23Twice() throws Exception {

        //TagOptionSingleton.getInstance().setResetTextEncodingForExistingFrames(false);
        File testFile = copyAudioToTmp("test79.mp3");
        MP3File f = (MP3File) AudioFileIO.read(testFile);
        assertEquals("Familial", f.getID3v2Tag().getFirst("TALB"));
        assertEquals(4, f.getID3v2Tag().getMajorVersion());
        AbstractID3v2Frame frame = (AbstractID3v2Frame) f
                .getID3v2Tag()
                .getFrame("TALB");
        AbstractFrameBodyTextInfo body =
                (AbstractFrameBodyTextInfo) frame.getBody();
        body.setText("ǿ");
        //It was UTF8
        assertEquals(3, body.getTextEncoding());

        ID3v23Tag tag = new ID3v23Tag(f.getID3v2Tag());
        frame = (AbstractID3v2Frame) tag.getFrame("TALB");
        body = (AbstractFrameBodyTextInfo) frame.getBody();
        //We default to 0
        assertEquals(0, body.getTextEncoding());
        f.setID3v2Tag(tag);
        f.commit();

        f = (MP3File) AudioFileIO.read(testFile);
        tag = (ID3v23Tag) f.getID3v2Tag();
        frame = (AbstractID3v2Frame) tag.getFrame("TALB");
        body = (AbstractFrameBodyTextInfo) frame.getBody();
        //It got converted to UTF16 at previous commit stage in order to store the value
        assertEquals(1, body.getTextEncoding());

        ID3v24Tag v24tag = f.getID3v2TagAsv24();
        frame = (AbstractID3v2Frame) v24tag.getFrame("TALB");
        body = (AbstractFrameBodyTextInfo) frame.getBody();
        //And not lost when convert to v24
        assertEquals(1, body.getTextEncoding());

        tag = new ID3v23Tag(v24tag);
        frame = (AbstractID3v2Frame) tag.getFrame("TALB");
        body = (AbstractFrameBodyTextInfo) frame.getBody();
        //or if convert from v24 view back down to v23 view
        assertEquals(1, body.getTextEncoding());
    }

    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testConvertCharsAtStartOfFile() throws Exception {
        File orig = fileResource("testdata", "test79.mp3");
        boolean isMP3v2 = false;
        ID3v24Tag v24tag = null;
        Tag tag = null;
        MP3File mP3AudioFile = (MP3File) AudioFileIO.read(orig);
        mP3AudioFile.getID3v2Tag().setField(FieldKey.ARTIST, "fred");
        mP3AudioFile.commit();

        mP3AudioFile = (MP3File) AudioFileIO.read(orig);
        if (mP3AudioFile.hasID3v2Tag()) {
            isMP3v2 = true;
            v24tag = mP3AudioFile.getID3v2TagAsv24(); // Abstracting any v2 tag as v2.4
        } else {
            tag = mP3AudioFile.getTag();
            isMP3v2 = false;
        }

        String s = (isMP3v2)
                ? v24tag.getFirst(ID3v24Frames.FRAME_ID_ARTIST)
                : tag.getFirst(FieldKey.ARTIST);
        System.out.println("IS v2:" + isMP3v2);
        System.out.println(s);
    }
}
