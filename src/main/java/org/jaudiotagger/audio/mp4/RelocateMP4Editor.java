package org.jaudiotagger.audio.mp4;

import org.jaudiotagger.audio.generic.Utils;
import org.jcodec.containers.mp4.BoxFactory;
import org.jcodec.containers.mp4.MP4Util;
import org.jcodec.containers.mp4.MP4Util.Atom;
import org.jcodec.containers.mp4.boxes.Box;
import org.jcodec.containers.mp4.boxes.Header;
import org.jcodec.containers.mp4.boxes.MovieBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Parses MP4 file, applies the edit and saves the result in a new file.
 * <p>
 * Relocates the movie header to the end of the file if necessary.
 *
 * @author The JCodec project
 */
public class RelocateMP4Editor {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public void modifyOrRelocate(FileChannel src, MovieBox edit)
            throws IOException {
        boolean modify = new InplaceMP4Editor().modify(src, edit);
        if (!modify) {
            relocate(src, edit);
        }
    }

    public void relocate(FileChannel fi, MovieBox edit) throws IOException {
        Atom moovAtom = getMoov(fi);
        ByteBuffer moovBuffer = fetchBox(fi, moovAtom);
        MovieBox moovBox = (MovieBox) parseBox(moovBuffer);

        for (Box box : edit.getBoxes()) {
            moovBox.replaceBox(box);
        }

        if (moovAtom.offset() + moovAtom.header().getSize() < fi.size()) {
            log.info("Relocating movie header to the end of the file.");
            fi.position(moovAtom.offset() + 4);
            fi.write(ByteBuffer.wrap(Header.FOURCC_FREE));
            fi.position(fi.size());
        } else {
            fi.position(moovAtom.offset());
        }
        MP4Util.writeMovie(fi, moovBox);
    }

    private ByteBuffer fetchBox(FileChannel fi, Atom moov) throws IOException {
        fi.position(moov.offset());
        ByteBuffer oldMov = Utils.fetchFromChannel(
                fi,
                (int) moov.header().getSize()
        );
        return oldMov;
    }

    private Box parseBox(ByteBuffer oldMov) {
        Header header = Header.read(oldMov);
        Box box = Box.parseBox(oldMov, header, BoxFactory.getDefault());
        return box;
    }

    private Atom getMoov(FileChannel f) throws IOException {
        for (Atom atom : MP4Util.getRootAtoms(f)) {
            if ("moov".equals(atom.header().getFourcc())) {
                return atom;
            }
        }
        return null;
    }
}
