package org.jcodec.containers.mp4.boxes;


import org.jaudiotagger.audio.generic.Utils;

import java.nio.ByteBuffer;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 *
 * @author The JCodec project
 */
public class FormatBox extends Box {
    private String fmt;

    public FormatBox(Header header) {
        super(header);
    }

    public static String fourcc() {
        return "frma";
    }

    public static FormatBox createFormatBox(String fmt) {
        FormatBox frma = new FormatBox(new Header(fourcc()));
        frma.fmt = fmt;
        return frma;
    }

    public void parse(ByteBuffer input) {
        this.fmt = Utils.readFourBytesAsChars(input);
    }

    protected void doWrite(ByteBuffer out) {
        out.put(fmt.getBytes());
    }

    @Override
    public int estimateSize() {
        return fmt.getBytes().length + 8;
    }
}