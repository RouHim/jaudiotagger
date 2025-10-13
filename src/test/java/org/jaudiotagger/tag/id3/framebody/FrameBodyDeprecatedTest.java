package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FrameBodyDeprecatedTest extends AbstractTestCase {

    @Test
    public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyDeprecated fb = null;
        try {
            fb = new FrameBodyDeprecated(FrameBodyTPE1Test.getInitialisedBody());
        } catch (Exception e) {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_ARTIST, fb.getIdentifier());
        assertEquals(
                FrameBodyTPE1Test.getInitialisedBody().getBriefDescription(),
                fb.getBriefDescription()
        );
    }
}
