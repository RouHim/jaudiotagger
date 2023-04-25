package org.jaudiotagger.audio.mp4;

import org.jaudiotagger.audio.generic.Utils;
import org.jcodec.containers.mp4.boxes.*;
import org.jcodec.containers.mp4.boxes.SampleToChunkBox.SampleToChunkEntry;
import org.jcodec.containers.mp4.boxes.TimeToSampleBox.TimeToSampleEntry;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.util.Arrays;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 *
 * @author The JCodec project
 */
public class ChunkReader {
    private int curChunk;
    private int sampleNo;
    private int s2cIndex;
    private int ttsInd = 0;
    private int ttsSubInd = 0;
    private long chunkTv = 0;
    private final long[] chunkOffsets;
    private final SampleToChunkEntry[] sampleToChunk;
    private final SampleSizesBox stsz;
    private final TimeToSampleEntry[] tts;
    private final SampleDescriptionBox stsd;
    private final SeekableByteChannel input;
    private final SampleEntry[] entries;

    public ChunkReader(TrakBox trakBox, SeekableByteChannel inputs) {
        TimeToSampleBox stts = trakBox.getStts();
        tts = stts.getEntries();
        ChunkOffsetsBox stco = trakBox.getStco();
        ChunkOffsets64Box co64 = trakBox.getCo64();
        stsz = trakBox.getStsz();
        SampleToChunkBox stsc = trakBox.getStsc();

        if (stco != null)
            chunkOffsets = stco.getChunkOffsets();
        else
            chunkOffsets = co64.getChunkOffsets();
        sampleToChunk = stsc.getSampleToChunk();
        stsd = trakBox.getStsd();
        entries = trakBox.getSampleEntries();
        this.input = inputs;
    }

    public boolean hasNext() {
        return curChunk < chunkOffsets.length;
    }

    public Chunk next() throws IOException {
        if (curChunk >= chunkOffsets.length)
            return null;

        if (s2cIndex + 1 < sampleToChunk.length && curChunk + 1 == sampleToChunk[s2cIndex + 1].getFirst())
            s2cIndex++;
        int sampleCount = sampleToChunk[s2cIndex].getCount();

        int[] samplesDur = null;
        int sampleDur = Chunk.UNEQUAL_DUR;
        if (ttsSubInd + sampleCount <= tts[ttsInd].getSampleCount()) {
            sampleDur = tts[ttsInd].getSampleDuration();
            ttsSubInd += sampleCount;
        } else {
            samplesDur = new int[sampleCount];
            for (int i = 0; i < sampleCount; i++) {
                if (ttsSubInd >= tts[ttsInd].getSampleCount() && ttsInd < tts.length - 1) {
                    ttsSubInd = 0;
                    ++ttsInd;
                }
                samplesDur[i] = tts[ttsInd].getSampleDuration();
                ++ttsSubInd;
            }
        }

        int size = Chunk.UNEQUAL_SIZES;
        int[] sizes = null;
        if (stsz.getDefaultSize() > 0) {
            size = getFrameSize();
        } else {
            sizes = Arrays.copyOfRange(stsz.getSizes(), sampleNo, sampleNo + sampleCount);
        }

        int dref = sampleToChunk[s2cIndex].getEntry();
        Chunk chunk = new Chunk(chunkOffsets[curChunk], chunkTv, sampleCount, size, sizes, sampleDur, samplesDur, dref);

        chunkTv += chunk.getDuration();
        sampleNo += sampleCount;
        ++curChunk;

        if (input != null) {
            SeekableByteChannel input = getInput(chunk);
            input.position(chunk.getOffset());
            chunk.setData(Utils.fetchFromChannel(input, (int) chunk.getSize()));
        }
        return chunk;
    }

    private SeekableByteChannel getInput(Chunk chunk) {
        SampleEntry se = entries[chunk.getEntry() - 1];
        if (se.getDrefInd() != 1)
            throw new RuntimeException("Multiple sample entries");
        return input;
    }

    private int getFrameSize() {
        int size = stsz.getDefaultSize();
        Box box = stsd.getBoxes().get(sampleToChunk[s2cIndex].getEntry() - 1);
        if (box instanceof AudioSampleEntry) {
            return ((AudioSampleEntry) box).calcFrameSize();
        }
        return size;
    }

    public int size() {
        return chunkOffsets.length;
    }
}