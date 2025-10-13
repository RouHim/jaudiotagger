package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class Issue411Test extends AbstractTestCase {

    @Test
    public void testIssue() {
        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);
        Exception caught = null;
        try {
            File testFile = copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrCreateAndSetDefault().setField(FieldKey.COMPOSER, "fred");
            assertInstanceOf(ID3v23Tag.class, af.getTag());
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("fred", af.getTag().getFirst(FieldKey.COMPOSER));
            af.getTag().addField(FieldKey.COMPOSER, "john");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("fred", af.getTag().getValue(FieldKey.COMPOSER, 0));
            assertEquals("fred", af.getTag().getFirst(FieldKey.COMPOSER));
            assertEquals("john", af.getTag().getValue(FieldKey.COMPOSER, 1));

            //No of Composer Values
            assertEquals(2, af.getTag().getAll(FieldKey.COMPOSER).size());

            //Actual No Of Fields used to store Composer
            assertEquals(1, af.getTag().getFields(FieldKey.COMPOSER).size());
        } catch (Exception e) {
            caught = e;
        }
        assertNull(caught);
    }

    @Test
    public void testIssue2() {
        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);

        Exception caught = null;
        try {
            File testFile = copyAudioToTmp("test.flac");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrCreateAndSetDefault().setField(FieldKey.COMPOSER, "fred");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("fred", af.getTag().getFirst(FieldKey.COMPOSER));
            af.getTag().addField(FieldKey.COMPOSER, "john");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("fred", af.getTag().getFirst(FieldKey.COMPOSER));
            assertEquals(
                    "john",
                    af.getTag().getFields(FieldKey.COMPOSER).get(1).toString()
            );
            assertEquals(2, af.getTag().getFields(FieldKey.COMPOSER).size());
        } catch (Exception e) {
            caught = e;
        }
        assertNull(caught);
    }

    @Test
    public void testIssue3() {
        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);

        Exception caught = null;
        try {
            File testFile = copyAudioToTmp(
                    "01.mp3",
                    "issue411TestIssue3.mp3"
            );
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrCreateAndSetDefault().setField(FieldKey.GENRE, "rock");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("Rock", af.getTag().getFirst(FieldKey.GENRE));
            af.getTag().addField(FieldKey.GENRE, "dance");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("Rock", af.getTag().getFirst(FieldKey.GENRE));
            assertEquals("Dance", af.getTag().getValue(FieldKey.GENRE, 1));
            assertEquals(1, af.getTag().getFields(FieldKey.GENRE).size());
        } catch (Exception e) {
            caught = e;
        }
        assertNull(caught);
    }

    @Test
    public void testIssue4() {
        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);

        Exception caught = null;
        try {
            File testFile = copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrCreateAndSetDefault().setField(FieldKey.ENGINEER, "fred");
            assertInstanceOf(ID3v23Tag.class, af.getTag());
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("fred", af.getTag().getFirst(FieldKey.ENGINEER));
            af.getTag().addField(FieldKey.ENGINEER, "john");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("fred", af.getTag().getFirst(FieldKey.ENGINEER));
            assertEquals("john", af.getTag().getValue(FieldKey.ENGINEER, 1));
            assertEquals(2, af.getTag().getFields(FieldKey.ENGINEER).size());
        } catch (Exception e) {
            caught = e;
        }
        assertNull(caught);
    }

    @Test
    public void testIssue5() {
        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);

        Exception caught = null;
        try {
            File testFile = copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrCreateAndSetDefault().setField(FieldKey.BARCODE, "BARCODE1");
            assertInstanceOf(ID3v23Tag.class, af.getTag());
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("BARCODE1", af.getTag().getFirst(FieldKey.BARCODE));
            af.getTag().addField(FieldKey.BARCODE, "BARCODE2");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("BARCODE1", af.getTag().getValue(FieldKey.BARCODE, 0));
            assertEquals("BARCODE1", af.getTag().getFirst(FieldKey.BARCODE));
            assertEquals("BARCODE2", af.getTag().getValue(FieldKey.BARCODE, 1));

            //No of Barcode Values
            assertEquals(2, af.getTag().getAll(FieldKey.BARCODE).size());

            //Actual No Of Fields used to store barcode, Should be only one
            assertEquals(1, af.getTag().getFields(FieldKey.BARCODE).size());
        } catch (Exception e) {
            caught = e;
        }
        assertNull(caught);
    }

    @Test
    public void testDeletions() {
        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);

        Exception caught = null;
        try {
            File testFile = copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);

            Tag tag = af.getTagOrCreateAndSetDefault();
            tag.setField(FieldKey.BARCODE, "BARCODE1");
            assertInstanceOf(ID3v23Tag.class, tag);
            tag.addField(FieldKey.BARCODE, "BARCODE2");
            assertEquals(2, tag.getAll(FieldKey.BARCODE).size());
            af.commit();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            tag.deleteField(FieldKey.BARCODE);
            assertEquals(0, tag.getAll(FieldKey.BARCODE).size());
        } catch (Exception e) {
            caught = e;
        }
        assertNull(caught);
    }
}
