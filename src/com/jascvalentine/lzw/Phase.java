package com.jascvalentine.lzw;

import java.util.Arrays;
import java.util.List;

public class Phase {
  private final byte[] phase;
  private final int hashcode;

  public Phase(byte[] phase) {
    this.phase = phase;
    this.hashcode = Arrays.hashCode(phase);
  }

  public static Phase fromList(List<Byte> list) {
    byte[] b = new byte[list.size()];
    for (int i = 0; i < list.size(); i++) {
      b[i] = list.get(i).byteValue();
    }
    return new Phase(b);
  }

  @Override
  public int hashCode() {
    return this.hashcode;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Phase) {
      Phase p = (Phase)obj;
      return Arrays.equals(this.phase, p.phase);
    }
    return false;
  }
}
