/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Raphaël Slinckx <raphael@slinckx.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jaudiotagger.audio.ogg.util;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * $Id$
 * <p>
 * reference:http://xiph.org/ogg/doc/framing.html
 *
 * @author Raphael Slinckx (KiKiDonK)
 * @version 16 december 2003
 */
public class OggPageHeader {

    private static final Logger logger = LoggerFactory.getLogger("org.jaudiotagger.audio.ogg.atom");

    //Capture pattern at start of header
    public static final byte[] CAPTURE_PATTERN = {'O', 'g', 'g', 'S'};

    //Ogg Page header is always 27 bytes plus the size of the segment table which is variable
    public static final int OGG_PAGE_HEADER_FIXED_LENGTH = 27;

    //Can have upto 255 segments in a page
    public static final int MAXIMUM_NO_OF_SEGMENT_SIZE = 255;

    //Each segment can be upto 255 bytes
    public static final int MAXIMUM_SEGMENT_SIZE = 255;

    //Maximum size of pageheader (27 + 255 = 282)
    public static final int MAXIMUM_PAGE_HEADER_SIZE = OGG_PAGE_HEADER_FIXED_LENGTH + MAXIMUM_NO_OF_SEGMENT_SIZE;

    //Maximum size of page data following the page header (255 * 255 = 65025)
    public static final int MAXIMUM_PAGE_DATA_SIZE = MAXIMUM_NO_OF_SEGMENT_SIZE * MAXIMUM_SEGMENT_SIZE;

    //Maximum size of page includes header and data (282 + 65025 = 65307 bytes)
    public static final int MAXIMUM_PAGE_SIZE = MAXIMUM_PAGE_HEADER_SIZE + MAXIMUM_PAGE_DATA_SIZE;

    //Starting positions of the various attributes
    public static final int FIELD_CAPTURE_PATTERN_POS = 0;
    public static final int FIELD_STREAM_STRUCTURE_VERSION_POS = 4;
    public static final int FIELD_HEADER_TYPE_FLAG_POS = 5;
    public static final int FIELD_ABSOLUTE_GRANULE_POS = 6;
    public static final int FIELD_STREAM_SERIAL_NO_POS = 14;
    public static final int FIELD_PAGE_SEQUENCE_NO_POS = 18;
    public static final int FIELD_PAGE_CHECKSUM_POS = 22;
    public static final int FIELD_PAGE_SEGMENTS_POS = 26;
    public static final int FIELD_SEGMENT_TABLE_POS = 27;

    //Length of various attributes
    public static final int FIELD_CAPTURE_PATTERN_LENGTH = 4;
    public static final int FIELD_STREAM_STRUCTURE_VERSION_LENGTH = 1;
    public static final int FIELD_HEADER_TYPE_FLAG_LENGTH = 1;
    public static final int FIELD_ABSOLUTE_GRANULE_LENGTH = 8;
    public static final int FIELD_STREAM_SERIAL_NO_LENGTH = 4;
    public static final int FIELD_PAGE_SEQUENCE_NO_LENGTH = 4;
    public static final int FIELD_PAGE_CHECKSUM_LENGTH = 4;
    public static final int FIELD_PAGE_SEGMENTS_LENGTH = 1;

    private final byte[] rawHeaderData;

    private final byte streamStructureRevision;
    private final byte headerTypeFlag;
    private long absoluteGranulePosition;

    private int streamSerialNumber;
    private int pageSequenceNumber;
    private int checksum;
    private byte pageSegments;
    private byte[] segmentTable;

    private transient boolean isValid;
    private transient boolean lastPacketIncomplete;
    private final transient boolean firstPage;
    private final transient boolean continuedPage;
    private final transient boolean lastPage;

    private transient int pageLength = 0;
    private transient long startByte = 0;
    private final transient List<PacketStartAndLength> packetList = new ArrayList<>();

    public static OggPageHeader createCommentHeader(int pageSize, boolean continued, int serial, int sequence) {
        int fullPacketSegments = pageSize / MAXIMUM_NO_OF_SEGMENT_SIZE;
        int pageRemainder = pageSize % MAXIMUM_NO_OF_SEGMENT_SIZE;

        int totalPacketSegments = fullPacketSegments;
        if (pageRemainder > 0) {
            // last segment is not full
            totalPacketSegments++;
        }

        byte[] raw = new byte[OGG_PAGE_HEADER_FIXED_LENGTH + totalPacketSegments];

        ByteBuffer buf = ByteBuffer.wrap(raw).order(ByteOrder.LITTLE_ENDIAN);
        buf.put(OggPageHeader.CAPTURE_PATTERN);
        buf.put((byte) 0x0);
        buf.put((byte) (continued ? 0x1 : 0x0));

        buf.putLong(0L);
        buf.putInt(serial);
        buf.putInt(sequence);
        buf.putInt(0x0); // checksum
        buf.put((byte) totalPacketSegments);

        // segment table
        for (int segNum = 0; segNum < (fullPacketSegments & 0xFF); segNum++) {
            buf.put((byte) 0xFF);
        }

        // last segment, if needed
        if (pageRemainder > 0) {
            buf.put((byte) pageRemainder);
        }

        return new OggPageHeader(raw);
    }

