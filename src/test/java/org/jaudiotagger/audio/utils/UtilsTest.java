package org.jaudiotagger.audio.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.jaudiotagger.audio.generic.Utils;
import org.junit.jupiter.api.Test;

public class UtilsTest {

  @Test
  public void testByteToUnsignedIntConversion() {
    byte maxByte = (byte) 0xff;
    int maxNotConverted = maxByte;
    int maxConverted = Utils.u(maxByte);
    System.out.println(maxConverted + ":" + maxNotConverted);
    assertEquals(255, maxConverted);
  }

  @Test
  public void testShortToUnsignedIntConversion() {
    short maxShort = (short) 0xffff;
    int maxNotConverted = maxShort;
    int maxConverted = Utils.u(maxShort);
    System.out.println(maxConverted + ":" + maxNotConverted);
    assertEquals(65535, maxConverted);
  }

  @Test
  public void testIntToUnsignedLongConversion() {
    int maxInt = 0xffffffff;
    long maxNotConverted = maxInt;
    long maxConverted = Utils.u(maxInt);
    System.out.println(maxConverted + ":" + maxNotConverted);
    assertEquals(4294967295L, maxConverted);
  }
}
