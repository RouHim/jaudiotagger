package org.jaudiotagger.audio.asf.data;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ChunkTest extends AbstractChunk<Chunk> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected Chunk createChunk(long pos, BigInteger size) {
        return new Chunk(GUID.GUID_UNSPECIFIED, pos, size);
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.Chunk#Chunk(GUID, BigInteger)}.
     */
    @Test
    public void testChunk() {
        assertInstanceOf(IllegalArgumentException.class, failOn(null, 0, null), "IllegalArgumentException expected");
        assertInstanceOf(IllegalArgumentException.class, failOn(GUID.GUID_UNSPECIFIED, 0, null), "IllegalArgumentException expected");
        assertInstanceOf(IllegalArgumentException.class, failOn(
                GUID.GUID_UNSPECIFIED,
                0,
                BigInteger.TEN.multiply(BigInteger.valueOf(-1))
        ), "IllegalArgumentException expected");
        assertNull(
                failOn(GUID.GUID_UNSPECIFIED, 0, BigInteger.TEN),
                "Should have worked fine"
        );
        assertNull(
                failOn(GUID.GUID_UNSPECIFIED, 0, BigInteger.ZERO),
                "Should have worked fine"
        );
    }

    /**
     * Tests the creation of chunks and should fail.<br>
     *
     * @param chunkGUID   GUID.
     * @param pos         position of the chunk
     * @param chunkLength chunk size.
     * @return The occurred exception on
     * {@link #createChunk(GUID, long, BigInteger)}.
     */
    public Exception failOn(
            final GUID chunkGUID,
            final long pos,
            final BigInteger chunkLength
    ) {
        Exception result = null;
        try {
            createChunk(chunkGUID, pos, chunkLength);
        } catch (Exception e) {
            result = e;
        }
        return result;
    }

    /**
     * Creates a chunk instance.
     *
     * @param chunkGUID   GUID
     * @param pos         position of chunk
     * @param chunkLength length of chunk
     * @return chunk instance.
     */
    protected Chunk createChunk(
            final GUID chunkGUID,
            final long pos,
            final BigInteger chunkLength
    ) {
        return new Chunk(chunkGUID, pos, chunkLength);
    }
}
