package com.jascvalentine.lzw;

import java.io.IOException;
import java.io.OutputStream;

class DecodeFileWriter implements CodewordListener {
  private OutputStream out;

  public void setOutputStream(OutputStream out) {
    this.out = out;
  }

  @Override
  public void codeword(int codeword) throws IOException {
    out.write(codeword);
  }
}