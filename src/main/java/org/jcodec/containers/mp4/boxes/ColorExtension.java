package org.jcodec.containers.mp4.boxes;

import org.jaudiotagger.audio.generic.Utils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 * <p>
 * Default box factory
 *
 * @author The JCodec project
 */
public class ColorExtension extends Box {

    static final byte RANGE_UNSPECIFIED = 0;
    static final byte AVCOL_RANGE_MPEG = 1;
    /// < the normal 219*2^(n-8) "MPEG" YUV ranges
    static final byte AVCOL_RANGE_JPEG = 2;
    private short primariesIndex;
    private short transferFunctionIndex;
    private short matrixIndex;
    private String type = "nclc";
    /// < the normal     2^n-1   "JPEG" YUV ranges
    private Byte colorRange = null;

    public ColorExtension(Header header) {
        super(header);
    }

    public static ColorExtension createColorExtension(
            short primariesIndex,
            short transferFunctionIndex,
            short matrixIndex
    ) {
        ColorExtension c = new ColorExtension(new Header(fourcc()));
        c.primariesIndex = primariesIndex;
        c.transferFunctionIndex = transferFunctionIndex;
        c.matrixIndex = matrixIndex;
        return c;
    }

    public static String fourcc() {
        return "colr";
    }

    public static ColorExtension createColr() {
        return new ColorExtension(new Header(fourcc()));
    }

    public void setColorRange(Byte colorRange) {
        this.colorRange = colorRange;
    }

    @Override
    public void parse(ByteBuffer input) {
        this.type = Utils.readFourBytesAsChars(input);
        primariesIndex = input.getShort();
        transferFunctionIndex = input.getShort();
        matrixIndex = input.getShort();
        if (input.hasRemaining()) {
            colorRange = input.get();
        }
    }

    @Override
    public void doWrite(ByteBuffer out) {
        out.put(type.getBytes(StandardCharsets.US_ASCII));
        out.putShort(primariesIndex);
        out.putShort(transferFunctionIndex);
        out.putShort(matrixIndex);
        if (colorRange != null) {
            out.put(colorRange);
        }
    }

    @Override
    public int estimateSize() {
        return 8 + 8;
    }

    public short getPrimariesIndex() {
        return primariesIndex;
    }

    public short getTransferFunctionIndex() {
        return transferFunctionIndex;
    }

    public short getMatrixIndex() {
        return matrixIndex;
    }
}
