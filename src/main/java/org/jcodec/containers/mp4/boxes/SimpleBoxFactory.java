package org.jcodec.containers.mp4.boxes;

import org.jaudiotagger.audio.generic.Utils;
import org.jcodec.containers.mp4.Boxes;
import org.jcodec.containers.mp4.IBoxFactory;

public class SimpleBoxFactory implements IBoxFactory {
    private final Boxes boxes;

    public SimpleBoxFactory(Boxes boxes) {
        this.boxes = boxes;
    }

    @Override
    public Box newBox(Header header) {
        Class<? extends Box> claz = boxes.toClass(header.getFourcc());
        if (claz == null)
            return new Box.LeafBox(header);
        Box box = Utils.newInstance(claz, new Object[]{header});
        return box;
    }

}
