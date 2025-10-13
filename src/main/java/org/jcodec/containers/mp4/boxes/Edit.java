package org.jcodec.containers.mp4.boxes;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 *
 * @author The JCodec project
 */
public class Edit {

    private final float rate;
    private long duration;
    private long mediaTime;
    public Edit(long duration, long mediaTime, float rate) {
        this.duration = duration;
        this.mediaTime = mediaTime;
        this.rate = rate;
    }

    public static Edit createEdit(Edit edit) {
        return new Edit(edit.duration, edit.mediaTime, edit.rate);
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getMediaTime() {
        return mediaTime;
    }

    public void setMediaTime(long l) {
        mediaTime = l;
    }

    public float getRate() {
        return rate;
    }

    public void shift(long shift) {
        mediaTime += shift;
    }
}
