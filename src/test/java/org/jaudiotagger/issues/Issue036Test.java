package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class Issue036Test extends AbstractTestCase {
    @Test
    public void testIDv24Frame() {
        ID3v24Frame frame1 = new ID3v24Frame();
        ID3v24Frame frame2 = new ID3v24Frame();
        ID3v24Frame frame3 = new ID3v24Frame("TPE1");
        ID3v24Frame frame4 = new ID3v24Frame("TPE1");
        ID3v24Frame frame5 = new ID3v24Frame("TPE1");
        frame5.getBody().setTextEncoding((byte) 1);

        assertEquals(frame1, frame1);
        assertEquals(frame1, frame2);
        assertNotEquals(frame1, frame3);

        assertEquals(frame3, frame3);
        assertEquals(frame3, frame4);
        assertNotEquals(frame3, frame5);
    }

    @Test
    public void testAllID3v24Frames() {
        for (String frameId : ID3v24Frames.getInstanceOf().getSupportedFrames()) {
            //System.out.println("Testing:"+frameId);
            ID3v24Frame frame1 = new ID3v24Frame(frameId);
            ID3v24Frame frame2 = new ID3v24Frame(frameId);
            assertEquals(frame1, frame2);
        }
    }

    @Test
    public void testIDv24Tag() throws Exception {
        ID3v24Tag tag1 = new ID3v24Tag();
        ID3v24Tag tag2 = new ID3v24Tag();
        ID3v24Tag tag3 = new ID3v24Tag();
        ID3v24Tag tag4 = new ID3v24Tag();
        ID3v24Tag tag5 = new ID3v24Tag();
        tag3.addField(FieldKey.ALBUM, "Porcupine");
        tag4.addField(FieldKey.ALBUM, "Porcupine");
        tag5.addField(FieldKey.ALBUM, "Porcupine");
        tag5.addField(FieldKey.ARTIST, "Echo & the Bunnymen");

        assertEquals(tag1, tag1);
        assertEquals(tag1, tag2);
        assertNotEquals(tag1, tag3);

        assertEquals(tag3, tag3);
        assertEquals(tag3, tag4);
        assertNotEquals(tag3, tag5);
    }


    @Test
    public void testIDv23Frame() {
        ID3v23Frame frame1 = new ID3v23Frame();
        ID3v23Frame frame2 = new ID3v23Frame();
        ID3v23Frame frame3 = new ID3v23Frame("TPE1");
        ID3v23Frame frame4 = new ID3v23Frame("TPE1");
        ID3v23Frame frame5 = new ID3v23Frame("TPE1");
        frame5.getBody().setTextEncoding((byte) 1);

        assertEquals(frame1, frame1);
        assertEquals(frame1, frame2);
        assertNotEquals(frame1, frame3);

        assertEquals(frame3, frame3);
        assertEquals(frame3, frame4);
        assertNotEquals(frame3, frame5);
    }

    @Test
    public void testAllID3v23Frames() {
        for (String frameId : ID3v23Frames.getInstanceOf().getSupportedFrames()) {
            //System.out.println("Testing:"+frameId);
            ID3v23Frame frame1 = new ID3v23Frame(frameId);
            ID3v23Frame frame2 = new ID3v23Frame(frameId);
            assertEquals(frame1, frame2);
        }
    }

    @Test
    public void testIDv23Tag() throws Exception {
        ID3v23Tag tag1 = new ID3v23Tag();
        ID3v23Tag tag2 = new ID3v23Tag();
        ID3v23Tag tag3 = new ID3v23Tag();
        ID3v23Tag tag4 = new ID3v23Tag();
        ID3v23Tag tag5 = new ID3v23Tag();
        tag3.addField(FieldKey.ALBUM, "Porcupine");
        tag4.addField(FieldKey.ALBUM, "Porcupine");
        tag5.addField(FieldKey.ALBUM, "Porcupine");
        tag5.addField(FieldKey.ARTIST, "Echo & the Bunnymen");

        assertEquals(tag1, tag1);
        assertEquals(tag1, tag2);
        assertNotEquals(tag1, tag3);

        assertEquals(tag3, tag3);
        assertEquals(tag3, tag4);
        assertNotEquals(tag3, tag5);
    }

    @Test
    public void testIDv22Frame() {
        ID3v22Frame frame1 = new ID3v22Frame();
        ID3v22Frame frame2 = new ID3v22Frame();
        ID3v22Frame frame3 = new ID3v22Frame("TP1");
        ID3v22Frame frame4 = new ID3v22Frame("TP1");
        ID3v22Frame frame5 = new ID3v22Frame("TP1");
        frame5.getBody().setTextEncoding((byte) 1);

        assertEquals(frame1, frame1);
        assertEquals(frame1, frame2);
        assertNotEquals(frame1, frame3);

        assertEquals(frame3, frame3);
        assertEquals(frame3, frame4);
        assertNotEquals(frame3, frame5);
    }

    @Test
    public void testAllID3v22Frames() {
        for (String frameId : ID3v22Frames.getInstanceOf().getSupportedFrames()) {
            ID3v22Frame frame1 = new ID3v22Frame(frameId);
            ID3v22Frame frame2 = new ID3v22Frame(frameId);
            assertEquals(frame1, frame2);
        }
    }

    @Test
    public void testIDv22Tag() throws Exception {
        ID3v22Tag tag1 = new ID3v22Tag();
        ID3v22Tag tag2 = new ID3v22Tag();
        ID3v22Tag tag3 = new ID3v22Tag();
        ID3v22Tag tag4 = new ID3v22Tag();
        ID3v22Tag tag5 = new ID3v22Tag();
        tag3.addField(FieldKey.ALBUM, "Porcupine");
        tag4.addField(FieldKey.ALBUM, "Porcupine");
        tag5.addField(FieldKey.ALBUM, "Porcupine");
        tag5.addField(FieldKey.ARTIST, "Echo & the Bunnymen");

        assertEquals(tag1, tag1);
        assertEquals(tag1, tag2);
        assertNotEquals(tag1, tag3);

        assertEquals(tag3, tag3);
        assertEquals(tag3, tag4);
        assertNotEquals(tag3, tag5);
    }
}