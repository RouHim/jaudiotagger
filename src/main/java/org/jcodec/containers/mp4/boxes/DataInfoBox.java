package org.jcodec.containers.mp4.boxes;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 * <p>
 * Creates MP4 file out of a set of samples
 *
 * @author The JCodec project
 */
public class DataInfoBox extends NodeBox {

    public DataInfoBox(Header atom) {
        super(atom);
    }

    public static DataInfoBox createDataInfoBox() {
        return new DataInfoBox(new Header(fourcc()));
    }

    public static String fourcc() {
        return "dinf";
    }

    public DataRefBox getDref() {
        return NodeBox.findFirst(this, DataRefBox.class, "dref");
    }
}
