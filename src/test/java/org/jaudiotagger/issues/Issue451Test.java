package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;
import org.jcodec.containers.mp4.MP4Util;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNull;

public class Issue451Test extends AbstractTestCase {

    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testCovrAtom() throws Exception {
        Exception ex = null;

        File testFile = copyAudioToTmp("test109.m4a");
        try {
            //Now just createField tree
            MP4Util.Movie mp4 = MP4Util.parseFullMovie(testFile);
            String json = new JSONObject(mp4.moov().toString()).toString(2);
            System.out.println(json);
        } catch (Exception e) {
        }

        try {
            AudioFile af = AudioFileIO.read(testFile);
            ImageFormats.getMimeTypeForBinarySignature(
                    af.getTag().getArtworkList().get(0).getBinaryData()
            );
        } catch (ArrayIndexOutOfBoundsException aex) {
            ex = aex;
        }
        assertNull(ex);
    }
}
