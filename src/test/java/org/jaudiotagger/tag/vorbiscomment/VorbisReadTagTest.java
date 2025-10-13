package org.jaudiotagger.tag.vorbiscomment;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.ogg.OggFileReader;
import org.jaudiotagger.tag.FieldKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.io.File;
import java.io.RandomAccessFile;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

public class VorbisReadTagTest extends AbstractTestCase {

    /**
     * Test reading a file with corrupt vorbis comment tag, however the ogg paging is actually correct
     * so no error found in this test.
     */
    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testReadCorruptOgg() {
        Exception exceptionCaught = null;
        try {
            //Can summarize file
            File testFile = copyAudioToTmp("test6.ogg");
            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggFileReader oggFileReader = new OggFileReader();
            oggFileReader.summarizeOggPageHeaders(testFile);
            raf.close();
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test reading corrupt file, because vorbis comment has an error (says no of comments is 5 but actually there
     * are 6 it should throw appropriate error
     */
    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testReadCorruptOgg2() {
        Exception exceptionCaught = null;
        try {
            //Can summarize file
            File testFile = copyAudioToTmp("test6.ogg");
            AudioFileIO.read(testFile);
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertInstanceOf(CannotReadException.class, exceptionCaught);
    }

    /**
     * Create Value with empty value and then read back, then try and create another field
     * Was expecting to fail but works ok
     */
    @Test
    public void testCreateCorruptFile() {
        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp(
                    "test.ogg",
                    "testWithEmptyField.ogg"
            );
            AudioFile file = AudioFileIO.read(testFile);
            file.getTag().setField(FieldKey.YEAR, "");
            file.commit();

            file = AudioFileIO.read(testFile);
            file.getTag().setField(FieldKey.TITLE, "testtitle");
            file.commit();

            file = AudioFileIO.read(testFile);
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }
}
