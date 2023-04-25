/*
 * Jaudiotagger Copyright (C)2004,2005
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 * you can getFields a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.FilePermissionsTest;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.id3.framebody.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ID3v23TagTest {

    @Test
    public void testReadID3v1ID3v23Tag() {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1Cbr128ID3v1v2.mp3");

        MP3File mp3File = null;

        try {
            mp3File = new MP3File(testFile);


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertNotNull(mp3File.getID3v1Tag());
        assertNotNull(mp3File.getID3v2Tag());
    }

    @Test
    public void testReadID3v23Tag() {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1Cbr128ID3v2.mp3");

        MP3File mp3File = null;

        try {
            mp3File = new MP3File(testFile);


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertNull(mp3File.getID3v1Tag());
        assertNotNull(mp3File.getID3v2Tag());
    }

    @Test
    public void testReadPaddedID3v23Tag() {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1Cbr128ID3v2pad.mp3");

        MP3File mp3File = null;

        try {
            mp3File = new MP3File(testFile);


        } catch (Exception e) {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertNull(mp3File.getID3v1Tag());
        assertNotNull(mp3File.getID3v2Tag());
    }

    @Test
    public void testDeleteID3v23Tag() {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1Cbr128ID3v1v2.mp3");

        MP3File mp3File = null;

        try {
            mp3File = new MP3File(testFile);


        } catch (Exception e) {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertNotNull(mp3File.getID3v1Tag());
        assertNotNull(mp3File.getID3v2Tag());

        mp3File.setID3v1Tag(null);
        mp3File.setID3v2Tag(null);
        try {
            mp3File.save();
        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        assertNull(mp3File.getID3v1Tag());
        assertNull(mp3File.getID3v2Tag());
    }

    @Test
    public void testCreateIDv23Tag() {
        ID3v23Tag v2Tag = new ID3v23Tag();
        assertEquals((byte) 2, v2Tag.getRelease());
        assertEquals((byte) 3, v2Tag.getMajorVersion());
        assertEquals((byte) 0, v2Tag.getRevision());
    }

    @Test
    public void testCreateID3v23FromID3v11() {
        ID3v11Tag v11Tag = ID3v11TagTest.getInitialisedTag();
        ID3v23Tag v2Tag = new ID3v23Tag(v11Tag);
        assertNotNull(v11Tag);
        assertNotNull(v2Tag);
        assertEquals(ID3v11TagTest.ARTIST, ((FrameBodyTPE1) ((ID3v23Frame) v2Tag.getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST)).getBody()).getText());
        assertEquals(ID3v11TagTest.ALBUM, ((FrameBodyTALB) ((ID3v23Frame) v2Tag.getFrame(ID3v23Frames.FRAME_ID_V3_ALBUM)).getBody()).getText());
        assertEquals(ID3v11TagTest.COMMENT, ((FrameBodyCOMM) ((ID3v23Frame) v2Tag.getFrame(ID3v23Frames.FRAME_ID_V3_COMMENT)).getBody()).getText());
        assertEquals(ID3v11TagTest.TITLE, ((FrameBodyTIT2) ((ID3v23Frame) v2Tag.getFrame(ID3v23Frames.FRAME_ID_V3_TITLE)).getBody()).getText());
        assertEquals(ID3v11TagTest.TRACK_VALUE, String.valueOf(((FrameBodyTRCK) ((ID3v23Frame) v2Tag.getFrame(ID3v23Frames.FRAME_ID_V3_TRACK)).getBody()).getTrackNo()));
        assertTrue(((FrameBodyTCON) ((ID3v23Frame) v2Tag.getFrame(ID3v23Frames.FRAME_ID_V3_GENRE)).getBody()).getText().endsWith(ID3v11TagTest.GENRE_VAL));
        assertEquals(ID3v11TagTest.YEAR, ((FrameBodyTYER) ((ID3v23Frame) v2Tag.getFrame(ID3v23Frames.FRAME_ID_V3_TYER)).getBody()).getText());

        assertEquals((byte) 2, v2Tag.getRelease());
        assertEquals((byte) 3, v2Tag.getMajorVersion());
        assertEquals((byte) 0, v2Tag.getRevision());

    }

    /**
     * Test converting a v24 tag to a v23 tag, the v24 tag contains:
     * <p/>
     * A frame which is known in v24 and v23
     *
     * @throws Exception
     */
    @Test
    public void testCreateID3v23FromID3v24knownInV3() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = null;
        mp3File = new MP3File(testFile);

        //Add v24 tag to mp3 with single tdrl frame (which is only supported in v24)
        ID3v24Tag tag = new ID3v24Tag();
        FrameBodyTIT2 framebodyTdrl = new FrameBodyTIT2();
        framebodyTdrl.setText("title");
        ID3v24Frame tdrlFrame = new ID3v24Frame("TIT2");
        tdrlFrame.setBody(framebodyTdrl);
        tag.setFrame(tdrlFrame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reread from File
        mp3File = new MP3File(testFile);

        //Convert to v23 ,frame converted and marked as unsupported
        ID3v23Tag v23tag = new ID3v23Tag(mp3File.getID3v2TagAsv24());
        ID3v23Frame v23frame = (ID3v23Frame) v23tag.getFrame("TIT2");
        assertNotNull(v23frame);
        assertTrue(v23frame.getBody() instanceof FrameBodyTIT2);

        //Save as v23 tag (side effect convert v23 to v24 tag as well)
        mp3File.setID3v2Tag(v23tag);
        mp3File.save();

        //Reread from File
        mp3File = new MP3File(testFile);

        //Convert to v23 ,frame should still exist
        v23tag = (ID3v23Tag) mp3File.getID3v2Tag();
        v23frame = (ID3v23Frame) v23tag.getFrame("TIT2");
        assertNotNull(v23frame);
        assertTrue(v23frame.getBody() instanceof FrameBodyTIT2);

        //Save as v23 tag (side effect convert v23 to v24 tag as well)
        mp3File.setID3v2Tag(v23tag);
        mp3File.save();

        //Check when converted to v24 has value been maintained
        assertEquals("title", ((FrameBodyTIT2) ((ID3v24Frame) mp3File.getID3v2TagAsv24().getFrame("TIT2")).getBody()).getText());
    }

    /**
     * Test converting a v24 tag to a v23 tag, the v24 tag contains:
     * <p/>
     * A frame which is known in v24 but not v23
     *
     * @throws Exception
     */
    @Test
    public void testCreateID3v23FromID3v24UnknownInV3() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = null;
        mp3File = new MP3File(testFile);

        //Add v24 tag to mp3 with single tdrl frame (which is only supported in v24)
        ID3v24Tag tag = new ID3v24Tag();
        FrameBodyTDRL framebodyTdrl = new FrameBodyTDRL();
        framebodyTdrl.setText("2008");
        ID3v24Frame tdrlFrame = new ID3v24Frame("TDRL");
        tdrlFrame.setBody(framebodyTdrl);
        tag.setFrame(tdrlFrame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reread from File
        mp3File = new MP3File(testFile);

        //Convert to v23 ,frame converted and marked as unsupported
        ID3v23Tag v23tag = new ID3v23Tag(mp3File.getID3v2TagAsv24());
        ID3v23Frame v23frame = (ID3v23Frame) v23tag.getFrame("TDRL");
        assertNotNull(v23frame);
        assertTrue(v23frame.getBody() instanceof FrameBodyUnsupported);

        //Save as v23 tag (side effect convert v23 to v24 tag as well)
        mp3File.setID3v2Tag(v23tag);
        mp3File.save();

        //Reread from File
        mp3File = new MP3File(testFile);

        //Convert to v23 ,frame should still exist
        v23tag = (ID3v23Tag) mp3File.getID3v2Tag();
        v23frame = (ID3v23Frame) v23tag.getFrame("TDRL");
        assertNotNull(v23frame);
        assertTrue(v23frame.getBody() instanceof FrameBodyUnsupported);

        //Save as v23 tag (side effect convert v23 to v24 tag as well)
        mp3File.setID3v2Tag(v23tag);
        mp3File.save();

        //Check value maintained, can only see as bytes
        FrameBodyUnsupported v23FrameBody = (FrameBodyUnsupported) v23frame.getBody();
        assertEquals((byte) '2', ((byte[]) v23FrameBody.getObjectValue(DataTypes.OBJ_DATA))[1]);
        assertEquals((byte) '0', ((byte[]) v23FrameBody.getObjectValue(DataTypes.OBJ_DATA))[2]);
        assertEquals((byte) '0', ((byte[]) v23FrameBody.getObjectValue(DataTypes.OBJ_DATA))[3]);
        assertEquals((byte) '8', ((byte[]) v23FrameBody.getObjectValue(DataTypes.OBJ_DATA))[4]);

        //Reread from File
        mp3File = new MP3File(testFile);
        v23tag = (ID3v23Tag) mp3File.getID3v2Tag();
        v23frame = (ID3v23Frame) v23tag.getFrame("TDRL");
        assertNotNull(v23frame);
        assertTrue(v23frame.getBody() instanceof FrameBodyUnsupported);

        //Check value maintained, can only see as bytes
        v23FrameBody = (FrameBodyUnsupported) v23frame.getBody();
        assertEquals((byte) '2', ((byte[]) v23FrameBody.getObjectValue(DataTypes.OBJ_DATA))[1]);
        assertEquals((byte) '0', ((byte[]) v23FrameBody.getObjectValue(DataTypes.OBJ_DATA))[2]);
        assertEquals((byte) '0', ((byte[]) v23FrameBody.getObjectValue(DataTypes.OBJ_DATA))[3]);
        assertEquals((byte) '8', ((byte[]) v23FrameBody.getObjectValue(DataTypes.OBJ_DATA))[4]);

        //Convert V24 representation to V23, and then save this
        v23tag = new ID3v23Tag(mp3File.getID3v2TagAsv24());
        mp3File.setID3v2TagOnly(v23tag);
        mp3File.setID3v2Tag(v23tag);
        mp3File.save();

        //Reread from File
        mp3File = new MP3File(testFile);
        v23tag = (ID3v23Tag) mp3File.getID3v2Tag();
        v23frame = (ID3v23Frame) v23tag.getFrame("TDRL");
        assertNotNull(v23frame);
        assertTrue(v23frame.getBody() instanceof FrameBodyUnsupported);

        //Check value maintained, can only see as bytes
        v23FrameBody = (FrameBodyUnsupported) v23frame.getBody();
        assertEquals((byte) '2', ((byte[]) v23FrameBody.getObjectValue(DataTypes.OBJ_DATA))[1]);
        assertEquals((byte) '0', ((byte[]) v23FrameBody.getObjectValue(DataTypes.OBJ_DATA))[2]);
        assertEquals((byte) '0', ((byte[]) v23FrameBody.getObjectValue(DataTypes.OBJ_DATA))[3]);
        assertEquals((byte) '8', ((byte[]) v23FrameBody.getObjectValue(DataTypes.OBJ_DATA))[4]);

    }

    @Test
    public void testDeleteFields() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);
        ID3v23Tag v2Tag = new ID3v23Tag();
        mp3File.setID3v2Tag(v2Tag);
        mp3File.save();

        //Delete using generic key
        AudioFile f = AudioFileIO.read(testFile);
        List<TagField> tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(0, tagFields.size());
        f.getTag().addField(FieldKey.ALBUM_ARTIST_SORT, "artist1");
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(1, tagFields.size());
        f.getTag().deleteField(FieldKey.ALBUM_ARTIST_SORT);
        f.commit();

        //Delete using flac id
        f = AudioFileIO.read(testFile);
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(0, tagFields.size());
        f.getTag().addField(FieldKey.ALBUM_ARTIST_SORT, "artist1");
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(1, tagFields.size());
        f.getTag().deleteField("TSO2");
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(0, tagFields.size());
        f.commit();

        f = AudioFileIO.read(testFile);
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(0, tagFields.size());
    }


    @Test
    public void testWriteWriteProtectedFileWithCheckEnabled() throws Exception {
        FilePermissionsTest.runWriteWriteProtectedFileWithCheckEnabled("testV1Cbr128ID3v2.mp3");
    }

    @Test
    public void testWriteMultipleGenresToID3v23TagUsingDefault() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1Cbr128ID3v2.mp3");
        MP3File file = null;
        file = new MP3File(testFile);
        assertNull(file.getID3v1Tag());
        assertNotNull(file.getID3v2Tag());
        file.getTag().deleteField(FieldKey.GENRE);
        file.getTag().addField(FieldKey.GENRE, "Genre1");
        file.getTag().addField(FieldKey.GENRE, "Genre2");
        file.commit();
        file = new MP3File(testFile);
        assertEquals("Genre1", file.getTag().getFirst(FieldKey.GENRE));
        assertEquals("Genre1", file.getTag().getValue(FieldKey.GENRE, 0));
        assertEquals("Genre2", file.getTag().getValue(FieldKey.GENRE, 1));

        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);
        file.getTag().deleteField(FieldKey.GENRE);
        file.getTag().addField(FieldKey.GENRE, "Death Metal");
        file.getTag().addField(FieldKey.GENRE, "(23)");
        assertEquals("Death Metal", file.getTag().getFirst(FieldKey.GENRE));
        assertEquals("Death Metal", file.getTag().getValue(FieldKey.GENRE, 0));
        assertEquals("Pranks", file.getTag().getValue(FieldKey.GENRE, 1));
        file.commit();
        file = new MP3File(testFile);
        assertEquals("Death Metal", file.getTag().getFirst(FieldKey.GENRE));
        assertEquals("Death Metal", file.getTag().getValue(FieldKey.GENRE, 0));
        assertEquals("Pranks", file.getTag().getValue(FieldKey.GENRE, 1));

        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(true);
        file.getTag().deleteField(FieldKey.GENRE);
        file.getTag().addField(FieldKey.GENRE, "Death Metal");
        file.getTag().addField(FieldKey.GENRE, "23");
        assertEquals("Death Metal", file.getTag().getFirst(FieldKey.GENRE));
        assertEquals("Death Metal", file.getTag().getValue(FieldKey.GENRE, 0));
        assertEquals("Pranks", file.getTag().getValue(FieldKey.GENRE, 1));
        file.commit();
        file = new MP3File(testFile);
        assertEquals("Death Metal", file.getTag().getFirst(FieldKey.GENRE));
        assertEquals("Death Metal", file.getTag().getValue(FieldKey.GENRE, 0));
        assertEquals("Pranks", file.getTag().getValue(FieldKey.GENRE, 1));
    }

    @Test
    public void testWriteMultipleGenresToID3v23TagUsingCreateField() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File file = null;
        file = new MP3File(testFile);
        assertNull(file.getID3v1Tag());
        file.setTag(new ID3v23Tag());
        assertNotNull(file.getTag());
        ID3v23Tag v23Tag = (ID3v23Tag) file.getTag();
        TagField genreField = v23Tag.createField(FieldKey.GENRE, "Genre1");
        v23Tag.addField(genreField);
        genreField = v23Tag.createField(FieldKey.GENRE, "Genre2");
        v23Tag.addField(genreField);
        file.commit();
        file = new MP3File(testFile);
        assertEquals("Genre1", file.getTag().getFirst(FieldKey.GENRE));
        assertEquals("Genre1", file.getTag().getValue(FieldKey.GENRE, 0));
        assertEquals("Genre2", file.getTag().getValue(FieldKey.GENRE, 1));

        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);
        file.getTag().deleteField(FieldKey.GENRE);
        v23Tag = (ID3v23Tag) file.getTag();
        genreField = v23Tag.createField(FieldKey.GENRE, "Death Metal");
        v23Tag.addField(genreField);
        genreField = v23Tag.createField(FieldKey.GENRE, "(23)");
        v23Tag.addField(genreField);
        assertEquals("Death Metal", file.getTag().getFirst(FieldKey.GENRE));
        assertEquals("Death Metal", file.getTag().getValue(FieldKey.GENRE, 0));
        assertEquals("Pranks", file.getTag().getValue(FieldKey.GENRE, 1));
        file.commit();
        file = new MP3File(testFile);
        assertEquals("Death Metal", file.getTag().getFirst(FieldKey.GENRE));
        assertEquals("Death Metal", file.getTag().getValue(FieldKey.GENRE, 0));
        assertEquals("Pranks", file.getTag().getValue(FieldKey.GENRE, 1));

        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(true);
        file.getTag().deleteField(FieldKey.GENRE);
        v23Tag = (ID3v23Tag) file.getTag();
        genreField = v23Tag.createField(FieldKey.GENRE, "Death Metal");
        v23Tag.addField(genreField);
        genreField = v23Tag.createField(FieldKey.GENRE, "23");
        v23Tag.addField(genreField);
        assertEquals("Death Metal", file.getTag().getFirst(FieldKey.GENRE));
        assertEquals("Death Metal", file.getTag().getValue(FieldKey.GENRE, 0));
        assertEquals("Pranks", file.getTag().getValue(FieldKey.GENRE, 1));
        file.commit();
        file = new MP3File(testFile);
        assertEquals("Death Metal", file.getTag().getFirst(FieldKey.GENRE));
        assertEquals("Death Metal", file.getTag().getValue(FieldKey.GENRE, 0));
        assertEquals("Pranks", file.getTag().getValue(FieldKey.GENRE, 1));
    }

    @Test
    public void testWriteMultipleGenresToID3v23TagUsingV23CreateField() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File file = null;
        file = new MP3File(testFile);
        assertNull(file.getID3v1Tag());
        file.setTag(new ID3v23Tag());
        assertNotNull(file.getTag());
        ID3v23Tag v23Tag = (ID3v23Tag) file.getTag();
        TagField genreField = v23Tag.createField(ID3v23FieldKey.GENRE, "Genre1");
        v23Tag.addField(genreField);
        genreField = v23Tag.createField(ID3v23FieldKey.GENRE, "Genre2");
        v23Tag.addField(genreField);
        file.commit();
        file = new MP3File(testFile);
        assertEquals("Genre1", file.getTag().getFirst(FieldKey.GENRE));
        assertEquals("Genre1", file.getTag().getValue(FieldKey.GENRE, 0));
        assertEquals("Genre2", file.getTag().getValue(FieldKey.GENRE, 1));

        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);
        file.getTag().deleteField(FieldKey.GENRE);
        v23Tag = (ID3v23Tag) file.getTag();
        genreField = v23Tag.createField(ID3v23FieldKey.GENRE, "Death Metal");
        v23Tag.addField(genreField);
        genreField = v23Tag.createField(ID3v23FieldKey.GENRE, "(23)");
        v23Tag.addField(genreField);
        assertEquals("Death Metal", file.getTag().getFirst(FieldKey.GENRE));
        assertEquals("Death Metal", file.getTag().getValue(FieldKey.GENRE, 0));
        assertEquals("Pranks", file.getTag().getValue(FieldKey.GENRE, 1));
        file.commit();
        file = new MP3File(testFile);
        assertEquals("Death Metal", file.getTag().getFirst(FieldKey.GENRE));
        assertEquals("Death Metal", file.getTag().getValue(FieldKey.GENRE, 0));
        assertEquals("Pranks", file.getTag().getValue(FieldKey.GENRE, 1));

        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(true);
        file.getTag().deleteField(FieldKey.GENRE);
        v23Tag = (ID3v23Tag) file.getTag();
        genreField = v23Tag.createField(ID3v23FieldKey.GENRE, "Death Metal");
        v23Tag.addField(genreField);
        genreField = v23Tag.createField(ID3v23FieldKey.GENRE, "23");
        v23Tag.addField(genreField);
        assertEquals("Death Metal", file.getTag().getFirst(FieldKey.GENRE));
        assertEquals("Death Metal", file.getTag().getValue(FieldKey.GENRE, 0));
        assertEquals("Pranks", file.getTag().getValue(FieldKey.GENRE, 1));
        file.commit();
        file = new MP3File(testFile);
        assertEquals("Death Metal", file.getTag().getFirst(FieldKey.GENRE));
        assertEquals("Death Metal", file.getTag().getValue(FieldKey.GENRE, 0));
        assertEquals("Pranks", file.getTag().getValue(FieldKey.GENRE, 1));
    }
}
