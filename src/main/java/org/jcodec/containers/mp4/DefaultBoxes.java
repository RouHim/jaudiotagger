package org.jcodec.containers.mp4;

import org.jcodec.containers.mp4.boxes.*;
import org.jcodec.containers.mp4.boxes.Box.LeafBox;

public class DefaultBoxes extends Boxes {
    public DefaultBoxes() {
        super();
        mappings.put(MovieExtendsBox.fourcc(), MovieExtendsBox.class);
        mappings.put(MovieExtendsHeaderBox.fourcc(), MovieExtendsHeaderBox.class);
        mappings.put(SegmentIndexBox.fourcc(), SegmentIndexBox.class);
        mappings.put(SegmentTypeBox.fourcc(), SegmentTypeBox.class);
        mappings.put(TrackExtendsBox.fourcc(), TrackExtendsBox.class);
        mappings.put(VideoMediaHeaderBox.fourcc(), VideoMediaHeaderBox.class);
        mappings.put(FileTypeBox.fourcc(), FileTypeBox.class);
        mappings.put(MovieBox.fourcc(), MovieBox.class);
        mappings.put(MovieHeaderBox.fourcc(), MovieHeaderBox.class);
        mappings.put(TrakBox.fourcc(), TrakBox.class);
        mappings.put(TrackHeaderBox.fourcc(), TrackHeaderBox.class);
        mappings.put("edts", NodeBox.class);
        mappings.put(EditListBox.fourcc(), EditListBox.class);
        mappings.put(MediaBox.fourcc(), MediaBox.class);
        mappings.put(MediaHeaderBox.fourcc(), MediaHeaderBox.class);
        mappings.put(MediaInfoBox.fourcc(), MediaInfoBox.class);
        mappings.put(HandlerBox.fourcc(), HandlerBox.class);
        mappings.put(DataInfoBox.fourcc(), DataInfoBox.class);
        mappings.put("stbl", NodeBox.class);
        mappings.put(SampleDescriptionBox.fourcc(), SampleDescriptionBox.class);
        mappings.put(TimeToSampleBox.fourcc(), TimeToSampleBox.class);
        mappings.put(SyncSamplesBox.STSS, SyncSamplesBox.class);
        mappings.put(PartialSyncSamplesBox.STPS, PartialSyncSamplesBox.class);
        mappings.put(SampleToChunkBox.fourcc(), SampleToChunkBox.class);
        mappings.put(SampleSizesBox.fourcc(), SampleSizesBox.class);
        mappings.put(ChunkOffsetsBox.fourcc(), ChunkOffsetsBox.class);
        mappings.put("keys", KeysBox.class);
        mappings.put(IListBox.fourcc(), IListBox.class);
        mappings.put("moof", NodeBox.class);
        mappings.put("traf", NodeBox.class);
        mappings.put("mfra", NodeBox.class);
        mappings.put("skip", NodeBox.class);
        mappings.put(MetaBox.fourcc(), MetaBox.class);
        mappings.put(DataRefBox.fourcc(), DataRefBox.class);
        mappings.put("ipro", NodeBox.class);
        mappings.put("sinf", NodeBox.class);
        mappings.put(ChunkOffsets64Box.fourcc(), ChunkOffsets64Box.class);
        mappings.put(SoundMediaHeaderBox.fourcc(), SoundMediaHeaderBox.class);
        mappings.put("clip", NodeBox.class);
        mappings.put(ClipRegionBox.fourcc(), ClipRegionBox.class);
        mappings.put(LoadSettingsBox.fourcc(), LoadSettingsBox.class);
        mappings.put("tapt", NodeBox.class);
        mappings.put("gmhd", NodeBox.class);
        mappings.put("tmcd", Box.LeafBox.class);
        mappings.put("tref", NodeBox.class);
        mappings.put(ClearApertureBox.CLEF, ClearApertureBox.class);
        mappings.put(ProductionApertureBox.PROF, ProductionApertureBox.class);
        mappings.put(EncodedPixelBox.ENOF, EncodedPixelBox.class);
        mappings.put(GenericMediaInfoBox.fourcc(), GenericMediaInfoBox.class);
        mappings.put(TimecodeMediaInfoBox.fourcc(), TimecodeMediaInfoBox.class);
        mappings.put(UdtaBox.fourcc(), UdtaBox.class);
        mappings.put(CompositionOffsetsBox.fourcc(), CompositionOffsetsBox.class);
        mappings.put(NameBox.fourcc(), NameBox.class);
        mappings.put("mdta", LeafBox.class);

        mappings.put(MovieFragmentHeaderBox.fourcc(), MovieFragmentHeaderBox.class);
        mappings.put(TrackFragmentHeaderBox.fourcc(), TrackFragmentHeaderBox.class);
        mappings.put(MovieFragmentBox.fourcc(), MovieFragmentBox.class);
        mappings.put(TrackFragmentBox.fourcc(), TrackFragmentBox.class);
        mappings.put(TrackFragmentBaseMediaDecodeTimeBox.fourcc(), TrackFragmentBaseMediaDecodeTimeBox.class);
        mappings.put(TrunBox.fourcc(), TrunBox.class);
    }

}
