package com.jascvalentine.lzw;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LZWEncoder {
  public static final int DICTIONARY_SIZE = 4096;
  public static final int INITIAL_SIZE = 256;

  private CodewordListener listener;
  private Map<Phase, Integer> dictionary;
  private List<Byte> buffer;

  public LZWEncoder(CodewordListener listener) {
    this.listener = listener;
    this.dictionary = new HashMap<Phase, Integer>(DICTIONARY_SIZE - INITIAL_SIZE);
    this.buffer = new ArrayList<Byte>();
  }

  public void encode(int b) throws IOException {
    buffer.add(Byte.valueOf((byte) (b & 0xFF)));
    Integer index = lookupIndex(buffer);

    if (index == null) {
      List<Byte> phase = buffer.subList(0,  buffer.size() - 1);
      listener.codeword(lookupIndex(phase).intValue());

      dictionary.put(Phase.fromList(buffer), Integer.valueOf(dictionary.size() + INITIAL_SIZE));
      if (dictionary.size() >= DICTIONARY_SIZE - INITIAL_SIZE) {
        dictionary.clear();
      }

      buffer.clear();
      buffer.add(Byte.valueOf((byte) (b & 0xFF)));
    }
  }

  public void finish() throws IOException {
    Integer lastValue = lookupIndex(buffer);
    if (lastValue != null) {
      listener.codeword(lastValue.intValue());
    }
    buffer.clear();
  }

  private Integer lookupIndex(List<Byte> phase) {
    return phase.size() == 1 ? Integer.valueOf(phase.get(0).byteValue() & 0xFF) : dictionary.get(Phase.fromList(phase));
  }
}
