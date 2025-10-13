package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.io.File;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class FrameTPOSTest extends AbstractTestCase {

    @Test
    public void testMergingMultipleFrames() throws Exception {
        ID3v24Tag tag = new ID3v24Tag();
        tag.setField(tag.createField(FieldKey.DISC_NO, "1"));
        tag.setField(tag.createField(FieldKey.DISC_TOTAL, "10"));
        assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
        assertEquals("10", tag.getFirst(FieldKey.DISC_TOTAL));
        assertInstanceOf(AbstractID3v2Frame.class, tag.getFrame("TPOS"));
    }

    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    public void testDiscNo() {
        Exception exceptionCaught = null;
        File orig = fileResource("testdata", "test82.mp3");
        try {
            AudioFile af = AudioFileIO.read(orig);
            Tag newTags = af.getTag();
            Iterator<TagField> i = newTags.getFields();
            while (i.hasNext()) {
                System.out.println(i.next().getId());
            }
            //Integer discNo = Integer.parseInt(newTags.get("Disc Number"));
            //tag.setField(FieldKey.DISC_NO,discNo.toString())
        } catch (Exception e) {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }
}
