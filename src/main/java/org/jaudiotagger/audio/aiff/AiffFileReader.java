package org.jaudiotagger.audio.aiff;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.AudioFileReader2;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.tag.Tag;

import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Reads Audio and Metadata information contained in Aiff file.
 */
public class AiffFileReader extends AudioFileReader2 {
    private final AiffInfoReader ir = new AiffInfoReader();
    private final AiffTagReader im = new AiffTagReader();

    @Override
    protected GenericAudioHeader getEncodingInfo(FileChannel channel, final String fileName) throws CannotReadException, IOException {
        return ir.read(channel, fileName);
    }

    @Override
    protected Tag getTag(FileChannel channel, final String fileName) throws CannotReadException, IOException {
        return im.read(channel, fileName);
    }
}
