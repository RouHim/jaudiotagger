package org.jaudiotagger.audio.opus;

import java.nio.charset.StandardCharsets;

/**
 * Defines variables common to all vorbis headers
 */
public interface OpusHeader {

    String HEAD_CAPTURE_PATTERN = "OpusHead";
    byte[] HEAD_CAPTURE_PATTERN_AS_BYTES = HEAD_CAPTURE_PATTERN.getBytes(StandardCharsets.ISO_8859_1);

    int HEAD_CAPTURE_PATTERN_POS = 0;
    int HEAD_CAPTURE_PATTERN_LENGTH = HEAD_CAPTURE_PATTERN_AS_BYTES.length;

    //Capture pattern at start of header
    String TAGS_CAPTURE_PATTERN = "OpusTags";
    byte[] TAGS_CAPTURE_PATTERN_AS_BYTES = TAGS_CAPTURE_PATTERN.getBytes(StandardCharsets.ISO_8859_1);

    int TAGS_CAPTURE_PATTERN_POS = 0;
    int TAGS_CAPTURE_PATTERN_LENGTH = TAGS_CAPTURE_PATTERN_AS_BYTES.length;
}
