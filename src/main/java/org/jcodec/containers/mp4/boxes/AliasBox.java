package org.jcodec.containers.mp4.boxes;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 *
 * @author The JCodec project
 */
public class AliasBox extends FullBox {
    public final static int DirectoryName = 0;
    public final static int DirectoryIDs = 1; // parent & higher directory ids
    // '/' is not counted, one
    // unsigned32 for each dir
    public final static int AbsolutePath = 2;
    public final static int AppleShareZoneName = 3;
    public final static int AppleShareServerName = 4;
    public final static int AppleShareUserName = 5;
    public final static int DriverName = 6;
    public final static int RevisedAppleShare = 9;
    public final static int AppleRemoteAccessDialup = 10;
    public final static int UNIXAbsolutePath = 18;
    public final static int UTF16AbsolutePath = 14;
    public final static int UFT16VolumeName = 15; // 26
    public final static int VolumeMountPoint = 19; // 1

    private String type;
    private short recordSize;
    private short version;
    private short kind;
    private String volumeName;
    private int volumeCreateDate;
    private short volumeSignature;
    private short volumeType;
    private int parentDirId;
    private String fileName;
    private int fileNumber;
    private int createdLocalDate;
    private String fileTypeName;
    private String creatorName;
    private short nlvlFrom;
    private short nlvlTo;
    private int volumeAttributes;
    private short fsId;
    private List<ExtraField> extra;

    public static String fourcc() {
        return "alis";
    }

    public static class ExtraField {
        short type;
        int len;
        byte[] data;

        public ExtraField(short type, int len, byte[] bs) {
            this.type = type;
            this.len = len;
            this.data = bs;
        }

        public String toString() {
            Charset charset = Charset.forName((type == 14 || type == 15)
                    ? TextEncoding.CHARSET_UTF_16
                    : TextEncoding.CHARSET_UTF_8);
            return new String(data, 0, len, charset);
        }
    }

    public AliasBox(Header atom) {
        super(atom);
    }

    public void parse(ByteBuffer is) {
        super.parse(is);
        if ((flags & 0x1) != 0) // self ref
            return;
        type = Utils.readFourBytesAsChars(is);
        recordSize = is.getShort();
        version = is.getShort();
        kind = is.getShort();
        volumeName = Utils.readPascalString(is);
        volumeCreateDate = is.getInt();
        volumeSignature = is.getShort();
        volumeType = is.getShort();
        parentDirId = is.getInt();
        fileName = Utils.readPascalString(is);
        fileNumber = is.getInt();
        createdLocalDate = is.getInt();
        fileTypeName = Utils.readFourBytesAsChars(is);
        creatorName = Utils.readFourBytesAsChars(is);
        nlvlFrom = is.getShort();
        nlvlTo = is.getShort();
        volumeAttributes = is.getInt();
        fsId = is.getShort();
        Utils.skip(is, 10);

        extra = new ArrayList<ExtraField>();
        while (true) {
            short type = is.getShort();
            if (type == -1)
                break;
            int len = is.getShort();
            byte[] bs = Utils.toArray(Utils.read(is, (len + 1) & 0xfffffffe));
            if (bs == null)
                break;
            extra.add(new ExtraField(type, len, bs));
        }
    }

    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);
        if ((flags & 0x1) != 0) // self ref
            return;
        out.put(type.getBytes(StandardCharsets.US_ASCII), 0, 4);
        out.putShort(recordSize);
        out.putShort(version);
        out.putShort(kind);
        Utils.writePascalString(out, volumeName);
        out.putInt(volumeCreateDate);
        out.putShort(volumeSignature);
        out.putShort(volumeType);
        out.putInt(parentDirId);
        Utils.writePascalString(out, fileName);
        out.putInt(fileNumber);
        out.putInt(createdLocalDate);
        out.put(fileTypeName.getBytes(StandardCharsets.US_ASCII), 0, 4);
        out.put(creatorName.getBytes(StandardCharsets.US_ASCII), 0, 4);
        out.putShort(nlvlFrom);
        out.putShort(nlvlTo);
        out.putInt(volumeAttributes);
        out.putShort(fsId);
        out.put(new byte[10]);
        for (ExtraField extraField : extra) {
            out.putShort(extraField.type);
            out.putShort((short) extraField.len);
            out.put(extraField.data);
        }
        out.putShort((short) -1);
        out.putShort((short) 0);
    }

    @Override
    public int estimateSize() {
        int sz = 166;
        if ((flags & 0x1) == 0) {
            for (ExtraField extraField : extra) {
                sz += 4 + extraField.data.length;
            }
        }
        return 12 + sz;
    }

    public int getRecordSize() {
        return recordSize;
    }

    public String getFileName() {
        return fileName;
    }

    public List<ExtraField> getExtras() {
        return extra;
    }

    public ExtraField getExtra(int type) {
        for (ExtraField extraField : extra) {
            if (extraField.type == type)
                return extraField;
        }
        return null;
    }

    public boolean isSelfRef() {
        return (flags & 0x1) != 0;
    }

    public static AliasBox createSelfRef() {
        AliasBox alis = new AliasBox(new Header(fourcc()));
        alis.setFlags(1);
        return alis;
    }

    public String getUnixPath() {
        ExtraField extraField = getExtra(AliasBox.UNIXAbsolutePath);
        return extraField == null ? null : "/" + extraField;
    }
}
