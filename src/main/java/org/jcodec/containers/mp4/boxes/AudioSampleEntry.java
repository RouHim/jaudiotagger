package org.jcodec.containers.mp4.boxes;

import org.jaudiotagger.audio.generic.Utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 * <p>
 * Describes audio payload sample
 *
 * @author The JCodec project
 */
public class AudioSampleEntry extends SampleEntry {

    //@formatter:off
    public static int kAudioFormatFlagIsFloat = 0x1;
    public static int kAudioFormatFlagIsBigEndian = 0x2;
    public static int kAudioFormatFlagIsSignedInteger = 0x4;
    public static int kAudioFormatFlagIsPacked = 0x8;
    public static int kAudioFormatFlagIsAlignedHigh = 0x10;
    public static int kAudioFormatFlagIsNonInterleaved = 0x20;
    public static int kAudioFormatFlagIsNonMixable = 0x40;
    //@formatter:on    

    public static AudioSampleEntry createAudioSampleEntry(Header header, short drefInd, short channelCount,
                                                          short sampleSize, int sampleRate, short revision, int vendor, int compressionId, int pktSize,
                                                          int samplesPerPkt, int bytesPerPkt, int bytesPerFrame, int bytesPerSample, short version) {
        AudioSampleEntry audio = new AudioSampleEntry(header);
        audio.drefInd = drefInd;
        audio.channelCount = channelCount;
        audio.sampleSize = sampleSize;
        audio.sampleRate = sampleRate;
        audio.revision = revision;
        audio.vendor = vendor;
        audio.compressionId = compressionId;
        audio.pktSize = pktSize;
        audio.samplesPerPkt = samplesPerPkt;
        audio.bytesPerPkt = bytesPerPkt;
        audio.bytesPerFrame = bytesPerFrame;
        audio.bytesPerSample = bytesPerSample;
        audio.version = version;
        return audio;
    }

    private short channelCount;
    private short sampleSize;
    private float sampleRate;

    private short revision;
    private int vendor;
    private int compressionId;
    private int pktSize;
    private int samplesPerPkt;
    private int bytesPerPkt;
    private int bytesPerFrame;
    private int bytesPerSample;
    private short version;
    private int lpcmFlags;

    public AudioSampleEntry(Header atom) {
        super(atom);
    }

