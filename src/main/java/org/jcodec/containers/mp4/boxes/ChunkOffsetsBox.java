package org.jcodec.containers.mp4.boxes;

import org.jaudiotagger.audio.generic.Utils;

import java.nio.ByteBuffer;

/**
 * This class is part of JCodec ( www.jcodec.org )
 * This software is distributed under FreeBSD License
 * <p>
 * A box to hold chunk offsets
 *
 * @author The JCodec project
 */

public class ChunkOffsetsBox extends FullBox {

    public ChunkOffsetsBox(Header atom) {
        super(atom);
    }

    private long[] chunkOffsets;

    public static String fourcc() {
        return "stco";
    }

    public static ChunkOffsetsBox createChunkOffsetsBox(long[] chunkOffsets) {
        ChunkOffsetsBox stco = new ChunkOffsetsBox(new Header(fourcc()));
        stco.chunkOffsets = chunkOffsets;
        return stco;
    }

    public void parse(ByteBuffer input) {
        super.parse(input);
        int length = input.getInt();
        chunkOffsets = new long[length];
        for (int i = 0; i < length; i++) {
            chunkOffsets[i] = Utils.u(input.getInt());
        }
    }

    @Override
    public void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putInt(chunkOffsets.length);
        for (int i = 0; i < chunkOffsets.length; i++) {
            long offset = chunkOffsets[i];
            out.putInt((int) offset);
        }
    }

    @Override
    public int estimateSize() {
        return 12 + 4 + chunkOffsets.length * 4;
    }

    public long[] getChunkOffsets() {
        return chunkOffsets;
    }

    public void setChunkOffsets(long[] chunkOffsets) {
        this.chunkOffsets = chunkOffsets;
    }
}
