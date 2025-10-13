package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.ID3v23Frame;
import org.jaudiotagger.tag.id3.ID3v23Frames;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTRDA;
import org.jaudiotagger.tag.reference.ID3V2Version;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Issue435Test extends AbstractTestCase {

    @Test
    public void testConvertV23TRDAToV24TRDC() {
        Throwable e = null;
        try {
            ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_TRDA);
            FrameBodyTRDA fb = new FrameBodyTRDA((byte) 0, "2008");
            frame.setBody(fb);

            File testFile = copyAudioToTmp("testV25.mp3");
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
            assertInstanceOf(ID3v24Tag.class, af.getTag());
            assertInstanceOf(AbstractID3v2Frame.class, ((ID3v24Tag) af.getTag()).getFrame("TDRC"));

            TagOptionSingleton.getInstance().setToDefault();
        } catch (Exception ex) {
            e = ex;
        }
        assertNull(e);
    }
}
