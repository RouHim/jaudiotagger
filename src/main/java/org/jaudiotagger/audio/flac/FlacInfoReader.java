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
package org.jaudiotagger.audio.flac;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.flac.metadatablock.BlockType;
import org.jaudiotagger.audio.flac.metadatablock.MetadataBlockDataStreamInfo;
import org.jaudiotagger.audio.flac.metadatablock.MetadataBlockHeader;
import org.jaudiotagger.audio.generic.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * Read info from Flac file
 */
public class FlacInfoReader {
    // Logger Object
    private static final Logger logger = LoggerFactory.getLogger("org.jaudiotagger.audio.flac");


    public FlacAudioHeader read(FileChannel fc, final String fileName) throws CannotReadException, IOException {
        logger.debug(fileName + ":start");
        FlacStreamReader flacStream = new FlacStreamReader(fc, fileName + " ");
        flacStream.findStream();

        MetadataBlockDataStreamInfo mbdsi = null;
        boolean isLastBlock = false;

        //Search for StreamInfo Block, but even after we found it we still have to continue through all
        //the metadata blocks so that we can find the start of the audio frames which we need to calculate
        //the bitrate
        while (!isLastBlock) {
            MetadataBlockHeader mbh = MetadataBlockHeader.readHeader(fc);
            logger.info(fileName + " " + mbh);
            if (mbh.getBlockType() == BlockType.STREAMINFO) {
                mbdsi = new MetadataBlockDataStreamInfo(mbh, fc);
                if (!mbdsi.isValid()) {
                    throw new CannotReadException(fileName + ":FLAC StreamInfo not valid");
                }
            } else {
                fc.position(fc.position() + mbh.getDataLength());
            }
            isLastBlock = mbh.isLastBlock();
        }

        //Audio continues from this point to end of file (normally - TODO might need to allow for an ID3v1 tag at file end ?)
        long streamStart = fc.position();

        if (mbdsi == null) {
            throw new CannotReadException(fileName + ":Unable to find Flac StreamInfo");
        }

        FlacAudioHeader info = new FlacAudioHeader();
        info.setNoOfSamples(mbdsi.getNoOfSamples());
        info.setPreciseLength(mbdsi.getPreciseLength());
        info.setChannelNumber(mbdsi.getNoOfChannels());
        info.setSamplingRate(mbdsi.getSamplingRate());
        info.setBitsPerSample(mbdsi.getBitsPerSample());
        info.setEncodingType(mbdsi.getEncodingType());
        info.setLossless(true);
        info.setMd5(mbdsi.getMD5Signature());
        info.setAudioDataLength(fc.size() - streamStart);
        info.setAudioDataStartPosition(streamStart);
        info.setAudioDataEndPosition(fc.size());
        info.setBitRate(computeBitrate(info.getAudioDataLength(), mbdsi.getPreciseLength()));
        return info;
    }

    private int computeBitrate(long size, float length) {
        return (int) ((size / Utils.KILOBYTE_MULTIPLIER) * Utils.BITS_IN_BYTE_MULTIPLIER / length);
    }

    /**
     * Count the number of metadatablocks, useful for debugging
     *
     * @param f
     * @return
     * @throws CannotReadException
     * @throws IOException
     */
    public int countMetaBlocks(File f) throws CannotReadException, IOException {
        try (FileChannel fc = new RandomAccessFile(f, "r").getChannel()) {
            FlacStreamReader flacStream = new FlacStreamReader(fc, f.getAbsolutePath() + " ");
            flacStream.findStream();

            boolean isLastBlock = false;

            int count = 0;
            while (!isLastBlock) {
                MetadataBlockHeader mbh = MetadataBlockHeader.readHeader(fc);
                logger.debug(f + ":Found block:" + mbh.getBlockType());
                fc.position(fc.position() + mbh.getDataLength());
                isLastBlock = mbh.isLastBlock();
                count++;
            }
            return count;
        }
    }
}
