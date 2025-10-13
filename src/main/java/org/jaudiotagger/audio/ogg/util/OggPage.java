package org.jaudiotagger.audio.ogg.util;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.Utils;

import java.nio.ByteBuffer;

public record OggPage(OggPageHeader header, ByteBuffer content) {

    public OggPage(OggPageHeader header, ByteBuffer content) {
        this.header = header;
        this.content = content;
        fixCksum();
    }

    private void fixCksum() {
        ByteBuffer temp = ByteBuffer.allocate(size());
        write(temp);

        //CRC should be zero before calculating it
        temp.putInt(OggPageHeader.FIELD_PAGE_CHECKSUM_POS, 0);
        byte[] crc = OggCRCFactory.computeCRC(temp.array());
        int cksum = Utils.getIntLE(crc);

        header.setChecksum(cksum);
    }

    public void write(ByteBuffer buf) {
        header.write(buf);
        buf.put(content.duplicate());
    }

    public int size() {
        return (
                OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + // header
                        header.getSegmentTable().length + // variable segment table
                        header.getPageLength()
        ); // content
    }

    public static OggPage parse(ByteBuffer buf) throws CannotReadException {
        OggPageHeader header = OggPageHeader.read(buf);

        ByteBuffer content = buf.slice();
        content.limit(header.getPageLength());
        Utils.skip(buf, header.getPageLength());

        return new OggPage(header, content);
    }

    public void setSequenceNo(int seqNo) {
        header.setPageSequence(seqNo);
        fixCksum();
    }
}
