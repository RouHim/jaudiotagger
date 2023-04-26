package org.jaudiotagger.audio.opus;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.generic.AudioFileWriter;
import org.jaudiotagger.tag.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Write tag data to Opus File
 * <p>
 * Only works for Opus files containing a vorbis stream
 */
public class OpusFileWriter extends AudioFileWriter {

    private static final Logger logger = LoggerFactory.getLogger("org.jaudiotagger.audio.opus");

    private final OpusVorbisTagWriter vtw = new OpusVorbisTagWriter();

    protected void writeTag(AudioFile audioFile, Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotReadException, CannotWriteException, IOException {
        vtw.write(tag, raf, rafTemp);
    }

    protected void deleteTag(Tag tag, RandomAccessFile raf, RandomAccessFile tempRaf) throws CannotReadException, CannotWriteException, IOException {
        vtw.delete(raf, tempRaf);
    }
}
