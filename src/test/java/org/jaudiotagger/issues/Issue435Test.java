package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.*;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTRDA;
import org.jaudiotagger.tag.reference.ID3V2Version;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Issue435Test extends AbstractTestCase {
    @Test
    public void testConvertV23TRDAToV24TRDC() {
        Throwable e = null;
        try {
            ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_TRDA);
            FrameBodyTRDA fb = new FrameBodyTRDA((byte) 0, "2008");
            frame.setBody(fb);

            File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            TagOptionSingleton.getInstance().setToDefault();
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
            af.getTagOrCreateAndSetDefault();
            ((ID3v23Tag) af.getTag()).setFrame(frame);
            af.commit();

            af = AudioFileIO.read(testFile);
            TagOptionSingleton.getInstance().setToDefault();
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
            af.getTagAndConvertOrCreateAndSetDefault();
            af.commit();
            assertTrue(af.getTag() instanceof ID3v24Tag);
            assertTrue(((ID3v24Tag) af.getTag()).getFrame("TDRC") instanceof AbstractID3v2Frame);

            TagOptionSingleton.getInstance().setToDefault();
        } catch (Exception ex) {
            e = ex;
        }
        assertNull(e);
    }
}
