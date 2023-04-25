package org.jcodec.containers.mp4.boxes;

import org.jaudiotagger.audio.generic.Utils;

import java.nio.ByteBuffer;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 * <p>
 * A handler description box
 *
 * @author The JCodec project
 */
public class HandlerBox extends FullBox {
    public HandlerBox(Header atom) {
        super(atom);
    }

    private String componentType;
    private String componentSubType;
    private String componentManufacturer;
    private int componentFlags;
    private int componentFlagsMask;
    private String componentName;

    public static String fourcc() {
        return "hdlr";
    }

    public static HandlerBox createHandlerBox(String componentType, String componentSubType,
                                              String componentManufacturer, int componentFlags, int componentFlagsMask) {
        HandlerBox hdlr = new HandlerBox(new Header(fourcc()));
        hdlr.componentType = componentType;
        hdlr.componentSubType = componentSubType;
        hdlr.componentManufacturer = componentManufacturer;
        hdlr.componentFlags = componentFlags;
        hdlr.componentFlagsMask = componentFlagsMask;
        hdlr.componentName = "";
        return hdlr;
    }

    public void parse(ByteBuffer input) {
        super.parse(input);

        componentType = Utils.readFourBytesAsChars(input);
        componentSubType = Utils.readFourBytesAsChars(input);
        componentManufacturer = Utils.readFourBytesAsChars(input);

        componentFlags = input.getInt();
        componentFlagsMask = input.getInt();
        componentName = Utils.readString(input, input.remaining());
    }

    public void doWrite(ByteBuffer out) {
        super.doWrite(out);

        out.put(componentType.getBytes());
        out.put(componentSubType.getBytes());
        out.put(componentManufacturer.getBytes());

        out.putInt(componentFlags);
        out.putInt(componentFlagsMask);
        if (componentName != null) {
            out.put(componentName.getBytes());
        }
    }

    @Override
    public int estimateSize() {
        return 12
                + componentType.getBytes().length
                + componentSubType.getBytes().length
                + componentManufacturer.getBytes().length + 9;
    }

    public String getComponentType() {
        return componentType;
    }

    public String getComponentSubType() {
        return componentSubType;
    }

    public String getComponentManufacturer() {
        return componentManufacturer;
    }

    public int getComponentFlags() {
        return componentFlags;
    }

    public int getComponentFlagsMask() {
        return componentFlagsMask;
    }
}