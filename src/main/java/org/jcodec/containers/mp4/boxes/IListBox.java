package org.jcodec.containers.mp4.boxes;

import org.jaudiotagger.audio.generic.Utils;
import org.jcodec.containers.mp4.Boxes;
import org.jcodec.containers.mp4.IBoxFactory;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 *
 * @author The JCodec project
 */
public class IListBox extends Box {

    private static final String FOURCC = "ilst";
    private Map<Integer, List<Box>> values;     // standard values
    private List<ReverseDnsBox> rdnsValues;  // reverse-dns-based values
    private final IBoxFactory factory;

    private static class LocalBoxes extends Boxes {
        //Initializing blocks are not supported by Javascript.
        LocalBoxes() {
            super();
            mappings.put(DataBox.fourcc(), DataBox.class);
            mappings.put(ReverseDnsBox.fourcc(), ReverseDnsBox.class);
        }
    }

    public IListBox(Header atom) {
        super(atom);
        factory = new SimpleBoxFactory(new LocalBoxes());
        values = new LinkedHashMap<>();
        rdnsValues = new ArrayList<>();
    }

    public static IListBox createIListBox(Map<Integer, List<Box>> values) {
        IListBox box = new IListBox(Header.createHeader(FOURCC, 0));
        box.values = values;
        return box;
    }

    public static IListBox createIListBox(Map<Integer, List<Box>> values, List<ReverseDnsBox> rdnsValues) {
        IListBox box = new IListBox(Header.createHeader(FOURCC, 0));
        box.values = values;
        box.rdnsValues = rdnsValues;
        return box;
    }

    public void parse(ByteBuffer input) {
        while (input.remaining() >= 4) {
            int size = input.getInt();
            ByteBuffer local = Utils.read(input, size - 4);
            int index = local.getInt();

            // check whether it's reverse-dns field
            if (Utils.reinterpretIntAsString(index).equals(ReverseDnsBox.fourcc())) {
                ReverseDnsBox box = (ReverseDnsBox) Box.parseBox(Utils.read(local, local.remaining()), Header.createHeader(ReverseDnsBox.fourcc(), local.remaining()), factory);
                rdnsValues.add(box);
                continue;
            }

            // it's not an rnds field
            List<Box> children = new ArrayList<>();
            values.put(index, children);
            while (local.hasRemaining()) {
                Header childAtom = Header.read(local);
                if (childAtom != null && local.remaining() >= childAtom.getBodySize()) {
                    Box box = Box.parseBox(Utils.read(local, (int) childAtom.getBodySize()), childAtom, factory);
                    children.add(box);
                }
            }
        }
    }

    public Map<Integer, List<Box>> getValues() {
        return values;
    }

    public List<ReverseDnsBox> getRdnsValues() {
        return rdnsValues;
    }

    public void setRdnsValues(List<ReverseDnsBox> rdnsValues) {
        this.rdnsValues = rdnsValues;
    }

    protected void doWrite(ByteBuffer out) {
        for (Entry<Integer, List<Box>> entry : values.entrySet()) {
            ByteBuffer fork = out.duplicate();
            out.putInt(0);
            out.putInt(entry.getKey());
            for (Box box : entry.getValue()) {
                box.write(out);
            }
            fork.putInt(out.position() - fork.position());
        }

        for (ReverseDnsBox rdns : rdnsValues) {
            rdns.write(out);
        }
    }

    @Override
    public int estimateSize() {
        int sz = 8;
        for (Entry<Integer, List<Box>> entry : values.entrySet()) {
            for (Box box : entry.getValue()) {
                sz += 8 + box.estimateSize();
            }
        }
        for (ReverseDnsBox rdns : rdnsValues) {
            sz += rdns.estimateSize();
        }
        return sz;
    }

    public static String fourcc() {
        return FOURCC;
    }
}