    private void calculateChecksumOverPage(ByteBuffer page) {
        //CRC should be zero before calculating it
        page.putInt(OggPageHeader.FIELD_PAGE_CHECKSUM_POS, 0);

        //Compute CRC over the  page  //TODO shouldnt really use array();
        byte[] crc = OggCRCFactory.computeCRC(page.array());
        for (int i = 0; i < crc.length; i++) {
            page.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + i, crc[i]);
        }

        //Rewind to start of Page
        page.rewind();
    }

    /**
     * Read next PageHeader from Buffer
     *
     * @param byteBuffer
     * @return
     * @throws CannotReadException
     */
    public static OggPageHeader read(ByteBuffer byteBuffer) throws CannotReadException {
        //byteBuffer
        int start = byteBuffer.position();
        logger.debug("Trying to read OggPage at:" + start);

        byte[] b = new byte[OggPageHeader.CAPTURE_PATTERN.length];
        byteBuffer.get(b);
        if (!(Arrays.equals(b, OggPageHeader.CAPTURE_PATTERN))) {
            throw new CannotReadException(ErrorMessage.OGG_HEADER_CANNOT_BE_FOUND.getMsg(new String(b)));
        }

        byteBuffer.position(start + OggPageHeader.FIELD_PAGE_SEGMENTS_POS);
        int pageSegments = byteBuffer.get() & 0xFF; //unsigned
        byteBuffer.position(start);

        b = new byte[OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + pageSegments];
        byteBuffer.get(b);

        //Now just after PageHeader, ready for Packet Data
        return new OggPageHeader(b);
    }

    /**
     * Read next PageHeader from file
     *
     * @param raf
     * @return
     * @throws IOException
     * @throws CannotReadException
     */
    public static OggPageHeader read(RandomAccessFile raf) throws IOException, CannotReadException {
        long start = raf.getFilePointer();
        logger.debug("Trying to read OggPage at: " + start);

        byte[] b = new byte[OggPageHeader.CAPTURE_PATTERN.length];
        raf.read(b);
        if (!(Arrays.equals(b, OggPageHeader.CAPTURE_PATTERN))) {
            raf.seek(start);
            if (AbstractID3v2Tag.isId3Tag(raf)) {
                logger.warn(ErrorMessage.OGG_CONTAINS_ID3TAG.getMsg(raf.getFilePointer() - start));
                raf.read(b);
                if ((Arrays.equals(b, OggPageHeader.CAPTURE_PATTERN))) {
                    //Go to the end of the ID3 header
                    start = raf.getFilePointer() - OggPageHeader.CAPTURE_PATTERN.length;
                }
            } else {
                throw new CannotReadException(ErrorMessage.OGG_HEADER_CANNOT_BE_FOUND.getMsg(new String(b)));
            }
        }

        raf.seek(start + OggPageHeader.FIELD_PAGE_SEGMENTS_POS);
        int pageSegments = raf.readByte() & 0xFF; //unsigned
        raf.seek(start);

        b = new byte[OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + pageSegments];
        raf.read(b);


        OggPageHeader pageHeader = new OggPageHeader(b);
        pageHeader.setStartByte(start);
        //Now just after PageHeader, ready for Packet Data
        return pageHeader;
    }

    public OggPageHeader(byte[] b) {
        this.rawHeaderData = b;
        streamStructureRevision = b[FIELD_STREAM_STRUCTURE_VERSION_POS];
        headerTypeFlag = b[FIELD_HEADER_TYPE_FLAG_POS];

        continuedPage = (headerTypeFlag & 0x01) != 0;
        firstPage = (headerTypeFlag & 0x02) != 0;
        lastPage = (headerTypeFlag & 0x04) != 0;

        if (streamStructureRevision == 0x0) {
            this.absoluteGranulePosition = Utils.getLongLE(b, FIELD_ABSOLUTE_GRANULE_POS, 13);
            this.streamSerialNumber = Utils.getIntLE(b, FIELD_STREAM_SERIAL_NO_POS, 17);
            this.pageSequenceNumber = Utils.getIntLE(b, FIELD_PAGE_SEQUENCE_NO_POS, 21);
            this.checksum = Utils.getIntLE(b, FIELD_PAGE_CHECKSUM_POS, 25);
            this.pageSegments = b[FIELD_PAGE_SEGMENTS_POS];

            this.segmentTable = new byte[b.length - OGG_PAGE_HEADER_FIXED_LENGTH];
            int packetLength = 0;
            Integer segmentLength = null;
            for (int i = 0; i < segmentTable.length; i++) {
                segmentTable[i] = b[OGG_PAGE_HEADER_FIXED_LENGTH + i];
                segmentLength = u(segmentTable[i]);
                this.pageLength += segmentLength;
                packetLength += segmentLength;

                if (segmentLength < MAXIMUM_SEGMENT_SIZE) {
                    packetList.add(new PacketStartAndLength(pageLength - packetLength, packetLength));
                    packetLength = 0;
                }
            }

            //If last segment value is 255 this packet continues onto next page
            //and will not have been added to the packetStartAndEnd list yet
            if (segmentLength != null) {
                if (segmentLength == MAXIMUM_SEGMENT_SIZE) {
                    packetList.add(new PacketStartAndLength(pageLength - packetLength, packetLength));
                    lastPacketIncomplete = true;
                }
            }
            isValid = true;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Constructed OggPage: " + this);
        }
    }

    public void write(ByteBuffer dst) {
        dst.order(ByteOrder.LITTLE_ENDIAN);

        dst.put(OggPageHeader.CAPTURE_PATTERN);
        dst.put(streamStructureRevision);
        dst.put(headerTypeFlag);

        if (streamStructureRevision == 0x0) {
            dst.putLong(absoluteGranulePosition);
            dst.putInt(streamSerialNumber);
            dst.putInt(pageSequenceNumber);
            dst.putInt(checksum);
            dst.put(pageSegments);
            dst.put(segmentTable);
        }
    }

    private int u(int i) {
        return i & 0xFF;
    }

    /**
     * @return true if the last packet on this page extends to the next page
     */
    public boolean isLastPacketIncomplete() {
        return lastPacketIncomplete;
    }

    public long getAbsoluteGranulePosition() {
        logger.debug("Number Of Samples: " + absoluteGranulePosition);
        return this.absoluteGranulePosition;
    }

    public int getCheckSum() {
        return checksum;
    }

    public void setChecksum(int checksum) {
        ByteBuffer.wrap(rawHeaderData).order(ByteOrder.LITTLE_ENDIAN).putInt(FIELD_PAGE_CHECKSUM_POS, checksum);
        this.checksum = checksum;
    }

    public byte getHeaderType() {
        return headerTypeFlag;
    }


    public int getPageLength() {
        logger.debug("This page length: " + pageLength);
        return this.pageLength;
    }

    public int getPageSequence() {
        return pageSequenceNumber;
    }

    public void setPageSequence(int seqNo) {
        ByteBuffer.wrap(rawHeaderData).order(ByteOrder.LITTLE_ENDIAN).putInt(FIELD_PAGE_SEQUENCE_NO_POS, seqNo);
        this.pageSequenceNumber = seqNo;
    }

    public int getSerialNumber() {
        return streamSerialNumber;
    }

    public byte[] getSegmentTable() {
        return this.segmentTable;
    }

    public boolean isValid() {
        return isValid;
    }

    /**
     * @return a list of packet start position and size within this page.
     */
    public List<PacketStartAndLength> getPacketList() {
        return packetList;
    }

    /**
     * @return the raw header data that this pageheader is derived from
     */
    public byte[] getRawHeaderData() {
        return rawHeaderData;
    }

    public String toString() {
        String out = "Ogg Page Header { isValid: " + isValid +
                ", type: " + headerTypeFlag +
                ", oggPageHeaderLength: " + rawHeaderData.length +
                ", length: " + pageLength +
                ", seqNo: " + getPageSequence() +
                ", packetIncomplete: " + isLastPacketIncomplete() +
                ", serNum: " + this.getSerialNumber() + " } ";

        for (PacketStartAndLength packet : getPacketList()) {
            out += packet.toString();
        }
        return out;
    }

    /**
     * Startbyte of this pageHeader in the file
     * <p>
     * This is useful for Ogg files that contain unsupported additional data at the start of the file such
     * as ID3 data
     */
    public long getStartByte() {
        return startByte;
    }

    public void setStartByte(long startByte) {
        this.startByte = startByte;
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public boolean isContinuedPage() {
        return continuedPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    /**
     * Within the page specifies the start and length of each packet
     * in the page offset from the end of the pageheader (after the segment table)
     */
    public static class PacketStartAndLength {
        private Integer startPosition = 0;
        private Integer length = 0;

        public PacketStartAndLength(int startPosition, int length) {
            this.startPosition = startPosition;
            this.length = length;
        }

        public int getStartPosition() {
            return startPosition;
        }

        public void setStartPosition(int startPosition) {
            this.startPosition = startPosition;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public String toString() {
            return "NextPkt(start: " + startPosition + ", length: " + length + "),";
        }
    }

    /**
     * This represents all the flags that can be set in the headerType field.
     * Note these values can be ORED together. For example the last packet in
     * a file would normally have a value of 0x5 because both the CONTINUED_PACKET
     * bit and the END_OF_BITSTREAM bit would be set.
     */
    public enum HeaderTypeFlag {
        FRESH_PACKET((byte) 0x0),
        CONTINUED_PACKET((byte) 0x1),
        START_OF_BITSTREAM((byte) 0x2),
        END_OF_BITSTREAM((byte) 0x4);

        byte fileValue;

        HeaderTypeFlag(byte fileValue) {
            this.fileValue = fileValue;
        }

        /**
         * @return the value that should be written to file to enable this flag
         */
        public byte getFileValue() {
            return fileValue;
        }
    }
}

