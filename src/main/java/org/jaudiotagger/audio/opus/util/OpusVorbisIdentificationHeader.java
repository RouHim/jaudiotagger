package org.jaudiotagger.audio.opus.util;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.opus.OpusHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * - Magic signature: "OpusHead" (64 bits)
 * - Version number (8 bits unsigned): 0x01 for this spec
 * - Channel count 'c' (8 bits unsigned): MUST be > 0
 * - Pre-skip (16 bits unsigned, little endian)
 * - Input sample rate (32 bits unsigned, little endian): informational only
 * - Output gain (16 bits, little endian, signed Q7.8 in dB) to apply when
 * decoding
 * - Channel mapping family (8 bits unsigned)
 * --  0 = one stream: mono or L,R stereo
 * --  1 = channels in vorbis spec order: mono or L,R stereo or ... or FL,C,FR,RL,RR,LFE, ...
 * --  2..254 = reserved (treat as 255)
 * --  255 = no defined channel meaning
 * If channel mapping family > 0
 * - Stream count 'N' (8 bits unsigned): MUST be > 0
 * - Two-channel stream count 'M' (8 bits unsigned): MUST satisfy M <= N, M+N <= 255
 * - Channel mapping (8*c bits)
 * -- one stream index (8 bits unsigned) per channel (255 means silent throughout the file)
 */
public class OpusVorbisIdentificationHeader {

    private static final Logger logger = LoggerFactory.getLogger("org.jaudiotagger.audio.ogg.opus");

    private boolean isValid = false;

    private byte vorbisVersion;
    private byte audioChannels;
    private short preSkip;
    private int audioSampleRate;
    private short outputGain;
    private byte channelMapFamily;

    private byte streamCount;
    private byte streamCountTwoChannel;
    private byte[] channelMap;

    private int bitrateMinimal;
    private int bitrateNominal;
    private int bitrateMaximal;


    public OpusVorbisIdentificationHeader(byte[] vorbisData) {
        decodeHeader(vorbisData);
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public byte getVorbisVersion() {
        return vorbisVersion;
    }

    public void setVorbisVersion(byte vorbisVersion) {
        this.vorbisVersion = vorbisVersion;
    }

    public byte getAudioChannels() {
        return audioChannels;
    }

    public void setAudioChannels(byte audioChannels) {
        this.audioChannels = audioChannels;
    }

    public short getPreSkip() {
        return preSkip;
    }

    public void setPreSkip(short preSkip) {
        this.preSkip = preSkip;
    }

    public int getAudioSampleRate() {
        return audioSampleRate;
    }

    public void setAudioSampleRate(int audioSampleRate) {
        this.audioSampleRate = audioSampleRate;
    }

    public short getOutputGain() {
        return outputGain;
    }

    public void setOutputGain(short outputGain) {
        this.outputGain = outputGain;
    }

    public byte getChannelMapFamily() {
        return channelMapFamily;
    }

    public void setChannelMapFamily(byte channelMapFamily) {
        this.channelMapFamily = channelMapFamily;
    }

    public byte getStreamCount() {
        return streamCount;
    }

    public void setStreamCount(byte streamCount) {
        this.streamCount = streamCount;
    }

    public byte getStreamCountTwoChannel() {
        return streamCountTwoChannel;
    }

    public void setStreamCountTwoChannel(byte streamCountTwoChannel) {
        this.streamCountTwoChannel = streamCountTwoChannel;
    }

    public byte[] getChannelMap() {
        return channelMap;
    }

    public void setChannelMap(byte[] channelMap) {
        this.channelMap = channelMap;
    }

    public int getBitrateMinimal() {
        return bitrateMinimal;
    }

    public void setBitrateMinimal(int bitrateMinimal) {
        this.bitrateMinimal = bitrateMinimal;
    }

    public int getBitrateNominal() {
        return bitrateNominal;
    }

    public void setBitrateNominal(int bitrateNominal) {
        this.bitrateNominal = bitrateNominal;
    }

    public int getBitrateMaximal() {
        return bitrateMaximal;
    }

    public void setBitrateMaximal(int bitrateMaximal) {
        this.bitrateMaximal = bitrateMaximal;
    }

    private void decodeHeader(byte[] b) {
        ByteBuffer buf = ByteBuffer.wrap(b);
        String oggHead = Utils.readString(buf, 8);

        if (oggHead.equals(OpusHeader.HEAD_CAPTURE_PATTERN)) {
            this.vorbisVersion = buf.get();
            this.audioChannels = buf.get();
            this.preSkip = buf.getShort();
            this.audioSampleRate = buf.getInt();
            this.outputGain = buf.getShort();
            this.channelMapFamily = buf.get();

            if (channelMapFamily > 0) {
                this.streamCount = buf.get();
                this.streamCountTwoChannel = buf.get();

                this.channelMap = new byte[audioChannels];
                for (int i = 0; i < audioChannels; i++) {
                    this.channelMap[i] = buf.get();
                }
            }

            isValid = true;
        }
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OpusVorbisIdentificationHeader{");
        sb.append("isValid=").append(isValid);
        sb.append(", vorbisVersion=").append(vorbisVersion);
        sb.append(", audioChannels=").append(audioChannels);
        sb.append(", preSkip=").append(preSkip);
        sb.append(", audioSampleRate=").append(audioSampleRate);
        sb.append(", outputGain=").append(outputGain);
        sb.append(", channelMapFamily=").append(channelMapFamily);
        sb.append(", streamCount=").append(streamCount);
        sb.append(", streamCountTwoChannel=").append(streamCountTwoChannel);
        sb.append(", channelMap=");
        if (channelMap == null) sb.append("null");
        else {
            sb.append('[');
            for (int i = 0; i < channelMap.length; ++i)
                sb.append(i == 0 ? "" : ", ").append(channelMap[i]);
            sb.append(']');
        }
        sb.append(", bitrateMinimal=").append(bitrateMinimal);
        sb.append(", bitrateNominal=").append(bitrateNominal);
        sb.append(", bitrateMaximal=").append(bitrateMaximal);
        sb.append('}');
        return sb.toString();
    }
}

