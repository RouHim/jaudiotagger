package org.jcodec.containers.mp4.boxes;

import java.nio.ByteBuffer;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 *
 * @author The JCodec project
 */
public class CleanApertureExtension extends Box {
    private int vertOffsetDenominator;
    private int vertOffsetNumerator;
    private int horizOffsetDenominator;
    private int horizOffsetNumerator;
    private int apertureHeightDenominator;
    private int apertureHeightNumerator;
    private int apertureWidthDenominator;
    private int apertureWidthNumerator;

    public CleanApertureExtension(Header header) {
        super(header);
    }

    public static CleanApertureExtension createCleanApertureExtension(int apertureWidthN, int apertureWidthD,
                                                                      int apertureHeightN, int apertureHeightD, int horizOffN, int horizOffD, int vertOffN, int vertOffD) {
        CleanApertureExtension clap = new CleanApertureExtension(new Header(fourcc()));
        clap.apertureWidthNumerator = apertureWidthN;
        clap.apertureWidthDenominator = apertureWidthD;
        clap.apertureHeightNumerator = apertureHeightN;
        clap.apertureHeightDenominator = apertureHeightD;
        clap.horizOffsetNumerator = horizOffN;
        clap.horizOffsetDenominator = horizOffD;
        clap.vertOffsetNumerator = vertOffN;
        clap.vertOffsetDenominator = vertOffD;
        return clap;
    }

    @Override
    public void parse(ByteBuffer is) {
        this.apertureWidthNumerator = is.getInt();
        this.apertureWidthDenominator = is.getInt();

        this.apertureHeightNumerator = is.getInt();
        this.apertureHeightDenominator = is.getInt();

        this.horizOffsetNumerator = is.getInt();
        this.horizOffsetDenominator = is.getInt();

        this.vertOffsetNumerator = is.getInt();
        this.vertOffsetDenominator = is.getInt();
    }

    public static String fourcc() {
        return "clap";
    }


    @Override
    public void doWrite(ByteBuffer out) {
        out.putInt(this.apertureWidthNumerator);
        out.putInt(this.apertureWidthDenominator);

        out.putInt(this.apertureHeightNumerator);
        out.putInt(this.apertureHeightDenominator);

        out.putInt(this.horizOffsetNumerator);
        out.putInt(this.horizOffsetDenominator);

        out.putInt(this.vertOffsetNumerator);
        out.putInt(this.vertOffsetDenominator);
    }

    @Override
    public int estimateSize() {
        return 32 + 8;
    }
}
