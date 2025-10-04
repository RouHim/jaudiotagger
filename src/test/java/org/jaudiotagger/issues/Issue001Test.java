package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.ID3v23Frame;
import org.jaudiotagger.tag.id3.ID3v23Frames;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyIPLS;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTXXX;
import org.junit.jupiter.api.Test;

public class Issue001Test extends AbstractTestCase {

  @Test
  public void testHandlingOfUnmappedChars() {
    Exception ex = null;
    try {
      File testFile = AbstractTestCase.copyAudioToTmp(
        "testV1.mp3",
        new File("test1001.mp3")
      );
      MP3File mp3File = new MP3File(testFile);

      //Create and Save
      ID3v23Tag tag = new ID3v23Tag();
      FrameBodyTXXX frameBody = new FrameBodyTXXX();
      frameBody.setDescription(FrameBodyTXXX.MOOD);
      frameBody.setText("\uDFFF");
      ID3v23Frame frame = new ID3v23Frame(
        ID3v23Frames.FRAME_ID_V3_USER_DEFINED_INFO
      );
      frame.setBody(frameBody);
      tag.setFrame(frame);
      mp3File.setID3v2Tag(tag);
      mp3File.save();
    } catch (Exception e) {
      e.printStackTrace();
      ex = e;
    }
    assertNull(ex);
  }

  @Test
  public void testHandlingOfUnmappedChars2() {
    Exception ex = null;
    try {
      File testFile = AbstractTestCase.copyAudioToTmp(
        "testV1.mp3",
        new File("test1001.mp3")
      );
      MP3File mp3File = new MP3File(testFile);

      //Create and Save
      ID3v23Tag tag = new ID3v23Tag();
      FrameBodyIPLS frameBody = new FrameBodyIPLS();
      frameBody.setText("producer\0eno,lanois\0engineer\0" + "\uDFFF");
      ID3v23Frame frame = new ID3v23Frame(
        ID3v23Frames.FRAME_ID_V3_INVOLVED_PEOPLE
      );
      frame.setBody(frameBody);
      tag.setFrame(frame);
      mp3File.setID3v2Tag(tag);
      mp3File.save();
    } catch (Exception e) {
      e.printStackTrace();
      ex = e;
    }
    assertNull(ex);
  }
}
