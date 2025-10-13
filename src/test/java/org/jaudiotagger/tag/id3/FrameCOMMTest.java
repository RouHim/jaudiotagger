package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.framebody.FrameBodyCOMM;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class FrameCOMMTest extends AbstractTestCase {

    /**
     * Should run without throwing Runtime excception, although COMMFrame wont be loaded and will
     * throwe invalid size exception
     */
    @Test
    public void testReadFileContainingInvalidSizeCOMMFrame() {
        Exception e = null;
        try {
            File testFile = prependAudioToTmp(
                    "Issue77.id3",
                    "testV1.mp3"
            );
            MP3File mp3File = new MP3File(testFile);
        } catch (Exception ie) {
            e = ie;
        }
        assertNull(e);
    }

    /**
     * Should run without throwing Runtime excception, although COMMFrame wont be loaded and will
     * throwe invalid datatype exception
     */
    @Test
    public void testReadFileContainingInvalidTextEncodingCOMMFrame() {
        Exception e = null;
        try {
            File testFile = prependAudioToTmp(
                    "Issue80.id3",
                    "testV1.mp3"
            );
            MP3File mp3File = new MP3File(testFile);
        } catch (Exception ie) {
            e = ie;
        }
        assertNull(e);
    }

    /**
     * Can read file containing a language code that does not actually map to a code , and write it back
     * In this real example the language code has been held as three space characters
     */
    @Test
    public void testreadFrameContainingInvalidlanguageCodeCOMMFrame() {
        final String INVALID_LANG_CODE = "   ";
        Exception e = null;
        try {
            File testFile = prependAudioToTmp(
                    "Issue108.id3",
                    "testV1.mp3"
            );
            MP3File mp3File = new MP3File(testFile);

            assertTrue(mp3File.getID3v2Tag().hasFrame("COMM"));

            ID3v24Frame commFrame = (ID3v24Frame) mp3File
                    .getID3v2Tag()
                    .getFrame("COMM");
            FrameBodyCOMM frameBody = (FrameBodyCOMM) commFrame.getBody();

            assertEquals(INVALID_LANG_CODE, frameBody.getLanguage());
        } catch (Exception ie) {
            e = ie;
        }
        assertNull(e);
    }

    /**
     * Can write file containing a COMM Frame with null language code
     */
    @Test
    public void testsaveFileContainingNullLanguageCodeCOMMFrame() {
        final String SAFE_LANG_CODE = "   ";
        final String SAFE_LONGER_LANG_CODE = "aa ";
        final String SAFE_SHORTER_LANG_CODE = "aaa";
        Exception e = null;
        try {
            //Read tag
            File testFile = prependAudioToTmp(
                    "Issue108.id3",
                    "testV1.mp3"
            );
            MP3File mp3File = new MP3File(testFile);
            ID3v24Frame commFrame = (ID3v24Frame) mp3File
                    .getID3v2Tag()
                    .getFrame("COMM");
            FrameBodyCOMM frameBody = (FrameBodyCOMM) commFrame.getBody();

            //Set language to null, this is common problem for new frames might null lang codes
            frameBody.setLanguage(null);
            mp3File.save();
            mp3File = new MP3File(testFile);
            commFrame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame("COMM");
            frameBody = (FrameBodyCOMM) commFrame.getBody();
            assertEquals(SAFE_LANG_CODE, frameBody.getLanguage());

            //Set language to too short a value
            frameBody.setLanguage("aa");
            mp3File.save();
            mp3File = new MP3File(testFile);
            commFrame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame("COMM");
            frameBody = (FrameBodyCOMM) commFrame.getBody();
            assertEquals(SAFE_LONGER_LANG_CODE, frameBody.getLanguage());

            //Set language to too long a value
            frameBody.setLanguage("aaaaaaa");
            mp3File.save();
            mp3File = new MP3File(testFile);
            commFrame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame("COMM");
            frameBody = (FrameBodyCOMM) commFrame.getBody();
            assertEquals(SAFE_SHORTER_LANG_CODE, frameBody.getLanguage());
        } catch (Exception ie) {
            e = ie;
        }
        assertNull(e);
    }
}
