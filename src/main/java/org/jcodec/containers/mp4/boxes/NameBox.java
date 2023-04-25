package org.jcodec.containers.mp4.boxes;

import org.jaudiotagger.audio.generic.Utils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 *
 * @author The JCodec project
 */
public class NameBox extends Box {
    private String name;

    public static String fourcc() {
        return "name";
    }

    public static NameBox createNameBox(String name) {
        NameBox box = new NameBox(new Header(fourcc()));
        box.name = name;
        return box;
    }

    public NameBox(Header header) {
        super(header);
    }

    public void parse(ByteBuffer input) {
        name = Utils.readNullTermStringCharset(input, StandardCharsets.US_ASCII);
    }

    protected void doWrite(ByteBuffer out) {
        out.put(name.getBytes(StandardCharsets.US_ASCII));
        out.putInt(0);
    }

    @Override
    public int estimateSize() {
        return 12 + name.getBytes(StandardCharsets.US_ASCII).length;
    }

    public String getName() {
        return name;
    }
}
