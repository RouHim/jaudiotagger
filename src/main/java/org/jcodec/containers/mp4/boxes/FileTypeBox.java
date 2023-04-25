package org.jcodec.containers.mp4.boxes;

import org.jaudiotagger.audio.generic.Utils;

import java.nio.ByteBuffer;
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
public class FileTypeBox extends Box {
    public FileTypeBox(Header header) {
        super(header);
        this.compBrands = new LinkedList<String>();
    }

    private String majorBrand;
    private int minorVersion;
    private Collection<String> compBrands;

    public static String fourcc() {
        return "ftyp";
    }

    public static FileTypeBox createFileTypeBox(String majorBrand, int minorVersion, Collection<String> compBrands) {
        FileTypeBox ftyp = new FileTypeBox(new Header(fourcc()));
        ftyp.majorBrand = majorBrand;
        ftyp.minorVersion = minorVersion;
        ftyp.compBrands = compBrands;
        return ftyp;
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
        out.put(majorBrand.getBytes());
        out.putInt(minorVersion);

        for (String string : compBrands) {
            out.put(string.getBytes());
        }
    }

    @Override
    public int estimateSize() {
        int size = 5 + 8;

        for (String string : compBrands) {
            size += string.getBytes().length;
        }

        return size;
    }
}