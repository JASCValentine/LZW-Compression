package com.jascvalentine.lzw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Compress {
  private static final int initialDictionarySize = 256;
  private static final int dictionarySizeLimit = 4096;

  //  public static List<Integer> compress2(String input) {
  //    Map<String, Integer> map = new HashMap<String, Integer>();
  //    for (int i = 0; i < 256; i++) {
  //      map.put(Character.toString((char)i), Integer.valueOf(i));
  //    }
  //    int current = 0;
  //    int count = 256;
  //    StringBuilder sb = new StringBuilder();
  //    List<Integer> output = new ArrayList<Integer>();
  //
  //    while (current < input.length()) {
  //      char next = input.charAt(current++);
  //
  //      Integer index = map.get(sb.toString() + next);
  //      if (index != null) {
  //        sb.append(next);
  //      } else {
  //        output.add(map.get(sb.toString()));
  //
  //        map.put(sb.toString() + next, Integer.valueOf(count++));
  //        sb = new StringBuilder().append(next);
  //      }
  //    }
  //    output.add(map.get(sb.toString()));
  //    return output;
  //  }
  //
  //  public static String decompress(List<Integer> input) {
  //    StringBuilder output = new StringBuilder();
  //    Map<Integer, String> map = new HashMap<Integer, String>();
  //    for (int i = 0; i < 256; i++) {
  //      map.put(Integer.valueOf(i), Character.toString((char)i).toString());
  //    }
  //
  //    int counter = 256;
  //    String p = "";
  //    Integer cW = input.get(0);
  //    output.append((char)cW.intValue());
  //
  //    int index = 1;
  //    while (index < input.size()) {
  //      Integer pW = cW;
  //      cW = input.get(index++);
  //      String s = map.get(cW);
  //      if (s != null) {
  //        output.append(s);
  //        char c = s.charAt(0);
  //        p = map.get(pW);
  //        map.put(Integer.valueOf(counter++), p + c);
  //      } else {
  //        p = map.get(pW);
  //        char c = p.charAt(0);
  //        output.append(p + c);
  //        map.put(Integer.valueOf(counter++), p + c);
  //      }
  //    }
  //    return output.toString();
  //  }

  public List<Integer> compress(String input) {
    Map<String, Integer> dictionary = new HashMap<String, Integer>(dictionarySizeLimit - initialDictionarySize);
    List<Integer> output = new ArrayList<Integer>(input.length());

    int charIndex = 0;
    int dictIndex = initialDictionarySize;
    StringBuilder buffer = new StringBuilder();

    while (charIndex < input.length()) {
      char current = input.charAt(charIndex++);
      buffer.append(current);

      Integer index = lookupIndex(buffer.toString(), dictionary);
      if (index == null) {
        output.add(lookupIndex(buffer.substring(0, buffer.length() - 1), dictionary));

        dictionary.put(buffer.toString(), Integer.valueOf(dictIndex++));
        if (dictionary.size() >= dictionarySizeLimit)
          dictionary.clear();

        buffer.setLength(0);
        buffer.append(current);
      }
    }
    output.add(dictionary.get(buffer.toString()));

    return output;
  }

  public String decompress(List<Integer> input) {
    Map<Integer, char[]> dictionary = new HashMap<Integer, char[]>(dictionarySizeLimit - initialDictionarySize);
    StringBuilder output = new StringBuilder();

    int charIndex = 0;
    int dictIndex = initialDictionarySize;

    // read first input
    Integer cW = input.get(charIndex++);
    output.append((char)cW.intValue());

    while (charIndex < input.size()) {
      Integer pW = cW;
      cW = input.get(charIndex++);
      char[] s = lookupPhase(cW, dictionary);

      char c;
      char[] p;
      if (s != null) {
        output.append(s);
        c = s[0];
        p = lookupPhase(pW, dictionary);
      } else {
        p = lookupPhase(pW, dictionary);
        c = p[0];
        output.append(p).append(c);
      }

      char[] t = new char[p.length + 1];
      System.arraycopy(p, 0, t, 0, p.length);
      t[p.length] = c;
      dictionary.put(Integer.valueOf(dictIndex++), t);
      if (dictionary.size() >= dictionarySizeLimit)
        dictionary.clear();
    }

    return output.toString();
  }

  private static Integer lookupIndex(String phase, Map<String, Integer> dictionary) {
    return phase.length() == 1 ? Integer.valueOf(phase.charAt(0)) : dictionary.get(phase);
  }

  private char[] lookupPhase(Integer index, Map<Integer, char[]> dictionary) {
    return index.intValue() < initialDictionarySize ? new char[] {(char)index.intValue()} : dictionary.get(index);
  }

  public static void main(String[] args) throws Exception {
    String input = "aabaacabcaabc";
    System.out.println(input);

    Compress c= new Compress();
    List<Integer> output = c.compress(input);
    System.out.println(Arrays.toString(output.toArray()));
    int[] expected = {97, 97, 98, 256, 99, 257, 260, 261};
    System.out.println(Arrays.toString(expected));

    //		List<Integer> output2 = c.compress2(input);
    //    System.out.println(Arrays.toString(output2.toArray()));

    String s = c.decompress(output);
    System.out.println(s);
  }
}
