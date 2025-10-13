package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Issue184Test extends AbstractTestCase {

    @Test
    public void testReadCorruptWma() {

        Exception ex = null;
        try {
            File testFile = copyAudioToTmp("test509.wma");
            AudioFileIO.read(testFile);
        } catch (Exception e) {
            assertInstanceOf(CannotReadException.class, e);
            ex = e;
        }
        assertNotNull(ex);
    }
}
