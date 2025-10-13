package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class DuplicateFrameTest extends AbstractTestCase {

    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testReadingFileWithCorruptFirstFrame() throws Exception {
        File testFile = copyAudioToTmp("test78.mp3");

        MP3File f = (MP3File) AudioFileIO.read(testFile);
        Tag tag = f.getTag();
        assertInstanceOf(ID3v23Tag.class, f.getTag());
        ID3v23Tag id3v23tag = (ID3v23Tag) tag;
        //Frame contains two TYER frames
        assertEquals(21, id3v23tag.getDuplicateBytes());
        assertEquals("*TYER*", "*" + id3v23tag.getDuplicateFrameId() + "*");
        f.commit();
        f = (MP3File) AudioFileIO.read(testFile);
        tag = f.getTag();
        id3v23tag = (ID3v23Tag) tag;
        //After save the duplicate frame has been discarded
        assertEquals(0, id3v23tag.getDuplicateBytes());
        assertEquals("", id3v23tag.getDuplicateFrameId());
    }
}
