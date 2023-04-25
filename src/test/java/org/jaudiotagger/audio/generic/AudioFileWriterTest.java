package org.jaudiotagger.audio.generic;


import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.flac.FlacTag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AudioFileWriterTest {

    private AudioFile audioFile;

    @BeforeEach
    protected void setUp() throws IOException {
        final File file = File.createTempFile("AudioFileWriterTest", ".bin");
        try (final FileOutputStream out = new FileOutputStream(file)) {
            for (int i = 0; i < 100; i++) out.write("Some random stuff\n".getBytes(StandardCharsets.US_ASCII));
        }
        this.audioFile = new AudioFile(file, null, new FlacTag());
        TagOptionSingleton.getInstance().setToDefault();
    }

    @AfterEach
    protected void tearDown() {
        if (audioFile.getFile().exists()) {
            audioFile.getFile().delete();
        }
    }

    @Test
    public void testSizeHasIncreased() throws CannotWriteException {
        sizeHasChanged(200);
    }

    @Test
    public void testSizeHasDecreased() throws CannotWriteException {
        sizeHasChanged(-200);
    }

    @Test
    public void testSizeHasIncreasedWithFileIdentityPreserved() throws CannotWriteException {
        TagOptionSingleton.getInstance().setPreserveFileIdentity(true);
        sizeHasChanged(200);
    }

    @Test
    public void testSizeHasDecreasedWithFileIdentityPreserved() throws CannotWriteException {
        TagOptionSingleton.getInstance().setPreserveFileIdentity(true);
        sizeHasChanged(-200);
    }

    private void sizeHasChanged(final int fileSizeDelta) throws CannotWriteException {
        final long originalFileSize = audioFile.getFile().length();
        final AudioFileWriter audioFileWriter = new MockAudioFileWriter(fileSizeDelta);
        audioFileWriter.write(this.audioFile);
        final long fileSize = audioFile.getFile().length();
        assertEquals(originalFileSize + fileSizeDelta, fileSize, "File size is not correct");
    }

    @Test
    public void testFileIdentity() throws Exception {
        try {
            if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
                System.out.println("Skipped testFileIdentity(), because we're on Windows.");
                return;
            }
            TagOptionSingleton.getInstance().setPreserveFileIdentity(true);
            final Path path = audioFile.getFile().toPath();
            final Long originalInode = (Long) Files.getAttribute(path, "unix:ino");
            final AudioFileWriter audioFileWriter = new MockAudioFileWriter();
            audioFileWriter.write(this.audioFile);
            final Long inode = (Long) Files.getAttribute(path, "unix:ino");
            assertEquals(originalInode, inode, "Inodes do not match");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    private static class MockAudioFileWriter extends AudioFileWriter {

        private final int fileSizeDelta;

        public MockAudioFileWriter() {
            this(0);
        }

        public MockAudioFileWriter(final int fileSizeDelta) {
            this.fileSizeDelta = fileSizeDelta;
        }

        @Override
        protected void deleteTag(final Tag tag, final RandomAccessFile raf, final RandomAccessFile tempRaf) {
            // not implemented
        }

        @Override
        protected void writeTag(final AudioFile audioFile, final Tag tag, final RandomAccessFile raf, final RandomAccessFile rafTemp) throws IOException {
            // dummy code, just copy from raf to rafTemp
            final long length = raf.length();
            raf.getChannel().transferTo(0, length, rafTemp.getChannel());
            if (fileSizeDelta != 0) {
                // now adjust the size
                rafTemp.setLength(length + fileSizeDelta);
                if (fileSizeDelta > 0) {
                    rafTemp.seek(length);
                    // fill extra with 6es.
                    final byte[] buf = new byte[fileSizeDelta];
                    Arrays.fill(buf, (byte) 6);
                    rafTemp.write(buf);
                }
            }
        }
    }
}
