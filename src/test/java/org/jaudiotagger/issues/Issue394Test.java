package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.AbstractTag;
import org.jaudiotagger.tag.id3.ID3v1Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class Issue394Test extends AbstractTestCase {

    @Test
    public void testCreatingID3v1TagfromID3v2tagWithMultipleComments() {
        Exception caught = null;
        try {
            Tag tag = new ID3v24Tag();
            tag.setField(FieldKey.COMMENT, "COMMENT1");
            tag.addField(FieldKey.COMMENT, "COMMENT2");

            Tag v1Tag = new ID3v1Tag((AbstractTag) tag);
        } catch (Exception e) {
            caught = e;
        }
        assertNull(caught);
    }
}
