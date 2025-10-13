package org.jaudiotagger.audio.asf.tag;

import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.asf.AsfTag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertNull;

public class AsfKeyMappingTest {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * This method tests whether each {@link org.jaudiotagger.tag.FieldKey} is mapped
     * to an {@link org.jaudiotagger.tag.asf.AsfFieldKey}.<br>
     */
    @Test
    public void testTagFieldKeyMappingComplete() {
        Exception exceptionCaught = null;
        Tag tag = new AsfTag();
        try {
            for (FieldKey curr : FieldKey.values()) {
                if (curr != FieldKey.ITUNES_GROUPING) {
                    tag.getFields(curr);
                }
            }
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }
}
