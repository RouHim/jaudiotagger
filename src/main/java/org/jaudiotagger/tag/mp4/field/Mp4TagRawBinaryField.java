package org.jaudiotagger.tag.mp4.field;

import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.mp4.Mp4TagField;

/**
 * Represents raw binary data
 *
 * <p>We use this when we find an atom under the ilst atom that we do not recognise , that does not
 * follow standard conventions in order to save the data without modification so it can be safetly
 * written back to file
 */
public class Mp4TagRawBinaryField extends Mp4TagField {
    protected int dataSize;
    protected byte[] dataBytes;

    protected Mp4TagRawBinaryField(String id) {
        super(id);
    }


    public Mp4FieldType getFieldType() {
        return Mp4FieldType.IMPLICIT;
    }

    /**
     * Used when creating raw content
     *
     * @return
     */
    protected byte[] getDataBytes() {
        return dataBytes;
    }

    public boolean isBinary() {
        return true;
    }

    public boolean isEmpty() {
        return this.dataBytes.length == 0;
    }

    public int getDataSize() {
        return dataSize;

    }

    public byte[] getData() {
        return this.dataBytes;
    }

    public void setData(byte[] d) {
        this.dataBytes = d;
    }

    public void copyContent(TagField field) {
        throw new UnsupportedOperationException("not done");
    }

}
