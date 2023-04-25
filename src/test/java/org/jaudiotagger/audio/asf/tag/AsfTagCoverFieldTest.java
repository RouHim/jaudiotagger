package org.jaudiotagger.audio.asf.tag;


import org.jaudiotagger.tag.asf.AsfTagCoverField;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AsfTagCoverFieldTest {

    /**
     * Tests the standard constructor.
     */
    @Test
    public void testConstructor() {
        final byte[] imageData = new byte[1024];
        final int pictureType = 11;
        final String description = "description";
        final String mimeType = "image/jpeg";
        final AsfTagCoverField tag = new AsfTagCoverField(imageData, pictureType, description, mimeType);

        assertEquals(imageData.length, tag.getImageDataSize());
        assertEquals(pictureType, tag.getPictureType());
        assertEquals(mimeType, tag.getMimeType());
        assertEquals(description, tag.getDescription());
    }

}
