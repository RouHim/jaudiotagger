package org.jaudiotagger.tag.id3;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.util.List;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.framebody.FrameBodyDeprecated;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTIME;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTYER;
import org.junit.jupiter.api.Test;

public class DeprecatedFrameTest extends AbstractTestCase {

  @Test
  public void testv24TagWithDeprecatedFrameShouldCreateAsDeprecated()
    throws Exception {
    File testFile = prependAudioToTmp(
      "Issue88.id3",
      "testV1.mp3"
    );

    MP3File mp3File = new MP3File(testFile);

    ID3v24Frame v24frame = (ID3v24Frame) mp3File
      .getID3v2Tag()
      .getFrame(ID3v23Frames.FRAME_ID_V3_TYER);
    assertNotNull(v24frame);
    assertInstanceOf(FrameBodyDeprecated.class, v24frame.getBody());
  }

  @Test
  public void testConvertTagWithDeprecatedFrameToTagWhereFrameShouldNoLongerBeDeprecated()
    throws Exception {
    File testFile = prependAudioToTmp(
      "Issue88.id3",
      "testV1.mp3"
    );

    MP3File mp3File = new MP3File(testFile);

    ID3v23Tag v23Tag = new ID3v23Tag(mp3File.getID3v2Tag());
    ID3v23Frame v23frame = (ID3v23Frame) ((List) v23Tag.getFrame(
      ID3v23Frames.FRAME_ID_V3_TYER
    )).get(0);
    assertInstanceOf(FrameBodyTYER.class, v23frame.getBody());
    v23frame = (ID3v23Frame) ((List) v23Tag.getFrame(
      ID3v23Frames.FRAME_ID_V3_TYER
    )).get(1);
    assertInstanceOf(FrameBodyTYER.class, v23frame.getBody());

    mp3File.setID3v2Tag(v23Tag);
    mp3File.save();

    mp3File = new MP3File(testFile);
    v23Tag = (ID3v23Tag) mp3File.getID3v2Tag();
    v23frame = (ID3v23Frame) v23Tag.getFrame(ID3v23Frames.FRAME_ID_V3_TYER);
    assertInstanceOf(FrameBodyTYER.class, v23frame.getBody());
  }

  @Test
  public void testSavingV24DeprecatedTIMETagToV23() throws Exception {
    File testFile = prependAudioToTmp(
      "Issue122-1.id3",
      "testV1.mp3"
    );
    MP3File mp3File = new MP3File(testFile);
    ID3v24Tag v24Tag = (ID3v24Tag) mp3File.getID3v2Tag();
    ID3v24Frame v24frame = (ID3v24Frame) v24Tag.getFrame(
      ID3v23Frames.FRAME_ID_V3_TIME
    );
    assertNotNull(v24frame);
    assertInstanceOf(FrameBodyDeprecated.class, v24frame.getBody());

    //Save as V23
    ID3v23Tag v23Tag = new ID3v23Tag(v24Tag);
    mp3File.setID3v2Tag(v23Tag);
    mp3File.save();

    mp3File = new MP3File(testFile);
    v23Tag = (ID3v23Tag) mp3File.getID3v2Tag();
    ID3v23Frame v23frame = (ID3v23Frame) v23Tag.getFrame(
      ID3v23Frames.FRAME_ID_V3_TIME
    );
    assertInstanceOf(FrameBodyTIME.class, v23frame.getBody());
  }

  @Test
  public void testSavingV24DeprecatedEmptyTDATTagToV23() throws Exception {
    File testFile = prependAudioToTmp(
      "Issue122-2.id3",
      "testV1.mp3"
    );
    MP3File mp3File = new MP3File(testFile);
    ID3v24Tag v24Tag = (ID3v24Tag) mp3File.getID3v2Tag();
    ID3v24Frame v24frame = (ID3v24Frame) v24Tag.getFrame(
      ID3v23Frames.FRAME_ID_V3_TDAT
    );
    assertNotNull(v24frame);
    assertInstanceOf(FrameBodyDeprecated.class, v24frame.getBody());

    //Save as V23
    ID3v23Tag v23Tag = new ID3v23Tag(v24Tag);
    mp3File.setID3v2Tag(v23Tag);
    mp3File.save();

    mp3File = new MP3File(testFile);
    v23Tag = (ID3v23Tag) mp3File.getID3v2Tag();
    Object v23frame = v23Tag.getFrame(ID3v23Frames.FRAME_ID_V3_TYER);
    assertNotNull(v23frame);
    assertInstanceOf(FrameBodyTYER.class, ((AbstractID3v2Frame) v23frame).getBody());
  }
}
