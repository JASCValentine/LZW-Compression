package com.jascvalentine.lzw;

import java.io.DataInput;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractDataInputStream extends FilterInputStream implements DataInput {

  protected AbstractDataInputStream(InputStream in) {
    super(in);
  }

  @Override
  public void readFully(byte[] b) throws IOException {
    throw new IOException();
  }

  @Override
  public void readFully(byte[] b, int off, int len) throws IOException {
    throw new IOException();
  }

  @Override
  public int skipBytes(int n) throws IOException {
    throw new IOException();
  }

  @Override
  public boolean readBoolean() throws IOException {
    throw new IOException();
  }

  @Override
  public byte readByte() throws IOException {
    throw new IOException();
  }

  @Override
  public int readUnsignedByte() throws IOException {
    throw new IOException();
  }

  @Override
  public short readShort() throws IOException {
    throw new IOException();
  }

  @Override
  public int readUnsignedShort() throws IOException {
    throw new IOException();
  }

  @Override
  public char readChar() throws IOException {
    throw new IOException();
  }

  @Override
  public int readInt() throws IOException {
    throw new IOException();
  }

  @Override
  public long readLong() throws IOException {
    throw new IOException();
  }

  @Override
  public float readFloat() throws IOException {
    throw new IOException();
  }

  @Override
  public double readDouble() throws IOException {
    throw new IOException();
  }

  @Override
  public String readLine() throws IOException {
    throw new IOException();
  }

  @Override
  public String readUTF() throws IOException {
    throw new IOException();
  }

}
