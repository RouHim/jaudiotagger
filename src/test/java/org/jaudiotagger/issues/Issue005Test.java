package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Issue005Test extends AbstractTestCase {

    @Test
    public void testReadingNonExistentFile() {
        Exception e = null;
        try {
            File orig = new File("src/test/resources/testdata", "testNonExistent.mp3");
            MP3File f = (MP3File) AudioFileIO.read(orig);
        } catch (Exception ex) {
            e = ex;
        }
        assertInstanceOf(FileNotFoundException.class, e);
    }

    @Test
    public void testReadingNonExistentFileMp3() {
        Exception e = null;
        try {
            File orig = new File("src/test/resources/testdata", "testNonExistent.mp3");
            MP3File f = new MP3File(orig);
        } catch (Exception ex) {
            e = ex;
        }
        assertInstanceOf(FileNotFoundException.class, e);
    }

    @Test
    public void testReadingNonExistentFileFlac() {
        Exception e = null;
        try {
            File orig = new File("src/test/resources/testdata", "testNonExistent.flac");
            AudioFile af = AudioFileIO.read(orig);
            af.getTag();
        } catch (Exception ex) {
            e = ex;
        }
        assertNotNull(e);
        assertInstanceOf(FileNotFoundException.class, e);
    }

    @Test
    public void testReadingNonExistentFileOgg() {
        Exception e = null;
        try {
            File orig = new File("src/test/resources/testdata", "testNonExistent.ogg");
            AudioFile af = AudioFileIO.read(orig);
            af.getTag();
        } catch (Exception ex) {
            e = ex;
        }
        assertNotNull(e);
        assertInstanceOf(FileNotFoundException.class, e);
    }

    @Test
    public void testReadingNonExistentFileM4a() {
        Exception e = null;
        try {
            File orig = new File("src/test/resources/testdata", "testNonExistent.m4a");
            AudioFile af = AudioFileIO.read(orig);
            af.getTag();
        } catch (Exception ex) {
            e = ex;
        }
        assertNotNull(e);
        assertInstanceOf(FileNotFoundException.class, e);
    }

    @Test
    public void testReadingNonExistentFileWma() {
        Exception e = null;
        try {
            File orig = new File("src/test/resources/testdata", "testNonExistent.wma");
            AudioFile af = AudioFileIO.read(orig);
            af.getTag();
        } catch (Exception ex) {
            e = ex;
        }
        assertNotNull(e);
        assertInstanceOf(FileNotFoundException.class, e);
    }

    @Test
    public void testReadingNonExistentFileWav() {
        Exception e = null;
        try {
            File orig = new File("src/test/resources/testdata", "testNonExistent.wav");
            AudioFile af = AudioFileIO.read(orig);
            af.getTag();
        } catch (Exception ex) {
            e = ex;
        }
        assertNotNull(e);
        assertInstanceOf(FileNotFoundException.class, e);
    }
}
