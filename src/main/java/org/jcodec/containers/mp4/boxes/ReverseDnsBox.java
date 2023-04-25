package org.jcodec.containers.mp4.boxes;

import org.jaudiotagger.audio.generic.Utils;
import org.jcodec.containers.mp4.Boxes;
import org.jcodec.containers.mp4.IBoxFactory;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ReverseDnsBox extends NodeBox {

    private static final String FOURCC = "----";

    private final IBoxFactory factory;

    public ReverseDnsBox(Header header) {
        super(header);
        factory = new SimpleBoxFactory(new ReverseDnsBox.LocalBoxes());
    }

    public static ReverseDnsBox createReverseDnsBox(String issuer, String name, DataBox data) {
        ReverseDnsBox box = new ReverseDnsBox(new Header(fourcc()));
        box.add(RdnsMeanBox.createRdnsMeanBox(issuer));
        box.add(RdnsNameBox.createRdnsNameBox(name));
        box.add(data);
        return box;
    }

    @Override
    public void parse(ByteBuffer input) {
        while (input.remaining() >= 8) {
            Box child = parseChildBox(input, factory);
            if (child != null)
                boxes.add(child);
        }
    }

    public RdnsNameBox getNameBox() {
        return NodeBox.findFirst(this, RdnsNameBox.class, "name");
    }

    public String getName() {
        RdnsNameBox name = NodeBox.findFirst(this, RdnsNameBox.class, "name");
        if (name != null)
            return name.getName();

        return null;
    }

    public RdnsMeanBox getMeanBox() {
        return NodeBox.findFirst(this, RdnsMeanBox.class, "mean");
    }

    public String getIssuer() {
        RdnsMeanBox mean = NodeBox.findFirst(this, RdnsMeanBox.class, "mean");
        if (mean != null)
            return mean.getIssuer();

        return null;
    }

    public DataBox getDataBox() {
        return NodeBox.findFirst(this, DataBox.class, "data");
    }

    public byte[] getData() {
        DataBox data = NodeBox.findFirst(this, DataBox.class, "data");
        if (data != null)
            return data.getData();

        return null;
    }

    public static String fourcc() {
        return FOURCC;
    }

    private static class LocalBoxes extends Boxes {
        //Initializing blocks are not supported by Javascript.
        LocalBoxes() {
            super();
            mappings.put(RdnsMeanBox.fourcc(), RdnsMeanBox.class);
            mappings.put(RdnsNameBox.fourcc(), RdnsNameBox.class);
            mappings.put(DataBox.fourcc(), DataBox.class);
        }
    }

    public static class RdnsMeanBox extends Box {

        private static final String FOURCC = "mean";

        private String issuer;

        public RdnsMeanBox(Header atom) {
            super(atom);
        }

        public static RdnsMeanBox createRdnsMeanBox(String issuer) {
            RdnsMeanBox box = new RdnsMeanBox(new Header(fourcc()));
            box.issuer = issuer;
            return box;
        }

        public String getIssuer() {
            return issuer;
        }

        public void setIssuer(String issuer) {
            this.issuer = issuer;
        }

        @Override
        public void parse(ByteBuffer input) {
            Utils.skip(input, 4);
            issuer = Utils.readString(input, input.remaining());
        }

        @Override
        protected void doWrite(ByteBuffer out) {
            out.putInt(0);
            out.put(issuer.getBytes(StandardCharsets.US_ASCII));
        }

        @Override
        public int estimateSize() {
            return 8 + 4 + issuer.getBytes(StandardCharsets.US_ASCII).length;
        }

        public static String fourcc() {
            return FOURCC;
        }
    }

    public static class RdnsNameBox extends Box {

        private static final String FOURCC = "name";

        private String name;

        public RdnsNameBox(Header atom) {
            super(atom);
        }

        public static RdnsNameBox createRdnsNameBox(String name) {
            RdnsNameBox box = new RdnsNameBox(new Header(fourcc()));
            box.name = name;
            return box;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public void parse(ByteBuffer input) {
            Utils.skip(input, 4);
            name = Utils.readString(input, input.remaining());
        }

        @Override
        protected void doWrite(ByteBuffer out) {
            out.putInt(0);
            out.put(name.getBytes(StandardCharsets.US_ASCII));
        }

        @Override
        public int estimateSize() {
            return 8 + 4 + name.getBytes(StandardCharsets.US_ASCII).length;
        }

        public static String fourcc() {
            return FOURCC;
        }
    }
}
