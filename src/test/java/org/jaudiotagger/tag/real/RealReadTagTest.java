package org.jaudiotagger.tag.real;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RealReadTagTest extends AbstractTestCase {

    @Test
    public void test01() throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException {
        File testFile = AbstractTestCase.copyAudioToTmp("test01.ra");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag();
        assertEquals(3, tag.getFieldCount()); // If this line fails we need to update our test as the RealMedia tag parser has been augmented
        assertEquals("Temptation Rag", tag.getFirst(FieldKey.TITLE));
        assertEquals("Prince's Military Band", tag.getFirst(FieldKey.ARTIST));
        assertEquals("1910 [Columbia A854]", tag.getFirst(FieldKey.COMMENT));
    }

    @Test
    public void test02() throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException {
        File testFile = AbstractTestCase.copyAudioToTmp("test02.ra");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag();
        assertEquals(3, tag.getFieldCount()); // If this line fails we need to update our test as the RealMedia tag parser has been augmented
        assertEquals("Dixieland Jass Band One Step", tag.getFirst(FieldKey.TITLE));
        assertEquals("Original Dixieland 'Jass' Band", tag.getFirst(FieldKey.ARTIST));
        assertEquals("1917 [Victor 18255-A]", tag.getFirst(FieldKey.COMMENT));
    }

    @Test
    public void test03() throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException {
        File testFile = AbstractTestCase.copyAudioToTmp("test03.ra");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag();
        assertEquals(3, tag.getFieldCount()); // If this line fails we need to update our test as the RealMedia tag parser has been augmented
        assertEquals("Here Comes My Daddy Now", tag.getFirst(FieldKey.TITLE));
        assertEquals("Collins and Harlan", tag.getFirst(FieldKey.ARTIST));
        assertEquals("1913 [Oxford 38528]", tag.getFirst(FieldKey.COMMENT));
    }

    @Test
    public void test04() throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException {
        File testFile = AbstractTestCase.copyAudioToTmp("test04.ra");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag();
        assertEquals(3, tag.getFieldCount()); // If this line fails we need to update our test as the RealMedia tag parser has been augmented
        assertEquals("A Cat-Astrophe", tag.getFirst(FieldKey.TITLE));
        assertEquals("Columbia Orchestra", tag.getFirst(FieldKey.ARTIST));
        assertEquals("1919 [Columbia A2855]", tag.getFirst(FieldKey.COMMENT));
    }

    @Test
    public void test05ra() throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException {
        File testFile = AbstractTestCase.copyAudioToTmp("test05.ra");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag();
        assertEquals(3, tag.getFieldCount()); // If this line fails we need to update our test as the RealMedia tag parser has been augmented
        assertEquals("It Makes My Love Come Down", tag.getFirst(FieldKey.TITLE));
        assertEquals("Bessie Smith, vocal; James P. Johnson, piano", tag.getFirst(FieldKey.ARTIST));
        assertEquals("1929 (Columbia 14464-D mx148904)", tag.getFirst(FieldKey.COMMENT));
    }

    @Test
    public void test05rm() throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException {
        File testFile = AbstractTestCase.copyAudioToTmp("test05.rm");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag();
        assertEquals(3, tag.getFieldCount()); // If this line fails we need to update our test as the RealMedia tag parser has been augmented
        assertEquals("It Makes My Love Come Down", tag.getFirst(FieldKey.TITLE));
        assertEquals("Bessie Smith, vocal; James P. Johnson, piano", tag.getFirst(FieldKey.ARTIST));
        assertEquals("1929 (Columbia 14464-D mx148904)", tag.getFirst(FieldKey.COMMENT));
    }

    @Test
    public void test06() throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException {
        File testFile = AbstractTestCase.copyAudioToTmp("test06.rm");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag();
        assertEquals(3, tag.getFieldCount()); // If this line fails we need to update our test as the RealMedia tag parser has been augmented
        assertEquals("No Trouble But You", tag.getFirst(FieldKey.TITLE));
        assertEquals("Ben Bernie & His Hotel Roosevelt Orchestra", tag.getFirst(FieldKey.ARTIST));
        assertEquals("1926 (Brunswick 3171-A)", tag.getFirst(FieldKey.COMMENT));
    }

    @Test
    public void test07() throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException {
        File testFile = AbstractTestCase.copyAudioToTmp("test07.rm");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag();
        assertEquals(3, tag.getFieldCount()); // If this line fails we need to update our test as the RealMedia tag parser has been augmented
        assertEquals("Is There A Place Up There For Me?", tag.getFirst(FieldKey.TITLE));
        assertEquals("Paul Tremaine & His Orchestra", tag.getFirst(FieldKey.ARTIST));
        assertEquals("circa 1931 (Columbia Tele-Focal Radio Series 91957)", tag.getFirst(FieldKey.COMMENT));
    }

    @Test
    public void test08() throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException {
        File testFile = AbstractTestCase.copyAudioToTmp("test08.rm");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag();
        assertEquals(3, tag.getFieldCount()); // If this line fails we need to update our test as the RealMedia tag parser has been augmented
        assertEquals("Let's Say Good Night Till The Morning", tag.getFirst(FieldKey.TITLE));
        assertEquals(" Jack Buchanan and Elsie Randolph", tag.getFirst(FieldKey.ARTIST));
        assertEquals("1926 (Columbia (British) 9147)", tag.getFirst(FieldKey.COMMENT));
    }

    @Test
    public void test09() throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException {
        File testFile = AbstractTestCase.copyAudioToTmp("test09.rm");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag();
        assertEquals(3, tag.getFieldCount()); // If this line fails we need to update our test as the RealMedia tag parser has been augmented
        assertEquals("Until Today", tag.getFirst(FieldKey.TITLE));
        assertEquals("Fletcher Henderson and his Orchestra", tag.getFirst(FieldKey.ARTIST));
        assertEquals("1936 (Victor 25373-B)", tag.getFirst(FieldKey.COMMENT));
    }

    @Test
    public void test10() throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException {
        File testFile = AbstractTestCase.copyAudioToTmp("test10.rm");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag();
        assertEquals(3, tag.getFieldCount()); // If this line fails we need to update our test as the RealMedia tag parser has been augmented
        assertEquals("Nobody Cares If I'm Blue", tag.getFirst(FieldKey.TITLE));
        assertEquals("Annette Hanshaw", tag.getFirst(FieldKey.ARTIST));
        assertEquals("1930 (Harmony 1196-H)", tag.getFirst(FieldKey.COMMENT));
    }

}
