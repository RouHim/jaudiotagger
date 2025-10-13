package org.jaudiotagger.tag.id3;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Disabled("Only for local testing")
class ID3v22FramesTest {

    @Test
    public void generate() {
        // used to generate entries for a kotlin enum
        ID3v24Frames frames = ID3v24Frames.getInstanceOf();
        for (Map.Entry<String, String> entry : frames.getIdToValueMap().entrySet()) {
            String key = entry.getKey();
            ID3v24FieldKey fieldKey = ID3v24FieldKey.fromFrameId(key);
            System.out.println("(\"" + key + "\", " + (fieldKey != null ? "KFieldKey." + fieldKey : "null") + ", \"" + entry.getValue() + "\", " + frames.isCommon(key) + ", " + frames.isBinary(key) + ", " + frames.isMultipleAllowed(key) + ", " + frames.isSupportedFrames(key) + ", " + frames.isExtensionFrames(key) + ", " + frames.isDiscardIfFileAltered(key) + "),");
        }
    }
}