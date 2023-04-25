/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Raphaël Slinckx <raphael@slinckx.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jaudiotagger.tag.mp4;

import org.jaudiotagger.audio.generic.AbstractTagCreator;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;
import org.jcodec.containers.mp4.boxes.Box;
import org.jcodec.containers.mp4.boxes.DataBox;
import org.jcodec.containers.mp4.boxes.IListBox;
import org.jcodec.containers.mp4.boxes.ReverseDnsBox;

import java.util.*;

/**
 * Create raw content of mp4 tag data, concerns itself with atoms upto the ilst atom
 *
 * <p>This level was selected because the ilst atom can be recreated without reference to existing mp4 fields
 * but fields above this level are dependent upon other information that is not held in the tag.
 *
 * <pre>
 * |--- ftyp
 * |--- moov
 * |......|
 * |......|----- mvdh
 * |......|----- trak
 * |......|----- udta
 * |..............|
 * |..............|-- meta
 * |....................|
 * |....................|-- hdlr
 * |....................|-- ilst
 * |....................|.. ..|
 * |....................|.....|---- @nam (Optional for each metadatafield)
 * |....................|.....|.......|-- data
 * |....................|.....|....... ecetera
 * |....................|.....|---- ---- (Optional for reverse dns field)
 * |....................|.............|-- mean
 * |....................|.............|-- name
 * |....................|.............|-- data
 * |....................|................ ecetere
 * |....................|-- free
 * |--- free
 * |--- mdat
 * </pre>
 */
public class Mp4TagCreator extends AbstractTagCreator<IListBox> {
    /**
     * Convert tagdata to rawdata ready for writing to file
     *
     * @param tag
     * @param padding TODO padding parameter currently ignored
     * @return
     */
    public IListBox convert(Tag tag, int padding) {
        Map<Integer, List<Box>> values = new LinkedHashMap<>();
        List<ReverseDnsBox> rdnsBoxes = new ArrayList<>();
        try {
            //Add metadata raw content
            Iterator<TagField> it = tag.getFields();
            while (it.hasNext()) {
                TagField frame = it.next();
                if (frame instanceof Mp4TagField) {
                    Mp4TagField mp4Frame = (Mp4TagField) frame;
                    Mp4FieldKey key = Mp4FieldKey.byFieldName(frame.getId());
                    DataBox data = DataBox.createDataBox(mp4Frame.getFieldType().getFileClassId(), 0, mp4Frame.getDataBytes());
                    if (key.isReverseDnsType()) {
                        rdnsBoxes.add(ReverseDnsBox.createReverseDnsBox(key.getIssuer(), key.getIdentifier(), data));
                    } else {
                        Integer keyAsInt = Utils.reinterpretStringAsInt(key.getFieldName());
                        values.computeIfAbsent(keyAsInt, idx -> new ArrayList<>()).add(data);
                    }
                }

            }
            return IListBox.createIListBox(values, rdnsBoxes);
        } catch (Exception ioe) {
            //Should never happen as not writing to file at this point
            throw new RuntimeException(ioe);
        }
    }
}
