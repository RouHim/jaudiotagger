package org.jcodec.containers.mp4.boxes;

import java.nio.ByteBuffer;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 * <p>
 * A box containing sample presentation time information
 *
 * @author The JCodec project
 */
public class TimeToSampleBox extends FullBox {

    private TimeToSampleEntry[] entries;

    public TimeToSampleBox(Header atom) {
        super(atom);
    }

    public static TimeToSampleBox createTimeToSampleBox(
            TimeToSampleEntry[] timeToSamples
    ) {
        TimeToSampleBox box = new TimeToSampleBox(new Header(fourcc()));
        box.entries = timeToSamples;
        return box;
    }

    public static String fourcc() {
        return "stts";
    }

    public void parse(ByteBuffer input) {
        super.parse(input);
        int foo = input.getInt();
        entries = new TimeToSampleEntry[foo];
        for (int i = 0; i < foo; i++) {
            entries[i] = new TimeToSampleEntry(input.getInt(), input.getInt());
        }
    }

    public TimeToSampleEntry[] getEntries() {
        return entries;
    }

    public void setEntries(TimeToSampleEntry[] entries) {
        this.entries = entries;
    }

    @Override
    public void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putInt(entries.length);
        for (int i = 0; i < entries.length; i++) {
            TimeToSampleEntry timeToSampleEntry = entries[i];
            out.putInt(timeToSampleEntry.getSampleCount());
            out.putInt(timeToSampleEntry.getSampleDuration());
        }
    }

    @Override
    public int estimateSize() {
        return 16 + entries.length * 8;
    }

    public static class TimeToSampleEntry {

        int sampleCount;
        int sampleDuration;

        public TimeToSampleEntry(int sampleCount, int sampleDuration) {
            this.sampleCount = sampleCount;
            this.sampleDuration = sampleDuration;
        }

        public int getSampleCount() {
            return sampleCount;
        }

        public void setSampleCount(int sampleCount) {
            this.sampleCount = sampleCount;
        }

        public int getSampleDuration() {
            return sampleDuration;
        }

        public void setSampleDuration(int sampleDuration) {
            this.sampleDuration = sampleDuration;
        }

        public long getSegmentDuration() {
            return (long) sampleCount * sampleDuration;
        }
    }
}
