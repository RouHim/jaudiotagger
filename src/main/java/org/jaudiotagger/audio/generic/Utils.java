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
package org.jaudiotagger.audio.generic;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.utils.FileTypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains various frequently used static functions in the different tag formats.
 *
 * @author Raphael Slinckx
 */
public class Utils {
    public static int BITS_IN_BYTE_MULTIPLIER = 8;
    public static int KILOBYTE_MULTIPLIER = 1000;

    private static final Logger logger = LoggerFactory.getLogger("org.jaudiotagger.audio.generic.utils");
    private static final int MAX_BASE_TEMP_FILENAME_LENGTH = 20;

    /**
     * Returns the extension of the given file.
     * The extension is empty if there is no extension
     * The extension is the string after the last "."
     *
     * @param f The file whose extension is requested
     * @return The extension of the given file
     */
    public static String getExtension(final File f) {
        final String name = f.getName().toLowerCase();
        final int i = name.lastIndexOf(".");
        if (i == -1) {
            return "";
        }

        return name.substring(i + 1);
    }

    /**
     * Returns the extension of the given file based on the file signature.
     * The extension is empty if the file signature is not recognized.
     *
     * @param f The file whose extension is requested
     * @return The extension of the given file
     */
    public static String getMagicExtension(final File f) throws IOException {
        final String fileType = FileTypeUtil.getMagicFileType(f);
        return FileTypeUtil.getMagicExt(fileType);
    }

    /**
     * Computes a number whereby the 1st byte is the least signifcant and the last
     * byte is the most significant.
     * So if storing a number which only requires one byte it will be stored in the first
     * byte.
     *
     * @param b The byte array @param start The starting offset in b
     *          (b[offset]). The less significant byte @param end The end index
     *          (included) in b (b[end]). The most significant byte
     * @return a long number represented by the byte sequence.
     */
    public static long getLongLE(final ByteBuffer b, final int start, final int end) {
        long number = 0;
        for (int i = 0; i < (end - start + 1); i++) {
            number += ((long) (b.get(start + i) & 0xFF) << i * 8);
        }

        return number;
    }

    /**
     * Computes a number whereby the 1st byte is the most significant and the last
     * byte is the least significant.
     * <p>
     * So if storing a number which only requires one byte it will be stored in the last
     * byte.
     * <p>
     * Will fail if end - start >= 8, due to the limitations of the long type.
     */
    public static long getLongBE(final ByteBuffer b, final int start, final int end) {
        long number = 0;
        for (int i = 0; i < (end - start + 1); i++) {
            number += ((long) ((b.get(end - i) & 0xFF)) << i * 8);
        }

        return number;
    }

    /**
     * Computes a number whereby the 1st byte is the least significant and the last
     * byte is the most significant. This version doesn't take a length,
     * and it returns an int rather than a long.
     *
     * @param b The byte array. Maximum length for valid results is 4 bytes.
     */
    public static int getIntLE(final byte[] b) {
        return (int) getLongLE(ByteBuffer.wrap(b), 0, b.length - 1);
    }

    /**
     * Computes a number whereby the 1st byte is the least significant and the last
     * byte is the most significant. end - start must be no greater than 4.
     *
     * @param b     The byte array
     * @param start The starting offset in b (b[offset]). The less
     *              significant byte
     * @param end   The end index (included) in b (b[end])
     * @return a int number represented by the byte sequence.
     */
    public static int getIntLE(final byte[] b, final int start, final int end) {
        return (int) getLongLE(ByteBuffer.wrap(b), start, end);
    }


    public static long getLongLE(final byte[] b, final int start, final int end) {
        return getLongLE(ByteBuffer.wrap(b), start, end);
    }

