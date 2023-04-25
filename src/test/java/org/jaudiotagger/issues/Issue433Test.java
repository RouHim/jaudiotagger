package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jcodec.containers.mp4.MP4Util;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue433Test extends AbstractTestCase {

    @Test
    public void testWriteMp4LargeIncreaseExistingUdtaWithDatButNotMetaAddDataLarge() throws Exception {
        Exception ex = null;
        File orig = new File("testdata", "test112.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("test112.m4a", new File("test112.m4a"));

        MP4Util.Movie mp4 = MP4Util.parseFullMovie(testFile);
        String json = new JSONObject(mp4.getMoov().toString()).toString(2);
        System.out.println(json);

        AudioFile af = AudioFileIO.read(testFile);

        af.getTag().setField(FieldKey.ALBUM, "fredwwwwwwwwwwwwwwwwwwwwwwww");
        af.commit();

        MP4Util.Movie mp42 = MP4Util.parseFullMovie(testFile);
        String json2 = new JSONObject(mp42.getMoov().toString()).toString(2);
        System.out.println(json2);

        af = AudioFileIO.read(testFile);
        assertEquals("fredwwwwwwwwwwwwwwwwwwwwwwww", af.getTag().getFirst(FieldKey.ALBUM));
    }

    @Test
    public void testWriteMp4LargeIncreaseExistingUdtaWithDatButNotMetaAddDataSmall() throws Exception {
        Exception ex = null;
        File orig = new File("testdata", "test112.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("test112.m4a", new File("test112WriteSmall.m4a"));

        MP4Util.Movie mp4 = MP4Util.parseFullMovie(testFile);
        String json = new JSONObject(mp4.getMoov().toString()).toString(2);
        System.out.println(json);

        AudioFile af = AudioFileIO.read(testFile);

        af.getTag().setField(FieldKey.ALBUM, "fred");
        af.commit();

        MP4Util.Movie mp42 = MP4Util.parseFullMovie(testFile);
        String json2 = new JSONObject(mp42.getMoov().toString()).toString(2);
        System.out.println(json2);

        af = AudioFileIO.read(testFile);
        assertEquals("fred", af.getTag().getFirst(FieldKey.ALBUM));
    }

    @Test
    public void testWriteMp4LargeIncreaseExistingUdtaWithMetaDataAndUnknownAddDataLarge() throws Exception {
        Exception ex = null;
        File orig = new File("testdata", "test141.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("test141.m4a", new File("test141Large.m4a"));

        MP4Util.Movie mp4 = MP4Util.parseFullMovie(testFile);
        String json = new JSONObject(mp4.getMoov().toString()).toString(2);
        System.out.println(json);

        AudioFile af = AudioFileIO.read(testFile);

        af.getTag().setField(FieldKey.ALBUM, "fredwwwwwwwwwwwwwwwwwwwwwwww");
        af.commit();

        MP4Util.Movie mp42 = MP4Util.parseFullMovie(testFile);
        String json2 = new JSONObject(mp42.getMoov().toString()).toString(2);
        System.out.println(json2);

        af = AudioFileIO.read(testFile);
        assertEquals("fredwwwwwwwwwwwwwwwwwwwwwwww", af.getTag().getFirst(FieldKey.ALBUM));
    }

    @Test
    public void testWriteMp4LargeIncreaseExistingUdtaWithMetaDataAndUnknownAddDataSmall() throws Exception {
        Exception ex = null;
        File orig = new File("testdata", "test141.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("test141.m4a", new File("test141Small.m4a"));

        MP4Util.Movie mp4 = MP4Util.parseFullMovie(testFile);
        String json = new JSONObject(mp4.getMoov().toString()).toString(2);
        System.out.println(json);

        AudioFile af = AudioFileIO.read(testFile);

        af.getTag().setField(FieldKey.ALBUM, "fred");
        af.commit();

        MP4Util.Movie mp42 = MP4Util.parseFullMovie(testFile);
        String json2 = new JSONObject(mp42.getMoov().toString()).toString(2);
        System.out.println(json2);


        af = AudioFileIO.read(testFile);
        assertEquals("fred", af.getTag().getFirst(FieldKey.ALBUM));
    }
}
