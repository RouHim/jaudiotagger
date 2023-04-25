package org.jcodec.containers.mp4.boxes;

import java.nio.ByteBuffer;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 * <p>
 * A media header atom
 *
 * @author The JCodec project
 */
public class MediaHeaderBox extends FullBox {
    public MediaHeaderBox(Header atom) {
        super(atom);
    }

    private long created;
    private long modified;
    private int timescale;
    private long duration;
    private int language;
    private int quality;

    public static String fourcc() {
        return "mdhd";
    }

    public static MediaHeaderBox createMediaHeaderBox(int timescale, long duration, int language, long created,
                                                      long modified, int quality) {
        MediaHeaderBox mdhd = new MediaHeaderBox(new Header(fourcc()));
        mdhd.timescale = timescale;
        mdhd.duration = duration;
        mdhd.language = language;
        mdhd.created = created;
        mdhd.modified = modified;
        mdhd.quality = quality;
        return mdhd;
    }

    public int getTimescale() {
        return timescale;
    }

    public long getDuration() {
        return duration;
    }

    public long getCreated() {
        return created;
    }

    public long getModified() {
        return modified;
    }

    public int getLanguage() {
        return language;
    }

    public int getQuality() {
        return quality;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setTimescale(int timescale) {
        this.timescale = timescale;
    }

    public void parse(ByteBuffer input) {
        super.parse(input);
        if (version == 0) {
            created = input.getInt();
            modified = input.getInt();
            timescale = input.getInt();
            duration = input.getInt();
        } else if (version == 1) {
            created = (int) input.getLong();
            modified = (int) input.getLong();
            timescale = input.getInt();
            duration = input.getLong();
        } else {
            throw new RuntimeException("Unsupported version");
        }
    }

    public void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putInt((int) created);
        out.putInt((int) modified);
        out.putInt(timescale);
        out.putInt((int) duration);
        out.putShort((short) language);
        out.putShort((short) quality);
    }

    @Override
    public int estimateSize() {
        return 32;
    }
}