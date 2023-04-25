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

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotReadVideoException;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.logging.ErrorMessage;
import org.jcodec.containers.mp4.MP4Util;
import org.jcodec.containers.mp4.boxes.*;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Logger;

/**
 * Read audio info from file.
 * <p>
 * <p>
 * The info is held in the mvdh and mdhd fields as shown below
 * <pre>
 * |--- ftyp
 * |--- moov
 * |......|
 * |......|----- mvdh
 * |......|----- trak
 * |...............|----- mdia
 * |.......................|---- mdhd
 * |.......................|---- minf
 * |..............................|---- smhd
 * |..............................|---- stbl
 * |......................................|--- stsd
 * |.............................................|--- mp4a
 * |......|----- udta
 * |
 * |--- mdat
 * </pre>
 */
public class Mp4InfoReader {
    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.mp4.atom");

    public GenericAudioHeader read(RandomAccessFile raf) throws CannotReadException, IOException {
        MP4Util.Movie mp4 = MP4Util.parseFullMovieChannel(raf.getChannel());
        Mp4AudioHeader info = new Mp4AudioHeader();

        //File Identification
        if (mp4 == null || mp4.getFtyp() == null) {
            throw new CannotReadException(ErrorMessage.MP4_FILE_NOT_CONTAINER.getMsg());
        }
        info.setBrand(mp4.getFtyp().getMajorBrand());

        //Get to the facts everything we are interested in is within the moov box, so just load data from file
        //once so no more file I/O needed
        MovieBox moov = mp4.getMoov();
        if (moov == null) {
            throw new CannotReadException(ErrorMessage.MP4_FILE_NOT_AUDIO.getMsg());
        }

        //Level 2-Searching for "mvhd" somewhere within "moov", we make a slice after finding header
        //so all get() methods will be relative to mvdh positions
        MovieHeaderBox mvhd = moov.getMovieHeader();
        if (mvhd == null) {
            throw new CannotReadException(ErrorMessage.MP4_FILE_NOT_AUDIO.getMsg());
        }
        info.setPreciseLength(mvhd.getDuration() / mvhd.getTimescale());

        //Level 2-Searching for "trak" within "moov"
        if (moov.getAudioTracks() == null || moov.getAudioTracks().isEmpty()) {
            throw new CannotReadException(ErrorMessage.MP4_FILE_NOT_AUDIO.getMsg());
        }

        //Level 3-Searching for "mdia" within "trak"
        TrakBox trak = moov.getAudioTracks().get(0);
        if (trak == null || trak.getMdia() == null) {
            throw new CannotReadException(ErrorMessage.MP4_FILE_NOT_AUDIO.getMsg());
        }
        //Level 4-Searching for "mdhd" within "mdia"
        MediaHeaderBox mdhd = NodeBox.findFirstPath(trak, MediaHeaderBox.class, Box.path("mdia.mdhd"));
        if (mdhd == null) {
            throw new CannotReadException(ErrorMessage.MP4_FILE_NOT_AUDIO.getMsg());
        }
        info.setSamplingRate(mdhd.getTimescale());

        //Level 4-Searching for "minf" within "mdia"
        if (trak.getMdia().getMinf() == null) {
            throw new CannotReadException(ErrorMessage.MP4_FILE_NOT_AUDIO.getMsg());
        }

        //Level 5-Searching for "smhd" within "minf"
        //Only an audio track would have a smhd frame
        SoundMediaHeaderBox smhd = NodeBox.findFirstPath(trak.getMdia().getMinf(), SoundMediaHeaderBox.class, Box.path("smhd"));
        if (smhd == null) {
            VideoMediaHeaderBox vmhd = NodeBox.findFirstPath(trak.getMdia().getMinf(), VideoMediaHeaderBox.class, Box.path("vmhd"));
            //try easy check to confirm that it is video
            if (vmhd != null) {
                throw new CannotReadVideoException(ErrorMessage.MP4_FILE_IS_VIDEO.getMsg());
            } else {
                throw new CannotReadException(ErrorMessage.MP4_FILE_NOT_AUDIO.getMsg());
            }
        }

        //Level 5-Searching for "stbl within "minf"
        NodeBox stbl = trak.getMdia().getMinf().getStbl();
        if (stbl == null) {
            throw new CannotReadException(ErrorMessage.MP4_FILE_NOT_AUDIO.getMsg());
        }

        //Level 6-Searching for "stsd within "stbl" and process it direct data, dont think these are mandatory so dont throw
        //exception if unable to find
        SampleDescriptionBox stsd = trak.getStsd();
        if (stsd != null) {
            ///Level 7-Searching for "mp4a within "stsd"
            EsdsBox esds = NodeBox.findFirstPath(stsd, EsdsBox.class, Box.path("mp4a.esds"));
            EsdsBox esdsProtected = NodeBox.findFirstPath(stsd, EsdsBox.class, Box.path("drms.esds"));
            AudioSampleEntry alac = NodeBox.findFirstPath(stsd, AudioSampleEntry.class, Box.path("alac"));
            if (esds != null) {
                //Set Bitrate in kbps
                info.setBitRate(esds.getAvgBitrate() / 1000);

                //Set Number of Channels
                info.setChannelNumber(esds.getNumberOfChannels());
                info.setKind(esds.getKind());
                info.setProfile(esds.getAudioProfile());
                info.setEncodingType(EncoderType.AAC.getDescription());
            } else if (esdsProtected != null) {
                //Level 7 -Searching for drms within stsd instead (m4p files)
                //Set Bitrate in kbps
                info.setBitRate(esdsProtected.getAvgBitrate() / 1000);

                //Set Number of Channels
                info.setChannelNumber(esdsProtected.getNumberOfChannels());

                info.setKind(esdsProtected.getKind());
                info.setProfile(esdsProtected.getAudioProfile());

                info.setEncodingType(EncoderType.DRM_AAC.getDescription());
            } else if (alac != null) {
                info.setBitRate((int) (alac.getSampleRate() / alac.getSampleSize() / 8));
                info.setBitsPerSample(alac.getSampleSize());

                //Set Number of Channels
                info.setChannelNumber(alac.getChannelCount());
                info.setKind(EsdsBox.Kind.MPEG4_AUDIO);
                info.setProfile(EsdsBox.AudioProfile.MAIN);
                info.setEncodingType(EncoderType.APPLE_LOSSLESS.getDescription());
            }

        }

        //Level 6-Searching for "stco within "stbl" to get size of audio data
        if (trak.getStco() != null && trak.getStco().getChunkOffsets().length > 0) {
            long[] offsets = trak.getStco().getChunkOffsets();
            info.setAudioDataStartPosition(offsets[0]);
            info.setAudioDataEndPosition(raf.length());
            info.setAudioDataLength(raf.length() - offsets[0]);
        }

        //Set default channels if couldn't calculate it
        if (info.getChannelNumber() == -1) {
            info.setChannelNumber(2);
        }

        //Set default bitrate if couldnt calculate it
        if (info.getBitRateAsNumber() == -1) {
            info.setBitRate(128);
        }

        //Set default bits per sample if couldn't calculate it
        if (info.getBitsPerSample() == -1) {
            info.setBitsPerSample(16);
        }

        //This is the most likely option if cant find a match
        if (info.getEncodingType().equals("")) {
            info.setEncodingType(EncoderType.AAC.getDescription());
        }

        logger.config(info.toString());

        //Level 2-Searching for others "trak" within "moov", if we find any traks containing video
        //then reject it if no track if not video then we allow it because many encoders seem to contain all sorts
        //of stuff that you wouldn't expect in an audio track
        for (TrakBox tbox : moov.getTracks()) {
            if (tbox.isVideo()) {
                throw new CannotReadVideoException(ErrorMessage.MP4_FILE_IS_VIDEO.getMsg());
            }
        }

        //Build AtomTree to ensure it is valid, this means we can detect any problems early on
        return info;
    }


}
