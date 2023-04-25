package org.jaudiotagger.audio.mp4;

import org.jcodec.containers.mp4.MP4Util;
import org.jcodec.containers.mp4.MP4Util.Movie;
import org.jcodec.containers.mp4.boxes.Box;
import org.jcodec.containers.mp4.boxes.MovieBox;

import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * A full fledged MP4 editor.
 * <p>
 * Parses MP4 file, applies the edit and saves the result in a new file.
 * <p>
 * Unlike InplaceMP4Edit any changes are allowed. This class will take care of
 * adjusting all the sample offsets so the result file will be correct.
 *
 * @author The JCodec project
 */
public class ReplaceMP4Editor {

    public void modifyOrReplace(FileChannel src, FileChannel dst, MovieBox edit) throws IOException {
        boolean modify = new InplaceMP4Editor().modify(dst, edit);
        if (!modify)
            copy(src, dst, edit);
    }

    public void copy(FileChannel src, FileChannel dst, MovieBox edit) throws IOException {
        final Movie movie = MP4Util.parseFullMovieChannel(src);

        for (Box box : edit.getBoxes()) {
            movie.getMoov().replaceBox(box);
        }

        Flatten fl = new Flatten();
        fl.flattenChannel(movie, dst);
    }
}