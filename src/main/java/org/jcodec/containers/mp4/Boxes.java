package org.jcodec.containers.mp4;

import java.util.HashMap;
import java.util.Map;
import org.jcodec.containers.mp4.boxes.Box;

public abstract class Boxes {

  protected final Map<String, Class<? extends Box>> mappings;

  public Boxes() {
    this.mappings = new HashMap<>();
  }

  public Class<? extends Box> toClass(String fourcc) {
    return mappings.get(fourcc);
  }

  public void override(String fourcc, Class<? extends Box> cls) {
    mappings.put(fourcc, cls);
  }

  public void clear() {
    mappings.clear();
  }
}
