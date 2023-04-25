package org.jaudiotagger.tag.mp4.field;

import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagTextField;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;
import org.jaudiotagger.tag.mp4.Mp4TagField;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Represents reverse dns field, used for custom information
 *
 * <p>Originally only used by Itunes for information that was iTunes specific but now used in a wide range of uses,
 * for example Musicbrainz uses it for many of its fields.
 * <p>
 * These fields have a more complex setup
 * Box ----  shows this is a reverse dns metadata field
 * Box mean  the issuer in the form of reverse DNS domain (e.g com.apple.iTunes)
 * Box name  descriptor identifying the type of contents
 * Box data  contents
 * <p>
 * The raw data passed starts from the mean box
 */
public class Mp4TagReverseDnsField extends Mp4TagField implements TagTextField {
    public static final String IDENTIFIER = "----";

    protected int dataSize;

    //Issuer
    private String issuer;

    //Descriptor
    private String descriptor;

    //Data Content,
    //TODO assuming always text at the moment
    protected String content;

    /**
     * Newly created Reverse Dns field
     *
     * @param id
     * @param content
     */
    public Mp4TagReverseDnsField(Mp4FieldKey id, String content) {
        super(id.getFieldName());
        this.issuer = id.getIssuer();
        this.descriptor = id.getIdentifier();
        this.content = content;
    }

    /**
     * Newly created Reverse Dns field bypassing the Mp4TagField enum for creation of temporary reverse dns fields
     *
     * @param fieldName
     * @param issuer
     * @param identifier
     * @param content
     */
    public Mp4TagReverseDnsField(final String fieldName, final String issuer, final String identifier, final String content) {
        super(fieldName);
        this.issuer = issuer;
        this.descriptor = identifier;
        this.content = content;
    }

    @Override
    public Mp4FieldType getFieldType() {
        //TODO always assuming text at moment but may not always be the case (though dont have any concrete
        //examples)
        return Mp4FieldType.TEXT;
    }

    @Override
    public void copyContent(TagField field) {
        if (field instanceof Mp4TagReverseDnsField) {
            this.issuer = ((Mp4TagReverseDnsField) field).getIssuer();
            this.descriptor = ((Mp4TagReverseDnsField) field).getDescriptor();
            this.content = ((Mp4TagReverseDnsField) field).getContent();
        }
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    protected byte[] getDataBytes() {
        return content.getBytes(getEncoding());
    }

    @Override
    public Charset getEncoding() {
        return StandardCharsets.UTF_8;
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return "".equals(this.content.trim());
    }

    @Override
    public void setContent(String s) {
        this.content = s;
    }

    @Override
    public void setEncoding(Charset s) {
        /* Not allowed */
    }

    @Override
    public String toString() {
        return content;
    }

    /**
     * @return the issuer
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * @return the descriptor
     */
    public String getDescriptor() {
        return descriptor;
    }

    /**
     * Set the issuer, usually reverse dns of the Companies domain
     *
     * @param issuer
     */
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    /**
     * Set the descriptor for the data (what type of data it is)
     *
     * @param descriptor
     */
    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }
}
