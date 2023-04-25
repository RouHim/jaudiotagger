package org.jcodec.containers.mp4;

import org.jcodec.containers.mp4.boxes.AudioSampleEntry;
import org.jcodec.containers.mp4.boxes.SampleEntry;
import org.jcodec.containers.mp4.boxes.TimecodeSampleEntry;

public class SampleBoxes extends Boxes {

    public SampleBoxes() {
        clear();

        override("ac-3", AudioSampleEntry.class);
        override("cac3", AudioSampleEntry.class);
        override("ima4", AudioSampleEntry.class);
        override("aac ", AudioSampleEntry.class);
        override("celp", AudioSampleEntry.class);
        override("hvxc", AudioSampleEntry.class);
        override("twvq", AudioSampleEntry.class);
        override(".mp1", AudioSampleEntry.class);
        override(".mp2", AudioSampleEntry.class);
        override("midi", AudioSampleEntry.class);
        override("apvs", AudioSampleEntry.class);
        override("alac", AudioSampleEntry.class);
        override("aach", AudioSampleEntry.class);
        override("aacl", AudioSampleEntry.class);
        override("aace", AudioSampleEntry.class);
        override("aacf", AudioSampleEntry.class);
        override("aacp", AudioSampleEntry.class);
        override("aacs", AudioSampleEntry.class);
        override("samr", AudioSampleEntry.class);
        override("AUDB", AudioSampleEntry.class);
        override("ilbc", AudioSampleEntry.class);
        override(new String(new byte[]{0x6D, 0x73, 0x00, 0x11}), AudioSampleEntry.class);
        override(new String(new byte[]{0x6D, 0x73, 0x00, 0x31}), AudioSampleEntry.class);
        override("aes3", AudioSampleEntry.class);
        override("NONE", AudioSampleEntry.class);
        override("raw ", AudioSampleEntry.class);
        override("twos", AudioSampleEntry.class);
        override("sowt", AudioSampleEntry.class);
        override("MAC3 ", AudioSampleEntry.class);
        override("MAC6 ", AudioSampleEntry.class);
        override("ima4", AudioSampleEntry.class);
        override("fl32", AudioSampleEntry.class);
        override("fl64", AudioSampleEntry.class);
        override("in24", AudioSampleEntry.class);
        override("in32", AudioSampleEntry.class);
        override("ulaw", AudioSampleEntry.class);
        override("alaw", AudioSampleEntry.class);
        override("dvca", AudioSampleEntry.class);
        override("QDMC", AudioSampleEntry.class);
        override("QDM2", AudioSampleEntry.class);
        override("Qclp", AudioSampleEntry.class);
        override(".mp3", AudioSampleEntry.class);
        override("mp4a", AudioSampleEntry.class);
        override("lpcm", AudioSampleEntry.class);

        override("tmcd", TimecodeSampleEntry.class);
        override("time", TimecodeSampleEntry.class);

        override("c608", SampleEntry.class);
        override("c708", SampleEntry.class);
        override("text", SampleEntry.class);

        //found in gopro video files
        override("fdsc", SampleEntry.class);
    }
}