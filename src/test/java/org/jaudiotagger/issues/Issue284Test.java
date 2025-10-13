package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.ID3v22Frame;
import org.jaudiotagger.tag.id3.ID3v22Frames;
import org.jaudiotagger.tag.id3.ID3v23Frame;
import org.jaudiotagger.tag.id3.ID3v23Frames;
import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.framebody.FrameBodyUnsupported;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Issue284Test extends AbstractTestCase {

    @Test
    public void testConvertv23v24() {

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = copyAudioToTmp("testV1.mp3");
            MP3File af = (MP3File) AudioFileIO.read(testFile);

            ID3v24Frame frame = new ID3v24Frame(
                    ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE
            );
            FrameBodyUnsupported fb = new FrameBodyUnsupported(
                    ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE
            );
            frame.setBody(fb);
            assertInstanceOf(FrameBodyUnsupported.class, frame.getBody());
            ID3v23Frame framev23 = new ID3v23Frame(frame);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    @Test
    public void testConvertv22v24() {

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = copyAudioToTmp("testV1.mp3");
            MP3File af = (MP3File) AudioFileIO.read(testFile);

            ID3v24Frame frame = new ID3v24Frame(
                    ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE
            );
            FrameBodyUnsupported fb = new FrameBodyUnsupported(
                    ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE
            );
            frame.setBody(fb);
            assertInstanceOf(FrameBodyUnsupported.class, frame.getBody());
            ID3v22Frame framev22 = new ID3v22Frame(frame);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    @Test
    public void testConvertv24v23() {

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = copyAudioToTmp("testV1.mp3");
            MP3File af = (MP3File) AudioFileIO.read(testFile);

            ID3v23Frame frame = new ID3v23Frame(
                    ID3v23Frames.FRAME_ID_V3_INVOLVED_PEOPLE
            );
            FrameBodyUnsupported fb = new FrameBodyUnsupported(
                    ID3v23Frames.FRAME_ID_V3_INVOLVED_PEOPLE
            );
            frame.setBody(fb);
            assertInstanceOf(FrameBodyUnsupported.class, frame.getBody());
            ID3v24Frame framev24 = new ID3v24Frame(frame);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    @Test
    public void testConvertv24v22() {

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = copyAudioToTmp("testV1.mp3");
            MP3File af = (MP3File) AudioFileIO.read(testFile);

            ID3v22Frame frame = new ID3v22Frame(ID3v22Frames.FRAME_ID_V2_TITLE);
            FrameBodyUnsupported fb = new FrameBodyUnsupported(
                    ID3v22Frames.FRAME_ID_V2_TITLE
            );
            frame.setBody(fb);
            assertInstanceOf(FrameBodyUnsupported.class, frame.getBody());
            ID3v24Frame framev24 = new ID3v24Frame(frame);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    @Test
    public void testConvertv22v23() {

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = copyAudioToTmp("testV1.mp3");
            MP3File af = (MP3File) AudioFileIO.read(testFile);

            ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_TITLE);
            FrameBodyUnsupported fb = new FrameBodyUnsupported(
                    ID3v23Frames.FRAME_ID_V3_TITLE
            );
            frame.setBody(fb);
            assertInstanceOf(FrameBodyUnsupported.class, frame.getBody());
            ID3v22Frame framev22 = new ID3v22Frame(frame);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }
}
