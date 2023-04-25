package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class Issue432Test extends AbstractTestCase {
    @Test
    public void testGetBitsPerSampleFlac() {
        Throwable e = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test.flac");
            AudioFile af = AudioFileIO.read(testFile);
            assertTrue(af.getAudioHeader().isLossless());
        } catch (Exception ex) {
            e = ex;
        }
        assertNull(e);
    }

    @Test
    public void testGetBitsPerSampleMp4() {
        Throwable e = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test.m4a");
            AudioFile af = AudioFileIO.read(testFile);
            assertFalse(af.getAudioHeader().isLossless());
        } catch (Exception ex) {
            e = ex;
        }
        assertNull(e);
    }

    @Test
    public void testGetBitsPerSampleOgg() {
        Throwable e = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test.ogg");
            AudioFile af = AudioFileIO.read(testFile);
            assertFalse(af.getAudioHeader().isLossless());
        } catch (Exception ex) {
            e = ex;
        }
        assertNull(e);
    }

    @Test
    public void testGetBitsPerSampleWma() {
        Throwable e = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test1.wma");
            AudioFile af = AudioFileIO.read(testFile);
            assertFalse(af.getAudioHeader().isLossless());
        } catch (Exception ex) {
            e = ex;
        }
        assertNull(e);
    }

    @Test
    public void testGetBitsPerSampleMp3() {
        Throwable e = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testGetBitsPerSampleMp3-2.mp3"));
            AudioFile af = AudioFileIO.read(testFile);
            assertFalse(af.getAudioHeader().isLossless());
        } catch (Exception ex) {
            e = ex;
        }
        assertNull(e);
    }
}
