package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTPE3;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FrameTPUBTest extends AbstractTestCase {

    @Test
    public void testGenericv22() {
        Exception e = null;
        try {
            Tag tag = new ID3v22Tag();
            tag.addField(FieldKey.RECORD_LABEL, "testrecordlabel");
            assertEquals("testrecordlabel", tag.getFirst(FieldKey.RECORD_LABEL));
            assertEquals("testrecordlabel", tag.getFirst("TPB"));
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }

    @Test
    public void testID3Specificv22() {
        Exception e = null;
        try {
            ID3v22Tag tag = new ID3v22Tag();
            ID3v22Frame frame = new ID3v22Frame("TPB");
            frame.setBody(
                    new FrameBodyTPE3(TextEncoding.ISO_8859_1, "testrecordlabel")
            );
            tag.addFrame(frame);
            assertEquals("testrecordlabel", tag.getFirst(FieldKey.RECORD_LABEL));
            assertEquals("testrecordlabel", tag.getFirst("TPB"));
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }

    @Test
    public void testGenericv23() {
        Exception e = null;
        try {
            Tag tag = new ID3v23Tag();
            tag.addField(FieldKey.RECORD_LABEL, "testrecordlabel");
            assertEquals("testrecordlabel", tag.getFirst(FieldKey.RECORD_LABEL));
            assertEquals("testrecordlabel", tag.getFirst("TPUB"));
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }

    @Test
    public void testID3Specificv23() {
        Exception e = null;
        try {
            ID3v23Tag tag = new ID3v23Tag();
            ID3v23Frame frame = new ID3v23Frame("TPUB");
            frame.setBody(
                    new FrameBodyTPE3(TextEncoding.ISO_8859_1, "testrecordlabel")
            );
            tag.addFrame(frame);
            assertEquals("testrecordlabel", tag.getFirst(FieldKey.RECORD_LABEL));
            assertEquals("testrecordlabel", tag.getFirst("TPUB"));
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }

    @Test
    public void testGenericv24() {
        Exception e = null;
        try {
            Tag tag = new ID3v24Tag();
            tag.addField(FieldKey.RECORD_LABEL, "testrecordlabel");
            assertEquals("testrecordlabel", tag.getFirst(FieldKey.RECORD_LABEL));
            assertEquals("testrecordlabel", tag.getFirst("TPUB"));
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }

    @Test
    public void testID3Specificv24() {
        Exception e = null;
        try {
            ID3v24Tag tag = new ID3v24Tag();
            ID3v24Frame frame = new ID3v24Frame("TPUB");
            frame.setBody(
                    new FrameBodyTPE3(TextEncoding.ISO_8859_1, "testrecordlabel")
            );
            tag.addFrame(frame);
            assertEquals("testrecordlabel", tag.getFirst(FieldKey.RECORD_LABEL));
            assertEquals("testrecordlabel", tag.getFirst("TPUB"));
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }
}
