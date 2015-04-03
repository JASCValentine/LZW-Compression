package com.jascvalentine.lzw;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LZWDecoder {
  public static final int DICTIOANRY_SIZE = 4096;
  public static final int INITIAL_SIZE = 256;

  private CodewordListener listener;
  private List<byte[]> dictionary;

  private boolean first;
  private int cW;

  public LZWDecoder(CodewordListener listener) {
    this.listener = listener;
    this.dictionary = new ArrayList<byte[]>(DICTIOANRY_SIZE - INITIAL_SIZE);
    this.first = true;
  }

  public void decode(int codeword) throws IOException {
    if (first) {
      first = false;
      cW = codeword;
      outputPhase(lookupPhase(cW));
      return;
    }

    // fill the buffer
    int pW = cW;
    cW = codeword;
    byte[] s = lookupPhase(cW);

    byte c;
    byte[] p;
    if (s != null) {
      outputPhase(s);
      c = s[0];
      p = lookupPhase(pW);
    } else {
      p = lookupPhase(pW);
      c = p[0];
      outputPhase(p);
      outputPhase(c);
    }

    // p + c
    byte[] pc = Arrays.copyOf(p, p.length + 1);
    pc[p.length] = c;
    dictionary.add(pc);

    if (dictionary.size() >= DICTIOANRY_SIZE - INITIAL_SIZE) {
      dictionary.clear();
    }
  }

  public void reset() {
    this.first = true;
  }

  private byte[] lookupPhase(int index) {
    if (index < INITIAL_SIZE) {
      return new byte[] { (byte) index };
    } else {
      int i = index - INITIAL_SIZE;
      return i < dictionary.size() ? dictionary.get(i) : null;
    }
  }

  private void outputPhase(byte... phase) throws IOException {
    for (byte b : phase) {
      listener.codeword(b & 0xFF);
    }
  }
}
