package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Issue183Test extends AbstractTestCase {
    @Test
    public void testReadCorruptOgg() {
        File orig = new File("testdata", "test508.ogg");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("test508.ogg");

        assertThatExceptionOfType(CannotReadException.class)
                .isThrownBy(() -> AudioFileIO.read(testFile))
                .withMessageContaining(""); // You can specify an expected message substring if needed
    }
}
