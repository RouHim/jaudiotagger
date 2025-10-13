package org.jcodec.containers.mp4.boxes;

import java.nio.ByteBuffer;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 * <p>
 * Movie fragment header box
 *
 * @author The JCodec project
 */
public class MovieFragmentHeaderBox extends FullBox {

    private int sequenceNumber;

    public MovieFragmentHeaderBox(Header atom) {
        super(atom);
    }

    public static MovieFragmentHeaderBox createMovieFragmentHeaderBox() {
        return new MovieFragmentHeaderBox(new Header(fourcc()));
    }

    public static String fourcc() {
        return "mfhd";
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        sequenceNumber = input.getInt();
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putInt(sequenceNumber);
    }

    @Override
    public int estimateSize() {
        return 16;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}