    public void parse(ByteBuffer input) {
        super.parse(input);

        version = input.getShort();
        revision = input.getShort();
        vendor = input.getInt();

        channelCount = input.getShort();
        sampleSize = input.getShort();

        compressionId = input.getShort();
        pktSize = input.getShort();

        long sr = Utils.u(input.getInt());
        sampleRate = (float) sr / 65536f;

        if (version == 1) {
            samplesPerPkt = input.getInt();
            bytesPerPkt = input.getInt();
            bytesPerFrame = input.getInt();
            bytesPerSample = input.getInt();
        } else if (version == 2) {
            input.getInt(); /* sizeof struct only */
            sampleRate = (float) Double.longBitsToDouble(input.getLong());
            channelCount = (short) input.getInt();
            input.getInt(); /* always 0x7F000000 */
            sampleSize = (short) input.getInt();
            lpcmFlags = input.getInt();
            bytesPerFrame = input.getInt();
            samplesPerPkt = input.getInt();
        }
        parseExtensions(input);
    }

    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);

        out.putShort(version);
        out.putShort(revision);
        out.putInt(vendor);

        if (version < 2) {
            out.putShort(channelCount);
            if (version == 0)
                out.putShort(sampleSize);
            else
                out.putShort((short) 16);

            out.putShort((short) compressionId);
            out.putShort((short) pktSize);

            out.putInt((int) Math.round(sampleRate * 65536d));

            if (version == 1) {
                out.putInt(samplesPerPkt);
                out.putInt(bytesPerPkt);
                out.putInt(bytesPerFrame);
                out.putInt(bytesPerSample);
            }
        } else if (version == 2) {
            out.putShort((short) 3);
            out.putShort((short) 16);
            out.putShort((short) -2);
            out.putShort((short) 0);
            out.putInt(65536);
            out.putInt(72);
            out.putLong(Double.doubleToLongBits(sampleRate));
            out.putInt(channelCount);
            out.putInt(0x7F000000);
            out.putInt(sampleSize);
            out.putInt(lpcmFlags);
            out.putInt(bytesPerFrame);
            out.putInt(samplesPerPkt);

        }
        writeExtensions(out);
    }

    public short getChannelCount() {
        return channelCount;
    }

    public int calcFrameSize() {
        if (version == 0 || bytesPerFrame == 0)
            return (sampleSize >> 3) * channelCount;
        else
            return bytesPerFrame;
    }

    public int calcSampleSize() {
        return calcFrameSize() / channelCount;
    }

    public short getSampleSize() {
        return sampleSize;
    }

    public float getSampleRate() {
        return sampleRate;
    }

    public int getBytesPerFrame() {
        return bytesPerFrame;
    }

    public int getBytesPerSample() {
        return bytesPerSample;
    }

    public short getVersion() {
        return version;
    }

    public ByteOrder getEndian() {
        EndianBox endianBox = NodeBox.findFirstPath(this, EndianBox.class, new String[]{WaveExtension.fourcc(), EndianBox.fourcc()});
        if (endianBox == null) {
            if ("twos".equals(header.getFourcc()))
                return ByteOrder.BIG_ENDIAN;
            else if ("lpcm".equals(header.getFourcc()))
                return (lpcmFlags & kAudioFormatFlagIsBigEndian) != 0 ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
            else if ("sowt".equals(header.getFourcc()))
                return ByteOrder.LITTLE_ENDIAN;
            else
                return ByteOrder.BIG_ENDIAN;
        }
        return endianBox.getEndian();
    }

    public boolean isFloat() {
        return "fl32".equals(header.getFourcc()) || "fl64".equals(header.getFourcc())
                || ("lpcm".equals(header.getFourcc()) && (lpcmFlags & kAudioFormatFlagIsFloat) != 0);
    }

    public static Set<String> pcms = new HashSet<String>();

    static {
        pcms.add("raw ");
        pcms.add("twos");
        pcms.add("sowt");
        pcms.add("fl32");
        pcms.add("fl64");
        pcms.add("in24");
        pcms.add("in32");
        pcms.add("lpcm");
    }

    public boolean isPCM() {
        return pcms.contains(header.getFourcc());
    }

    public static AudioSampleEntry compressedAudioSampleEntry(String fourcc, int drefId, int sampleSize, int channels,
                                                              int sampleRate, int samplesPerPacket, int bytesPerPacket, int bytesPerFrame) {
        AudioSampleEntry ase = createAudioSampleEntry(Header.createHeader(fourcc, 0), (short) drefId,
                (short) channels, (short) 16, sampleRate, (short) 0, 0, 65534, 0, samplesPerPacket, bytesPerPacket,
                bytesPerFrame, 16 / 8, (short) 0);
        return ase;
    }

    public static AudioSampleEntry audioSampleEntry(String fourcc, int drefId, int sampleSize, int channels,
                                                    int sampleRate, ByteOrder endian) {
        AudioSampleEntry ase = createAudioSampleEntry(Header.createHeader(fourcc, 0), (short) drefId,
                (short) channels, (short) 16, sampleRate, (short) 0, 0, 65535, 0, 1, sampleSize, channels * sampleSize,
                sampleSize, (short) 1);

        NodeBox wave = new NodeBox(new Header("wave"));
        ase.add(wave);

        wave.add(FormatBox.createFormatBox(fourcc));
        wave.add(EndianBox.createEndianBox(endian));
        wave.add(Box.terminatorAtom());
        // ase.add(new ChannelBox(atom));

        return ase;
    }


}