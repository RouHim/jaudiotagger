package org.jaudiotagger.tag;

import org.jaudiotagger.tag.asf.AsfTag;
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.tag.id3.ID3v1Tag;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;
import org.junit.jupiter.api.Test;

public class TagEnumsTest {

  @Test
  public void allTagsShouldHaveAllValues() {
    for (FieldKey fk : FieldKey.values()) {
      checkTag(new Mp4Tag(), fk);
      checkTag(new ID3v1Tag(), fk);
      checkTag(new ID3v24Tag(), fk);
      checkTag(new ID3v22Tag(), fk);
      checkTag(new ID3v23Tag(), fk);
      // checkTag(new AiffTag(), fk);  // derived from ID3
      checkTag(new AsfTag(), fk);
      // checkTag(new WavTag(READ_ID3_ONLY), fk); // derived from ID3
      checkTag(new VorbisCommentTag(), fk);
      checkTag(new FlacTag(), fk);
    }
  }

  private void checkTag(Tag tag, FieldKey fk) {
    try {
      tag.getFields(fk);
    } catch (Exception ex) {
      // System.out.println("No tag " + fk + " in type " + tag.getClass().getSimpleName());
      throw new RuntimeException(
        "No tag " + fk + " in type " + tag.getClass().getSimpleName()
      );
    }
  }
}
