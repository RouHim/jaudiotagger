package org.jaudiotagger.audio.ogg;

import org.jaudiotagger.AbstractBaseTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.ogg.util.OggPageHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentFieldKey;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.io.File;
import java.io.RandomAccessFile;

import static org.junit.jupiter.api.Assertions.*;

public class OggVorbisHeaderTest extends AbstractBaseTestCase {

    /**
     * Testing reading of vorbis audio header info
     */
    @Test
    public void testReadFile() {
        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp(
                    "test.ogg",
                    "testReadFile.ogg"
            );
            AudioFile f = AudioFileIO.read(testFile);

            //assertEquals("192",f.getAudioHeader().getBitRate());
            //assertEquals("Ogg Vorbis v1",f.getAudioHeader().getEncodingType());
            //assertEquals("2",f.getAudioHeader().getChannels());
            //assertEquals("44100",f.getAudioHeader().getSampleRate());

            assertInstanceOf(VorbisCommentTag.class, f.getTag());
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Testing reading of vorbis audio header info
     * <p/>
     * TODO, need to replace with file that is not copyrighted
     */
    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testReadPaddedFile() {
        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp(
                    "test2.ogg",
                    "test2.ogg"
            );
            AudioFile f = AudioFileIO.read(testFile);

            f.getTag().setField(FieldKey.ALBUM, "bbbbbbb");
            f.commit();

            //assertEquals("192",f.getAudioHeader().getBitRate());
            //assertEquals("Ogg Vorbis v1",f.getAudioHeader().getEncodingType());
            //assertEquals("2",f.getAudioHeader().getChannels());
            //assertEquals("44100",f.getAudioHeader().getSampleRate());

            //assertTrue(f.getTag() instanceof VorbisCommentTag);
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test simple write to file, comment and setup header just spread over one page before and afterwards
     */
    @Test
    public void testWriteFile() {
        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp(
                    "test.ogg",
                    "testWriteTagToFile.ogg"
            );
            AudioFile f = AudioFileIO.read(testFile);

            //Size of VorbisComment should increase
            assertInstanceOf(VorbisCommentTag.class, f.getTag());
            f.getTag().setField(FieldKey.ALBUM, "bbbbbbb");
            f.commit();

            f = AudioFileIO.read(testFile);
            assertInstanceOf(VorbisCommentTag.class, f.getTag());
            assertEquals("bbbbbbb", f.getTag().getFirst(FieldKey.ALBUM));

            OggFileReader ofr = new OggFileReader();
            OggPageHeader oph = ofr.readOggPageHeader(
                    new RandomAccessFile(testFile, "r"),
                    0
            );
            assertEquals(30, oph.getPageLength());
            assertEquals(0, oph.getPageSequence());
            assertEquals(559748870, oph.getSerialNumber());
            assertEquals(-2111591604, oph.getCheckSum());

            oph = ofr.readOggPageHeader(new RandomAccessFile(testFile, "r"), 1);
            assertEquals(3745, oph.getPageLength());
            assertEquals(1, oph.getPageSequence());
            assertEquals(559748870, oph.getSerialNumber());
            assertEquals(233133993, oph.getCheckSum());
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test writing to file where previoslu comment was spread over many pages, now only over one so the sequence nos
     * for all subsequent pages have to be redone with checksums
     */
    @Test
    public void testWritePreviouslyLargeFile() {
        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp(
                    "testlargeimage.ogg",
                    "testWritePreviouslyLargeFile.ogg"
            );
            AudioFile f = AudioFileIO.read(testFile);

            //Size of VorbisComment should decrease just setting a nonsical but muuch smaller value for image
            assertInstanceOf(VorbisCommentTag.class, f.getTag());
            VorbisCommentTag vorbisTag = (VorbisCommentTag) f.getTag();
            vorbisTag.setField(
                    vorbisTag.createField(VorbisCommentFieldKey.COVERART, "ccc")
            );
            f.commit();

            f = AudioFileIO.read(testFile);
            assertInstanceOf(VorbisCommentTag.class, f.getTag());

            OggFileReader ofr = new OggFileReader();
            OggPageHeader oph = ofr.readOggPageHeader(
                    new RandomAccessFile(testFile, "r"),
                    0
            );
            assertEquals(30, oph.getPageLength());
            assertEquals(0, oph.getPageSequence());
            assertEquals(559748870, oph.getSerialNumber());
            assertEquals(-2111591604, oph.getCheckSum());
            assertEquals(2, oph.getHeaderType());

            oph = ofr.readOggPageHeader(new RandomAccessFile(testFile, "r"), 1);
            assertEquals(3783, oph.getPageLength());
            assertEquals(1, oph.getPageSequence());
            assertEquals(559748870, oph.getSerialNumber());
            assertEquals(1677220898, oph.getCheckSum());
            assertEquals(0, oph.getHeaderType());

            //First Audio Frames
            oph = ofr.readOggPageHeader(new RandomAccessFile(testFile, "r"), 2);
            assertEquals(4156, oph.getPageLength());
            assertEquals(2, oph.getPageSequence());
            assertEquals(559748870, oph.getSerialNumber());
            assertEquals(1176378771, oph.getCheckSum());
            assertEquals(0, oph.getHeaderType());
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Testing writing multi page comment header (existing header is multipage)
     */
    @Test
    public void testLargeWriteFile() {
        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp(
                    "testlargeimage.ogg",
                    "testLargeWriteFile.ogg"
            );
            AudioFile f = AudioFileIO.read(testFile);

            //Size of VorbisComment should increase
            assertInstanceOf(VorbisCommentTag.class, f.getTag());
            f.getTag().setField(FieldKey.ALBUM, "bbbbbbb");
            f.commit();

            f = AudioFileIO.read(testFile);
            assertInstanceOf(VorbisCommentTag.class, f.getTag());
            assertEquals("bbbbbbb", f.getTag().getFirst(FieldKey.ALBUM));

            OggFileReader ofr = new OggFileReader();
            OggPageHeader oph = ofr.readOggPageHeader(
                    new RandomAccessFile(testFile, "r"),
                    0
            );
            assertEquals(30, oph.getPageLength());
            assertEquals(0, oph.getPageSequence());
            assertEquals(559748870, oph.getSerialNumber());
            assertEquals(-2111591604, oph.getCheckSum());
            assertEquals(2, oph.getHeaderType());

            oph = ofr.readOggPageHeader(new RandomAccessFile(testFile, "r"), 1);
            assertEquals(65025, oph.getPageLength());
            assertEquals(1, oph.getPageSequence());
            assertEquals(559748870, oph.getSerialNumber());
            assertEquals(-1172108515, oph.getCheckSum());
            assertEquals(0, oph.getHeaderType());
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Testing writing multi page comment header where the setup header has to be split because there is not enough
     * room on the last Comment header Page
     */
    @Test
    public void testLargeWriteFileWithSplitSetupHeader() {
        Exception exceptionCaught = null;
        int count = 0;
        try {
            File testFile = copyAudioToTmp(
                    "testlargeimage.ogg",
                    "testAwkwardSizeWriteFile.ogg"
            );
            AudioFile f = AudioFileIO.read(testFile);

            //Size of VorbisComment should increase and to a level that the setupheader cant fit completely
            //in last page pf comment header so has to be split over two pages
            assertInstanceOf(VorbisCommentTag.class, f.getTag());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 24000; i++) {
                sb.append("z");
            }
            f.getTag().setField(FieldKey.ALBUM, "bbbbbbb");
            f.getTag().setField(FieldKey.TITLE, sb.toString());
            f.commit();

            f = AudioFileIO.read(testFile);
            assertInstanceOf(VorbisCommentTag.class, f.getTag());
            assertEquals("bbbbbbb", f.getTag().getFirst(FieldKey.ALBUM));
            assertEquals(sb.toString(), f.getTag().getFirst(FieldKey.TITLE));

            //Identification Header type oggFlag =2
            OggFileReader ofr = new OggFileReader();
            OggPageHeader oph = ofr.readOggPageHeader(
                    new RandomAccessFile(testFile, "r"),
                    0
            );
            assertEquals(30, oph.getPageLength());
            assertEquals(0, oph.getPageSequence());
            assertEquals(559748870, oph.getSerialNumber());
            assertEquals(-2111591604, oph.getCheckSum());
            assertEquals(2, oph.getHeaderType());

            //Start of Comment Header, ogg Flag =0
            oph = ofr.readOggPageHeader(new RandomAccessFile(testFile, "r"), 1);
            assertEquals(65025, oph.getPageLength());
            assertEquals(1, oph.getPageSequence());
            assertEquals(559748870, oph.getSerialNumber());
            assertEquals(2037809131, oph.getCheckSum());
            assertEquals(0, oph.getHeaderType());

            //Continuing Comment Header, ogg Flag = 1
            oph = ofr.readOggPageHeader(new RandomAccessFile(testFile, "r"), 2);
            assertEquals(1, oph.getHeaderType());

            //Addtional checking that audio is also readable
            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggPageHeader lastPageHeader = null;
            while (raf.getFilePointer() < raf.length()) {
                OggPageHeader pageHeader = OggPageHeader.read(raf);
                int packetLengthTotal = 0;
                for (OggPageHeader.PacketStartAndLength packetAndStartLength : pageHeader.getPacketList()) {
                    packetLengthTotal += packetAndStartLength.getLength();
                }
                assertEquals(pageHeader.getPageLength(), packetLengthTotal);
                if (lastPageHeader != null) {
                    assertEquals(
                            lastPageHeader.getPageSequence() + 1,
                            pageHeader.getPageSequence()
                    );
                }
                raf.seek(raf.getFilePointer() + pageHeader.getPageLength());
                count++;
                lastPageHeader = pageHeader;
            }
            assertEquals(raf.length(), raf.getFilePointer());
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        assertEquals(26, count);
    }
}
