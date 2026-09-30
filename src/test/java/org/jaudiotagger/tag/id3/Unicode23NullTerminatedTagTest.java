package org.jaudiotagger.tag.id3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;

import org.jaudiotagger.AbstractBaseTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.framebody.FrameBodyWXXX;
import org.jaudiotagger.tag.id3.framebody.FrameBodyWXXXTest;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Unicode23NullTerminatedTagTest extends AbstractBaseTestCase {

  /////////////////////////////////////////////////////////////////////////
  // TestCase classes to override
  /////////////////////////////////////////////////////////////////////////

  /**
   *
   */
  @BeforeEach
  protected void setUp() {
    TagOptionSingleton.getInstance().setToDefault();
  }

  /**
   *
   */
  protected void tearDown() {
  }

  /**
   *
   */
  //    protected void runTest()
  //    {
  //    }

  /////////////////////////////////////////////////////////////////////////
  // Tests
  /////////////////////////////////////////////////////////////////////////

  /**
   * Create a String that only contains text within IS8859 charset so should be
   * as ISO_88859
   *
   * @throws Exception
   */
  @Test
  public void testCreateISO8859EncodedNullTerminatedString() throws Exception {
    File testFile = copyAudioToTmp("testV1.mp3");
    MP3File mp3File = new MP3File(testFile);

    ID3v23Frame frame = new ID3v23Frame(
      ID3v23Frames.FRAME_ID_V3_USER_DEFINED_URL
    );
    Exception exceptionCaught = null;
    FrameBodyWXXX fb = null;
    try {
      fb = FrameBodyWXXXTest.getInitialisedBody();
      frame.setBody(fb);
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertNull(exceptionCaught);
    assertEquals(ID3v23Frames.FRAME_ID_V3_USER_DEFINED_URL, fb.getIdentifier());
    assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
    assertEquals(FrameBodyWXXXTest.WXXX_TEST_STRING, fb.getDescription());
    assertEquals(FrameBodyWXXXTest.WXXX_TEST_URL, fb.getUrlLink());

    //Create and Save
    ID3v23Tag tag = new ID3v23Tag();
    tag.setFrame(frame);
    mp3File.setID3v2Tag(tag);
    mp3File.save();

    //Reload, should be written as UTF16 because of the text
    mp3File = new MP3File(testFile);
    frame = (ID3v23Frame) mp3File
      .getID3v2Tag()
      .getFrame(ID3v23Frames.FRAME_ID_V3_USER_DEFINED_URL);
    FrameBodyWXXX body = (FrameBodyWXXX) frame.getBody();
    assertEquals(
      ID3v23Frames.FRAME_ID_V3_USER_DEFINED_URL,
      body.getIdentifier()
    );
    assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
    assertEquals(FrameBodyWXXXTest.WXXX_TEST_STRING, body.getDescription());
    assertEquals(FrameBodyWXXXTest.WXXX_TEST_URL, body.getUrlLink());
  }

  /**
   * Can explictly uses UTF-16 even if not required
   * as UTf16 by default
   *
   * @throws Exception
   */
  @Test
  public void testCreateUTF16BOMLEEncodedNullTerminatedString()
    throws Exception {
    File testFile = copyAudioToTmp("testV1.mp3");
    MP3File mp3File = new MP3File(testFile);

    ID3v23Frame frame = new ID3v23Frame(
      ID3v23Frames.FRAME_ID_V3_USER_DEFINED_URL
    );
    Exception exceptionCaught = null;
    FrameBodyWXXX fb = null;
    try {
      fb = FrameBodyWXXXTest.getInitialisedBody();
      fb.setTextEncoding(TextEncoding.UTF_16);
      frame.setBody(fb);
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertNull(exceptionCaught);
    assertEquals(ID3v23Frames.FRAME_ID_V3_USER_DEFINED_URL, fb.getIdentifier());
    assertEquals(TextEncoding.UTF_16, fb.getTextEncoding());
    assertEquals(FrameBodyWXXXTest.WXXX_TEST_STRING, fb.getDescription());

    //Create and Save
    ID3v23Tag tag = new ID3v23Tag();
    tag.setFrame(frame);
    mp3File.setID3v2Tag(tag);
    mp3File.save();

    //Reload, should be written as UTF16 because of the text
    mp3File = new MP3File(testFile);
    frame = (ID3v23Frame) mp3File
      .getID3v2Tag()
      .getFrame(ID3v23Frames.FRAME_ID_V3_USER_DEFINED_URL);
    FrameBodyWXXX body = (FrameBodyWXXX) frame.getBody();
    assertEquals(
      ID3v23Frames.FRAME_ID_V3_USER_DEFINED_URL,
      body.getIdentifier()
    );
    assertEquals(TextEncoding.UTF_16, body.getTextEncoding());
    assertEquals(FrameBodyWXXXTest.WXXX_TEST_STRING, body.getDescription());
    assertEquals(FrameBodyWXXXTest.WXXX_TEST_URL, body.getUrlLink());
  }

  /**
   * Can explictly uses UTF-16 even if not required
   * as UTf16 by default
   *
   * @throws Exception
   */
  @Test
  public void testCreateUTF16BOMBEEncodedNullTerminatedString()
    throws Exception {
    TagOptionSingleton.getInstance().setEncodeUTF16BomAsLittleEndian(false);
    File testFile = copyAudioToTmp("testV1.mp3");
    MP3File mp3File = new MP3File(testFile);

    ID3v23Frame frame = new ID3v23Frame(
      ID3v23Frames.FRAME_ID_V3_USER_DEFINED_URL
    );
    Exception exceptionCaught = null;
    FrameBodyWXXX fb = null;
    try {
      fb = FrameBodyWXXXTest.getInitialisedBody();
      fb.setTextEncoding(TextEncoding.UTF_16);
      frame.setBody(fb);
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertNull(exceptionCaught);
    assertEquals(ID3v23Frames.FRAME_ID_V3_USER_DEFINED_URL, fb.getIdentifier());
    assertEquals(TextEncoding.UTF_16, fb.getTextEncoding());
    assertEquals(FrameBodyWXXXTest.WXXX_TEST_STRING, fb.getDescription());

    //Create and Save
    ID3v23Tag tag = new ID3v23Tag();
    tag.setFrame(frame);
    mp3File.setID3v2Tag(tag);
    mp3File.save();

    //Reload, should be written as UTF16 because of the text
    mp3File = new MP3File(testFile);
    frame = (ID3v23Frame) mp3File
      .getID3v2Tag()
      .getFrame(ID3v23Frames.FRAME_ID_V3_USER_DEFINED_URL);
    FrameBodyWXXX body = (FrameBodyWXXX) frame.getBody();
    assertEquals(
      ID3v23Frames.FRAME_ID_V3_USER_DEFINED_URL,
      body.getIdentifier()
    );
    assertEquals(TextEncoding.UTF_16, body.getTextEncoding());
    assertEquals(FrameBodyWXXXTest.WXXX_TEST_STRING, body.getDescription());
    assertEquals(FrameBodyWXXXTest.WXXX_TEST_URL, body.getUrlLink());
  }

  /**
   * Create a String that contains text outside of the IS8859 charset should be written
   * as UTf16 by default
   *
   * @throws Exception
   */
  @Test
  public void testCreateUTF16AutoEncodedNullTerminatedString()
    throws Exception {
    File testFile = copyAudioToTmp("testV1.mp3");
    MP3File mp3File = new MP3File(testFile);

    ID3v23Frame frame = new ID3v23Frame(
      ID3v23Frames.FRAME_ID_V3_USER_DEFINED_URL
    );
    Exception exceptionCaught = null;
    FrameBodyWXXX fb = null;
    try {
      fb = FrameBodyWXXXTest.getUnicodeRequiredInitialisedBody();
      frame.setBody(fb);
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertNull(exceptionCaught);
    assertEquals(ID3v23Frames.FRAME_ID_V3_USER_DEFINED_URL, fb.getIdentifier());
    assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
    assertEquals(
      FrameBodyWXXXTest.WXXX_UNICODE_REQUIRED_TEST_STRING,
      fb.getDescription()
    );
    assertEquals(FrameBodyWXXXTest.WXXX_TEST_URL, fb.getUrlLink());

    //Create and Save
    ID3v23Tag tag = new ID3v23Tag();
    tag.setFrame(frame);
    mp3File.setID3v2Tag(tag);
    mp3File.save();

    //Reload, should be written as UTF16 because of the text
    mp3File = new MP3File(testFile);
    frame = (ID3v23Frame) mp3File
      .getID3v2Tag()
      .getFrame(ID3v23Frames.FRAME_ID_V3_USER_DEFINED_URL);
    FrameBodyWXXX body = (FrameBodyWXXX) frame.getBody();
    assertEquals(
      ID3v23Frames.FRAME_ID_V3_USER_DEFINED_URL,
      body.getIdentifier()
    );
    assertEquals(TextEncoding.UTF_16, body.getTextEncoding());
    assertEquals(
      FrameBodyWXXXTest.WXXX_UNICODE_REQUIRED_TEST_STRING,
      body.getDescription()
    );
    assertEquals(FrameBodyWXXXTest.WXXX_TEST_URL, body.getUrlLink());
  }

  /**
   * Strings can be written to UTF16BE if text encoding explicitly set
   *
   * @throws Exception
   */
  @Test
  public void testCreateUTF16BEEncodedNullTerminatedString() throws Exception {
    File testFile = copyAudioToTmp("testV1.mp3");
    MP3File mp3File = new MP3File(testFile);

    ID3v23Frame frame = new ID3v23Frame(
      ID3v23Frames.FRAME_ID_V3_USER_DEFINED_URL
    );
    Exception exceptionCaught = null;
    FrameBodyWXXX fb = null;
    try {
      fb = FrameBodyWXXXTest.getUnicodeRequiredInitialisedBody();
      fb.setTextEncoding(TextEncoding.UTF_16BE);
      frame.setBody(fb);
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertNull(exceptionCaught);
    assertEquals(ID3v23Frames.FRAME_ID_V3_USER_DEFINED_URL, fb.getIdentifier());
    assertEquals(TextEncoding.UTF_16BE, fb.getTextEncoding());
    assertEquals(
      FrameBodyWXXXTest.WXXX_UNICODE_REQUIRED_TEST_STRING,
      fb.getDescription()
    );
    assertEquals(FrameBodyWXXXTest.WXXX_TEST_URL, fb.getUrlLink());

    //Create and Save
    ID3v23Tag tag = new ID3v23Tag();
    tag.setFrame(frame);
    mp3File.setID3v2Tag(tag);
    mp3File.save();

    //Reload, should be written as UTF16BE
    mp3File = new MP3File(testFile);
    frame = (ID3v23Frame) mp3File
      .getID3v2Tag()
      .getFrame(ID3v23Frames.FRAME_ID_V3_USER_DEFINED_URL);
    FrameBodyWXXX body = (FrameBodyWXXX) frame.getBody();
    assertEquals(
      ID3v23Frames.FRAME_ID_V3_USER_DEFINED_URL,
      body.getIdentifier()
    );
    assertEquals(TextEncoding.UTF_16BE, body.getTextEncoding());
    assertEquals(
      FrameBodyWXXXTest.WXXX_UNICODE_REQUIRED_TEST_STRING,
      body.getDescription()
    );
    assertEquals(FrameBodyWXXXTest.WXXX_TEST_URL, body.getUrlLink());
  }

  /**
   * Strings can be written to UTF8 if text encoding explicitly set
   *
   * @throws Exception
   */
  @Test
  public void testCreateUTF8EncodedNullTerminatedString() throws Exception {
    File testFile = copyAudioToTmp("testV1.mp3");
    MP3File mp3File = new MP3File(testFile);

    ID3v23Frame frame = new ID3v23Frame(
      ID3v23Frames.FRAME_ID_V3_USER_DEFINED_URL
    );
    Exception exceptionCaught = null;
    FrameBodyWXXX fb = null;
    try {
      fb = FrameBodyWXXXTest.getUnicodeRequiredInitialisedBody();
      fb.setTextEncoding(TextEncoding.UTF_8);
      frame.setBody(fb);
    } catch (Exception e) {
      exceptionCaught = e;
    }

    assertNull(exceptionCaught);
    assertEquals(ID3v23Frames.FRAME_ID_V3_USER_DEFINED_URL, fb.getIdentifier());
    assertEquals(TextEncoding.UTF_8, fb.getTextEncoding());
    assertEquals(
      FrameBodyWXXXTest.WXXX_UNICODE_REQUIRED_TEST_STRING,
      fb.getDescription()
    );
    assertEquals(FrameBodyWXXXTest.WXXX_TEST_URL, fb.getUrlLink());

    //Create and Save
    ID3v23Tag tag = new ID3v23Tag();
    tag.setFrame(frame);
    mp3File.setID3v2Tag(tag);
    mp3File.save();

    //Reload, should be written as UTF8
    mp3File = new MP3File(testFile);
    frame = (ID3v23Frame) mp3File
      .getID3v2Tag()
      .getFrame(ID3v23Frames.FRAME_ID_V3_USER_DEFINED_URL);
    FrameBodyWXXX body = (FrameBodyWXXX) frame.getBody();
    assertEquals(
      ID3v23Frames.FRAME_ID_V3_USER_DEFINED_URL,
      body.getIdentifier()
    );
    assertEquals(TextEncoding.UTF_8, body.getTextEncoding());
    assertEquals(
      FrameBodyWXXXTest.WXXX_UNICODE_REQUIRED_TEST_STRING,
      body.getDescription()
    );
    assertEquals(FrameBodyWXXXTest.WXXX_TEST_URL, body.getUrlLink());
  }
}
