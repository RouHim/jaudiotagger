package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNull;

public class Issue308Test extends AbstractTestCase {

    public static int countExceptions = 0;

    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testAddingLargeImageToOgg() {

        Exception e = null;
        try {
            final File testFile = copyAudioToTmp("test72.ogg");
            AudioFile af = AudioFileIO.read(testFile);
            Artwork artwork = ArtworkFactory.getNew();
            artwork.setFromFile(fileResource("testdata", "coverart_large.jpg"));

            af.getTag().setField(artwork);
            af.commit();

            //Reread
            System.out.println("Read Audio");
            af = AudioFileIO.read(testFile);
            System.out.println("Rewrite Audio");
            af.commit();

            //Resave
            af.getTag().addField(FieldKey.TITLE, "TESTdddddddddddddddddddddddd");
            af.commit();
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }
}
