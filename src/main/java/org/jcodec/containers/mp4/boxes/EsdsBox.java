package org.jcodec.containers.mp4.boxes;

import org.jaudiotagger.audio.generic.Utils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 * <p>
 * MPEG 4 elementary stream descriptor
 *
 * @author The JCodec project
 */
public class EsdsBox extends FullBox {

    public static abstract class Descriptor {
        private final int _tag;
        private final int size;

        public Descriptor(int tag, int size) {
            this._tag = tag;
            this.size = size;
        }

        public void write(ByteBuffer out) {
            ByteBuffer fork = out.duplicate();
            Utils.skip(out, 5);
            doWrite(out);

            int length = out.position() - fork.position() - 5;
            fork.put((byte) _tag);
            Utils.writeBER32(fork, length);
        }

        protected abstract void doWrite(ByteBuffer out);

        int getTag() {
            return _tag;
        }
    }

    public static class NodeDescriptor extends Descriptor {
        private final Collection<Descriptor> children;

        public NodeDescriptor(int tag, Collection<Descriptor> children) {
            super(tag, 0);
            this.children = children;
        }

        protected void doWrite(ByteBuffer out) {
            for (Descriptor descr : children) {
                descr.write(out);
            }
        }

        public Collection<Descriptor> getChildren() {
            return children;
        }

        public static <T> T findByTag(Descriptor es, int tag) {
            if (es.getTag() == tag)
                return (T) es;
            else {
                if (es instanceof NodeDescriptor) {
                    for (Descriptor descriptor : ((NodeDescriptor) es).getChildren()) {
                        T res = findByTag(descriptor, tag);
                        if (res != null)
                            return res;
                    }
                }
            }
            return null;
        }
    }

    public static class DecoderConfig extends NodeDescriptor {
        private final int objectType;
        private final int bufSize;
        private final int maxBitrate;
        private final int avgBitrate;

        public DecoderConfig(int objectType, int bufSize, int maxBitrate, int avgBitrate, Collection<Descriptor> children) {
            super(tag(), children);
            this.objectType = objectType;
            this.bufSize = bufSize;
            this.maxBitrate = maxBitrate;
            this.avgBitrate = avgBitrate;
        }

        protected void doWrite(ByteBuffer out) {
            out.put((byte) objectType);
            // flags (= Audiostream)
            out.put((byte) 0x15);
            out.put((byte) (bufSize >> 16));
            out.putShort((short) bufSize);
            out.putInt(maxBitrate);
            out.putInt(avgBitrate);

            super.doWrite(out);
        }

        public static int tag() {
            return 0x4;
        }

        public int getObjectType() {
            return objectType;
        }

        public int getBufSize() {
            return bufSize;
        }

        public int getMaxBitrate() {
            return maxBitrate;
        }

        public int getAvgBitrate() {
            return avgBitrate;
        }
    }

    public static class DecoderSpecific extends Descriptor {

        private final ByteBuffer data;

        public DecoderSpecific(ByteBuffer data) {
            super(tag(), 0);
            this.data = data;
        }

        protected void doWrite(ByteBuffer out) {
            Utils.write(out, data);
        }

        public static int tag() {
            return 0x5;
        }

        public ByteBuffer getData() {
            return data;
        }

    }

    public static class SL extends Descriptor {

        public SL() {
            super(tag(), 0);
        }

        protected void doWrite(ByteBuffer out) {
            out.put((byte) 0x2);
        }

        public static int tag() {
            return 0x06;
        }
    }

    public static class ES extends NodeDescriptor {
        private final int trackId;

        public ES(int trackId, Collection<Descriptor> children) {
            super(tag(), children);
            this.trackId = trackId;
        }

        public static int tag() {
            return 0x03;
        }

        protected void doWrite(ByteBuffer out) {
            out.putShort((short) trackId);
            out.put((byte) 0);
            super.doWrite(out);
        }

        public int getTrackId() {
            return trackId;
        }
    }

    public static class DescriptorParser {

        private final static int ES_TAG = 0x03;
        private final static int DC_TAG = 0x04;
        private final static int DS_TAG = 0x05;
        private final static int SL_TAG = 0x06;

