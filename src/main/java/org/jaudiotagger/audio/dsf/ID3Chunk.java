package org.jaudiotagger.audio.dsf;

import org.jaudiotagger.audio.generic.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by Paul on 28/01/2016.
 */
public class ID3Chunk {
    private static final Logger logger = LoggerFactory.getLogger("org.jaudiotagger.audio.generic.ID3Chunk");

    private final ByteBuffer dataBuffer;

    public static ID3Chunk readChunk(ByteBuffer dataBuffer) {
        String type = Utils.readThreeBytesAsChars(dataBuffer);
        if (DsfChunkType.ID3.getCode().equals(type)) {
            return new ID3Chunk(dataBuffer);
        }
        logger.warn("Invalid type:" + type + " where expected ID3 tag");
        return null;
    }

    public ID3Chunk(ByteBuffer dataBuffer) {
        this.dataBuffer = dataBuffer;
    }

    public ByteBuffer getDataBuffer() {
        return dataBuffer;
    }
}
