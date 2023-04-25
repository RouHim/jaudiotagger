package org.jaudiotagger.audio;

import org.jaudiotagger.audio.dsf.Dsf;
import org.jaudiotagger.audio.real.RealTag;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.aiff.AiffTag;
import org.jaudiotagger.tag.asf.AsfTag;
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;
import org.jaudiotagger.tag.wav.WavTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Files formats currently supported by Library.
 * Each enum value is associated with a file suffix (extension).
 */
public enum SupportedFileFormat {
    OGG("ogg") {
        @Override
        public Tag createDefaultTag() {
            return VorbisCommentTag.createNewTag();
        }
    },
    MP3("mp3") {
        @Override
        public Tag createDefaultTag() {
            return TagOptionSingleton.createDefaultID3Tag();
        }
    },
    FLAC("flac") {
        @Override
        public Tag createDefaultTag() {
            return new FlacTag(VorbisCommentTag.createNewTag(), new ArrayList<>());
        }
    },
    MP4("mp4") {
        @Override
        public Tag createDefaultTag() {
            return new Mp4Tag();
        }
    },
    M4A("m4a") {
        @Override
        public Tag createDefaultTag() {
            return new Mp4Tag();
        }
    },
    M4P("m4p") {
        @Override
        public Tag createDefaultTag() {
            return new Mp4Tag();
        }
    },
    WMA("wma") {
        @Override
        public Tag createDefaultTag() {
            return new AsfTag();
        }
    },
    WAV("wav") {
        @Override
        public Tag createDefaultTag() {
            return new WavTag(TagOptionSingleton.getInstance().getWavOptions());
        }
    },
    RA("ra") {
        @Override
        public Tag createDefaultTag() {
            return new RealTag();
        }
    },
    RM("rm") {
        @Override
        public Tag createDefaultTag() {
            return new RealTag();
        }
    },
    M4B("m4b") {
        @Override
        public Tag createDefaultTag() {
            return new Mp4Tag();
        }
    },
    AIF("aif") {
        @Override
        public Tag createDefaultTag() {
            return new AiffTag();
        }
    },
    AIFF("aiff") {
        @Override
        public Tag createDefaultTag() {
            return new AiffTag();
        }
    },
    AIFC("aifc") {
        @Override
        public Tag createDefaultTag() {
            return new AiffTag();
        }
    },
    DSF("dsf") {
        @Override
        public Tag createDefaultTag() {
            return Dsf.createDefaultTag();
        }
    },
    OPUS("opus") {
        @Override
        public Tag createDefaultTag() {
            return VorbisCommentTag.createNewTag();
        }
    },
    UNKNOWN("") {
        @Override
        public Tag createDefaultTag() {
            throw new RuntimeException("Unable to create default tag for this file format:" + name());
        }
    };

    private static final Map<String, SupportedFileFormat> extensionMap;

    static {
        final SupportedFileFormat[] values = values();
        extensionMap = new HashMap<>(values.length);
        for (int i = 0; i < values.length; i++) {
            final SupportedFileFormat format = values[i];
            extensionMap.put(format.filesuffix, format);
        }
    }

    private final String filesuffix;

    /**
     * Get the format from the file extension.
     *
     * @param fileExtension file extension
     * @return the format for the extension or UNKNOWN if the extension not recognized
     */
    public static SupportedFileFormat fromExtension(String fileExtension) {
        if (fileExtension == null) {
            return UNKNOWN;
        }
        SupportedFileFormat format = extensionMap.get(fileExtension.toLowerCase(Locale.ROOT));
        return format == null ? UNKNOWN : format;
    }

    /**
     * Constructor for internal use by this enum.
     */
    SupportedFileFormat(String filesuffix) {
        this.filesuffix = filesuffix.toLowerCase(Locale.ROOT);  // ensure lowercase
    }

    /**
     * Returns the file suffix (lower case without initial .) associated with the format.
     */
    public String getFilesuffix() {
        return filesuffix;
    }

    /**
     * Create for this format
     *
     * @return the default tag for the given type
     * @throws RuntimeException if can't create the default tag
     */
    // TODO: 1/7/17 should we have a more specific type of runtime exception? RuntimeException is copied from legacy
    public abstract Tag createDefaultTag() throws RuntimeException;
}
