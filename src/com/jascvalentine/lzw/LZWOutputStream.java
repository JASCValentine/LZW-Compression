package com.jascvalentine.lzw;

import java.io.DataOutput;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LZWOutputStream extends FilterOutputStream {
  public static final int DICTIONARY_SIZE = 4096; // requirement
  public static final int INITIAL_SIZE = 256; // requirement

  private Map<List<Byte>, Number> dictionary;
  private int dictionaryIndex;
  private List<Byte> buffer;

  public LZWOutputStream(DataOutput out, Map<List<Byte>, Number> dictionary) {
    super((OutputStream)out);
    this.dictionary = dictionary;
    this.dictionaryIndex = INITIAL_SIZE;
    this.buffer = new ArrayList<Byte>();
  }

  public static Map<List<Byte>, Number> getDefaultDictionary() {
    return new HashMap<List<Byte>, Number>(DICTIONARY_SIZE - INITIAL_SIZE);  
  }
  
  public Map<List<Byte>, Number> getDictionary() {
    return dictionary;
  }

  private static List<Byte> getBytes(List<Byte> buffer, int limitDelta) {
    return new ArrayList<Byte>(buffer.subList(0, buffer.size() - limitDelta));
  }

  private Number lookupIndex(List<Byte> phase) {
    return phase.size() == 1 ? phase.get(0) : dictionary.get(phase);
  }

  @Override
  public void write(int b) throws IOException {
    buffer.add(Byte.valueOf((byte)b));

    List<Byte> phase;
    
    phase = getBytes(buffer, 0);
    Number index = lookupIndex(phase);
    if (index == null) {
      phase = getBytes(buffer, 1);
      ((DataOutput)out).writeInt(lookupIndex(phase).intValue());

      phase = getBytes(buffer, 0);
      dictionary.put(phase, Integer.valueOf(this.dictionaryIndex++));
      if (dictionary.size() >= DICTIONARY_SIZE)
        dictionary.clear();

      buffer.clear();
      buffer.add(Byte.valueOf((byte)b));
    }
  }

  @Override
  public void flush() throws IOException {
    ((DataOutput)out).writeInt(lookupIndex(getBytes(buffer, 0)).intValue());
    super.flush();
  }
}
