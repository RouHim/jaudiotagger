package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jcodec.containers.mp4.MP4Util;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Issue291Test extends AbstractTestCase {

    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testSavingFile() {

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = copyAudioToTmp("test83.mp4");
            AudioFile af = AudioFileIO.read(testFile);
            System.out.println("Tag is" + af.getTag().toString());
            af
                    .getTag()
                    .setField(af.getTag().createField(FieldKey.ARTIST, "Kenny Rankin1"));
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("Kenny Rankin1", af.getTag().getFirst(FieldKey.ARTIST));
        } catch (Exception e) {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testPrintAtomTree() {

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = copyAudioToTmp("test83.mp4");
            MP4Util.Movie mp4 = MP4Util.parseFullMovie(testFile);
            String json = new JSONObject(mp4.moov().toString()).toString(2);
            System.out.println(json);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }
}
