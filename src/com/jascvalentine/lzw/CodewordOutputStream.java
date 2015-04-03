package com.jascvalentine.lzw;

import java.io.IOException;
import java.io.OutputStream;

public class CodewordOutputStream extends AbstractDataOutputStream {
  private boolean occupied;
  private byte buffer;
  private boolean closeStream;

  public CodewordOutputStream(OutputStream out, boolean closeStream) {
    super(out);
    this.closeStream = closeStream;
  }

  @Override
  public void writeInt(int i) throws IOException {
    if (!occupied) {
      // write the first 8 bits
      write(i >>> 4);

      // copy the remaining 4 bits to buffer
      buffer = (byte) (i & 0xF);
    } else {
      // write the buffer plus first 4 bits of incoming integer
      write((buffer & 0xFF) << 4 | i >>> 8);

      // write the remaining 8 bits
      write(i & 0xFF);
    }

    occupied = !occupied;
  }

  @Override
  public void close() throws IOException {
    if (occupied) {
      write((buffer & 0xFF) << 4);

      // write the buffer once only
      occupied = false;
    }
    if (closeStream) {
      super.close();
    } else {
      flush();
    }
  }
}
