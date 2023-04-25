package org.jaudiotagger.tag.dsf;


import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.FilePermissionsTest;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class DsfAudioFileTest {

    @Test
    public void testReadDsfMetadata() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test122.dsf");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test122.dsf", new File("test122readmetadata.dsf"));
        try {
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getTag());
            assertTrue(f.getTag() instanceof ID3v24Tag);
            assertEquals("test3", f.getTag().getFirst(FieldKey.TITLE));
            assertEquals("Artist", f.getTag().getFirst(FieldKey.ARTIST));
            assertEquals("Album Artist", f.getTag().getFirst(FieldKey.ALBUM_ARTIST));
            assertEquals("Album", f.getTag().getFirst(FieldKey.ALBUM));
            //assertEquals("Crossover", f.getTag().getFirst(FieldKey.GENRE));
            assertEquals("comments", f.getTag().getFirst(FieldKey.COMMENT));
            //assertEquals("Publisher ", f.getTag().getFirst(FieldKey.RECORD_LABEL));
            //assertEquals("Composer ", f.getTag().getFirst(FieldKey.COMPOSER));
            //assertEquals("1971", f.getTag().getFirst(FieldKey.YEAR));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);


    }

    @Test
    public void testWriteWriteProtectedFileWithCheckDisabled() throws Exception {

        FilePermissionsTest.runWriteWriteProtectedFileWithCheckDisabled("test122.dsf");
    }

    @Test
    public void testWriteWriteProtectedFileWithCheckEnabled() throws Exception {

        FilePermissionsTest.runWriteWriteProtectedFileWithCheckEnabled("test122.dsf");
    }

    @Test
    public void testWriteReadOnlyFileWithCheckDisabled() throws Exception {

        FilePermissionsTest.runWriteReadOnlyFileWithCheckDisabled("test122.dsf");
    }


}
