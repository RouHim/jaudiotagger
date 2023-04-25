package org.jcodec.containers.mp4.boxes;

import org.jaudiotagger.audio.generic.Utils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedList;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 * <p>
 * File type box
 *
 * @author The JCodec project
 */
public class SegmentTypeBox extends Box {
    public SegmentTypeBox(Header header) {
        super(header);
        this.compBrands = new LinkedList<String>();
    }

    private String majorBrand;
    private int minorVersion;
    private Collection<String> compBrands;

    public static SegmentTypeBox createSegmentTypeBox(String majorBrand, int minorVersion, Collection<String> compBrands) {
        SegmentTypeBox styp = new SegmentTypeBox(new Header(fourcc()));
        styp.majorBrand = majorBrand;
        styp.minorVersion = minorVersion;
        styp.compBrands = compBrands;
        return styp;
    }

    public static String fourcc() {
        return "styp";
    }

    public void parse(ByteBuffer input) {
        majorBrand = Utils.readFourBytesAsChars(input);
        minorVersion = input.getInt();

        String brand;
        while (input.hasRemaining() && (brand = Utils.readFourBytesAsChars(input)) != null) {
            compBrands.add(brand);
        }
    }

    public String getMajorBrand() {
        return majorBrand;
    }

    public Collection<String> getCompBrands() {
        return compBrands;
    }

    public void doWrite(ByteBuffer out) {
        out.put(majorBrand.getBytes(StandardCharsets.US_ASCII));
        out.putInt(minorVersion);

        for (String string : compBrands) {
            out.put(string.getBytes(StandardCharsets.US_ASCII));
        }
    }

    @Override
    public int estimateSize() {
        int sz = 13;

        for (String string : compBrands) {
            sz += string.getBytes(StandardCharsets.US_ASCII).length;
        }
        return sz;
    }
}