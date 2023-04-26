package org.jcodec.containers.mp4.boxes;

import org.jaudiotagger.audio.generic.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 * <p>
 * An MP4 file structure (atom)
 *
 * @author The JCodec project
 */
public class Header {

    private static final Logger logger = LoggerFactory.getLogger(Header.class.getCanonicalName());

    public static final byte[] FOURCC_FREE = new byte[]{'f', 'r', 'e', 'e'};
    private static final long MAX_UNSIGNED_INT = 0x100000000L;
    private final String fourcc;
    private long size;
    private boolean lng;

    public Header(String fourcc) {
        this.fourcc = fourcc;
    }

    public static Header createHeader(String fourcc, long size) {
        Header header = new Header(fourcc);
        header.size = size;
        return header;
    }

    public static Header newHeader(String fourcc, long size, boolean lng) {
        Header header = new Header(fourcc);
        header.size = size;
        header.lng = lng;
        return header;
    }

    public static Header read(ByteBuffer input) {
        long size = 0;
        while (input.remaining() >= 4 && (size = Utils.u(input.getInt())) == 0)
            ;
        if (input.remaining() < 4 || size < 8 && size != 1) {
            logger.error("Broken atom of size " + size);
            return null;
        }

        String fourcc = Utils.readFourBytesAsChars(input);
        boolean lng = false;
        if (size == 1) {
            if (input.remaining() >= 8) {
                lng = true;
                size = input.getLong();
            } else {
                logger.error("Broken atom of size " + size);
                return null;
            }
        }

        return newHeader(fourcc, size, lng);
    }

    public long headerSize() {
        return lng || (size > MAX_UNSIGNED_INT) ? 16 : 8;
    }

    public static int estimateHeaderSize(int size) {
        return size + 8 > MAX_UNSIGNED_INT ? 16 : 8;
    }

    public byte[] readContents(InputStream di) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < size - headerSize(); i++) {
            baos.write(di.read());
        }
        return baos.toByteArray();
    }

    public String getFourcc() {
        return fourcc;
    }

    public long getBodySize() {
        return size - headerSize();
    }

    public void setBodySize(int length) {
        size = length + headerSize();
    }

    public void write(ByteBuffer out) {
        if (size > MAX_UNSIGNED_INT)
            out.putInt(1);
        else
            out.putInt((int) size);
        byte[] bt = fourcc.getBytes(StandardCharsets.US_ASCII);
        if (bt != null && bt.length == 4)
            out.put(bt);
        else
            out.put(FOURCC_FREE);
        if (size > MAX_UNSIGNED_INT) {
            out.putLong(size);
        }
    }

    public void writeChannel(FileChannel output) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(16);
        write(bb);
        bb.flip();
        output.write(bb);
    }

    public long getSize() {
        return size;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fourcc == null) ? 0 : fourcc.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Header other = (Header) obj;
        if (fourcc == null) {
            return other.fourcc == null;
        } else return fourcc.equals(other.fourcc);
    }
}