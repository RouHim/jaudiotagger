/**
 *
 */
package org.jaudiotagger.audio.asf.io;


import org.jaudiotagger.audio.asf.data.ContentBranding;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.data.MetadataContainerUtils;
import org.jaudiotagger.audio.asf.util.Utils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContentBrandingData {

    @Test
    public void testContentBrandingWriteRead() throws IOException {
        ContentBranding cb = new ContentBranding();
        cb.setCopyRightURL("CP URL");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        cb.writeInto(bos);
        assertEquals(cb.getCurrentAsfChunkSize(), bos.toByteArray().length);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        assertEquals(GUID.GUID_CONTENT_BRANDING, Utils.readGUID(bis));
        ContentBranding read = (ContentBranding) new ContentBrandingReader()
                .read(GUID.GUID_CONTENT_BRANDING, bis, 0);
        MetadataContainerUtils.equals(cb, read);
    }

}
