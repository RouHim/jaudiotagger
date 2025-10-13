package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNull;

public class Issue315Test extends AbstractTestCase {

    /*
     *
     * @throws Exception
     */
    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testReadWriteTagWithPictureBlockAtStart() {

        Exception e = null;
        try {
            final File testFile = copyAudioToTmp("test54.flac");
            AudioFile af = AudioFileIO.read(testFile);

            //Modify File
            af.getTag().setField(FieldKey.TITLE, "newtitle");
            af.commit();

            //Reread File
            af = AudioFileIO.read(testFile);
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }
}