        public static Descriptor read(ByteBuffer input) {
            if (input.remaining() < 2)
                return null;
            int tag = input.get() & 0xff;
            int size = Utils.readBER32(input);

            ByteBuffer byteBuffer = Utils.read(input, size);

            switch (tag) {
                case ES_TAG:
                    return parseES(byteBuffer);
                case SL_TAG:
                    return parseSL(byteBuffer);
                case DC_TAG:
                    return parseDecoderConfig(byteBuffer);
                case DS_TAG:
                    return parseDecoderSpecific(byteBuffer);
                default:
                    throw new RuntimeException("unknown tag " + tag);
            }
        }

        private static NodeDescriptor parseNodeDesc(ByteBuffer input) {
            Collection<Descriptor> children = new ArrayList<Descriptor>();
            Descriptor d;
            do {
                d = read(input);
                if (d != null)
                    children.add(d);
            } while (d != null);
            return new NodeDescriptor(0, children);
        }

        private static ES parseES(ByteBuffer input) {
            int trackId = input.getShort();
            input.get();
            NodeDescriptor node = parseNodeDesc(input);
            return new ES(trackId, node.getChildren());
        }

        private static SL parseSL(ByteBuffer input) {
            return new SL();
        }

        private static DecoderSpecific parseDecoderSpecific(ByteBuffer input) {
            ByteBuffer data = Utils.readBuf(input);
            return new DecoderSpecific(data);
        }

        private static DecoderConfig parseDecoderConfig(ByteBuffer input) {
            int objectType = input.get() & 0xff;
            input.get();
            int bufSize = ((input.get() & 0xff) << 16) | (input.getShort() & 0xffff);
            int maxBitrate = input.getInt();
            int avgBitrate = input.getInt();

            NodeDescriptor node = parseNodeDesc(input);
            return new DecoderConfig(objectType, bufSize, maxBitrate, avgBitrate, node.getChildren());
        }
    }

    public enum Kind {
        V1(1),
        V2(2),
        MPEG4_VIDEO(32),
        MPEG4_AVC_SPS(33),
        MPEG4_AVC_PPS(34),
        MPEG4_AUDIO(64),
        MPEG2_SIMPLE_VIDEO(96),
        MPEG2_MAIN_VIDEO(97),
        MPEG2_SNR_VIDEO(98),
        MPEG2_SPATIAL_VIDEO(99),
        MPEG2_HIGH_VIDEO(100),
        MPEG2_422_VIDEO(101),
        MPEG4_ADTS_MAIN(102),
        MPEG4_ADTS_LOW_COMPLEXITY(103),
        MPEG4_ADTS_SCALEABLE_SAMPLING(104),
        MPEG2_ADTS_MAIN(105),
        MPEG1_VIDEO(106),
        MPEG1_ADTS(107),
        JPEG_VIDEO(108),
        PRIVATE_AUDIO(192),
        PRIVATE_VIDEO(208),
        PCM_LITTLE_ENDIAN_AUDIO(224),
        VORBIS_AUDIO(225),
        DOLBY_V3_AUDIO(226),
        ALAW_AUDIO(227),
        MULAW_AUDIO(228),
        ADPCM_AUDIO(229),
        PCM_BIG_ENDIAN_AUDIO(230),
        YV12_VIDEO(240),
        H264_VIDEO(241),
        H263_VIDEO(242),
        H261_VIDEO(243);

        private final int id;

