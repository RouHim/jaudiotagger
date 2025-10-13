package org.jaudiotagger.audio.dsf;

import org.jaudiotagger.audio.generic.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by Paul on 28/01/2016.
 */
public record ID3Chunk(ByteBuffer dataBuffer) {

    private static final Logger log = LoggerFactory.getLogger(ID3Chunk.class);

    public static ID3Chunk readChunk(ByteBuffer dataBuffer) {
        String type = Utils.readThreeBytesAsChars(dataBuffer);
        if (DsfChunkType.ID3.getCode().equals(type)) {
            return new ID3Chunk(dataBuffer);
        }
        log.warn("Invalid type:" + type + " where expected ID3 tag");
        return null;
    }
}
