package org.jaudiotagger.audio.dsf;

import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagOptionSingleton;

/**
 * Created by Paul on 28/01/2016.
 */
public class Dsf {
    public static Tag createDefaultTag() {
        return TagOptionSingleton.createDefaultID3Tag();
    }
}
