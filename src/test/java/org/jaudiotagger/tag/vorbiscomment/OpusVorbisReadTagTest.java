package org.jaudiotagger.tag.vorbiscomment;

import org.jaudiotagger.AbstractBaseTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.opus.OpusFileReader;
import org.jaudiotagger.audio.opus.OpusVorbisTagReader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.RandomAccessFile;

public class OpusVorbisReadTagTest extends AbstractBaseTestCase {

    @Test
    public void testReadOggOpus() throws Exception {
        File testFile = copyAudioToTmp("test.opus");
        RandomAccessFile raf = new RandomAccessFile(testFile, "r");
        OpusFileReader opusFileReader = new OpusFileReader();
        opusFileReader.summarizeOggPageHeaders(testFile);
        raf.close();
    }

    @Test
    public void testReadTagFromOgg() throws Exception {
        File testFile = copyAudioToTmp("test.opus");
        RandomAccessFile raf = new RandomAccessFile(testFile, "r");
        OpusVorbisTagReader tagReader = new OpusVorbisTagReader();
        Tag vorbisTag = tagReader.read(raf);
        raf.close();
    }

    @Test
    public void testWriteBigTagToOgg() throws Exception {
        File testFile = copyAudioToTmp(
                "test.opus",
                "test-opus-file-big.opus"
        );
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag();

        tag.setField(FieldKey.WORK, "Masterpiece");
        f.commit();
    }

    @Test
    public void testWriteSmallTagToOgg() throws Exception {
        File testFile = copyAudioToTmp(
                "test.opus",
                "test-opus-file-small.opus"
        );
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag();

        tag.deleteField(FieldKey.COVER_ART);
        f.commit();
    }
}
