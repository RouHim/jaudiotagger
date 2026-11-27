package org.jaudiotagger.audio.aiff;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.jaudiotagger.AbstractBaseTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.junit.jupiter.api.Test;

public class AiffAudioFileTest extends AbstractBaseTestCase {

  @Test
  public void testReadAifcNotCompressed() {
    Exception exceptionCaught = null;


    File testFile = copyAudioToTmp("test119.aif");
    try {
      AudioFile f = AudioFileIO.read(testFile);
      AudioHeader ah = f.getAudioHeader();
      assertInstanceOf(AiffAudioHeader.class, ah);
      AiffAudioHeader aah = (AiffAudioHeader) ah;
      assertEquals("128", ah.getBitRate());
      assertEquals(128, ah.getBitRateAsNumber());
      assertEquals("2", ah.getChannels());
      assertEquals("8000", ah.getSampleRate());
      assertEquals(3, ah.getTrackLength());
      assertEquals("not compressed", ah.getEncodingType());
      assertFalse(ah.isVariableBitRate());
      assertEquals(2.936625d, ah.getPreciseTrackLength());
      assertNull(aah.getName());
      assertNull(aah.getCopyright());
      assertNull(aah.getAuthor());
      assertTrue(aah.getComments().isEmpty());
      assertEquals(1, aah.getAnnotations().size());
      //assertEquals("AFspdate: 2003-01-30 03:28:35 UTC\u0000user: kabal@CAPELLA\u0000program: CopyAudio ", aah.getAnnotations().get(0));
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  @Test
  public void testReadAiff2() {
    Exception exceptionCaught = null;


    File testFile = copyAudioToTmp("test120.aif");
    try {
      AudioFile f = AudioFileIO.read(testFile);
      AudioHeader ah = f.getAudioHeader();
      assertInstanceOf(AiffAudioHeader.class, ah);
      AiffAudioHeader aah = (AiffAudioHeader) ah;


      assertEquals("1411", ah.getBitRate());
      assertEquals(1411, ah.getBitRateAsNumber());
      assertFalse(ah.isVariableBitRate());
      assertEquals("2", ah.getChannels());
      assertEquals("44100", ah.getSampleRate());
      assertEquals(1, ah.getTrackLength());
      assertEquals("not compressed", ah.getEncodingType());
      assertEquals(0.84d, ah.getPreciseTrackLength());
      assertNull(aah.getName());
      assertNull(aah.getCopyright());
      assertNull(aah.getAuthor());
      assertTrue(aah.getComments().isEmpty());
      assertTrue(aah.getAnnotations().isEmpty());
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  @Test
  public void testReadAiff3() {
    Exception exceptionCaught = null;


    File testFile = copyAudioToTmp("test121.aif");
    try {
      AudioFile f = AudioFileIO.read(testFile);
      AudioHeader ah = f.getAudioHeader();
      assertInstanceOf(AiffAudioHeader.class, ah);
      AiffAudioHeader aah = (AiffAudioHeader) ah;


      assertEquals("1411", ah.getBitRate());
      assertEquals(1411, ah.getBitRateAsNumber());
      assertFalse(ah.isVariableBitRate());
      assertEquals("2", ah.getChannels());
      assertEquals("44100", ah.getSampleRate());
      assertEquals(5, ah.getTrackLength());
      assertEquals("not compressed", ah.getEncodingType());
      assertEquals(5.0, ah.getPreciseTrackLength());
      assertNull(aah.getName());
      assertNull(aah.getCopyright());
      assertNull(aah.getAuthor());
      assertTrue(aah.getComments().isEmpty());
      assertTrue(aah.getAnnotations().isEmpty());
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  @Test
  public void testReadAifcCompressedAlaw() {
    Exception exceptionCaught = null;


    File testFile = copyAudioToTmp("test132.aif");
    try {
      AudioFile f = AudioFileIO.read(testFile);
      AudioHeader ah = f.getAudioHeader();
      assertInstanceOf(AiffAudioHeader.class, ah);
      AiffAudioHeader aah = (AiffAudioHeader) ah;
      assertEquals("128", ah.getBitRate());
      assertEquals(128, ah.getBitRateAsNumber());
      assertEquals("2", ah.getChannels());
      assertEquals("8000", ah.getSampleRate());
      assertEquals(3, ah.getTrackLength());
      assertEquals("Alaw 2:1", ah.getEncodingType());
      assertTrue(ah.isVariableBitRate());
      assertEquals(2.936625d, ah.getPreciseTrackLength());
      assertNull(aah.getName());
      assertNull(aah.getCopyright());
      assertNull(aah.getAuthor());
      assertTrue(aah.getComments().isEmpty());
      assertEquals(1, aah.getAnnotations().size());
      //assertEquals("AFspdate: 2003-01-30 03:28:35 UTC\u0000user: kabal@CAPELLA\u0000program: CopyAudio ", aah.getAnnotations().get(0));
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  @Test
  public void testReadAifcCompressedUlaw() {
    Exception exceptionCaught = null;


    File testFile = copyAudioToTmp("test133.aif");
    try {
      AudioFile f = AudioFileIO.read(testFile);
      AudioHeader ah = f.getAudioHeader();
      assertInstanceOf(AiffAudioHeader.class, ah);
      AiffAudioHeader aah = (AiffAudioHeader) ah;
      assertEquals("128", ah.getBitRate());
      assertEquals(128, ah.getBitRateAsNumber());
      assertEquals("2", ah.getChannels());
      assertEquals("8000", ah.getSampleRate());
      assertEquals(3, ah.getTrackLength());
      assertEquals("µlaw 2:1", ah.getEncodingType());
      assertTrue(ah.isVariableBitRate());
      assertEquals(2.936625d, ah.getPreciseTrackLength());
      assertNull(aah.getName());
      assertNull(aah.getCopyright());
      assertNull(aah.getAuthor());
      assertTrue(aah.getComments().isEmpty());
      assertEquals(1, aah.getAnnotations().size());
      //assertEquals("AFspdate: 2003-01-30 03:28:35 UTC\u0000user: kabal@CAPELLA\u0000program: CopyAudio ", aah.getAnnotations().get(0));
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  @Test
  public void testReadAifcFloating64() {
    Exception exceptionCaught = null;


    File testFile = copyAudioToTmp("test134.aif");
    try {
      AudioFile f = AudioFileIO.read(testFile);
      AudioHeader ah = f.getAudioHeader();
      assertInstanceOf(AiffAudioHeader.class, ah);
      AiffAudioHeader aah = (AiffAudioHeader) ah;
      assertEquals("1024", ah.getBitRate());
      assertEquals(1024, ah.getBitRateAsNumber());
      assertEquals("2", ah.getChannels());
      assertEquals("8000", ah.getSampleRate());
      assertEquals(3, ah.getTrackLength());
      assertEquals("PCM 64-bit", ah.getEncodingType());
      assertTrue(ah.isVariableBitRate());
      assertEquals(2.936625d, ah.getPreciseTrackLength());
      assertNull(aah.getName());
      assertNull(aah.getCopyright());
      assertNull(aah.getAuthor());
      assertTrue(aah.getComments().isEmpty());
      assertEquals(1, aah.getAnnotations().size());
      //assertEquals("AFspdate: 2003-01-30 03:28:35 UTC\u0000user: kabal@CAPELLA\u0000program: CopyAudio ", aah.getAnnotations().get(0));
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  @Test
  public void testReadAifcSSNDBeforeCOMMChunk() {
    Exception exceptionCaught = null;


    File testFile = copyAudioToTmp("test135.aif");
    try {
      AudioFile f = AudioFileIO.read(testFile);
      AudioHeader ah = f.getAudioHeader();
      assertInstanceOf(AiffAudioHeader.class, ah);
      AiffAudioHeader aah = (AiffAudioHeader) ah;
      assertEquals("199", ah.getBitRate());
      assertEquals(199, ah.getBitRateAsNumber());
      assertEquals("1", ah.getChannels());
      assertEquals("8000", ah.getSampleRate());
      assertEquals(0, ah.getTrackLength());
      assertEquals("CCITT G.711 u-law", ah.getEncodingType());
      assertTrue(ah.isVariableBitRate());
      assertEquals(0.001125, ah.getPreciseTrackLength());
      assertNull(aah.getName());
      assertNull(aah.getCopyright());
      assertNull(aah.getAuthor());
      assertTrue(aah.getComments().isEmpty());
      assertEquals(0, aah.getAnnotations().size());
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  @Test
  public void testReadAifcWithOddChunk() {
    Exception exceptionCaught = null;


    File testFile = copyAudioToTmp("test136.aif");
    try {
      AudioFile f = AudioFileIO.read(testFile);
      AudioHeader ah = f.getAudioHeader();
      assertInstanceOf(AiffAudioHeader.class, ah);
      AiffAudioHeader aah = (AiffAudioHeader) ah;
      assertEquals("199", ah.getBitRate());
      assertEquals(199, ah.getBitRateAsNumber());
      assertEquals("1", ah.getChannels());
      assertEquals("8000", ah.getSampleRate());
      assertEquals(0, ah.getTrackLength());
      assertEquals("CCITT G.711 u-law", ah.getEncodingType());
      assertTrue(ah.isVariableBitRate());
      assertEquals(0.001125, ah.getPreciseTrackLength());
      assertNull(aah.getName());
      assertNull(aah.getCopyright());
      assertNull(aah.getAuthor());
      assertTrue(aah.getComments().isEmpty());
      assertEquals(0, aah.getAnnotations().size());
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  @Test
  public void testReadAifcWithJunk() {
    Exception exceptionCaught = null;


    File testFile = copyAudioToTmp("test137.aif");
    try {
      AudioFile f = AudioFileIO.read(testFile);
      AudioHeader ah = f.getAudioHeader();
      assertInstanceOf(AiffAudioHeader.class, ah);
      AiffAudioHeader aah = (AiffAudioHeader) ah;
      assertEquals("199", ah.getBitRate());
      assertEquals(199, ah.getBitRateAsNumber());
      assertEquals("1", ah.getChannels());
      assertEquals("8000", ah.getSampleRate());
      assertEquals(0, ah.getTrackLength());
      assertEquals("CCITT G.711 u-law", ah.getEncodingType());
      assertTrue(ah.isVariableBitRate());
      assertEquals(0.001125, ah.getPreciseTrackLength());
      assertNull(aah.getName());
      assertNull(aah.getCopyright());
      assertNull(aah.getAuthor());
      assertTrue(aah.getComments().isEmpty());
      assertEquals(0, aah.getAnnotations().size());
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  @Test
  public void testReadAiffUnknownCompressionAndNameChunkAndCopyrightChunks() {
    Exception exceptionCaught = null;


    File testFile = copyAudioToTmp("test138.aiff");
    try {
      AudioFile f = AudioFileIO.read(testFile);
      AudioHeader ah = f.getAudioHeader();
      assertInstanceOf(AiffAudioHeader.class, ah);
      AiffAudioHeader aah = (AiffAudioHeader) ah;
      assertEquals("19", ah.getBitRate());
      assertEquals(19, ah.getBitRateAsNumber());
      assertEquals("1", ah.getChannels());
      assertEquals("8000", ah.getSampleRate());
      assertEquals(0, ah.getTrackLength());
      assertEquals("CCITT G.728", ah.getEncodingType());
      assertTrue(ah.isVariableBitRate());
      assertEquals(0.03675, ah.getPreciseTrackLength());
      assertEquals("woodblock", aah.getName());
      assertEquals("Copyright 1991, Prosonus", aah.getCopyright());
      assertEquals("Prosonus", aah.getAuthor());
      assertTrue(aah.getComments().isEmpty());
      assertEquals(0, aah.getAnnotations().size());
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }
}
