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
package org.jaudiotagger.audio.ogg;

import org.jaudiotagger.audio.ogg.util.VorbisHeader;
import org.jaudiotagger.audio.ogg.util.VorbisPacketType;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Creates a Vorbis Comment Tag from a VorbisComment for use within an OggVorbis Container.
 * <p>
 * When a Vorbis Comment is used within OggVorbis it additionally has a vorbis header and a framing
 * bit.
 * <p>
 * When a Vorbis Comment is used within OpusVorbis it additionally has a OpusTags header.
 */
public class OggVorbisCommentTagCreator {

    private static final Logger logger = LoggerFactory.getLogger("org.jaudiotagger.audio.ogg");

    public static final int FIELD_FRAMING_BIT_LENGTH = 1;
    public static final byte FRAMING_BIT_VALID_VALUE = (byte) 0x01;

    private final VorbisCommentCreator creator = new VorbisCommentCreator();
    private final byte[] prefix;
    private final byte[] capturePattern;
    private final boolean shouldWriteFramingBit;

    public OggVorbisCommentTagCreator(byte[] prefix, byte[] pattern, boolean shouldWriteFramingBit) {
        this.prefix = prefix;
        this.capturePattern = pattern;
        this.shouldWriteFramingBit = shouldWriteFramingBit;
    }

    public OggVorbisCommentTagCreator() {
        prefix = new byte[]{(byte) VorbisPacketType.COMMENT_HEADER.getType()};
        capturePattern = VorbisHeader.CAPTURE_PATTERN_AS_BYTES;
        shouldWriteFramingBit = true;
    }

    // Creates the ByteBuffer for the ogg tag
    public ByteBuffer convert(Tag tag) throws UnsupportedEncodingException {
        ByteBuffer ogg = creator.convert(tag);
        int tagLength = ogg.capacity() + prefix.length + capturePattern.length;
        if (shouldWriteFramingBit) {
            tagLength += OggVorbisCommentTagCreator.FIELD_FRAMING_BIT_LENGTH;
        }

        ByteBuffer buf = ByteBuffer.allocate(tagLength);

        //[packet type=comment0x03]['vorbis']
        buf.put(prefix);
        buf.put(capturePattern);

        //The actual tag
        buf.put(ogg);

        //Framing bit = 1
        if (shouldWriteFramingBit) {
            buf.put(FRAMING_BIT_VALID_VALUE);
        }

        buf.rewind();
        return buf;
    }
}
