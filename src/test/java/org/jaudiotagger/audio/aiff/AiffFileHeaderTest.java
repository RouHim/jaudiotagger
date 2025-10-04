package org.jaudiotagger.audio.aiff;

import static org.jaudiotagger.audio.aiff.AiffType.AIFC;
import static org.jaudiotagger.audio.aiff.AiffType.AIFF;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.junit.jupiter.api.Test;

public class AiffFileHeaderTest {

  @Test
  public void testValidAIFF() throws IOException, CannotReadException {
    final int size = 1234;
    final File aiffFile = createAIFF("FORM", "AIFF", size);

    try (FileChannel fc = new RandomAccessFile(aiffFile, "rw").getChannel()) {
      final AiffFileHeader header = new AiffFileHeader();
      final AiffAudioHeader aiffAudioHeader = new AiffAudioHeader();
      final long remainingBytes = header.readHeader(
        fc,
        aiffAudioHeader,
        aiffFile.toString()
      );
      assertEquals(size - 4, remainingBytes);
      assertEquals(AIFF, aiffAudioHeader.getFileType());
    }

    aiffFile.delete();
  }

  @Test
  public void testInvalidFormatType() throws IOException {
    final int size = 5762;
    final File aiffFile = createAIFF("FORM", "COOL", size);

    try (FileChannel fc = new RandomAccessFile(aiffFile, "rw").getChannel()) {
      new AiffFileHeader().readHeader(
        fc,
        new AiffAudioHeader(),
        aiffFile.toString()
      );
      fail("Expected " + CannotReadException.class.getSimpleName());
    } catch (CannotReadException e) {
      // expected this
    }

    aiffFile.delete();
  }

  @Test
  public void testInvalidFormat1() throws IOException {
    final int size = 34242;
    final File aiffFile = createAIFF("FURM", "AIFF", size);

    try (FileChannel fc = new RandomAccessFile(aiffFile, "rw").getChannel()) {
      new AiffFileHeader().readHeader(
        fc,
        new AiffAudioHeader(),
        aiffFile.toString()
      );
      fail("Expected " + CannotReadException.class.getSimpleName());
    } catch (CannotReadException e) {
      // expected this
    }

    aiffFile.delete();
  }

  @Test
  public void testInvalidFormat2() throws IOException {
    final int size = 34234;
    final File aiffFile = createAIFF("FORMA", "AIFF", size);

    try (FileChannel fc = new RandomAccessFile(aiffFile, "rw").getChannel()) {
      new AiffFileHeader().readHeader(
        fc,
        new AiffAudioHeader(),
        aiffFile.toString()
      );
      fail("Expected " + CannotReadException.class.getSimpleName());
    } catch (CannotReadException e) {
      // expected this
    }

    aiffFile.delete();
  }

  @Test
  public void testValidAIFC() throws IOException, CannotReadException {
    final int size = 3452;
    final File aiffFile = createAIFF("FORM", "AIFC", size);

    try (FileChannel fc = new RandomAccessFile(aiffFile, "rw").getChannel()) {
      final AiffFileHeader header = new AiffFileHeader();
      final AiffAudioHeader aiffAudioHeader = new AiffAudioHeader();
      final long remainingBytes = header.readHeader(
        fc,
        aiffAudioHeader,
        aiffFile.toString()
      );
      assertEquals(size - 4, remainingBytes);
      assertEquals(AIFC, aiffAudioHeader.getFileType());
    }

    aiffFile.delete();
  }

  private static File createAIFF(
    final String form,
    final String formType,
    final int size
  ) throws IOException {
    final File tempFile = File.createTempFile(
      AiffFileHeaderTest.class.getSimpleName(),
      ".aif"
    );
    tempFile.deleteOnExit();

    try (
      final DataOutputStream out = new DataOutputStream(
        new FileOutputStream(tempFile)
      )
    ) {
      // write format
      out.write(form.getBytes(StandardCharsets.US_ASCII));
      // write size
      out.writeInt(size);
      // write format type
      out.write(formType.getBytes(StandardCharsets.US_ASCII));
      // write remaining random data
      final byte[] remainingData = new byte[size - formType.length()];
      final Random random = new Random();
      random.nextBytes(remainingData);
      out.write(remainingData);
    }
    return tempFile;
  }
}
