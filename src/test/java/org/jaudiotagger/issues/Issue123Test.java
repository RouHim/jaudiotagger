package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.tag.vorbiscomment.VorbisAlbumArtistReadOptions;
import org.jaudiotagger.tag.vorbiscomment.VorbisAlbumArtistSaveOptions;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentFieldKey;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class Issue123Test extends AbstractTestCase {

    @Test
    public void testWriteJRiverAlbumArtistOgg() {

        Exception ex = null;
        try {
            File testFile = copyAudioToTmp("test.ogg");
            AudioFile af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag());
            System.out.println(af.getTag());
            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "fred");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    "fred"
            );
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    ""
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "tom");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    "fred"
            );
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    "tom"
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_BOTH
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "jim");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    "jim"
            );
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    "jim"
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST
            );
            af.getTag().deleteField(FieldKey.ALBUM_ARTIST);
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    ""
            );
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    "jim"
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST
            );
            af.getTag().deleteField(FieldKey.ALBUM_ARTIST);
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    ""
            );
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    ""
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_BOTH
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "jim");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    "jim"
            );
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    "jim"
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_BOTH
            );
            af.getTag().deleteField(FieldKey.ALBUM_ARTIST);
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    ""
            );
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    ""
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "fred");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    "fred"
            );
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    ""
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "tom");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    "fred"
            );
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    "tom"
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST_AND_DELETE_JRIVER_ALBUMARTIST
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "freddy");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    "freddy"
            );
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    ""
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST_AND_DELETE_ALBUMARTIST
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "tommy");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    ""
            );
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    "tommy"
            );
        } catch (Exception e) {
            ex = e;
        }
        assertNull(ex);
    }

    @Test
    public void testWriteJRiverAlbumArtistFlac() {

        Exception ex = null;
        try {
            File testFile = copyAudioToTmp("test.flac");
            AudioFile af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag());
            System.out.println(af.getTag());
            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "fred");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    "fred"
            );
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    "Album Artist"
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "tom");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    "fred"
            );
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    "tom"
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_BOTH
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "jim");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    "jim"
            );
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    "jim"
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST
            );
            af.getTag().deleteField(FieldKey.ALBUM_ARTIST);
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    ""
            );
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    "jim"
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST
            );
            af.getTag().deleteField(FieldKey.ALBUM_ARTIST);
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    ""
            );
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    ""
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_BOTH
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "jim");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    "jim"
            );
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    "jim"
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_BOTH
            );
            af.getTag().deleteField(FieldKey.ALBUM_ARTIST);
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    ""
            );
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    ""
            );
            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "fred");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    "fred"
            );
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    ""
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "tom");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    "fred"
            );
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    "tom"
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST_AND_DELETE_JRIVER_ALBUMARTIST
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "freddy");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    "freddy"
            );
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    ""
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST_AND_DELETE_ALBUMARTIST
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "tommy");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    ""
            );
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    "tommy"
            );
        } catch (Exception e) {
            ex = e;
        }
        assertNull(ex);
    }

    @Test
    public void testReadJRiverAlbumArtistOgg() {

        Exception ex = null;
        try {
            File testFile = copyAudioToTmp("test.ogg");
            AudioFile af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag());
            System.out.println(af.getTag());
            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(
                    VorbisAlbumArtistReadOptions.READ_ALBUMARTIST
            );
            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "fred");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(af.getTag().getFirst(FieldKey.ALBUM_ARTIST), "fred");
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    "fred"
            );
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    ""
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(
                    VorbisAlbumArtistReadOptions.READ_JRIVER_ALBUMARTIST
            );
            assertEquals(af.getTag().getFirst(FieldKey.ALBUM_ARTIST), "");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(
                    VorbisAlbumArtistReadOptions.READ_ALBUMARTIST_THEN_JRIVER
            );
            assertEquals(af.getTag().getFirst(FieldKey.ALBUM_ARTIST), "fred");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(
                    VorbisAlbumArtistReadOptions.READ_JRIVER_THEN_ALBUMARTIST
            );
            assertEquals(af.getTag().getFirst(FieldKey.ALBUM_ARTIST), "fred");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "tom");
            af.commit();

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(
                    VorbisAlbumArtistReadOptions.READ_ALBUMARTIST
            );
            assertEquals(af.getTag().getFirst(FieldKey.ALBUM_ARTIST), "fred");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(
                    VorbisAlbumArtistReadOptions.READ_JRIVER_ALBUMARTIST
            );
            assertEquals(af.getTag().getFirst(FieldKey.ALBUM_ARTIST), "tom");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(
                    VorbisAlbumArtistReadOptions.READ_ALBUMARTIST_THEN_JRIVER
            );
            assertEquals(af.getTag().getFirst(FieldKey.ALBUM_ARTIST), "fred");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(
                    VorbisAlbumArtistReadOptions.READ_JRIVER_THEN_ALBUMARTIST
            );
            assertEquals(af.getTag().getFirst(FieldKey.ALBUM_ARTIST), "tom");
            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "fred");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    "fred"
            );
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    "tom"
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "tom");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    "fred"
            );
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    "tom"
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST_AND_DELETE_JRIVER_ALBUMARTIST
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "freddy");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    "freddy"
            );
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    ""
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST_AND_DELETE_ALBUMARTIST
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "tommy");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    ""
            );
            assertEquals(
                    ((VorbisCommentTag) af.getTag()).getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    "tommy"
            );
        } catch (Exception e) {
            ex = e;
        }
        assertNull(ex);
    }

    @Test
    public void testReadJRiverAlbumArtistFlac() {

        Exception ex = null;
        try {
            File testFile = copyAudioToTmp("test.flac");
            AudioFile af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag());
            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST
            );
            af.getTag().deleteField(FieldKey.ALBUM_ARTIST);
            af.commit();

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(
                    VorbisAlbumArtistReadOptions.READ_ALBUMARTIST
            );
            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "fred");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(af.getTag().getFirst(FieldKey.ALBUM_ARTIST), "fred");
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    "fred"
            );
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    ""
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(
                    VorbisAlbumArtistReadOptions.READ_JRIVER_ALBUMARTIST
            );
            assertEquals(af.getTag().getFirst(FieldKey.ALBUM_ARTIST), "");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(
                    VorbisAlbumArtistReadOptions.READ_ALBUMARTIST_THEN_JRIVER
            );
            assertEquals(af.getTag().getFirst(FieldKey.ALBUM_ARTIST), "fred");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(
                    VorbisAlbumArtistReadOptions.READ_JRIVER_THEN_ALBUMARTIST
            );
            assertEquals(af.getTag().getFirst(FieldKey.ALBUM_ARTIST), "fred");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "tom");
            af.commit();

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(
                    VorbisAlbumArtistReadOptions.READ_ALBUMARTIST
            );
            assertEquals(af.getTag().getFirst(FieldKey.ALBUM_ARTIST), "fred");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(
                    VorbisAlbumArtistReadOptions.READ_JRIVER_ALBUMARTIST
            );
            assertEquals(af.getTag().getFirst(FieldKey.ALBUM_ARTIST), "tom");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(
                    VorbisAlbumArtistReadOptions.READ_ALBUMARTIST_THEN_JRIVER
            );
            assertEquals(af.getTag().getFirst(FieldKey.ALBUM_ARTIST), "fred");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(
                    VorbisAlbumArtistReadOptions.READ_JRIVER_THEN_ALBUMARTIST
            );
            assertEquals(af.getTag().getFirst(FieldKey.ALBUM_ARTIST), "tom");
            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "fred");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    "fred"
            );
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    "tom"
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "tom");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    "fred"
            );
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    "tom"
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST_AND_DELETE_JRIVER_ALBUMARTIST
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "freddy");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    "freddy"
            );
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    ""
            );

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(
                    VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST_AND_DELETE_ALBUMARTIST
            );
            af.getTag().setField(FieldKey.ALBUM_ARTIST, "tommy");
            af.commit();
            System.out.println(af.getTag());
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST
                    ),
                    ""
            );
            assertEquals(
                    ((FlacTag) af.getTag()).getVorbisCommentTag().getFirst(
                            VorbisCommentFieldKey.ALBUMARTIST_JRIVER
                    ),
                    "tommy"
            );
        } catch (Exception e) {
            ex = e;
        }
        assertNull(ex);
    }
}
