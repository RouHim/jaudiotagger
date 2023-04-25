package org.jaudiotagger.tag.mp4.field;

import org.jaudiotagger.tag.mp4.Mp4FieldKey;
import org.jaudiotagger.tag.reference.GenreTypes;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Represents the Genre field , when user has selected from the set list of genres
 *
 * <p>This class allows you to retrieve either the internal genreid, or the display value
 */
public class Mp4GenreField extends Mp4TagTextNumberField {

    /**
     * Precheck to see if the value is a valid genre or whether you should use a custom genre.
     *
     * @param genreId
     * @return
     */
    public static boolean isValidGenre(String genreId) {
        //Is it an id (within old id3 range)      
        try {
            short genreVal = Short.parseShort(genreId);
            if ((genreVal - 1) <= GenreTypes.getMaxStandardGenreId()) {
                return true;
            }
        } catch (NumberFormatException nfe) {
            //Do Nothing test as String instead
        }

        //Is it the String value ?
        Integer id3GenreId = GenreTypes.getInstanceOf().getIdForValue(genreId);
        if (id3GenreId != null) {
            return id3GenreId <= GenreTypes.getMaxStandardGenreId();
        }
        return false;
    }

    public Mp4GenreField(byte[] data) {
        super(Mp4FieldKey.GENRE.getFieldName(), bytesToData(data));
        ByteBuffer buf = ByteBuffer.wrap(data);
        numbers = new ArrayList<>();
        numbers.add(buf.getShort());
    }

    private static String bytesToData(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        short genreId = buf.getShort();

        if ((genreId - 1) <= GenreTypes.getMaxStandardGenreId()) {
            return GenreTypes.getInstanceOf().getValueForId(genreId - 1);
        }
        return "Other";
    }

    /**
     * Construct genre, if cant find match just default to first genre
     *
     * @param genreId key into ID3v1 list (offset by one) or String value in ID3list
     */
    public Mp4GenreField(String genreId) {
        super(Mp4FieldKey.GENRE.getFieldName(), genreId);

        //Is it an id
        try {
            short genreVal = Short.parseShort(genreId);
            if (genreVal <= GenreTypes.getMaxStandardGenreId()) {
                numbers = new ArrayList<>();
                numbers.add(++genreVal);
                return;
            }

            //Default
            numbers = new ArrayList<>();
            numbers.add((short) (1));
            return;
        } catch (NumberFormatException nfe) {
            //Do Nothing test as String instead
        }

        //Is it the String value ?
        Integer id3GenreId = GenreTypes.getInstanceOf().getIdForValue(genreId);
        if (id3GenreId != null) {
            if (id3GenreId <= GenreTypes.getMaxStandardGenreId()) {
                numbers = new ArrayList<>();
                numbers.add((short) (id3GenreId + 1));
                return;
            }
        }
        numbers = new ArrayList<>();
        numbers.add((short) (1));
    }
}
