package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue452Test extends AbstractTestCase {

    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testFindAudioHeaderWhenTagSizeIsTooShortAndHasNullPadding()
            throws Exception {
        Exception ex = null;

        File testFile = copyAudioToTmp(
                "test110.mp3",
                "testFindAudioHeaderWhenTagSizeIsTooShortAndHasNullPadding.mp3"
        );
        MP3File mp3File = new MP3File(testFile);
        System.out.println("AudioHeaderBefore" + mp3File.getMP3AudioHeader());
        System.out.println(
                "AlbumField:" + mp3File.getTag().getFirst(FieldKey.ALBUM)
        );
        assertEquals(0x54adf, mp3File.getMP3AudioHeader().getMp3StartByte());

        mp3File.getTag().setField(FieldKey.ALBUM, "newalbum");
        mp3File.commit();
        mp3File = new MP3File(testFile);
        assertEquals(0x54adf, mp3File.getMP3AudioHeader().getMp3StartByte());
        System.out.println("AudioHeaderAfter" + mp3File.getMP3AudioHeader());
        System.out.println(
                "AlbumField" + mp3File.getTag().getFirst(FieldKey.ALBUM)
        );
    }
}
