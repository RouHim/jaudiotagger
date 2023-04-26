/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Raphaël Slinckx <raphael@slinckx.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jaudiotagger.audio.mp4;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.mp4.Mp4TagCreator;
import org.jcodec.containers.mp4.MP4Util;
import org.jcodec.containers.mp4.boxes.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;


/**
 * Writes metadata from mp4, the metadata tags are held under the {@code ilst} atom as shown below, (note all free atoms are
 * optional).
 * <p/>
 * When writing changes the size of all the atoms up to {@code ilst} has to be recalculated, then if the size of
 * the metadata is increased the size of the free atom (below {@code meta}) should be reduced accordingly or vice versa.
 * If the size of the metadata has increased by more than the size of the {@code free} atom then the size of {@code meta},
 * {@code udta} and {@code moov} should be recalculated and the top level {@code free} atom reduced accordingly.
 * If there is not enough space even if using both of the {@code free} atoms, then the {@code mdat} atom has to be
 * shifted down accordingly to make space, and the {@code stco} atoms have to have their offsets to {@code mdat}
 * chunks table adjusted accordingly.
 * <p/>
 * Exceptions are that the meta/udta/ilst do not currently exist, in which udta/meta/ilst are created. Note it is valid
 * to have meta/ilst without udta but this is less common so we always try to write files according to the Apple/iTunes
 * specification. *
 * <p/>
 * <p/>
 * <pre>
 * |--- ftyp
 * |--- free
 * |--- moov
 * |......|
 * |......|----- mvdh
 * |......|----- trak (there may be more than one trak atom, e.g. Native Instrument STEM files)
 * |......|.......|
 * |......|.......|-- tkhd
 * |......|.......|-- mdia
 * |......|............|
 * |......|............|-- mdhd
 * |......|............|-- hdlr
 * |......|............|-- minf
 * |......|.................|
 * |......|.................|-- smhd
 * |......|.................|-- dinf
 * |......|.................|-- stbl
 * |......|......................|
 * |......|......................|-- stsd
 * |......|......................|-- stts
 * |......|......................|-- stsc
 * |......|......................|-- stsz
 * |......|......................|-- stco (important! may need to be adjusted.)
 * |......|
 * |......|----- udta
 * |..............|
 * |..............|-- meta
 * |....................|
 * |....................|-- hdlr
 * |....................|-- ilst
 * |....................|.. ..|
 * |....................|.....|---- @nam (Optional for each metadatafield)
 * |....................|.....|.......|-- data
 * |....................|.....|....... ecetera
 * |....................|.....|---- ---- (Optional for reverse dns field)
 * |....................|.............|-- mean
 * |....................|.............|-- name
 * |....................|.............|-- data
 * |....................|................ ecetera
 * |....................|-- free
 * |--- free
 * |--- mdat
 * </pre>
 */
public class Mp4TagWriter {
    // Logger Object
    private static final Logger logger = LoggerFactory.getLogger("org.jaudiotagger.tag.mp4");

    private final Mp4TagCreator tc = new Mp4TagCreator();


    /**
     * Write tag to {@code rafTemp} file.
     *
     * @param raf     current file
     * @param rafTemp temporary file for writing
     * @throws CannotWriteException
     * @throws IOException
     */
    public void write(AudioFile af, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotWriteException, IOException {
        logger.debug("Started writing tag data");
        try (FileChannel fi = raf.getChannel();
             FileChannel fo = rafTemp.getChannel()) {
            MP4Util.Movie mp4 = MP4Util.parseFullMovieChannel(fi);
            writeTagBox(af.getTag(), mp4);

            MovieExtendsBox mvex = NodeBox.findFirst(mp4.getMoov(), MovieExtendsBox.class, MovieExtendsBox.fourcc());
            if (mvex != null) {
                // segmented file
                fo.position(0);
                MP4Util.writeFullMovie(fo, mp4);

                // copy segments
                for (MP4Util.Atom atom : mp4.getOthers()) {
                    atom.copy(fi, fo);
                }
            } else {
                String path = af.getFile().getCanonicalPath();
                for (TrakBox tb : mp4.getMoov().getTracks()) {
                    // flattern only works with data refs
                    tb.setDataRef("file://" + path);
                }

                // non-segmented file, need to keep chunk offsets
                fi.position(0);
                Utils.copy(fi, fo, raf.length());

                fi.position(0);
                fo.position(0);
                new ReplaceMP4Editor().modifyOrReplace(fi, fo, mp4.getMoov());
            }
        }
    }

    private void writeTagBox(Tag tag, MP4Util.Movie mp4) throws java.io.UnsupportedEncodingException {
        IListBox ilst = tc.convert(tag);

        UdtaBox udta = NodeBox.findFirst(mp4.getMoov(), UdtaBox.class, UdtaBox.fourcc());
        if (udta == null) {
            udta = UdtaBox.createUdtaBox();
            mp4.getMoov().add(udta);
        }

        UdtaMetaBox meta = NodeBox.findFirst(udta, UdtaMetaBox.class, UdtaMetaBox.fourcc());
        if (meta == null) {
            meta = UdtaMetaBox.createUdtaMetaBox();
            udta.add(meta);
        }
        meta.replace(IListBox.fourcc(), ilst);
    }

    /**
     * Delete the tag.
     * <p/>
     * <p>This is achieved by writing an empty {@code ilst} atom.
     *
     * @param raf
     * @param rafTemp
     * @throws IOException
     */
    public void delete(RandomAccessFile raf, RandomAccessFile rafTemp) throws IOException {
        try (FileChannel fi = raf.getChannel()) {
            MP4Util.Movie mp4 = MP4Util.parseFullMovieChannel(fi);
            mp4.getMoov().removeChildren(new String[]{UdtaBox.fourcc()});

            new InplaceMP4Editor().modify(fi, mp4.getMoov());
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }
}
