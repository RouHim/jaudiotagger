package org.jaudiotagger.tag.vorbiscomment;

import org.jaudiotagger.AbstractBaseTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.ogg.OggFileReader;
import org.jaudiotagger.audio.ogg.util.OggPageHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VorbisWriteTagTest extends AbstractBaseTestCase {

    /**
     * Can summarize file
     */
    @Test
    public void testSummarizeFile() {
        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp(
                    "test.ogg",
                    "testSummarizeFile.ogg"
            );
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
     * Test writing to file, comments too large to fit on single page anymore
     */
    @Test
    public void testWriteToFileMuchLarger() {
        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp(
                    "test.ogg",
                    "testWriteTagTestRequiresTwoPages.ogg"
            );

            AudioFile f = AudioFileIO.read(testFile);
            assertInstanceOf(VorbisCommentTag.class, f.getTag());
            VorbisCommentTag tag = (VorbisCommentTag) f.getTag();

            //Add new image, requires two fields in oggVorbis with data in  base64 encoded form
            RandomAccessFile imageFile = new RandomAccessFile(
                    fileResource("testdata", "coverart.bmp"),
                    "r"
            );
            byte[] imagedata = new byte[(int) imageFile.length()];
            imageFile.read(imagedata);
            String base64image = Base64.getEncoder().encodeToString(imagedata);
            tag.setField(
                    tag.createField(VorbisCommentFieldKey.COVERART, base64image)
            );
            tag.setField(
                    tag.createField(VorbisCommentFieldKey.COVERARTMIME, "image/png")
            );

            //Save
            f.commit();

            //Reread
            f = AudioFileIO.read(testFile);
            tag = (VorbisCommentTag) f.getTag();

            //Check changes
            assertEquals(1, tag.get(VorbisCommentFieldKey.COVERART).size());
            assertEquals(1, tag.get(VorbisCommentFieldKey.COVERARTMIME).size());

            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggFileReader oggFileReader = new OggFileReader();
            System.out.println(oggFileReader.readOggPageHeader(raf, 0));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 1));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 2));
            raf.close();
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test writing to file, comments too large to fit on single page anymore, and also setup header gets split
     */
    @Test
    public void testWriteToFileMuchLargerSetupHeaderSplit() {
        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp(
                    "test.ogg",
                    "testWriteTagTestRequiresTwoPagesHeaderSplit.ogg"
            );

            AudioFile f = AudioFileIO.read(testFile);
            assertInstanceOf(VorbisCommentTag.class, f.getTag());
            VorbisCommentTag tag = (VorbisCommentTag) f.getTag();

            //Add new pretend image to force split of setup header
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 128000; i++) {
                sb.append("a");
            }
            tag.setField(
                    tag.createField(VorbisCommentFieldKey.COVERART, sb.toString())
            );
            tag.setField(
                    tag.createField(VorbisCommentFieldKey.COVERARTMIME, "image/png")
            );

            //Save
            f.commit();

            //Reread
            f = AudioFileIO.read(testFile);
            tag = (VorbisCommentTag) f.getTag();

            //Check changes
            assertEquals(1, tag.get(VorbisCommentFieldKey.COVERART).size());
            assertEquals(1, tag.get(VorbisCommentFieldKey.COVERARTMIME).size());

            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggFileReader oggFileReader = new OggFileReader();
            System.out.println(oggFileReader.readOggPageHeader(raf, 0));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 1));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 2));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 3));

            raf.close();
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Issue 197, test writing to file when audio packet come straight after setup packet on same oggPage, edit so
     * comment data is changed but size of comment header is same length
     */
    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testWriteToFileWithExtraPacketsOnSamePageAsSetupHeader() {
        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp(
                    "test2.ogg",
                    "testWriteTagWithExtraPacketsHeaderSameSize.ogg"
            );

            OggFileReader oggFileReader = new OggFileReader();
            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggPageHeader pageHeader = oggFileReader.readOggPageHeader(raf, 1);
            int packetsInSecondPageCount = pageHeader.getPacketList().size();
            pageHeader = null;
            raf.close();

            AudioFile f = AudioFileIO.read(testFile);
            assertInstanceOf(VorbisCommentTag.class, f.getTag());
            VorbisCommentTag tag = (VorbisCommentTag) f.getTag();

            //These have methods coz common over all formats
            tag.setField(FieldKey.ARTIST, "AUTHOR");

            //Save
            f.commit();

            //Reread
            f = AudioFileIO.read(testFile);
            tag = (VorbisCommentTag) f.getTag();

            //Check changes
            assertEquals("AUTHOR", tag.getFirst(FieldKey.ARTIST));

            //Check 2nd page has same number of packets, this is only the case for this specific test, so check
            //in test not code itself.
            raf = new RandomAccessFile(testFile, "r");
            pageHeader = oggFileReader.readOggPageHeader(raf, 1);
            raf.close();
            assertEquals(packetsInSecondPageCount, pageHeader.getPacketList().size());
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Issue 197, test writing to file when audio packet come straight after setup packet on same oggPage, edit enough
     * so that comment is larger, but the comment, header and extra packets can still all fit on page 2
     */
    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testWriteToFileWithExtraPacketsOnSamePageAsSetupHeaderLarger() {
        File orig = fileResource("testdata", "test2.ogg");

        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp(
                    "test2.ogg",
                    "testWriteTagWithExtraPacketsHeaderLargerSize.ogg"
            );

            OggFileReader oggFileReader = new OggFileReader();
            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggPageHeader pageHeader = oggFileReader.readOggPageHeader(raf, 1);
            int packetsInSecondPageCount = pageHeader.getPacketList().size();
            pageHeader = null;
            raf.close();

            AudioFile f = AudioFileIO.read(testFile);
            assertInstanceOf(VorbisCommentTag.class, f.getTag());
            VorbisCommentTag tag = (VorbisCommentTag) f.getTag();

            //These have methods coz common over all formats
            tag.setField(FieldKey.ARTIST, "ARTISTIC");

            //Save
            f.commit();

            //Reread
            f = AudioFileIO.read(testFile);
            tag = (VorbisCommentTag) f.getTag();

            //Check changes
            assertEquals("ARTISTIC", tag.getFirst(FieldKey.ARTIST));

            //Check 2nd page has same number of packets, this is only the case for this specific test, so check
            //in test not code itself.
            raf = new RandomAccessFile(testFile, "r");
            pageHeader = oggFileReader.readOggPageHeader(raf, 1);
            raf.close();
            assertEquals(packetsInSecondPageCount, pageHeader.getPacketList().size());
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Issue 197, test writing to file when audio packet come straight after setup packet on same oggPage, edit enough
     * so that comment is much larger, so that comment, header and extra packets can no longer fit on page 2
     */
    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testWriteToFileWithExtraPacketsOnSamePageAsSetupHeaderMuchLarger() {
        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp(
                    "test.ogg",
                    "testWriteTagWithExtraPacketsHeaderMuchLargerSize.ogg"
            );

            AudioFile f = AudioFileIO.read(testFile);
            assertInstanceOf(VorbisCommentTag.class, f.getTag());
            VorbisCommentTag tag = (VorbisCommentTag) f.getTag();

            //Add new image, requires two fields in oggVorbis with data in  base64 encoded form
            RandomAccessFile imageFile = new RandomAccessFile(
                    fileResource("testdata", "coverart.bmp"),
                    "r"
            );
            byte[] imagedata = new byte[(int) imageFile.length()];
            imageFile.read(imagedata);
            String base64image = Base64.getEncoder().encodeToString(imagedata);
            tag.setField(
                    tag.createField(VorbisCommentFieldKey.COVERART, base64image)
            );
            tag.setField(
                    tag.createField(VorbisCommentFieldKey.COVERARTMIME, "image/png")
            );

            //Save
            f.commit();

            //Reread
            f = AudioFileIO.read(testFile);
            tag = (VorbisCommentTag) f.getTag();

            //Check changes
            assertEquals(1, tag.get(VorbisCommentFieldKey.COVERART).size());
            assertEquals(1, tag.get(VorbisCommentFieldKey.COVERARTMIME).size());

            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggFileReader oggFileReader = new OggFileReader();
            System.out.println(oggFileReader.readOggPageHeader(raf, 0));
            raf.seek(0);
            System.out.println("Page 2" + oggFileReader.readOggPageHeader(raf, 1));
            //System.out.println("Page 3"+oggFileReader.readOggPageHeader(raf,2));
            //oggFileReader.readOggPageHeader(raf,4);
            raf.close();
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Issue 197, test writing to file when audio packet come straight after setup packet on same oggPage, edit enough
     * so that comment is much larger, so that comment, header and extra packets can no longer fit on page 2 AND
     * setup header is also split over two
     */
    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testWriteToFileWithExtraPacketsOnSamePageAsSetupHeaderMuchLargerAndSplit() {
        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp(
                    "test2.ogg",
                    "testWriteTagWithExtraPacketsHeaderMuchLargerSizeAndSplit.ogg"
            );

            AudioFile f = AudioFileIO.read(testFile);
            assertInstanceOf(VorbisCommentTag.class, f.getTag());
            VorbisCommentTag tag = (VorbisCommentTag) f.getTag();

            //Add new pretend image to force split of setup header
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 128000; i++) {
                sb.append("a");
            }
            tag.setField(
                    tag.createField(VorbisCommentFieldKey.COVERART, sb.toString())
            );
            tag.setField(
                    tag.createField(VorbisCommentFieldKey.COVERARTMIME, "image/png")
            );

            //Save
            f.commit();

            //Reread
            f = AudioFileIO.read(testFile);

            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggFileReader oggFileReader = new OggFileReader();
            System.out.println(oggFileReader.readOggPageHeader(raf, 0));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 1));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 2));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 3));

            raf.close();

            //tag = (VorbisCommentTag)f.getTag();

            //Check changes
            //assertEquals(1,tag.getFields(VorbisCommentFieldKey.COVERART).size());
            //assertEquals(1,tag.getFields(VorbisCommentFieldKey.COVERARTMIME).size());
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test writing to file, comments was too large for one page but not anymore
     */
    @Test
    public void testWriteToFileNoLongerRequiresTwoPages() {
        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp(
                    "test3.ogg",
                    "testWriteTagTestNoLongerRequiresTwoPages.ogg"
            );

            AudioFile f = AudioFileIO.read(testFile);
            assertInstanceOf(VorbisCommentTag.class, f.getTag());
            VorbisCommentTag tag = (VorbisCommentTag) f.getTag();

            //Delete Large Image
            tag.deleteField(VorbisCommentFieldKey.COVERART);
            tag.deleteField(VorbisCommentFieldKey.COVERARTMIME);

            //Save
            f.commit();

            //Reread
            f = AudioFileIO.read(testFile);
            tag = (VorbisCommentTag) f.getTag();

            //Check changes
            assertEquals(0, tag.get(VorbisCommentFieldKey.COVERART).size());
            assertEquals(0, tag.get(VorbisCommentFieldKey.COVERARTMIME).size());

            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggFileReader oggFileReader = new OggFileReader();
            System.out.println(oggFileReader.readOggPageHeader(raf, 0));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 1));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 2));
            raf.close();
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test writing to file, comments was too large for one page and setup header split but not anymore
     */
    @Test
    public void testWriteToFileNoLongerRequiresTwoPagesNorSplit() {
        File orig = fileResource("testdata", "test5.ogg");

        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp(
                    "test5.ogg",
                    "testWriteTagTestNoLongerRequiresTwoPagesNorSplit.ogg"
            );

            AudioFile f = AudioFileIO.read(testFile);
            assertInstanceOf(VorbisCommentTag.class, f.getTag());
            VorbisCommentTag tag = (VorbisCommentTag) f.getTag();

            //Delete Large Image
            tag.deleteField(VorbisCommentFieldKey.COVERART);
            tag.deleteField(VorbisCommentFieldKey.COVERARTMIME);

            //Save
            f.commit();

            //Reread
            f = AudioFileIO.read(testFile);
            tag = (VorbisCommentTag) f.getTag();

            //Check changes
            assertEquals(0, tag.get(VorbisCommentFieldKey.COVERART).size());
            assertEquals(0, tag.get(VorbisCommentFieldKey.COVERARTMIME).size());

            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggFileReader oggFileReader = new OggFileReader();
            System.out.println(oggFileReader.readOggPageHeader(raf, 0));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 1));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 2));
            raf.close();
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test writing to file, comments was too large for one page but not anymore
     */
    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testWriteToFileWriteToFileWithExtraPacketsNoLongerRequiresTwoPages() {
        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp(
                    "test4.ogg",
                    "testWriteTagTestWithPacketsNoLongerRequiresTwoPages.ogg"
            );

            AudioFile f = AudioFileIO.read(testFile);
            assertInstanceOf(VorbisCommentTag.class, f.getTag());
            VorbisCommentTag tag = (VorbisCommentTag) f.getTag();

            //Delete Large Image
            tag.deleteField(VorbisCommentFieldKey.ARTIST);
            tag.deleteField(VorbisCommentFieldKey.COVERART);
            tag.deleteField(VorbisCommentFieldKey.COVERARTMIME);

            //Save
            f.commit();

            //Reread
            f = AudioFileIO.read(testFile);
            tag = (VorbisCommentTag) f.getTag();

            //Check changes
            assertEquals(0, tag.get(VorbisCommentFieldKey.ARTIST).size());
            assertEquals(0, tag.get(VorbisCommentFieldKey.COVERART).size());
            assertEquals(0, tag.get(VorbisCommentFieldKey.COVERARTMIME).size());

            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggFileReader oggFileReader = new OggFileReader();
            System.out.println(oggFileReader.readOggPageHeader(raf, 0));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 1));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 2));
            raf.close();
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    @Test
    public void testDeleteTag() throws Exception {
        File testFile = copyAudioToTmp(
                "test.ogg",
                "testDelete.ogg"
        );
        AudioFile f = AudioFileIO.read(testFile);
        f.setTag(VorbisCommentTag.createNewTag());
        f.commit();

        f = AudioFileIO.read(testFile);
        assertTrue(f.getTag().isEmpty());
        assertEquals("jaudiotagger", ((VorbisCommentTag) f.getTag()).getVendor());
    }

    @Test
    public void testDeleteTag2() throws Exception {
        File testFile = copyAudioToTmp(
                "test.ogg",
                "testDelete2.ogg"
        );
        AudioFile f = AudioFileIO.read(testFile);
        AudioFileIO.delete(f);

        f = AudioFileIO.read(testFile);
        assertTrue(f.getTag().isEmpty());
        assertEquals("jaudiotagger", ((VorbisCommentTag) f.getTag()).getVendor());
    }

    @Test
    public void testWriteMultipleFields() throws Exception {
        TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(
                VorbisAlbumArtistReadOptions.READ_ALBUMARTIST
        );
        TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST
        );

        File testFile = copyAudioToTmp(
                "test.ogg",
                "testWriteMultiple.ogg"
        );
        AudioFile f = AudioFileIO.read(testFile);
        f.getTag().addField(FieldKey.ALBUM_ARTIST, "artist1");
        f.getTag().addField(FieldKey.ALBUM_ARTIST, "artist2");
        f.commit();
        f = AudioFileIO.read(testFile);
        List<TagField> tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST);
        assertEquals(tagFields.size(), 2);
    }

    @Test
    public void testDeleteFields() throws Exception {
        //Delete using generic key
        File testFile = copyAudioToTmp(
                "test.ogg",
                "testDeleteFields.ogg"
        );
        AudioFile f = AudioFileIO.read(testFile);
        List<TagField> tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(0, tagFields.size());
        f.getTag().addField(FieldKey.ALBUM_ARTIST_SORT, "artist1");
        f.getTag().addField(FieldKey.ALBUM_ARTIST_SORT, "artist2");
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(2, tagFields.size());
        f.getTag().deleteField(FieldKey.ALBUM_ARTIST_SORT);
        f.commit();

        //Delete using flac id
        f = AudioFileIO.read(testFile);
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(0, tagFields.size());
        f.getTag().addField(FieldKey.ALBUM_ARTIST_SORT, "artist1");
        f.getTag().addField(FieldKey.ALBUM_ARTIST_SORT, "artist2");
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(2, tagFields.size());
        f.getTag().deleteField("ALBUMARTISTSORT");
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(0, tagFields.size());
        f.commit();

        f = AudioFileIO.read(testFile);
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(0, tagFields.size());
    }
}
