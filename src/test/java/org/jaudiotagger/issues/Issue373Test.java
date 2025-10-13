package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNull;

public class Issue373Test extends AbstractTestCase {

    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testIssue() {
        Exception caught = null;
        try {
            File testFile = copyAudioToTmp("test94.mp3");

            AudioFile af = AudioFileIO.read(testFile);
            af.setTag(new ID3v23Tag());
            af.getTag().setField(FieldKey.ARTIST, "artist");

            Thread.sleep(20000);
            //Now open in another program to lock it, cannot reproduce programtically
            //FileChannel channel = new RandomAccessFile(testFile, "rw").getChannel();
            //FileLock lock = channel.lock();

            af.commit();
        } catch (Exception e) {
            caught = e;
        }
        assertNull(caught);
    }
}
