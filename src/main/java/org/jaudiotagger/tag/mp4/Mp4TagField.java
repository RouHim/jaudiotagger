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
package org.jaudiotagger.tag.mp4;

import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.mp4.field.Mp4FieldType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * This abstract class represents a link between piece of data, and how it is stored as an mp4 atom
 * <p>
 * Note there isnt a one to one correspondance between a tag field and a box because some fields are represented
 * by multiple boxes, for example many of the MusicBrainz fields use the '----' box, which in turn uses one of mean,
 * name and data box. So an instance of a tag field maps to one item of data such as 'Title', but it may have to read
 * multiple boxes to do this.
 * <p>
 * There are various subclasses that represent different types of fields
 */
public abstract class Mp4TagField implements TagField {
    // Logger Object
    private static final Logger logger = LoggerFactory.getLogger("org.jaudiotagger.tag.mp4");


    protected String id;

    protected Mp4TagField(String id) {
        this.id = id;
    }

    /**
     * @return field identifier
     */
    public String getId() {
        return id;
    }

    public void isBinary(boolean b) {
        /* One cannot choose if an arbitrary block can be binary or not */
    }

    public boolean isCommon() {
        return id.equals(Mp4FieldKey.ARTIST.getFieldName()) || id.equals(Mp4FieldKey.ALBUM.getFieldName()) || id.equals(Mp4FieldKey.TITLE.getFieldName()) || id.equals(Mp4FieldKey.TRACK.getFieldName()) || id.equals(Mp4FieldKey.DAY.getFieldName()) || id.equals(Mp4FieldKey.COMMENT.getFieldName()) || id.equals(Mp4FieldKey.GENRE.getFieldName());
    }

    /**
     * @return field identifier as it will be held within the file
     */
    protected byte[] getIdBytes() {
        return getId().getBytes(StandardCharsets.ISO_8859_1);
    }

    /**
     * @return the data as it is held on file
     * @throws UnsupportedEncodingException
     */
    protected abstract byte[] getDataBytes() throws UnsupportedEncodingException;


    /**
     * @return the field type of this field
     */
    public abstract Mp4FieldType getFieldType();


    /**
     * Convert back to raw content, includes parent and data atom as views as one thing externally
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public byte[] getRawContent() throws UnsupportedEncodingException {
        logger.debug("Getting Raw data for:" + getId());
        return getDataBytes();
    }
}