    /**
     * Computes a number whereby the 1st byte is the most significant and the last
     * byte is the least significant.
     *
     * @param b     The ByteBuffer
     * @param start The starting offset in b. The less
     *              significant byte
     * @param end   The end index (included) in b
     * @return an int number represented by the byte sequence.
     */
    public static int getIntBE(final ByteBuffer b, final int start, final int end) {
        return (int) getLongBE(b, start, end);
    }

    /**
     * Computes a number whereby the 1st byte is the most significant and the last
     * byte is the least significant.
     *
     * @param b     The ByteBuffer
     * @param start The starting offset in b. The less
     *              significant byte
     * @param end   The end index (included) in b
     * @return a short number represented by the byte sequence.
     */
    public static short getShortBE(final ByteBuffer b, final int start, final int end) {
        return (short) getIntBE(b, start, end);
    }

    /**
     * Convert int to byte representation - Big Endian (as used by mp4).
     *
     * @param size
     * @return byte representation
     */
    public static byte[] getSizeBEInt32(final int size) {
        final byte[] b = new byte[4];
        b[0] = (byte) ((size >> 24) & 0xFF);
        b[1] = (byte) ((size >> 16) & 0xFF);
        b[2] = (byte) ((size >> 8) & 0xFF);
        b[3] = (byte) (size & 0xFF);
        return b;
    }

    /**
     * Convert short to byte representation - Big Endian (as used by mp4).
     *
     * @param size number to convert
     * @return byte representation
     */
    public static byte[] getSizeBEInt16(final short size) {
        final byte[] b = new byte[2];
        b[0] = (byte) ((size >> 8) & 0xFF);
        b[1] = (byte) (size & 0xFF);
        return b;
    }

    /**
     * Convert int to byte representation - Little Endian (as used by ogg vorbis).
     *
     * @param size number to convert
     * @return byte representation
     */
    public static byte[] getSizeLEInt32(final int size) {
        final byte[] b = new byte[4];
        b[0] = (byte) (size & 0xff);
        b[1] = (byte) ((size >>> 8) & 0xffL);
        b[2] = (byte) ((size >>> 16) & 0xffL);
        b[3] = (byte) ((size >>> 24) & 0xffL);
        return b;
    }

    /**
     * Convert a byte array to a Pascal string. The first byte is the byte count,
     * followed by that many active characters.
     *
     * @param bb
     * @return
     * @throws IOException
     */
    public static String readPascalString(final ByteBuffer bb) {
        final int len = Utils.u(bb.get()); //Read as unsigned value
        final byte[] buf = new byte[len];
        bb.get(buf);
        return new String(buf, 0, len, StandardCharsets.ISO_8859_1);
    }

    public static void writePascalString(ByteBuffer buffer, String name) {
        buffer.put((byte) name.length());
        buffer.put(name.getBytes(StandardCharsets.ISO_8859_1));
    }

    /**
     * Reads bytes from a ByteBuffer as if they were encoded in the specified CharSet.
     *
     * @param buffer
     * @param offset   offset from current position
     * @param length   size of data to process
     * @param encoding
     * @return
     */
    public static String getString(final ByteBuffer buffer, final int offset, final int length, final Charset encoding) {
        final byte[] b = new byte[length];
        buffer.position(buffer.position() + offset);
        buffer.get(b);
        return new String(b, 0, length, encoding);
    }

    /**
     * Reads bytes from a ByteBuffer as if they were encoded in the specified CharSet.
     *
     * @param buffer
     * @param encoding
     * @return
     */
    public static String getString(final ByteBuffer buffer, final Charset encoding) {
        final byte[] b = new byte[buffer.remaining()];
        buffer.get(b);
        return new String(b, encoding);
    }

    /**
     * Read a 32-bit big-endian unsigned integer using a DataInput.
     * <p>
     * Reads 4 bytes but returns as long
     */
    public static long readUint32(final DataInput di) throws IOException {
        final byte[] buf8 = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        di.readFully(buf8, 4, 4);
        return ByteBuffer.wrap(buf8).getLong();
    }

