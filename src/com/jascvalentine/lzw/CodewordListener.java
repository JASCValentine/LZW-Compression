package com.jascvalentine.lzw;

import java.io.IOException;

public interface CodewordListener {
  // notify when a codeword is available to read/write to/from stream
  void codeword(int codeword) throws IOException;
}