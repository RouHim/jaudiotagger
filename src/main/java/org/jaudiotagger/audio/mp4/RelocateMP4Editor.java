package org.jaudiotagger.audio.mp4;

import org.jaudiotagger.audio.generic.Utils;
import org.jcodec.containers.mp4.BoxFactory;
import org.jcodec.containers.mp4.MP4Util;
import org.jcodec.containers.mp4.MP4Util.Atom;
import org.jcodec.containers.mp4.boxes.Box;
import org.jcodec.containers.mp4.boxes.Header;
import org.jcodec.containers.mp4.boxes.MovieBox;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.logging.Logger;

/**
 * Parses MP4 file, applies the edit and saves the result in a new file.
 * <p>
 * Relocates the movie header to the end of the file if necessary.
 *
 * @author The JCodec project
 */
public class RelocateMP4Editor {

    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.mp4.writer");

    public void modifyOrRelocate(FileChannel src, MovieBox edit) throws IOException {
        boolean modify = new InplaceMP4Editor().modify(src, edit);
        if (!modify)
            relocate(src, edit);
    }

    public void relocate(FileChannel fi, MovieBox edit) throws IOException {
        Atom moovAtom = getMoov(fi);
        ByteBuffer moovBuffer = fetchBox(fi, moovAtom);
        MovieBox moovBox = (MovieBox) parseBox(moovBuffer);

        for (Box box : edit.getBoxes()) {
            moovBox.replaceBox(box);
        }

        if (moovAtom.getOffset() + moovAtom.getHeader().getSize() < fi.size()) {
            logger.info("Relocating movie header to the end of the file.");
            fi.position(moovAtom.getOffset() + 4);
            fi.write(ByteBuffer.wrap(Header.FOURCC_FREE));
            fi.position(fi.size());
        } else {
            fi.position(moovAtom.getOffset());
        }
        MP4Util.writeMovie(fi, moovBox);
    }

    private ByteBuffer fetchBox(FileChannel fi, Atom moov) throws IOException {
        fi.position(moov.getOffset());
        ByteBuffer oldMov = Utils.fetchFromChannel(fi, (int) moov.getHeader().getSize());
        return oldMov;
    }

    private Box parseBox(ByteBuffer oldMov) {
        Header header = Header.read(oldMov);
        Box box = Box.parseBox(oldMov, header, BoxFactory.getDefault());
        return box;
    }

    private Atom getMoov(FileChannel f) throws IOException {
        for (Atom atom : MP4Util.getRootAtoms(f)) {
            if ("moov".equals(atom.getHeader().getFourcc())) {
                return atom;
            }
        }
        return null;
    }
}
