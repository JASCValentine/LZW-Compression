package com.jascvalentine.lzw;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LZW {
  private static void writeFileList(OutputStream out, String... pathNames) throws IOException {
    for (String s : pathNames) {
      for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        out.write(c);
      }
      out.write('\n');
    }
    out.write('\n');
    out.flush();
  }

  private static List<String> readFileList(InputStream in) throws IOException {
    List<String> list = new ArrayList<String>();

    StringBuilder sb = new StringBuilder();
    while (true) {
      int b = in.read();
      if (b == -1)
        throw new Error(); // should not reach EOF here

      if (b == '\n') {
        list.add(sb.toString());
        sb.setLength(0);
        b = in.read();
        if (b == -1)
          throw new Error(); // should not reach EOF here
        if (b == '\n')
          break;
        sb.append((char) b);
      } else {
        sb.append((char) b);
      }
    }

    return list;
  }

  private static void compress(OutputStream out, String... pathNames) throws IOException {
    final CodewordOutputStream cwout = new CodewordOutputStream(out, false);
    LZWEncoder encoder = new LZWEncoder(new CodewordListener() {
      @Override
      public void codeword(int codeword) throws IOException {
        cwout.writeInt(codeword);
      }
    });

    for (String s : pathNames) {
      InputStream in = new BufferedInputStream(new FileInputStream(s));
      int b;
      while ((b = in.read()) != -1) {
        encoder.encode(b);
      }
      in.close();
      encoder.finish();

      cwout.writeInt(0xFFF);
    }
    cwout.close();
  }

  private static void decompress(InputStream in, List<String> pathNames) throws IOException {
    DecodeFileWriter writer = new DecodeFileWriter();

    CodewordInputStream cwin = new CodewordInputStream(in);
    LZWDecoder decoder = new LZWDecoder(writer);

    for (String s : pathNames) {
      OutputStream out = new BufferedOutputStream(new FileOutputStream(s));
      writer.setOutputStream(out);

      int codeword;
      while ((codeword = cwin.readInt()) != 0xFFF) {
        decoder.decode(codeword);
      }
      out.close();
      decoder.reset();
    }

    cwin.close();
  }

  private static void usage() {
    System.err.println("[Compression] -c <output file name> <list of files name>");
    System.err.println("[Decompression] -d <lzw filename>");
  }

  public static void main(String[] args) throws IOException {
    System.out.println("The Chinese University of Hong Kong 2014/15");
    System.out.println("CMSC5714 Multimedia Technology");
    System.out.println("Programming Assignment LZW Compressor");
    System.out.println("By LAM Chung Sing (1155042110)");

    if (args.length == 0) {
      usage();
      System.exit(1);
    }

    if ("-c".equals(args[0]) && args.length > 2) {
      OutputStream out = new BufferedOutputStream(new FileOutputStream(args[1]));
      String[] list = Arrays.copyOfRange(args, 2, args.length);

      writeFileList(out, list);
      compress(out, list);
    } else if ("-d".equals(args[0]) && args.length > 1) {
      InputStream in = new BufferedInputStream(new FileInputStream(args[1]));

      List<String> list = readFileList(in);
      decompress(in, list);
    } else {
      usage();
      System.exit(1);
    }
  }
}
