package org.jcodec.containers.mp4;

import org.jaudiotagger.audio.generic.Utils;
import org.jcodec.containers.mp4.boxes.Box;
import org.jcodec.containers.mp4.boxes.FileTypeBox;
import org.jcodec.containers.mp4.boxes.Header;
import org.jcodec.containers.mp4.boxes.MovieBox;
import org.jcodec.containers.mp4.boxes.MovieFragmentBox;
import org.jcodec.containers.mp4.boxes.TrakBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 *
 * @author The JCodec project
 */
public class MP4Util {

    protected static final Logger log = LoggerFactory.getLogger(MP4Util.class);

    public static List<MovieFragmentBox> parseMovieFragments(FileChannel input)
            throws IOException {
        MovieBox moov = null;
        LinkedList<MovieFragmentBox> fragments = new LinkedList<MovieFragmentBox>();
        for (Atom atom : getRootAtoms(input)) {
            if ("moov".equals(atom.header().getFourcc())) {
                moov = (MovieBox) atom.parseBox(input);
            } else if ("moof".equalsIgnoreCase(atom.header().getFourcc())) {
                fragments.add((MovieFragmentBox) atom.parseBox(input));
            }
        }
        for (MovieFragmentBox fragment : fragments) {
            fragment.setMovie(moov);
        }
        return fragments;
    }

    public static List<Atom> getRootAtoms(FileChannel input) throws IOException {
        input.position(0);
        List<Atom> result = new ArrayList<Atom>();
        long off = 0;
        Header atom;
        while (off < input.size()) {
            input.position(off);
            atom = Header.read(Utils.fetchFromChannel(input, 16));
            if (atom == null) {
                break;
            }
            result.add(new Atom(atom, off));
            off += atom.getSize();
        }

        return result;
    }

    public static Atom getMoov(List<Atom> rootAtoms) {
        for (Atom atom : rootAtoms) {
            if ("moov".equals(atom.header().getFourcc())) {
                return atom;
            }
        }
        return null;
    }

    public static Atom findFirstAtom(String fourcc, FileChannel input)
            throws IOException {
        List<Atom> rootAtoms = getRootAtoms(input);
        for (Atom atom : rootAtoms) {
            if (fourcc.equals(atom.header().getFourcc())) {
                return atom;
            }
        }
        return null;
    }

    public static Atom atom(FileChannel input) throws IOException {
        long off = input.position();
        Header atom = Header.read(Utils.fetchFromChannel(input, 16));

        return atom == null ? null : new Atom(atom, off);
    }

    public static Box parseBox(
            ByteBuffer input,
            Header childAtom,
            IBoxFactory factory
    ) {
        Box box = factory.newBox(childAtom);

        if (childAtom.getBodySize() < Box.MAX_BOX_SIZE) {
            box.parse(input);
            return box;
        } else {
            return new Box.LeafBox(Header.createHeader("free", 8));
        }
    }

