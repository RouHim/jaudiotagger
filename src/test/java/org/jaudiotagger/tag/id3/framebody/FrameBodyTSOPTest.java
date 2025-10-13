package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FrameBodyTSOPTest extends AbstractTestCase {

    public static final String ARTIST_SORT = "artistsort";

    public static FrameBodyTSOP getInitialisedBody() {
        FrameBodyTSOP fb = new FrameBodyTSOP();
        fb.setText(FrameBodyTSOPTest.ARTIST_SORT);
        return fb;
    }

    @Test
    public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyTSOP fb = null;
        try {
            fb = new FrameBodyTSOP(
                    TextEncoding.ISO_8859_1,
                    FrameBodyTSOPTest.ARTIST_SORT
            );
        } catch (Exception e) {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_ARTIST_SORT_ORDER, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTSOPTest.ARTIST_SORT, fb.getText());
    }

    @Test
    public void testCreateFrameBodyEmptyConstructor() {
        Exception exceptionCaught = null;
        FrameBodyTSOP fb = null;
        try {
            fb = new FrameBodyTSOP();
            fb.setText(FrameBodyTSOPTest.ARTIST_SORT);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_ARTIST_SORT_ORDER, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTSOPTest.ARTIST_SORT, fb.getText());
    }
}
