package org.jaudiotagger.audio.mp4;

import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jcodec.containers.mp4.boxes.EsdsBox;

/**
 * Store some additional attributes useful for Mp4s
 */
public class Mp4AudioHeader extends GenericAudioHeader {
    private EsdsBox.Kind kind;
    private EsdsBox.AudioProfile profile;
    private String brand;


    public void setKind(EsdsBox.Kind kind) {
        this.kind = kind;
    }

    /**
     * @return kind
     */
    public EsdsBox.Kind getKind() {
        return kind;
    }

    /**
     * The key for the profile
     *
     * @param profile
     */
    public void setProfile(EsdsBox.AudioProfile profile) {
        this.profile = profile;
    }

    /**
     * @return audio profile
     */
    public EsdsBox.AudioProfile getProfile() {
        return profile;
    }

    /**
     * @param brand
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }


    /**
     * @return brand
     */
    public String getBrand() {
        return brand;
    }


}
