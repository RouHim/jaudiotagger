package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.tag.FieldKey;
import org.jcodec.containers.mp4.MP4Util;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class Issue255Test extends AbstractTestCase {

    /**
     * Test Mp4 with padding after last atom
     */
    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testReadMp4FileWithPaddingAfterLastAtom() {

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = copyAudioToTmp("test35.m4a");

            //Read File
            AudioFile af = AudioFileIO.read(testFile);

            //Print Out Tree
        } catch (Exception e) {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);

        try {
            //Now just createField tree
            MP4Util.Movie mp4 = MP4Util.parseFullMovie(testFile);
            String json = new JSONObject(mp4.moov().toString()).toString(2);
            System.out.println(json);
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test to write all data to a m4p which has a padding but no MDAT Dat aso fails on read
     * <p/>
     */
    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testReadFileWithInvalidPadding() {

        Exception exceptionCaught = null;
        try {
            File testFile = copyAudioToTmp(
                    "test28.m4p",
                    "WriteFileWithInvalidFreeAtom.m4p"
            );

            AudioFile f = AudioFileIO.read(testFile);
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertInstanceOf(CannotReadException.class, exceptionCaught);
    }

    /**
     * Test Mp4 with padding after last atom
     */
    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testWriteMp4FileWithPaddingAfterLastAtom() {

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = copyAudioToTmp("test35.m4a");

            //Add a v24Tag
            AudioFile af = AudioFileIO.read(testFile);
            af.getTag().setField(FieldKey.ALBUM, "NewValue");
            af.commit();
        } catch (Exception e) {
            exceptionCaught = e;
        }

        //Ensure temp file deleted
        File[] files = testFile.getParentFile().listFiles();
        for (File file : files) {
            System.out.println("Checking " + file.getName());
            assertFalse(file.getName().matches(".*test35.*.tmp"));
        }
        assertNull(exceptionCaught);
    }
}
