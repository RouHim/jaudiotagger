package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import static org.junit.jupiter.api.Assertions.assertNull;

public class Issue370Test extends AbstractTestCase {

    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testIssue() {
        Exception caught = null;
        try {
            //ToDO Fix Issue
            //File testFile = copyAudioToTmp("test96.m4a");
            //AudioFile af = AudioFileIO.read(testFile);
        } catch (Exception e) {
            caught = e;
        }
        assertNull(caught);
    }
}
