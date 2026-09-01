package org.jaudiotagger.issues;

import java.io.File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

public class Issue146Test extends AbstractTestCase {

  @Test
  @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
  public void testIssue146() throws Exception {

    File file = copyAudioToTmp("test158.mp3");

    if (file.exists()) {
      AudioFile afile = AudioFileIO.read(file);
      Tag tag = afile.getTagOrCreateDefault();

      System.out.println(tag);
      if (tag == null) {
        System.out.println("Tag is null");
        tag = afile.createDefaultTag();
        System.out.println(tag);
        afile.setTag(tag);
      }
      tag.setField(FieldKey.TITLE, "好好学习");
      afile.commit();
      System.out.println(
        tag.getValue(FieldKey.TITLE, 0) +
          tag.getValue(FieldKey.TITLE, 0).getBytes().length
      );
      tag = AudioFileIO.read(file).getTag();
      System.out.println(
        tag.getValue(FieldKey.TITLE, 0) +
          tag.getValue(FieldKey.TITLE, 0).getBytes().length
      );
    }
  }
}
