package org.jaudiotagger.tag.flac;

import org.jaudiotagger.AbstractBaseTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.flac.FlacInfoReader;
import org.jaudiotagger.audio.flac.metadatablock.MetadataBlockDataPicture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class FlacReadTest extends AbstractBaseTestCase {

    /**
     * Read Flac File
     */
    @Test
    public void testReadTwoChannelFile() {
        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp(
                    "test2.flac",
                    "test2read.flac"
            );
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("192", f.getAudioHeader().getBitRate());
            assertEquals("FLAC 16 bits", f.getAudioHeader().getEncodingType());
            assertEquals("2", f.getAudioHeader().getChannels());
            assertEquals("44100", f.getAudioHeader().getSampleRate());
            assertEquals(5, f.getAudioHeader().getTrackLength());
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read Flac File
     */
    @Test
    public void testReadSingleChannelFile() {
        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp(
                    "test3.flac",
                    "test3read.flac"
            );
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("FLAC 8 bits", f.getAudioHeader().getEncodingType());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("16000", f.getAudioHeader().getSampleRate());
            assertEquals(1, f.getAudioHeader().getTrackLength());
            assertEquals("47", f.getAudioHeader().getBitRate()); //is this correct value
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test can identify file that isnt flac
     */
    @Test
    public void testNotFlac() {
        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp(
                    "testV1.mp3",
                    "testV1noFlac.flac"
            );
            AudioFile f = AudioFileIO.read(testFile);
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertInstanceOf(CannotReadException.class, exceptionCaught);
    }

    /**
     * Reading file that contains cuesheet
     */
    @Test
    public void testReadCueSheet() {
        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp("test3.flac");
            AudioFile f = AudioFileIO.read(testFile);
            FlacInfoReader infoReader = new FlacInfoReader();
            assertEquals(5, infoReader.countMetaBlocks(f.getFile()));
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * test read flac file with preceding ID3 header
     */
    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testReadFileWithId3Header() {
        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp(
                    "test22.flac",
                    "testreadFlacWithId3.flac"
            );
            AudioFile f = AudioFileIO.read(testFile);
            FlacInfoReader infoReader = new FlacInfoReader();
            assertEquals(4, infoReader.countMetaBlocks(f.getFile()));
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * test read flac file with no header
     */
    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testReadFileWithOnlyStreamInfoAndPaddingHeader() {
        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp(
                    "test102.flac",
                    "test102.flac"
            );
            AudioFile f = AudioFileIO.read(testFile);
            FlacInfoReader infoReader = new FlacInfoReader();
            assertEquals(2, infoReader.countMetaBlocks(f.getFile()));
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * test read flac file with no header
     */
    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testReadArtwork() {
        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp(
                    "test154.flac",
                    "test154.flac"
            );
            AudioFile f = AudioFileIO.read(testFile);
            MetadataBlockDataPicture mbdp = (((FlacTag) f.getTag()).getImages().get(
                    0
            ));
            System.out.println(mbdp);
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }
}
