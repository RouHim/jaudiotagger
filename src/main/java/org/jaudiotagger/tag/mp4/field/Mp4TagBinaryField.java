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
package org.jaudiotagger.tag.mp4.field;

import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.mp4.Mp4TagField;

import java.io.UnsupportedEncodingException;

/**
 * Represents binary data
 *
 * <p>Subclassed by cover art field,
 * TODO unaware of any other binary fields at the moment
 */
public class Mp4TagBinaryField extends Mp4TagField {
    protected int dataSize;
    protected byte[] dataBytes;
    protected boolean isBinary = false;

    /**
     * Construct an empty Binary Field
     *
     * @param id
     */
    public Mp4TagBinaryField(String id) {
        super(id);
    }

    /**
     * Construct new binary field with binarydata provided
     *
     * @param id
     * @param data
     * @throws UnsupportedEncodingException
     */
    public Mp4TagBinaryField(String id, byte[] data) {
        super(id);
        this.dataBytes = data;
    }

    public Mp4FieldType getFieldType() {
        //TODO dont know what value this should be do we actually have any binary fields other
        //than cover art
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
        return isBinary;
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
        if (field instanceof Mp4TagBinaryField) {
            this.dataBytes = ((Mp4TagBinaryField) field).getData();
            this.isBinary = field.isBinary();
        }
    }
}
