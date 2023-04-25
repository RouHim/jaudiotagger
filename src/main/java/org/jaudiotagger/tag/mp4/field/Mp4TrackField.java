package org.jaudiotagger.tag.mp4.field;

import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Represents the Track No field
 *
 * <p>There are a number of reserved fields making matters more complicated
 * Reserved:2 bytes
 * Track Number:2 bytes
 * No of Tracks:2 bytes (or zero if not known)
 * PlayListTitleReserved: 1 byte
 * playtitlenameReserved:0 bytes
 */
public class Mp4TrackField extends Mp4TagTextNumberField {
    private static final int NONE_VALUE_INDEX = 0;
    private static final int TRACK_NO_INDEX = 1;
    private static final int TRACK_TOTAL_INDEX = 2;
    private static final int NONE_END_VALUE_INDEX = 3;

    /**
     * Create new Track Field parsing the String for the trackno/total
     *
     * @param trackValue
     * @throws org.jaudiotagger.tag.FieldDataInvalidException
     */
    public Mp4TrackField(String trackValue) throws FieldDataInvalidException {
        super(Mp4FieldKey.TRACK.getFieldName(), trackValue);

        numbers = new ArrayList<>();
        numbers.add(Short.valueOf("0"));

        String[] values = trackValue.split("/");
        switch (values.length) {
            case 1:
                try {
                    numbers.add(Short.parseShort(values[0]));
                } catch (NumberFormatException nfe) {
                    throw new FieldDataInvalidException("Value of:" + values[0] + " is invalid for field:" + id);
                }
                numbers.add(Short.valueOf("0"));
                numbers.add(Short.valueOf("0"));
                break;

            case 2:
                try {
                    numbers.add(Short.parseShort(values[0]));
                } catch (NumberFormatException nfe) {
                    throw new FieldDataInvalidException("Value of:" + values[0] + " is invalid for field:" + id);
                }
                try {
                    numbers.add(Short.parseShort(values[1]));
                } catch (NumberFormatException nfe) {
                    throw new FieldDataInvalidException("Value of:" + values[1] + " is invalid for field:" + id);
                }
                numbers.add(Short.valueOf("0"));
                break;

            default:
                throw new FieldDataInvalidException("Value is invalid for field:" + id);
        }
    }


    /**
     * Create new Track Field with only track No
     *
     * @param trackNo
     */
    public Mp4TrackField(int trackNo) {
        super(Mp4FieldKey.TRACK.getFieldName(), String.valueOf(trackNo));
        numbers = new ArrayList<>();
        numbers.add(Short.valueOf("0"));
        numbers.add((short) trackNo);
        numbers.add(Short.valueOf("0"));
        numbers.add(Short.valueOf("0"));
    }

    public Mp4TrackField(byte[] data) {
        super(Mp4FieldKey.TRACK.getFieldName(), bytesToData(data));
        ByteBuffer buf = ByteBuffer.wrap(data);
        numbers = new ArrayList<>();
        numbers.add(buf.getShort());
        numbers.add(buf.getShort());
        numbers.add(buf.getShort());
        numbers.add(buf.getShort());
    }

    private static String bytesToData(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        buf.getShort();
        return buf.getShort() + "/" + buf.getShort();
    }

    /**
     * Create new Track Field with track No and total tracks
     *
     * @param trackNo
     * @param total
     */
    public Mp4TrackField(int trackNo, int total) {
        super(Mp4FieldKey.TRACK.getFieldName(), String.valueOf(trackNo));
        numbers = new ArrayList<>();
        numbers.add(Short.valueOf("0"));
        numbers.add((short) trackNo);
        numbers.add((short) total);
        numbers.add(Short.valueOf("0"));
    }

    /**
     * @return
     */
    public Short getTrackNo() {
        return numbers.get(TRACK_NO_INDEX);
    }

    /**
     * @return
     */
    public Short getTrackTotal() {
        if (numbers.size() <= TRACK_TOTAL_INDEX) {
            return 0;
        }
        return numbers.get(TRACK_TOTAL_INDEX);
    }

    /**
     * Set Track No
     *
     * @param trackNo
     */
    public void setTrackNo(int trackNo) {
        numbers.set(TRACK_NO_INDEX, (short) trackNo);
    }


    /**
     * Set total number of tracks
     *
     * @param trackTotal
     */
    public void setTrackTotal(int trackTotal) {
        numbers.set(TRACK_TOTAL_INDEX, (short) trackTotal);
    }
}
