package org.jaudiotagger.tag.dsf;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import org.jaudiotagger.FilePermissionsTest;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.junit.jupiter.api.Test;

public class DsfAudioFileTest extends FilePermissionsTest {

  @Test
  public void testReadDsfMetadata() {
    Exception exceptionCaught = null;


    File testFile = copyAudioToTmp(
      "test122.dsf",
      "test122readmetadata.dsf"
    );
    try {
      AudioFile f = AudioFileIO.read(testFile);
      System.out.println(f.getTag());
      assertInstanceOf(ID3v24Tag.class, f.getTag());
      assertEquals("test3", f.getTag().getFirst(FieldKey.TITLE));
      assertEquals("Artist", f.getTag().getFirst(FieldKey.ARTIST));
      assertEquals("Album Artist", f.getTag().getFirst(FieldKey.ALBUM_ARTIST));
      assertEquals("Album", f.getTag().getFirst(FieldKey.ALBUM));
      //assertEquals("Crossover", f.getTag().getFirst(FieldKey.GENRE));
      assertEquals("comments", f.getTag().getFirst(FieldKey.COMMENT));
      //assertEquals("Publisher ", f.getTag().getFirst(FieldKey.RECORD_LABEL));
      //assertEquals("Composer ", f.getTag().getFirst(FieldKey.COMPOSER));
      //assertEquals("1971", f.getTag().getFirst(FieldKey.YEAR));
    } catch (Exception e) {
      exceptionCaught = e;
    }
    assertNull(exceptionCaught);
  }

  @Test
  public void testWriteWriteProtectedFileWithCheckDisabled() throws Exception {
    runWriteWriteProtectedFileWithCheckDisabled(
      "test122.dsf"
    );
  }

  @Test
  public void testWriteWriteProtectedFileWithCheckEnabled() throws Exception {
    runWriteWriteProtectedFileWithCheckEnabled(
      "test122.dsf"
    );
  }

  @Test
  public void testWriteReadOnlyFileWithCheckDisabled() throws Exception {
    runWriteReadOnlyFileWithCheckDisabled("test122.dsf");
  }
}