    /**
     * Read a 16-bit big-endian unsigned integer.
     * <p>
     * Reads 2 bytes but returns as an integer
     */
    public static int readUint16(final DataInput di) throws IOException {
        final byte[] buf = {0x00, 0x00, 0x00, 0x00};
        di.readFully(buf, 2, 2);
        return ByteBuffer.wrap(buf).getInt();
    }


    /**
     * Read a string of a specified number of ASCII bytes.
     */
    public static String readString(final DataInput di, final int charsToRead) throws IOException {
        final byte[] buf = new byte[charsToRead];
        di.readFully(buf);
        return new String(buf, StandardCharsets.US_ASCII);
    }

    /**
     * Get a base for temp file, this should be long enough so that it easy to work out later what file the temp file
     * was created for if it is left lying round, but not ridiculously long as this can cause problems with max filename
     * limits and is not very useful.
     *
     * @param file
     * @return
     */
    public static String getBaseFilenameForTempFile(final File file) {
        final String filename = getMinBaseFilenameAllowedForTempFile(file);
        if (filename.length() <= MAX_BASE_TEMP_FILENAME_LENGTH) {
            return filename;
        }
        return filename.substring(0, MAX_BASE_TEMP_FILENAME_LENGTH);
    }

    /**
     * @param file
     * @return filename with audioformat separator stripped of, lengthened to ensure not too small for valid tempfile
     * creation.
     */
    public static String getMinBaseFilenameAllowedForTempFile(final File file) {
        final String s = AudioFile.getBaseFilename(file);
        if (s.length() >= 3) {
            return s;
        }
        if (s.length() == 1) {
            return s + "000";
        } else if (s.length() == 1) {
            return s + "00";
        } else if (s.length() == 2) {
            return s + "0";
        }
        return s;
    }