    public static MovieBox parseMovie(File source) throws IOException {
        FileChannel input = null;
        try {
            input = new FileInputStream(source).getChannel();
            return parseMovieChannel(input);
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    public static MovieBox parseMovieChannel(FileChannel input)
            throws IOException {
        for (Atom atom : getRootAtoms(input)) {
            if ("moov".equals(atom.header().getFourcc())) {
                return (MovieBox) atom.parseBox(input);
            }
        }
        return null;
    }

    public static MovieBox createRefMovieFromFile(File source)
            throws IOException {
        FileChannel input = null;
        try {
            input = new FileInputStream(source).getChannel();
            return createRefMovie(input, "file://" + source.getCanonicalPath());
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    public static MovieBox createRefMovie(FileChannel input, String url)
            throws IOException {
        MovieBox movie = parseMovieChannel(input);

        TrakBox[] tracks = movie.getTracks();
        for (int i = 0; i < tracks.length; i++) {
            TrakBox trakBox = tracks[i];
            trakBox.setDataRef(url);
        }
        return movie;
    }

    public static void writeMovieToFile(File f, MovieBox movie)
            throws IOException {
        try (FileChannel out = new FileOutputStream(f).getChannel()) {
            writeMovie(out, movie);
        }
    }

    public static void writeMovie(FileChannel out, MovieBox movie)
            throws IOException {
        doWriteMovieToChannel(out, movie, 0);
    }

    public static void doWriteMovieToChannel(
            FileChannel out,
            MovieBox movie,
            int additionalSize
    ) throws IOException {
        int sizeHint = estimateMoovBoxSize(movie) + additionalSize;
        log.debug("Using " + sizeHint + " bytes for MOOV box");

        ByteBuffer buf = ByteBuffer.allocate(sizeHint * 4);
        movie.write(buf);
        buf.flip();
        out.write(buf);
    }

    /**
     * Estimate buffer size needed to write MOOV box based on the amount of
     * stuff in there
     *
     * @param movie
     * @return
     */
    public static int estimateMoovBoxSize(MovieBox movie) {
        return movie.estimateSize() + (4 << 10);
    }

    public static Movie parseFullMovie(File source) throws IOException {
        FileChannel input = null;
        try {
            input = new FileInputStream(source).getChannel();
            return parseFullMovieChannel(input);
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    public static Movie parseFullMovieChannel(FileChannel input)
            throws IOException {
        FileTypeBox ftyp = null;
        List<Atom> rootAtoms = getRootAtoms(input);
        Iterator<Atom> itr = rootAtoms.iterator();
        while (itr.hasNext()) {
            Atom atom = itr.next();
            if ("ftyp".equals(atom.header().getFourcc())) {
                ftyp = (FileTypeBox) atom.parseBox(input);
                itr.remove();
            } else if ("moov".equals(atom.header().getFourcc())) {
                MovieBox moov = (MovieBox) atom.parseBox(input);
                itr.remove();
                return new Movie(ftyp, moov, rootAtoms);
            }
        }
        return null;
    }

    public static Movie createRefFullMovieFromFile(File source)
            throws IOException {
        FileChannel input = null;
        try {
            input = new FileInputStream(source).getChannel();
            return createRefFullMovie(input, "file://" + source.getCanonicalPath());
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    public static Movie createRefFullMovie(FileChannel input, String url)
            throws IOException {
        Movie movie = parseFullMovieChannel(input);

        TrakBox[] tracks = movie.moov.getTracks();
        for (int i = 0; i < tracks.length; i++) {
            TrakBox trakBox = tracks[i];
            trakBox.setDataRef(url);
        }
        return movie;
    }

    public static void writeFullMovie(FileChannel out, Movie movie)
            throws IOException {
        doWriteFullMovieToChannel(out, movie, 0);
    }

    public static void doWriteFullMovieToChannel(
            FileChannel out,
            Movie movie,
            int additionalSize
    ) throws IOException {
        int sizeHint = estimateMoovBoxSize(movie.moov()) + additionalSize;
        log.debug("Using " + sizeHint + " bytes for MOOV box");

        ByteBuffer buf = ByteBuffer.allocate(sizeHint + 128);
        movie.ftyp().write(buf);
        movie.moov().write(buf);
        buf.flip();
        out.write(buf);
    }

    public static ByteBuffer writeBox(Box box, int approxSize) {
        ByteBuffer buf = ByteBuffer.allocate(approxSize);
        box.write(buf);
        buf.flip();

        return buf;
    }

    public record Movie(FileTypeBox ftyp, MovieBox moov, List<Atom> others) {

    }

    public record Atom(Header header, long offset) {

        public Box parseBox(FileChannel input) throws IOException {
                input.position(offset + header.headerSize());
                return MP4Util.parseBox(
                        Utils.fetchFromChannel(input, (int) header.getBodySize()),
                        header,
                        BoxFactory.getDefault()
                );
            }

            public void copy(FileChannel input, WritableByteChannel out)
                    throws IOException {
                input.position(offset);
                Utils.copy(input, out, header.getSize());
            }
        }
}
