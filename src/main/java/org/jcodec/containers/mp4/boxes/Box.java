package org.jcodec.containers.mp4.boxes;

import org.jaudiotagger.audio.generic.Utils;
import org.jcodec.containers.mp4.IBoxFactory;

import java.nio.ByteBuffer;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 * <p>
 * An MP4 file struncture (box).
 *
 * @author The JCodec project
 */
public abstract class Box {
    public Header header;
    public static final int MAX_BOX_SIZE = 128 * 1024 * 1024;

    public Box(Header header) {
        this.header = header;
    }

    public Header getHeader() {
        return header;
    }

    public abstract void parse(ByteBuffer buf);

    public void write(ByteBuffer buf) {
        ByteBuffer dup = buf.duplicate();
        Utils.skip(buf, 8);
        doWrite(buf);

        header.setBodySize(buf.position() - dup.position() - 8);
        header.write(dup);
    }

    protected abstract void doWrite(ByteBuffer out);

    public abstract int estimateSize();

    public String getFourcc() {
        return header.getFourcc();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        dump(sb);
        return sb.toString();

    }

    protected void dump(StringBuilder sb) {
        sb.append("{\"tag\":\"" + header.getFourcc() + "\", \"size\":" + estimateSize() + "}");
    }

    public static Box terminatorAtom() {
        return createLeafBox(new Header(new String(new byte[4])), ByteBuffer.allocate(0));
    }

    public static boolean containsBox(NodeBox box, String path) {
        Box b = NodeBox.findFirstPath(box, Box.class, new String[]{path});
        return b != null;
    }

    public static String[] path(String path) {
        return path.split("\\.");
    }

    public static LeafBox createLeafBox(Header atom, ByteBuffer data) {
        LeafBox leaf = new LeafBox(atom);
        leaf.data = data;
        return leaf;
    }

    public static Box parseBox(ByteBuffer input, Header childAtom, IBoxFactory factory) {
        Box box = factory.newBox(childAtom);

        if (childAtom.getBodySize() < Box.MAX_BOX_SIZE) {
            box.parse(input);
            return box;
        } else {
            return new LeafBox(Header.createHeader("free", 8));
        }
    }

    public static <T extends Box> T asBox(Class<T> clazz, Box box) {
        try {
            T res = clazz.getConstructor(box.getHeader().getClass()).newInstance(box.getHeader());
            ByteBuffer buffer = ByteBuffer.allocate((int) box.getHeader().getBodySize());
            box.doWrite(buffer);
            buffer.flip();
            res.parse(buffer);
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class LeafBox extends Box {
        ByteBuffer data;

        public LeafBox(Header atom) {
            super(atom);
        }

        public void parse(ByteBuffer input) {
            data = Utils.read(input, (int) header.getBodySize());
        }

        public ByteBuffer getData() {
            return data.duplicate();
        }

        @Override
        protected void doWrite(ByteBuffer out) {
            out.put(getData());
        }

        @Override
        public int estimateSize() {
            return data.remaining() + Header.estimateHeaderSize(data.remaining());
        }
    }

}