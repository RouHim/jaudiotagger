package org.jaudiotagger.audio.mp4;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.utils.IntArrayList;
import org.jcodec.containers.mp4.boxes.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 *
 * @author The JCodec project
 */
public class ChunkWriter {
    private final long[] offsets;
    private final SampleEntry[] entries;
    private final SeekableByteChannel input;
    private int curChunk;
    private final SeekableByteChannel out;
    byte[] buf;
    private final TrakBox trak;
    private final IntArrayList sampleSizes;
    private int sampleSize;
    private int sampleCount;

    public ChunkWriter(TrakBox trak, SeekableByteChannel input, SeekableByteChannel out) {
        this.buf = new byte[8092];
        entries = trak.getSampleEntries();
        ChunkOffsetsBox stco = trak.getStco();
        ChunkOffsets64Box co64 = trak.getCo64();
        int size;
        if (stco != null)
            size = stco.getChunkOffsets().length;
        else
            size = co64.getChunkOffsets().length;
        this.input = input;

        offsets = new long[size];
        this.out = out;
        this.trak = trak;
        this.sampleSizes = IntArrayList.createIntArrayList();
    }

    public void apply() {
        NodeBox stbl = NodeBox.findFirstPath(trak, NodeBox.class, Box.path("mdia.minf.stbl"));
        stbl.removeChildren(new String[]{"stco", "co64"});

        stbl.add(ChunkOffsets64Box.createChunkOffsets64Box(offsets));
        cleanDrefs(trak);

        SampleSizesBox stsz = sampleCount != 0 ? SampleSizesBox.createSampleSizesBox(sampleSize, sampleCount)
                : SampleSizesBox.createSampleSizesBox2(sampleSizes.toArray());
        stbl.replaceBox(stsz);
    }

    private void cleanDrefs(TrakBox trak) {
        MediaInfoBox minf = trak.getMdia().getMinf();
        DataInfoBox dinf = trak.getMdia().getMinf().getDinf();
        if (dinf == null) {
            dinf = DataInfoBox.createDataInfoBox();
            minf.add(dinf);
        }

        DataRefBox dref = dinf.getDref();
        if (dref == null) {
            dref = DataRefBox.createDataRefBox();
            dinf.add(dref);
        }

        dref.getBoxes().clear();
        dref.add(AliasBox.createSelfRef());

        SampleEntry[] sampleEntries = trak.getSampleEntries();
        for (int i = 0; i < sampleEntries.length; i++) {
            SampleEntry entry = sampleEntries[i];
            entry.setDrefInd((short) 1);
        }
    }

    private SeekableByteChannel getInput(Chunk chunk) {
        SampleEntry se = entries[chunk.getEntry() - 1];
        if (se.getDrefInd() != 1)
            throw new RuntimeException("Multiple sample entries not supported");
        return input;
    }

    public void write(Chunk chunk) throws IOException {
        long pos = out.position();

        ByteBuffer chunkData = chunk.getData();
        if (chunkData == null) {
            SeekableByteChannel input = getInput(chunk);
            input.position(chunk.getOffset());
            chunkData = Utils.fetchFromChannel(input, (int) chunk.getSize());
        }

        out.write(chunkData);
        offsets[curChunk++] = pos;

        if (chunk.getSampleSize() == Chunk.UNEQUAL_SIZES) {
            if (sampleCount != 0)
                throw new RuntimeException("Mixed chunks unsupported 1.");
            sampleSizes.addAll(chunk.getSampleSizes());
        } else {
            if (sampleSizes.size() != 0)
                throw new RuntimeException("Mixed chunks unsupported 2.");
            if (sampleCount == 0) {
                sampleSize = chunk.getSampleSize();
            } else if (sampleSize != chunk.getSampleSize()) {
                throw new RuntimeException("Mismatching default sizes");
            }
            sampleCount += chunk.getSampleCount();
        }
    }
}