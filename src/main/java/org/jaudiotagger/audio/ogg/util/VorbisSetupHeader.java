package org.jaudiotagger.audio.ogg.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * Vorbis Setup header
 * <p>
 * We dont need to decode a vorbis setup header for metatagging, but we should be able to identify
 * it.
 *
 * @author Paul Taylor
 * @version 12th August 2007
 */
public class VorbisSetupHeader implements VorbisHeader {
    // Logger Object
    private static final Logger logger = LoggerFactory.getLogger("org.jaudiotagger.audio.ogg.atom");

    private boolean isValid = false;

    public VorbisSetupHeader(byte[] vorbisData) {
        decodeHeader(vorbisData);
    }

    public boolean isValid() {
        return isValid;
    }

    public void decodeHeader(byte[] b) {
        int packetType = b[FIELD_PACKET_TYPE_POS];
        logger.debug("packetType" + packetType);
        String vorbis = new String(b, FIELD_CAPTURE_PATTERN_POS, FIELD_CAPTURE_PATTERN_LENGTH, StandardCharsets.ISO_8859_1);
        if (packetType == VorbisPacketType.SETUP_HEADER.getType() && vorbis.equals(CAPTURE_PATTERN)) {
            isValid = true;
        }
    }

}
