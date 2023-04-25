package org.jaudiotagger.audio.opus;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.audio.ogg.util.OggPageHeader;
import org.jaudiotagger.audio.opus.util.OpusVorbisIdentificationHeader;
import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Read encoding info, only implemented for vorbis streams
 */
public class OpusInfoReader {

    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.opus.atom");

    public GenericAudioHeader read(RandomAccessFile raf) throws CannotReadException, IOException {
        long start = raf.getFilePointer();
        GenericAudioHeader info = new GenericAudioHeader();
        logger.fine("Started");

        //Check start of file does it have Ogg pattern
        byte[] b = new byte[OggPageHeader.CAPTURE_PATTERN.length];
        raf.read(b);
        if (!(Arrays.equals(b, OggPageHeader.CAPTURE_PATTERN))) {
            raf.seek(0);
            if (AbstractID3v2Tag.isId3Tag(raf)) {
                raf.read(b);
                if ((Arrays.equals(b, OggPageHeader.CAPTURE_PATTERN))) {
                    start = raf.getFilePointer();
                }
            } else {
                throw new CannotReadException(ErrorMessage.OGG_HEADER_CANNOT_BE_FOUND.getMsg(new String(b)));
            }
        }
        raf.seek(start);

        //1st page = Identification Header
        OggPageHeader pageHeader = OggPageHeader.read(raf);
        byte[] vorbisData = new byte[pageHeader.getPageLength()];

        raf.read(vorbisData);
        OpusVorbisIdentificationHeader opusIdHeader = new OpusVorbisIdentificationHeader(vorbisData);

        //Map to generic encodingInfo
        info.setChannelNumber(opusIdHeader.getAudioChannels());
        info.setSamplingRate(opusIdHeader.getAudioSampleRate());
        info.setEncodingType("Opus Vorbis 1.0");

        // find last Opus Header
        OggPageHeader last = lastValidHeader(raf);
        if (last == null) {
            throw new CannotReadException("Opus file contains ID and Comment headers but no audio content");
        }

        info.setNoOfSamples(last.getAbsoluteGranulePosition() - opusIdHeader.getPreSkip());
        info.setPreciseLength(info.getNoOfSamples() / 48000D);

        return info;
    }

    private OggPageHeader lastValidHeader(RandomAccessFile raf) throws IOException {
        OggPageHeader best = null;
        while (true) {
            try {
                OggPageHeader candidate = OggPageHeader.read(raf);
                raf.seek(raf.getFilePointer() + candidate.getPageLength());
                if (candidate.isValid() && !candidate.isLastPacketIncomplete()) {
                    best = candidate;
                }
            } catch (CannotReadException ignored) {
                break;
            }
        }

        return best;
    }
}

