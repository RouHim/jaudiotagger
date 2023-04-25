package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNull;

public class Issue373Test extends AbstractTestCase {
    @Test
    public void testIssue() {
        Exception caught = null;
        try {
            File orig = new File("testdata", "test94.mp3");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = AbstractTestCase.copyAudioToTmp("test94.mp3");


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
            e.printStackTrace();
        }
        assertNull(caught);
    }
}