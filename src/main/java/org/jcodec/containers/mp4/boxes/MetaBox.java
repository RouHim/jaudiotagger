package org.jcodec.containers.mp4.boxes;

import org.jaudiotagger.audio.asf.util.Utils;

import java.util.*;
import java.util.Map.Entry;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 *
 * @author The JCodec project
 */
public class MetaBox extends NodeBox {

    private static final String FOURCC = "meta";

    public MetaBox(Header atom) {
        super(atom);
    }

    public static MetaBox createMetaBox() {
        return new MetaBox(Header.createHeader(fourcc(), 0));
    }

    public Map<String, MetaValue> getKeyedMeta() {
        Map<String, MetaValue> result = new LinkedHashMap<String, MetaValue>();

        IListBox ilst = NodeBox.findFirst(this, IListBox.class, IListBox.fourcc());
        MdtaBox[] keys = NodeBox.findAllPath(this, MdtaBox.class, new String[]{KeysBox.fourcc(), MdtaBox.fourcc()});

        if (ilst == null || keys.length == 0)
            return result;

        for (Entry<Integer, List<Box>> entry : ilst.getValues().entrySet()) {
            Integer index = entry.getKey();
            if (index == null)
                continue;
            DataBox db = getDataBox(entry.getValue());
            if (db == null)
                continue;
            MetaValue value = MetaValue.createOtherWithLocale(db.getType(), db.getLocale(), db.getData());
            if (index > 0 && index <= keys.length) {
                result.put(keys[index - 1].getKey(), value);
            }
        }
        return result;
    }

    private DataBox getDataBox(List<Box> value) {
        for (Box box : value) {
            if (box instanceof DataBox) {
                return (DataBox) box;
            }
        }
        return null;
    }

    public Map<Integer, List<MetaValue>> getItunesMeta() {
        Map<Integer, List<MetaValue>> result = new LinkedHashMap<>();

        IListBox ilst = NodeBox.findFirst(this, IListBox.class, IListBox.fourcc());

        if (ilst == null)
            return result;

        for (Entry<Integer, List<Box>> entry : ilst.getValues().entrySet()) {
            Integer index = entry.getKey();
            if (index == null)
                continue;

            for (Box box : entry.getValue()) {
                if (box instanceof DataBox db) {
                    MetaValue value = MetaValue.createOtherWithLocale(db.getType(), db.getLocale(), db.getData());
                    if (result.containsKey(index)) {
                        result.get(index).add(value);
                    } else {
                        List<MetaValue> metaVals = new ArrayList<>();
                        metaVals.add(value);
                        result.put(index, metaVals);
                    }
                }
            }
        }
        return result;
    }

    public Map<String, MetaValue> getRdnsMeta() {
        IListBox ilst = NodeBox.findFirst(this, IListBox.class, IListBox.fourcc());

        if (ilst == null || ilst.getRdnsValues() == null)
            return Collections.emptyMap();

        Map<String, MetaValue> map = new HashMap<>();
        for (ReverseDnsBox rdns : ilst.getRdnsValues()) {
            if (!Utils.isEmpty(rdns.getName()) && !Utils.isEmpty(rdns.getIssuer())) {
                if (rdns.getData() != null && rdns.getData().length > 0) {
                    map.put("----:" + rdns.getIssuer() + ":" + rdns.getName(), MetaValue.createOtherWithLocale(rdns.getDataBox()));
                }
            }
        }
        return map;
    }

    public void setKeyedMeta(Map<String, MetaValue> map) {
        if (map.isEmpty())
            return;
        KeysBox keys = KeysBox.createKeysBox();
        Map<Integer, List<Box>> data = new LinkedHashMap<Integer, List<Box>>();
        int i = 1;
        for (Entry<String, MetaValue> entry : map.entrySet()) {
            keys.add(MdtaBox.createMdtaBox(entry.getKey()));
            MetaValue v = entry.getValue();
            List<Box> children = new ArrayList<Box>();
            children.add(DataBox.createDataBox(v.getType(), v.getLocale(), v.getData()));
            data.put(i, children);
            ++i;
        }
        IListBox ilst = IListBox.createIListBox(data);
        this.replaceBox(keys);
        this.replaceBox(ilst);
    }

    public void setItunesMeta(Map<Integer, MetaValue> map) {
        if (map.isEmpty())
            return;
        Map<Integer, MetaValue> copy = new LinkedHashMap<Integer, MetaValue>();
        copy.putAll(map);
        IListBox ilst = NodeBox.findFirst(this, IListBox.class, IListBox.fourcc());
        Map<Integer, List<Box>> data;
        if (ilst == null) {
            data = new LinkedHashMap<Integer, List<Box>>();
        } else {
            data = ilst.getValues();

            // Updating values
            for (Entry<Integer, List<Box>> entry : data.entrySet()) {
                int index = entry.getKey();
                MetaValue v = copy.get(index);
                if (v != null) {
                    DataBox dataBox = DataBox.createDataBox(v.getType(), v.getLocale(), v.getData());
                    dropChildBox(entry.getValue(), DataBox.fourcc());
                    entry.getValue().add(dataBox);
                    copy.remove(index);
                }
            }
        }

        // Adding values
        for (Entry<Integer, MetaValue> entry : copy.entrySet()) {
            int index = entry.getKey();
            MetaValue v = entry.getValue();
            DataBox dataBox = DataBox.createDataBox(v.getType(), v.getLocale(), v.getData());
            List<Box> children = new ArrayList<Box>();
            data.put(index, children);
            children.add(dataBox);
        }

        // Dropping values
        Set<Integer> keySet = new HashSet<Integer>(data.keySet());
        keySet.removeAll(map.keySet());
        for (Integer dropped : keySet) {
            data.remove(dropped);
        }

        this.replaceBox(IListBox.createIListBox(data));
    }

    private void dropChildBox(List<Box> children, String fourcc2) {
        ListIterator<Box> listIterator = children.listIterator();
        while (listIterator.hasNext()) {
            Box next = listIterator.next();
            if (fourcc2.equals(next.getFourcc())) {
                listIterator.remove();
            }
        }
    }

    public static String fourcc() {
        return FOURCC;
    }
}
