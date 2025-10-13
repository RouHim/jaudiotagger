package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.flac.FlacAudioHeader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class Issue428Test extends AbstractTestCase {

    @Test
    public void testGetMD5ForFlac() {
        Throwable e = null;
        try {
            File testFile = copyAudioToTmp("test.flac");
            AudioFile af = AudioFileIO.read(testFile);
            assertInstanceOf(FlacAudioHeader.class, af.getAudioHeader());
            assertEquals(
                    32,
                    ((FlacAudioHeader) af.getAudioHeader()).getMd5().length()
            );
            assertEquals(
                    "4d285826d15a2d38b4d02b4dc2d3f4e1",
                    ((FlacAudioHeader) af.getAudioHeader()).getMd5()
            );
        } catch (Exception ex) {
            e = ex;
        }
        assertNull(e);
    }

    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testGetMD5ForFlac2() {

        Throwable e = null;
        try {
            File testFile = copyAudioToTmp("test102.flac");
            AudioFile af = AudioFileIO.read(testFile);
            assertInstanceOf(FlacAudioHeader.class, af.getAudioHeader());
            assertEquals(
                    32,
                    ((FlacAudioHeader) af.getAudioHeader()).getMd5().length()
            );
            assertEquals(
                    "3a6c3caaf7987d84c2ff65a4c9f6a0d4",
                    ((FlacAudioHeader) af.getAudioHeader()).getMd5()
            );
        } catch (Exception ex) {
            e = ex;
        }
        assertNull(e);
    }
}
