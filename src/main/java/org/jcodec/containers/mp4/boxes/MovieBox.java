package org.jcodec.containers.mp4.boxes;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 * <p>
 * Creates MP4 file out of a set of samples
 *
 * @author The JCodec project
 */
public class MovieBox extends NodeBox {

    public MovieBox(Header atom) {
        super(atom);
    }

    public static String fourcc() {
        return "moov";
    }

    public static MovieBox createMovieBox() {
        return new MovieBox(new Header(fourcc()));
    }

    public TrakBox[] getTracks() {
        return NodeBox.findAll(this, TrakBox.class, "trak");
    }

    public TrakBox getVideoTrack() {
        TrakBox[] tracks = getTracks();
        for (int i = 0; i < tracks.length; i++) {
            TrakBox trakBox = tracks[i];
            if (trakBox.isVideo())
                return trakBox;
        }
        return null;
    }

    public TrakBox getTimecodeTrack() {
        TrakBox[] tracks = getTracks();
        for (int i = 0; i < tracks.length; i++) {
            TrakBox trakBox = tracks[i];
            if (trakBox.isTimecode())
                return trakBox;
        }
        return null;
    }

    public int getTimescale() {
        return getMovieHeader().getTimescale();
    }

    public long rescale(long tv, long ts) {
        return (tv * getTimescale()) / ts;
    }

    public void fixTimescale(int newTs) {
        int oldTs = getTimescale();
        setTimescale(newTs);

        TrakBox[] tracks = getTracks();
        for (int i = 0; i < tracks.length; i++) {
            TrakBox trakBox = tracks[i];
            trakBox.setDuration(rescale(trakBox.getDuration(), oldTs));

            List<Edit> edits = trakBox.getEdits();
            if (edits == null)
                continue;
            ListIterator<Edit> lit = edits.listIterator();
            while (lit.hasNext()) {
                Edit edit = lit.next();
                lit.set(new Edit(rescale(edit.getDuration(), oldTs), edit.getMediaTime(), edit.getRate()));
            }
        }

        setDuration(rescale(getDuration(), oldTs));
    }

    private void setTimescale(int newTs) {
        NodeBox.findFirst(this, MovieHeaderBox.class, "mvhd").setTimescale(newTs);
    }

    public void setDuration(long movDuration) {
        getMovieHeader().setDuration(movDuration);
    }

    public MovieHeaderBox getMovieHeader() {
        return NodeBox.findFirst(this, MovieHeaderBox.class, "mvhd");
    }

    public List<TrakBox> getAudioTracks() {
        ArrayList<TrakBox> result = new ArrayList<TrakBox>();
        TrakBox[] tracks = getTracks();
        for (int i = 0; i < tracks.length; i++) {
            TrakBox trakBox = tracks[i];
            if (trakBox.isAudio())
                result.add(trakBox);
        }
        return result;
    }

    public long getDuration() {
        return getMovieHeader().getDuration();
    }

    public TrakBox importTrack(MovieBox movie, TrakBox track) {
        TrakBox newTrack = (TrakBox) NodeBox.cloneBox(track, 1024 * 1024, factory);

        List<Edit> edits = newTrack.getEdits();

        ArrayList<Edit> result = new ArrayList<Edit>();
        if (edits != null) {
            for (Edit edit : edits) {
                result.add(new Edit(rescale(edit.getDuration(), movie.getTimescale()), edit.getMediaTime(), edit
                        .getRate()));
            }
        }
        newTrack.setEdits(result);

        return newTrack;
    }

    public void appendTrack(TrakBox newTrack) {
        newTrack.getTrackHeader().setNo(getMovieHeader().getNextTrackId());
        getMovieHeader().setNextTrackId(getMovieHeader().getNextTrackId() + 1);
        boxes.add(newTrack);
    }

    public boolean isPureRefMovie() {
        boolean pureRef = true;
        TrakBox[] tracks = getTracks();
        for (int i = 0; i < tracks.length; i++) {
            TrakBox trakBox = tracks[i];
            pureRef &= trakBox.isPureRef();
        }
        return pureRef;
    }

    public void updateDuration() {
        TrakBox[] tracks = getTracks();
        long min = Integer.MAX_VALUE;
        for (int i = 0; i < tracks.length; i++) {
            TrakBox trakBox = tracks[i];
            if (trakBox.getDuration() < min)
                min = trakBox.getDuration();
        }
        getMovieHeader().setDuration(min);
    }
}