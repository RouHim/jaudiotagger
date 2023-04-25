package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Issue005Test extends AbstractTestCase {
    @Test
    public void testReadingNonExistentFile() {
        Exception e = null;
        try {
            File orig = new File("testdata", "testNonExistent.mp3");
            MP3File f = (MP3File) AudioFileIO.read(orig);
        } catch (Exception ex) {
            e = ex;
        }
        assertTrue(e instanceof FileNotFoundException);
    }

    @Test
    public void testReadingNonExistentFileMp3() {
        Exception e = null;
        try {
            File orig = new File("testdata", "testNonExistent.mp3");
            MP3File f = new MP3File(orig);
        } catch (Exception ex) {
            e = ex;
        }
        assertTrue(e instanceof FileNotFoundException);
    }

    @Test
    public void testReadingNonExistentFileFlac() {
        Exception e = null;
        try {
            File orig = new File("testdata", "testNonExistent.flac");
            AudioFile af = AudioFileIO.read(orig);
            af.getTag();
        } catch (Exception ex) {
            e = ex;
        }
        assertNotNull(e);
        assertTrue(e instanceof FileNotFoundException);
    }

    @Test
    public void testReadingNonExistentFileOgg() {
        Exception e = null;
        try {
            File orig = new File("testdata", "testNonExistent.ogg");
            AudioFile af = AudioFileIO.read(orig);
            af.getTag();
        } catch (Exception ex) {
            e = ex;
        }
        assertNotNull(e);
        assertTrue(e instanceof FileNotFoundException);
    }

    @Test
    public void testReadingNonExistentFileM4a() {
        Exception e = null;
        try {
            File orig = new File("testdata", "testNonExistent.m4a");
            AudioFile af = AudioFileIO.read(orig);
            af.getTag();
        } catch (Exception ex) {
            e = ex;
        }
        assertNotNull(e);
        assertTrue(e instanceof FileNotFoundException);
    }

    @Test
    public void testReadingNonExistentFileWma() {
        Exception e = null;
        try {
            File orig = new File("testdata", "testNonExistent.wma");
            AudioFile af = AudioFileIO.read(orig);
            af.getTag();
        } catch (Exception ex) {
            e = ex;
        }
        assertNotNull(e);
        assertTrue(e instanceof FileNotFoundException);
    }

    @Test
    public void testReadingNonExistentFileWav() {
        Exception e = null;
        try {
            File orig = new File("testdata", "testNonExistent.wav");
            AudioFile af = AudioFileIO.read(orig);
            af.getTag();
        } catch (Exception ex) {
            e = ex;
        }
        assertNotNull(e);
        assertTrue(e instanceof FileNotFoundException);
    }

}