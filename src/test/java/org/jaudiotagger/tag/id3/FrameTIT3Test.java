package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTPE1;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FrameTIT3Test extends AbstractTestCase {
    @Test
    public void testID3Specific() {
        Exception e = null;
        try {
            ID3v23Tag tag = new ID3v23Tag();
            ID3v23Frame frame = new ID3v23Frame("TIT3");
            frame.setBody(new FrameBodyTPE1(TextEncoding.ISO_8859_1, "testsubtitle"));
            tag.addFrame(frame);
            assertEquals("testsubtitle", tag.getFirst("TIT3"));
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }

}