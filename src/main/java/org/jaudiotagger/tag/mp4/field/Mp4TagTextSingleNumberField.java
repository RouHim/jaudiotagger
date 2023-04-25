package org.jaudiotagger.tag.mp4.field;

import java.util.ArrayList;

public class Mp4TagTextSingleNumberField extends Mp4TagTextNumberField {

    public Mp4TagTextSingleNumberField(String id, String number) {
        super(id, number);
        numbers = new ArrayList<>(1);
        numbers.add(Short.valueOf(number));
    }

    public Mp4FieldType getFieldType() {
        return Mp4FieldType.INTEGER;
    }
}
