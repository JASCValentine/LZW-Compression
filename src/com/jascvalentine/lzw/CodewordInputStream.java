package com.jascvalentine.lzw;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class CodewordInputStream extends AbstractDataInputStream {
  private boolean occupied;
  private byte buffer;

  public CodewordInputStream(InputStream in) {
    super(in);
  }

  @Override
  public int readInt() throws IOException {
    int codeword;

    if (occupied) {
      int ch1 = in.read();
      if (ch1 == -1) {
        // incomplete codeword
        throw new EOFException();
      }

      // buffer as first 4 bits, with ch1 as last 8 bits
      codeword = (buffer & 0xFF) << 8 | ch1;
    } else {
      int ch1 = in.read();
      int ch2 = in.read();
      if ((ch1 | ch2) < 0) {
        // incomplete codeword
        throw new EOFException();
      }

      // ch1 as the first 8 bits of output, with first 4 bits of ch2 as last 4 bits
      codeword = ch1 << 4 | ch2 >>> 4;

      // copy the remaining 4 bits to buffer
      buffer = (byte) (ch2 & 0xF);
    }

    occupied = !occupied;
    return codeword;
  }
}
