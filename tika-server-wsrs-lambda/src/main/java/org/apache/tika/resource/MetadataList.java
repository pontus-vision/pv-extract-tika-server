package org.apache.tika.resource;


import java.util.List;

import org.apache.tika.metadata.Metadata;

/**
 * wrapper class to make isWriteable in MetadataListMBW simpler
 */
public class MetadataList {
  private final List<Metadata> metadata;

  public MetadataList(List<Metadata> metadata) {
    this.metadata = metadata;
  }

  public List<Metadata> getMetadata() {
    return metadata;
  }
}
