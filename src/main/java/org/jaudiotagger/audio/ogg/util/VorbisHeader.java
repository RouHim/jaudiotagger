package org.jaudiotagger.audio.ogg.util;

import java.nio.charset.StandardCharsets;

/**
 * Defines variables common to all vorbis headers
 */
public interface VorbisHeader {
    //Capture pattern at start of header
    String CAPTURE_PATTERN = "vorbis";
    byte[] CAPTURE_PATTERN_AS_BYTES = CAPTURE_PATTERN.getBytes(StandardCharsets.ISO_8859_1);

    int FIELD_PACKET_TYPE_POS = 0;
    int FIELD_CAPTURE_PATTERN_POS = 1;

    int FIELD_PACKET_TYPE_LENGTH = 1;
    int FIELD_CAPTURE_PATTERN_LENGTH = CAPTURE_PATTERN_AS_BYTES.length;
}
