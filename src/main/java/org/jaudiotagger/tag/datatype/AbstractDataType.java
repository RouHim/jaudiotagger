/**
 * @author : Paul Taylor
 * @author : Eric Farng
 * <p>
 * Version @version:$Id$
 * <p>
 * MusicTag Copyright (C)2003,2004
 * <p>
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 * you can get a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.InvalidDataTypeException;
import org.jaudiotagger.tag.id3.AbstractTagFrameBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Represents a field/data type that can be held within a frames body, these map loosely onto
 * Section 4. ID3v2 frame overview at http://www.id3.org/id3v2.4.0-structure.txt
 */
public abstract class AbstractDataType {

    protected static final String TYPE_ELEMENT = "element";

    /**
     * Holds the data
     */
    protected Object value = null;

    /**
     * Holds the key such as "Text" or "PictureType", the naming of keys are fairly arbitary but are intended
     * to make it easier to for the developer, the keys themseleves are not written to the tag.
     */
    protected String identifier = "";

    /**
     * Holds the calling body, allows an datatype to query other objects in the
     * body such as the Text Encoding of the frame
     */
    protected AbstractTagFrameBody frameBody = null;

    /**
     * Holds the size of the data in file when read/written
     */
    protected int size;

    /**
     * Construct an abstract datatype identified by identifier and linked to a framebody without setting
     * an initial value.
     *
     * @param identifier to allow retrieval of this datatype by name from framebody
     * @param frameBody  that the dataype is associated with
     */
    protected AbstractDataType(
            String identifier,
            AbstractTagFrameBody frameBody
    ) {
        this.identifier = identifier;
        this.frameBody = frameBody;
    }

    /**
     * Construct an abstract datatype identified by identifier and linked to a framebody initilised with a value
     *
     * @param identifier to allow retrieval of this datatype by name from framebody
     * @param frameBody  that the dataype is associated with
     * @param value      of this DataType
     */
    protected AbstractDataType(
            String identifier,
            AbstractTagFrameBody frameBody,
            Object value
    ) {
        this.identifier = identifier;
        this.frameBody = frameBody;
        setValue(value);
    }

    /**
     * This is used by subclasses, to clone the data within the copyObject
     * <p>
     * TODO:It seems to be missing some of the more complex value types.
     *
     * @param copyObject
     */
    protected AbstractDataType(AbstractDataType copyObject) {
        // no copy constructor in super class
        this.identifier = copyObject.identifier;
        switch (copyObject.value) {
            case null -> this.value = null;
            case String s -> this.value = copyObject.value;
            case Boolean b -> this.value = copyObject.value;
            case Byte b -> this.value = copyObject.value;
            case Character c -> this.value = copyObject.value;
            case Double v -> this.value = copyObject.value;
            case Float v -> this.value = copyObject.value;
            case Integer i -> this.value = copyObject.value;
            case Long l -> this.value = copyObject.value;
            case Short i -> this.value = copyObject.value;
            case MultipleTextEncodedStringNullTerminated.Values values -> this.value = copyObject.value;
            case PairedTextEncodedStringNullTerminated.ValuePairs valuePairs -> this.value = copyObject.value;
            case PartOfSet.PartOfSetValue partOfSetValue -> this.value = copyObject.value;
            case boolean[] booleans -> this.value = booleans.clone();
            case byte[] bytes -> this.value = bytes.clone();
            case char[] chars -> this.value = chars.clone();
            case double[] doubles -> this.value = doubles.clone();
            case float[] floats -> this.value = floats.clone();
            case int[] ints -> this.value = ints.clone();
            case long[] longs -> this.value = longs.clone();
            case short[] shorts -> this.value = shorts.clone();
            case Object[] objects -> this.value = objects.clone();
            case ArrayList arrayList -> this.value = arrayList.clone();
            case LinkedList linkedList -> this.value = linkedList.clone();
            default -> throw new UnsupportedOperationException(
                    "Unable to create copy of class " + copyObject.getClass()
            );
        }
    }

    /**
     * Get the framebody associated with this datatype
     *
     * @return the framebody that this datatype is associated with
     */
    public AbstractTagFrameBody getBody() {
        return frameBody;
    }

    /**
     * Set the framebody that this datatype is associated with
     *
     * @param frameBody
     */
    public void setBody(AbstractTagFrameBody frameBody) {
        this.frameBody = frameBody;
    }

    /**
     * Return the key as declared by the frame bodies datatype list
     *
     * @return the key used to reference this datatype from a framebody
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Simplified wrapper for reading bytes from file into Object.
     * Used for reading Strings, this class should be overridden
     * for non String Objects
     *
     * @param arr
     * @throws org.jaudiotagger.tag.InvalidDataTypeException
     */
    public final void readByteArray(byte[] arr) throws InvalidDataTypeException {
        readByteArray(arr, 0);
    }

    /**
     * This is the starting point for reading bytes from the file into the ID3 datatype
     * starting at offset.
     * This class must be overridden
     *
     * @param arr
     * @param offset
     * @throws org.jaudiotagger.tag.InvalidDataTypeException
     */
    public abstract void readByteArray(byte[] arr, int offset)
            throws InvalidDataTypeException;

    /**
     * This defines the size in bytes of the datatype being
     * held when read/written to file.
     *
     * @return the size in bytes of the datatype
     */
    public abstract int getSize();

    /**
     * @param obj
     * @return whether this and obj are deemed equivalent
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof AbstractDataType object)) {
            return false;
        }
        if (!this.identifier.equals(object.identifier)) {
            return false;
        }
        if ((this.value == null) && (object.value == null)) {
            return true;
        } else if ((this.value == null) || (object.value == null)) {
            return false;
        }
        // boolean[]
        return switch (this.value) {
            case boolean[] booleans when object.value instanceof boolean[] ->
                    Arrays.equals(booleans, (boolean[]) object.value);
            // byte[]
            case byte[] bytes when object.value instanceof byte[] -> Arrays.equals(bytes, (byte[]) object.value);
            // char[]
            case char[] chars when object.value instanceof char[] -> Arrays.equals(chars, (char[]) object.value);
            // double[]
            case double[] doubles when object.value instanceof double[] ->
                    Arrays.equals(doubles, (double[]) object.value);
            // float[]
            case float[] floats when object.value instanceof float[] -> Arrays.equals(floats, (float[]) object.value);
            // int[]
            case int[] ints when object.value instanceof int[] -> Arrays.equals(ints, (int[]) object.value);
            // long[]
            case long[] longs when object.value instanceof long[] -> Arrays.equals(longs, (long[]) object.value);
            // Object[]
            case Object[] objects when object.value instanceof Object[] ->
                    Arrays.equals(objects, (Object[]) object.value);
            // short[]
            case short[] shorts when object.value instanceof short[] -> Arrays.equals(shorts, (short[]) object.value);
            default -> this.value.equals(object.value);
        };
    }

    /**
     * Starting point write ID3 Datatype back to array of bytes.
     * This class must be overridden.
     *
     * @return the array of bytes representing this datatype that should be written to file
     */
    public abstract byte[] writeByteArray();

    /**
     * Return String Representation of Datatype     *
     */
    public void createStructure() {
        MP3File.getStructureFormatter().addElement(
                identifier,
                getValue().toString()
        );
    }

    /**
     * Get value held by this Object
     *
     * @return value held by this Object
     */
    public Object getValue() {
        return value;
    }

    /**
     * Set the value held by this datatype, this is used typically used when the
     * user wants to modify the value in an existing frame.
     *
     * @param value
     */
    public void setValue(Object value) {
        this.value = value;
    }
}
