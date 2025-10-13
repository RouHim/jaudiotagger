package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.images.ArtworkFactory;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class Issue282Test extends AbstractTestCase {

    @Test
    public void testWriteToRelativeWmaFile() {

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = copyAudioToTmp("test1.wma");

            //Copy up a level coz we need it to be in same folder as working directory so can just specify filename
            File outputFile = tempFileResource("target_" + testFile.getName());
            boolean result = copy(testFile, outputFile);
            assertTrue(result);

            //make Relative
            assertTrue(outputFile.exists());
            //Read File okay
            AudioFile af = AudioFileIO.read(outputFile);
            System.out.println(af.getTag().toString());

            //Change File
            af
                    .getTag()
                    .setField(
                            ArtworkFactory.createArtworkFromFile(
                                    fileResource("testdata", "coverart.jpg")
                            )
                    );

            af.commit();
            outputFile.delete();
        } catch (Exception e) {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    @Test
    public void testCopy() throws Exception {
        String fileName = "testV1.mp3";
        final File sourceFile = new File(ClassLoader.getSystemResource("testdata/" + fileName).toURI());
        final File destFile = copyAudioToTmp(fileName);
        assertTrue(destFile.exists());
        assertEquals(sourceFile.length(), destFile.length());
    }

    @Test
    public void testWriteToRelativeMp3File() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = copyAudioToTmp("testV1.mp3");

            //Copy up a level coz we need it to be in same folder as working directory so can just specify filename
            File outputFile = tempFileResource("target_" + testFile.getName());
            boolean result = copy(testFile, outputFile);
            assertTrue(result);

            //make Relative
            assertTrue(outputFile.exists());
            //Read File okay
            AudioFile af = AudioFileIO.read(outputFile);

            //Create tag and Change File
            af.getTagOrCreateAndSetDefault();
            af
                    .getTag()
                    .setField(
                            ArtworkFactory.createArtworkFromFile(
                                    fileResource("testdata", "coverart.jpg")
                            )
                    );
            af.commit();
        } catch (Exception e) {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }
}
