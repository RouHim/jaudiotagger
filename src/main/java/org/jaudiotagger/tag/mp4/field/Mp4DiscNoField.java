package org.jaudiotagger.tag.mp4.field;

import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Represents the Disc No field
 *
 * <p>Contains some reserved fields that we currently ignore
 * <p>
 * Reserved:2 bytes
 * Disc Number:2 bytes
 * Total no of Discs:2 bytes
 */
public class Mp4DiscNoField extends Mp4TagTextNumberField {
    private static final int NONE_VALUE_INDEX = 0;
    private static final int DISC_NO_INDEX = 1;
    private static final int DISC_TOTAL_INDEX = 2;

    /**
     * Create new Disc Field parsing the String for the discno/total
     *
     * @param discValue
     * @throws org.jaudiotagger.tag.FieldDataInvalidException
     */
    public Mp4DiscNoField(String discValue) throws FieldDataInvalidException {
        super(Mp4FieldKey.DISCNUMBER.getFieldName(), discValue);

        numbers = new ArrayList<>();
        numbers.add(Short.valueOf("0"));

        String[] values = discValue.split("/");
        switch (values.length) {
            case 1:

                try {
                    numbers.add(Short.parseShort(values[0]));
                } catch (NumberFormatException nfe) {
                    throw new FieldDataInvalidException("Value of:" + values[0] + " is invalid for field:" + id);
                }
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
                break;

            default:
                throw new FieldDataInvalidException("Value is invalid for field:" + id);
        }
    }


    /**
     * Create new Disc No field with only discNo
     *
     * @param discNo
     */
    public Mp4DiscNoField(int discNo) {
        super(Mp4FieldKey.DISCNUMBER.getFieldName(), String.valueOf(discNo));
        numbers = new ArrayList<>();
        numbers.add(Short.valueOf("0"));
        numbers.add((short) discNo);
        numbers.add(Short.valueOf("0"));
    }

    /**
     * Create new Disc No Field with Disc No and total number of discs
     *
     * @param discNo
     * @param total
     */
    public Mp4DiscNoField(int discNo, int total) {
        super(Mp4FieldKey.DISCNUMBER.getFieldName(), String.valueOf(discNo));
        numbers = new ArrayList<>();
        numbers.add(Short.valueOf("0"));
        numbers.add((short) discNo);
        numbers.add((short) total);
    }

    public Mp4DiscNoField(byte[] data) {
        super(Mp4FieldKey.DISCNUMBER.getFieldName(), bytesToData(data));
        ByteBuffer buf = ByteBuffer.wrap(data);
        numbers = new ArrayList<>();
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
     * @return
     */
    public Short getDiscNo() {
        return numbers.get(DISC_NO_INDEX);
    }

    /**
     * Set Disc No
     *
     * @param discNo
     */
    public void setDiscNo(int discNo) {
        numbers.set(DISC_NO_INDEX, (short) discNo);
    }

    /**
     * @return
     */
    public Short getDiscTotal() {
        if (numbers.size() <= DISC_TOTAL_INDEX) {
            return 0;
        }
        return numbers.get(DISC_TOTAL_INDEX);
    }

    /**
     * Set total number of discs
     *
     * @param discTotal
     */
    public void setDiscTotal(int discTotal) {
        numbers.set(DISC_TOTAL_INDEX, (short) discTotal);
    }
}
