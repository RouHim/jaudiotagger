package org.jcodec.containers.mp4.boxes;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 * <p>
 * Track fragment box
 * <p>
 * Contains routines dedicated to simplify working with track fragments
 *
 * @author The JCodec project
 */
public class TrackFragmentBox extends NodeBox {

    public TrackFragmentBox(Header atom) {
        super(atom);
    }

    public static String fourcc() {
        return "traf";
    }

    public int getTrackId() {
        TrackFragmentHeaderBox tfhd = NodeBox
                .findFirst(this, TrackFragmentHeaderBox.class, TrackFragmentHeaderBox.fourcc());
        if (tfhd == null)
            throw new RuntimeException("Corrupt track fragment, no header atom found");
        return tfhd.getTrackId();
    }

    public static TrackFragmentBox createTrackFragmentBox() {
        return new TrackFragmentBox(new Header(fourcc()));
    }
}
