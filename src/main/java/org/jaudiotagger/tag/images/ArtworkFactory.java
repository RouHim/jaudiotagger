package org.jaudiotagger.tag.images;

import java.io.File;
import java.io.IOException;
import org.jaudiotagger.audio.flac.metadatablock.MetadataBlockDataPicture;

/**
 * Get appropriate Artwork class
 */
public class ArtworkFactory {

  public static Artwork getNew() {
    return new StandardArtwork();
  }

  /**
   * Create Artwork instance from A Flac Metadata Block
   *
   * @param coverArt
   * @return
   */
  public static Artwork createArtworkFromMetadataBlockDataPicture(
    MetadataBlockDataPicture coverArt
  ) {
    return StandardArtwork.createArtworkFromMetadataBlockDataPicture(coverArt);
  }

  /**
   * Create Artwork instance from an image file
   *
   * @param file
   * @return
   * @throws IOException
   */
  public static Artwork createArtworkFromFile(File file) throws IOException {
    return StandardArtwork.createArtworkFromFile(file);
  }

  /**
   * Create Artwork instance from an image file
   *
   * @param link
   * @return
   * @throws IOException
   */
  public static Artwork createLinkedArtworkFromURL(String link)
    throws IOException {
    return StandardArtwork.createLinkedArtworkFromURL(link);
  }
}
