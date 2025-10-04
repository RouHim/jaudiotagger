package org.jaudiotagger.tag.wma;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import org.jaudiotagger.audio.asf.data.AsfHeader;
import org.jaudiotagger.audio.asf.data.ContainerType;
import org.jaudiotagger.audio.asf.data.MetadataContainer;
import org.jaudiotagger.audio.asf.data.MetadataContainerUtils;
import org.jaudiotagger.audio.asf.io.AsfHeaderReader;
import org.jaudiotagger.audio.asf.util.TagConverter;
import org.jaudiotagger.tag.asf.AsfTag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TagConverterTest extends WmaTestCase {

  public static final String TEST_FILE = "test6.wma";

  public TagConverterTest() {
    super(TEST_FILE);
  }

  /**
   * {@inheritDoc}
   */
  @BeforeEach
  protected void setUp() throws Exception {
    super.setUp();
  }

  /**
   * Test method for
   * {@link org.jaudiotagger.audio.asf.util.TagConverter#distributeMetadata(org.jaudiotagger.tag.asf.AsfTag)}
   * .
   */
  @Test
  public void testDistributeMetadata() throws IOException {
    AsfHeader header = AsfHeaderReader.readHeader(prepareTestFile(null));
    MetadataContainer contentDesc = header.findMetadataContainer(
      ContainerType.CONTENT_DESCRIPTION
    );
    assertNotNull(contentDesc);
    MetadataContainer extContentDesc = header.findMetadataContainer(
      ContainerType.EXTENDED_CONTENT
    );
    assertNotNull(extContentDesc);
    AsfTag createTagOf = TagConverter.createTagOf(header);
    MetadataContainer[] distributeMetadata = TagConverter.distributeMetadata(
      createTagOf
    );
    assertTrue(
      MetadataContainerUtils.equals(contentDesc, distributeMetadata[0])
    );
    assertTrue(
      MetadataContainerUtils.equals(extContentDesc, distributeMetadata[2])
    );
  }
}
