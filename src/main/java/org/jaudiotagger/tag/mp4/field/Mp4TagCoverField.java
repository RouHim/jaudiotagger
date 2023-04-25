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

import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;

import java.io.UnsupportedEncodingException;

/**
 * Represents Cover Art
 *
 * <p>Note:Within this library we have a seperate TagCoverField for every image stored, however this does not map
 * very directly to how they are physically stored within a file, because all are stored under a single covr atom, so
 * a more complex conversion has to be done then for other fields when writing multiple images back to file.
 */
public class Mp4TagCoverField extends Mp4TagBinaryField {

    //Type
    private Mp4FieldType imageType;

    //Contains the size of each atom including header, required because may only have data atom or
    //may have data and name atom
    private int dataAndHeaderSize;

    /**
     * Empty CoverArt Field
     */
    public Mp4TagCoverField() {
        super(Mp4FieldKey.ARTWORK.getFieldName());
    }

    /**
     * @return data and header size
     */
    public int getDataAndHeaderSize() {
        return dataAndHeaderSize;
    }

    /**
     * Construct new cover art with binarydata provided
     * <p>
     * <p>
     * Identifies the imageType by looking at the data
     *
     * @param data
     * @throws UnsupportedEncodingException
     */
    public Mp4TagCoverField(byte[] data) {
        super(Mp4FieldKey.ARTWORK.getFieldName(), data);

        //Read signature
        if (ImageFormats.binaryDataIsPngFormat(data)) {
            imageType = Mp4FieldType.COVERART_PNG;
        } else if (ImageFormats.binaryDataIsJpgFormat(data)) {
            imageType = Mp4FieldType.COVERART_JPEG;
        } else if (ImageFormats.binaryDataIsGifFormat(data)) {
            imageType = Mp4FieldType.COVERART_GIF;
        } else if (ImageFormats.binaryDataIsBmpFormat(data)) {
            imageType = Mp4FieldType.COVERART_BMP;
        } else {
            logger.warning(ErrorMessage.GENERAL_UNIDENITIFED_IMAGE_FORMAT.getMsg());
            imageType = Mp4FieldType.COVERART_PNG;
        }
    }


    /**
     * Return field type, for artwork this also identifies the imagetype
     *
     * @return field type
     */
    public Mp4FieldType getFieldType() {
        return imageType;
    }

    public boolean isBinary() {
        return true;
    }


    public String toString() {
        return imageType + ":" + dataBytes.length + "bytes";
    }

    /**
     * @param imageType
     * @return the corresponding mimetype
     */
    public static String getMimeTypeForImageType(Mp4FieldType imageType) {
        if (imageType == Mp4FieldType.COVERART_PNG) {
            return ImageFormats.MIME_TYPE_PNG;
        } else if (imageType == Mp4FieldType.COVERART_JPEG) {
            return ImageFormats.MIME_TYPE_JPEG;
        } else if (imageType == Mp4FieldType.COVERART_GIF) {
            return ImageFormats.MIME_TYPE_GIF;
        } else if (imageType == Mp4FieldType.COVERART_BMP) {
            return ImageFormats.MIME_TYPE_BMP;
        } else {
            return null;
        }
    }
}
