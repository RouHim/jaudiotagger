package org.jaudiotagger.tag.wma;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.asf.data.AsfHeader;
import org.jaudiotagger.audio.asf.data.ContentDescription;
import org.jaudiotagger.audio.asf.io.AsfHeaderReader;
import org.jaudiotagger.audio.asf.util.TagConverter;
import org.jaudiotagger.tag.asf.AsfFieldKey;
import org.jaudiotagger.tag.asf.AsfTag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WmaContentDescriptionTest extends WmaTestCase {

    /**
     * Testfile to use as source.
     */
    public static final String TEST_FILE = "test1.wma";

    /**
     * Creates an instance.
     */
    public WmaContentDescriptionTest() {
        super(TEST_FILE);
    }

    /**
     * tests whether the content description chunk will disappear if not
     * necessary.<br>
     *
     * @throws Exception On I/O Errors
     */
    @Test
    public void testContentDescriptionRemoval() throws Exception {
        AudioFile file = getAudioFile();
        AsfTag tag = (AsfTag) file.getTag();
        for (String curr : ContentDescription.ALLOWED) {
            tag.deleteField(AsfFieldKey.getAsfFieldKey(curr));
        }
        file.commit();
        AsfHeader header = AsfHeaderReader.readHeader(file.getFile());
        assertNull(header.getContentDescription());
        for (String currKey : ContentDescription.ALLOWED) {
            AsfFieldKey curr = AsfFieldKey.getAsfFieldKey(currKey);
            AudioFileIO.delete(file);
            header = AsfHeaderReader.readHeader(file.getFile());
            assertNull(header.getContentDescription());
            assertNull(header.getExtendedContentDescription());
            file = AudioFileIO.read(file.getFile());
            tag = (AsfTag) file.getTag();
            tag.addField(tag.createField(curr, curr.getFieldName()));
            file.commit();
            header = AsfHeaderReader.readHeader(file.getFile());
            assertNotNull(header.getContentDescription());
            assertNull(
                    header.getExtendedContentDescription(),
                    "Key: " + curr.getFieldName()
            );
            assertEquals(
                    curr.getFieldName(),
                    TagConverter.createTagOf(header).getFirst(curr.getFieldName())
            );
        }
    }
}
