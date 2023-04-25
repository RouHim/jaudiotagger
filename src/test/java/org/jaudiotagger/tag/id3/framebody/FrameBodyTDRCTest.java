package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FrameBodyTDRCTest extends AbstractTestCase {
    public static final String TEST_YEAR = "2002";


    @Test
    public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyTDRC fb = null;
        try {
            fb = new FrameBodyTDRC();
            fb.setDate(TEST_YEAR);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_YEAR, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTDRCTest.TEST_YEAR, fb.getDate());
    }
}
