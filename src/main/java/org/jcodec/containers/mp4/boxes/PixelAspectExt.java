package org.jcodec.containers.mp4.boxes;


import java.nio.ByteBuffer;

/**
 * This class is part of JCodec ( www.jcodec.org )
 * This software is distributed under FreeBSD License
 * <p>
 * Pixel aspect ratio video sample entry extension
 *
 * @author The JCodec project
 */
public class PixelAspectExt extends Box {
    private int hSpacing;
    private int vSpacing;

    public PixelAspectExt(Header header) {
        super(header);
    }

    public void parse(ByteBuffer input) {
        hSpacing = input.getInt();
        vSpacing = input.getInt();
    }

    protected void doWrite(ByteBuffer out) {
        out.putInt(hSpacing);
        out.putInt(vSpacing);
    }

    @Override
    public int estimateSize() {
        return 16;
    }

    public int gethSpacing() {
        return hSpacing;
    }

    public int getvSpacing() {
        return vSpacing;
    }

    public static String fourcc() {
        return "pasp";
    }
}