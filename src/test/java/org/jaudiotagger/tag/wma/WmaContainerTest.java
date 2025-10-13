package org.jaudiotagger.tag.wma;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.asf.data.Chunk;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.data.MetadataContainer;
import org.jaudiotagger.audio.asf.data.MetadataContainerUtils;
import org.jaudiotagger.audio.asf.io.AsfHeaderUtils;
import org.jaudiotagger.audio.asf.io.MetadataReader;
import org.jaudiotagger.audio.asf.util.Utils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class WmaContainerTest extends WmaTestCase {

    public static final String TEST_FILE = "test6.wma";

    public WmaContainerTest() {
        super(TEST_FILE);
    }

    @Test
    public void testExtContentAfterWrite() throws Exception {
        File prepareTestFile = prepareTestFile(null);
        AudioFile read = AudioFileIO.read(prepareTestFile);
        read.commit(); // Normalize Text file
        byte[] ext = AsfHeaderUtils.getFirstChunk(
                read.getFile(),
                GUID.GUID_EXTENDED_CONTENT_DESCRIPTION
        );
        read.commit();
        byte[] ext2 = AsfHeaderUtils.getFirstChunk(
                read.getFile(),
                GUID.GUID_EXTENDED_CONTENT_DESCRIPTION
        );
        assertArrayEquals(ext, ext2);
        //        assertEquals(ext, ext2);
    }

    @Test
    public void testReadWriteEquality() throws IOException {
        File prepareTestFile = prepareTestFile(null);
        byte[] tmp = AsfHeaderUtils.getFirstChunk(
                prepareTestFile,
                GUID.GUID_EXTENDED_CONTENT_DESCRIPTION
        );

        MetadataReader reader = new MetadataReader();
        ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
        GUID readGUID = Utils.readGUID(bis);
        assertEquals(GUID.GUID_EXTENDED_CONTENT_DESCRIPTION, readGUID);
        Chunk read1 = reader.read(GUID.GUID_EXTENDED_CONTENT_DESCRIPTION, bis, 0);
        System.out.println(read1);
        assertInstanceOf(MetadataContainer.class, read1);
        assertEquals(
                tmp.length,
                ((MetadataContainer) read1).getCurrentAsfChunkSize()
        );
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ((MetadataContainer) read1).writeInto(bos);
        assertEquals(tmp.length, bos.toByteArray().length);
        bis = new ByteArrayInputStream(bos.toByteArray());
        readGUID = Utils.readGUID(bis);
        assertEquals(GUID.GUID_EXTENDED_CONTENT_DESCRIPTION, readGUID);
        Chunk read2 = reader.read(GUID.GUID_EXTENDED_CONTENT_DESCRIPTION, bis, 0);
        System.out.println(
                MetadataContainerUtils.equals(
                        (MetadataContainer) read1,
                        (MetadataContainer) read2
                )
        );
    }
}