        Kind(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    /**
     * Audio profile, held in Section 5 this is usually type LOW_COMPLEXITY
     */
    public enum AudioProfile {
        MAIN(1, "Main"),
        LOW_COMPLEXITY(2, "Low Complexity"),
        SCALEABLE(3, "Scaleable Sample rate"),
        T_F(4, "T/F"),
        T_F_MAIN(5, "T/F Main"),
        T_F_LC(6, "T/F LC"),
        TWIN_VQ(7, "TWIN"),
        CELP(8, "CELP"),
        HVXC(9, "HVXC"),
        HILN(10, "HILN"),
        TTSI(11, "TTSI"),
        MAIN_SYNTHESIS(12, "MAIN_SYNTHESIS"),
        WAVETABLE(13, "WAVETABLE"),
        ;

        private final int id;
        private final String description;

        /**
         * @param id          it is stored as in file
         * @param description human readable description
         */
        AudioProfile(int id, String description) {
            this.id = id;
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }
    }

    private static final Map<Integer, Kind> kindMap;
    private static final Map<Integer, AudioProfile> audioProfileMap;


    static {
        //Create maps to speed up lookup from raw value to enum
        kindMap = new HashMap<>();
        for (Kind next : Kind.values()) {
            kindMap.put(next.getId(), next);
        }

        audioProfileMap = new HashMap<Integer, AudioProfile>();
        for (AudioProfile next : AudioProfile.values()) {
            audioProfileMap.put(next.getId(), next);
        }
    }

    private ByteBuffer streamInfo;
    private int objectType;
    private int bufSize;
    private int maxBitrate;
    private int avgBitrate;
    private int trackId;

    public static String fourcc() {
        return "esds";
    }

    public EsdsBox(Header atom) {
        super(atom);
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);

        if (streamInfo != null && streamInfo.remaining() > 0) {
            ArrayList<Descriptor> l = new ArrayList<>();
            ArrayList<Descriptor> l1 = new ArrayList<>();
            l1.add(new DecoderSpecific(streamInfo));
            l.add(new DecoderConfig(objectType, bufSize, maxBitrate, avgBitrate, l1));
            l.add(new SL());
            new ES(trackId, l).write(out);
        } else {
            ArrayList<Descriptor> l = new ArrayList<>();
            l.add(new DecoderConfig(objectType, bufSize, maxBitrate, avgBitrate, new ArrayList<>()));
            l.add(new SL());
            new ES(trackId, l).write(out);
        }
    }

    @Override
    public int estimateSize() {
        return 64;
    }

    public void parse(ByteBuffer input) {
        super.parse(input);
        ES es = (ES) DescriptorParser.read(input);

        trackId = es.getTrackId();
        DecoderConfig decoderConfig = NodeDescriptor.findByTag(es, DecoderConfig.tag());
        objectType = decoderConfig.getObjectType();
        bufSize = decoderConfig.getBufSize();
        maxBitrate = decoderConfig.getMaxBitrate();
        avgBitrate = decoderConfig.getAvgBitrate();
        DecoderSpecific decoderSpecific = NodeDescriptor.findByTag(decoderConfig, DecoderSpecific.tag());
        streamInfo = decoderSpecific.getData();
    }

    public ByteBuffer getStreamInfo() {
        return streamInfo;
    }

    public int getObjectType() {
        return objectType;
    }

    public Kind getKind() {
        return kindMap.get(getObjectType());
    }

    public AudioProfile getAudioProfile() {
        ByteBuffer data = streamInfo.duplicate();
        if (data.remaining() < 1)
            return AudioProfile.MAIN;

        return audioProfileMap.get(streamInfo.duplicate().get() >> 3);
    }

    public Integer getNumberOfChannels() {
        ByteBuffer data = streamInfo.duplicate();
        if (data.remaining() < 2)
            return 2;

        Utils.skip(data, 1);
        return (data.get() << 1) >> 4;
    }

    public int getBufSize() {
        return bufSize;
    }

    public int getMaxBitrate() {
        return maxBitrate;
    }

    public int getAvgBitrate() {
        return avgBitrate;
    }

    public int getTrackId() {
        return trackId;
    }

    public static EsdsBox createEsdsBox(ByteBuffer streamInfo, int objectType, int bufSize, int maxBitrate,
                                        int avgBitrate, int trackId) {
        EsdsBox esds = new EsdsBox(new Header(fourcc()));
        esds.objectType = objectType;
        esds.bufSize = bufSize;
        esds.maxBitrate = maxBitrate;
        esds.avgBitrate = avgBitrate;
        esds.trackId = trackId;
        esds.streamInfo = streamInfo;
        return esds;
    }

    public static EsdsBox newEsdsBox() {
        return new EsdsBox(new Header(fourcc()));
    }
}