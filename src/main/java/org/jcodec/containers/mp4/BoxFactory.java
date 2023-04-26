package org.jcodec.containers.mp4;

import org.jaudiotagger.audio.generic.Utils;
import org.jcodec.containers.mp4.boxes.*;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 * <p>
 * Default box factory
 *
 * @author The JCodec project
 */
public class BoxFactory implements IBoxFactory {

    private static final IBoxFactory instance = new BoxFactory(new DefaultBoxes());
    private static final IBoxFactory audio = new BoxFactory(new AudioBoxes());
    private static final IBoxFactory data = new BoxFactory(new DataBoxes());
    private static final IBoxFactory sample = new BoxFactory(new SampleBoxes());
    private static final IBoxFactory timecode = new BoxFactory(new TimecodeBoxes());
    private static final IBoxFactory waveext = new BoxFactory(new WaveExtBoxes());

    private final Boxes boxes;

    public static IBoxFactory getDefault() {
        return instance;
    }

    public BoxFactory(Boxes boxes) {
        this.boxes = boxes;
    }

    @Override
    public Box newBox(Header header) {
        Class<? extends Box> claz = boxes.toClass(header.getFourcc());
        if (claz == null)
            return new Box.LeafBox(header);
        Box box = Utils.newInstance(claz, new Object[]{header});
        if (box instanceof NodeBox nodebox) {
            if (nodebox instanceof SampleDescriptionBox) {
                nodebox.setFactory(sample);
            } else if (nodebox instanceof AudioSampleEntry) {
                nodebox.setFactory(audio);
            } else if (nodebox instanceof TimecodeSampleEntry) {
                nodebox.setFactory(timecode);
            } else if (nodebox instanceof DataRefBox) {
                nodebox.setFactory(data);
            } else if (nodebox instanceof WaveExtension) {
                nodebox.setFactory(waveext);
            } else {
                nodebox.setFactory(this);
            }
        }
        return box;
    }
}