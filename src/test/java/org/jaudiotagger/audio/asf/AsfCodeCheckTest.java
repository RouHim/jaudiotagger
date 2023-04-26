package org.jaudiotagger.audio.asf;


import org.jaudiotagger.audio.asf.data.AsfHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.asf.AsfFieldKey;
import org.jaudiotagger.tag.asf.AsfTag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AsfCodeCheckTest {

    /**
     * Tests the correct implementation of {@link AsfTag}.<br>
     * For example if {@link AsfTag#createAlbumField(String)} returns a field whose {@link org.jaudiotagger.tag.TagField#getId()}
     * equals {@link org.jaudiotagger.tag.asf.AsfFieldKey#ALBUM}s }.
     */
    @Test
    public void testAsfTagImpl() throws Exception {
        final AsfTag asfTag = new AsfTag();
        assertEquals(asfTag.createField(FieldKey.ALBUM, "").getId(), AsfFieldKey.ALBUM.getFieldName());
        assertEquals(asfTag.createField(FieldKey.ARTIST, "").getId(), AsfFieldKey.AUTHOR.getFieldName());
        assertEquals(asfTag.createField(FieldKey.COMMENT, "").getId(), AsfFieldKey.DESCRIPTION.getFieldName());
        assertEquals(asfTag.createField(FieldKey.GENRE, "").getId(), AsfFieldKey.GENRE.getFieldName());
        assertEquals(asfTag.createField(FieldKey.TITLE, "").getId(), AsfFieldKey.TITLE.getFieldName());
        assertEquals(asfTag.createField(FieldKey.TRACK, "").getId(), AsfFieldKey.TRACK.getFieldName());
        assertEquals(asfTag.createField(FieldKey.YEAR, "").getId(), AsfFieldKey.YEAR.getFieldName());
    }

    /**
     * Tests some constants which must have values.
     */
    @Test
    public void testConstants() {
        // UTF16-LE by specification
        assertEquals("UTF-16LE", AsfHeader.ASF_CHARSET.name(), "ONLY \"UTF-16LE\" text encoding specified");
    }
}
