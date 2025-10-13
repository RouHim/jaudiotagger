package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class Issue233Test extends AbstractTestCase {

    @Test
    public void testDeletingID3v2Tag() {
        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp("testV1.mp3");

            //No Tags
            MP3File mp3File = new MP3File(testFile);
            assertFalse(mp3File.hasID3v1Tag());
            assertFalse(mp3File.hasID3v2Tag());

            //Save and deleteField v24 tag
            mp3File.setID3v2Tag(new ID3v24Tag());
            mp3File.save();
            mp3File = new MP3File(testFile);
            assertFalse(mp3File.hasID3v1Tag());
            assertTrue(mp3File.hasID3v2Tag());

            mp3File.setID3v2Tag(null);
            mp3File.save();
            mp3File = new MP3File(testFile);
            assertFalse(mp3File.hasID3v1Tag());
            assertFalse(mp3File.hasID3v2Tag());

            //Save and deleteField v23 tag
            mp3File.setID3v2Tag(new ID3v23Tag());
            mp3File.save();
            mp3File = new MP3File(testFile);
            assertFalse(mp3File.hasID3v1Tag());
            assertTrue(mp3File.hasID3v2Tag());

            mp3File.setID3v2Tag(null);
            mp3File.save();
            mp3File = new MP3File(testFile);
            assertFalse(mp3File.hasID3v1Tag());
            assertFalse(mp3File.hasID3v2Tag());

            //Save and deleteField v22 tag
            mp3File.setID3v2Tag(new ID3v22Tag());
            mp3File.save();
            mp3File = new MP3File(testFile);
            assertFalse(mp3File.hasID3v1Tag());
            assertTrue(mp3File.hasID3v2Tag());

            mp3File.setID3v2Tag(null);
            mp3File.save();
            mp3File = new MP3File(testFile);
            assertFalse(mp3File.hasID3v1Tag());
            assertFalse(mp3File.hasID3v2Tag());
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testDeletingID3v1Tag() {
        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp("test32.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            AudioFileIO.delete(af);
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testReadingID3v1Tag() {
        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp("test32.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            MP3File mf = (MP3File) af;
            assertEquals("The Ides Of March", af.getTag().getFirst(FieldKey.TITLE));
            assertEquals("Iron Maiden", mf.getID3v1Tag().getFirst(FieldKey.ARTIST));
            assertEquals("", mf.getID3v2Tag().getFirst(FieldKey.ARTIST));
            assertEquals("", af.getTag().getFirst(FieldKey.ARTIST));
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }
}
