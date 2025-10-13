package org.jaudiotagger;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

public class TestFileTypeMagic extends AbstractBaseTestCase {

    @Test
    public void testMagic() throws Exception {
        File testFileLoc = fileResource("testdata", "test.m4a");

        testFileLoc = copyAudioToTmp("test.m4a");
        AudioFile f = AudioFileIO.readMagic(testFileLoc);
        Tag audioTag = f.getTag();
        log.error("audiotag:" + audioTag.toString());
        audioTag.setField(FieldKey.ALBUM, "TestAsPass");
        AudioFileIO.write(f);
    }
}