    /**
     * Rename file, and if normal rename fails, try copy and delete instead.
     *
     * @param fromFile
     * @param toFile
     * @return
     */
    public static boolean rename(final File fromFile, final File toFile) {
        logger.debug("Renaming From:" + fromFile.getAbsolutePath() + " to " + toFile.getAbsolutePath());

        if (toFile.exists()) {
            logger.error("Destination File:" + toFile + " already exists");
            return false;
        }

        //Rename File, could fail because being  used or because trying to rename over filesystems
        final boolean result;
        try {
            result = Files.move(fromFile.toPath(), toFile.toPath(), StandardCopyOption.REPLACE_EXISTING) != null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!result) {
            // Might be trying to rename over filesystem, so try copy and delete instead
            if (copy(fromFile, toFile)) {
                //If copy works but deletion of original file fails then it is because the file is being used
                //so we need to delete the file we have just created
                boolean deleteResult = fromFile.delete();
                if (!deleteResult) {
                    logger.error("Unable to delete File:" + fromFile);
                    toFile.delete();
                    return false;
                }
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Copy a File.
     * <p>
     * ToDo refactor AbstractTestCase to use this method as it contains an exact duplicate.
     *
     * @param fromFile The existing File
     * @param toFile   The new File
     * @return <code>true</code> if and only if the renaming succeeded;
     * <code>false</code> otherwise
     */
    public static boolean copy(final File fromFile, final File toFile) {
        try {
            copyThrowsOnException(fromFile, toFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void write(ByteBuffer to, ByteBuffer from) {
        if (from.hasArray()) {
            to.put(from.array(), from.arrayOffset() + from.position(), Math.min(to.remaining(), from.remaining()));
        } else {
            to.put(toArray(from));
        }
    }

    public static int skip(ByteBuffer buffer, int count) {
        int toSkip = Math.min(buffer.remaining(), count);
        buffer.position(buffer.position() + toSkip);
        return toSkip;
    }

    public static ByteBuffer read(ByteBuffer buffer, int count) {
        ByteBuffer slice = buffer.duplicate();
        int limit = buffer.position() + count;
        slice.limit(limit);
        buffer.position(limit);
        return slice;
    }

    public static String reinterpretIntAsString(Integer i) {
        return new String(ByteBuffer.allocate(4).putInt(i).array(), StandardCharsets.ISO_8859_1);
    }

    public static Integer reinterpretStringAsInt(String str) {
        return ByteBuffer.wrap(str.getBytes(StandardCharsets.ISO_8859_1)).getInt();
    }

    public static ByteBuffer readBuf(ByteBuffer buffer) {
        ByteBuffer result = buffer.duplicate();
        buffer.position(buffer.limit());
        return result;
    }

    /**
     * Reads 4 bytes and concatenates them into a String.
     * This pattern is used for ID's of various kinds.
     *
     * @param bytes
     * @return
     * @throws IOException
     */
    public static String readFourBytesAsChars(final ByteBuffer bytes) {
        if (bytes.remaining() < 4)
            return null;

        byte[] b = new byte[4];
        bytes.get(b);
        return new String(b, StandardCharsets.ISO_8859_1);
    }

    /**
     * Read a string of a specified number of ASCII bytes.
     */
    public static String readString(final ByteBuffer bytes, final int charsToRead) {
        final byte[] buf = new byte[charsToRead];
        bytes.get(buf);
        return new String(buf, StandardCharsets.US_ASCII);
    }

    public static byte[] toArray(ByteBuffer buffer) {
        byte[] result = new byte[buffer.remaining()];
        buffer.duplicate().get(result);
        return result;
    }

    public static String readNullTermStringCharset(ByteBuffer buffer, Charset charset) {
        ByteBuffer fork = buffer.duplicate();
        while (buffer.hasRemaining() && buffer.get() != 0) {
        }
        fork.limit(buffer.position() - 1);
        return new String(toArray(fork), charset);
    }

    /**
     * Reads 3 bytes and concatenates them into a String.
     * This pattern is used for ID's of various kinds.
     *
     * @param bytes
     * @return
     */
    public static String readThreeBytesAsChars(final ByteBuffer bytes) {
        byte[] b = new byte[3];
        bytes.get(b);
        return new String(b, StandardCharsets.ISO_8859_1);
    }

    /**
     * Used to convert (signed integer) to an long as if signed integer was unsigned hence allowing
     * it to represent full range of integral values.
     *
     * @param n
     * @return
     */
    public static long u(final int n) {
        return n & 0xFFFFFFFFL;
    }

    /**
     * Used to convert (signed short) to an integer as if signed short was unsigned hence allowing
     * it to represent values 0 -> 65536 rather than -32786 -> 32786
     *
     * @param n
     * @return
     */
    public static int u(final short n) {
        return n & 0xFFFF;
    }

    /**
     * Used to convert (signed byte) to an integer as if signed byte was unsigned hence allowing
     * it to represent values 0 -> 255 rather than -128 -> 127.
     *
     * @param n
     * @return
     */
    public static int u(final byte n) {
        return n & 0xFF;
    }

    public static ByteBuffer fetchFromChannel(ReadableByteChannel ch, int size) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(size);
        readFromChannel(ch, buf);
        buf.flip();
        return buf;
    }

    public static int readFromChannel(ReadableByteChannel channel, ByteBuffer buffer) throws IOException {
        int rem = buffer.position();
        while (channel.read(buffer) != -1 && buffer.hasRemaining())
            ;
        return buffer.position() - rem;
    }

    public static ByteBuffer duplicate(ByteBuffer bb) {
        ByteBuffer out = ByteBuffer.allocate(bb.remaining());
        out.put(bb.duplicate());
        out.flip();
        return out;
    }

    public static void copy(ReadableByteChannel _in, WritableByteChannel out, long amount) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(0x10000);
        int read;
        do {
            buf.position(0);
            buf.limit((int) Math.min(amount, buf.capacity()));
            read = _in.read(buf);
            if (read != -1) {
                buf.flip();
                out.write(buf);
                amount -= read;
            }
        } while (read != -1 && amount > 0);
    }

    /**
     * @param fc
     * @param size
     * @return
     * @throws IOException
     */
    public static ByteBuffer readFileDataIntoBufferLE(FileChannel fc, final int size) throws IOException {
        final ByteBuffer tagBuffer = ByteBuffer.allocateDirect(size);
        fc.read(tagBuffer);
        tagBuffer.position(0);
        tagBuffer.order(ByteOrder.LITTLE_ENDIAN);
        return tagBuffer;
    }

    /**
     * @param fc
     * @param size
     * @return
     * @throws IOException
     */
    public static ByteBuffer readFileDataIntoBufferBE(FileChannel fc, final int size) throws IOException {
        final ByteBuffer tagBuffer = ByteBuffer.allocateDirect(size);
        fc.read(tagBuffer);
        tagBuffer.position(0);
        tagBuffer.order(ByteOrder.BIG_ENDIAN);
        return tagBuffer;
    }

    /**
     * Copy src file to dst file. FileChannels are used to maximize performance.
     *
     * @param source      source File
     * @param destination destination File which will be created or truncated, before copying, if it already exists
     * @throws IOException if any error occurS
     */
    public static void copyThrowsOnException(final File source, final File destination) throws IOException {
        // Must be done in a loop as there's no guarantee that a request smaller than request count will complete in one invocation.
        // Setting the transfer size more than about 1MB is pretty pointless because there is no asymptotic benefit. What you're trying
        // to achieve with larger transfer sizes is fewer context switches, and every time you double the transfer size you halve the
        // context switch cost. Pretty soon it vanishes into the noise.
        try (FileInputStream inStream = new FileInputStream(source); FileOutputStream outStream = new FileOutputStream(destination)) {
            final FileChannel inChannel = inStream.getChannel();
            final FileChannel outChannel = outStream.getChannel();
            final long size = inChannel.size();
            long position = 0;
            while (position < size) {
                position += inChannel.transferTo(position, 1024L * 1024L, outChannel);
            }
        } //Closeables closed exiting try block in all circumstances
    }

    /**
     * @param length
     * @return true if length is an odd number
     */
    public static boolean isOddLength(long length) {
        return (length & 1) != 0;
    }

    private final static Map<Class, Class> boxed2primitive = new HashMap<Class, Class>();

    static {
        boxed2primitive.put(Void.class, void.class);
        boxed2primitive.put(Byte.class, byte.class);
        boxed2primitive.put(Short.class, short.class);
        boxed2primitive.put(Character.class, char.class);
        boxed2primitive.put(Integer.class, int.class);
        boxed2primitive.put(Long.class, long.class);
        boxed2primitive.put(Float.class, float.class);
        boxed2primitive.put(Double.class, double.class);
    }

    private static Class[] classes(Object[] params) {
        Class[] classes = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            Class<?> cls = params[i].getClass();
            classes[i] = boxed2primitive.containsKey(cls) ? boxed2primitive.get(cls) : cls;
        }
        return classes;
    }

    public static <T> T newInstance(Class<T> clazz, Object[] params) {
        try {
            return clazz.getConstructor(classes(params)).newInstance(params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeBER32(ByteBuffer buffer, int value) {
        buffer.put((byte) ((value >> 21) | 0x80));
        buffer.put((byte) ((value >> 14) | 0x80));
        buffer.put((byte) ((value >> 7) | 0x80));
        buffer.put((byte) (value & 0x7F));
    }

    public static int readBER32(ByteBuffer input) {
        int size = 0;
        for (int i = 0; i < 4; i++) {
            byte b = input.get();
            size = (size << 7) | (b & 0x7f);
            if (((b & 0xff) >> 7) == 0)
                break;
        }
        return size;
    }
}
