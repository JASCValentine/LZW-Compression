package com.jascvalentine.lzw;

import java.io.DataOutput;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class AbstractDataOutputStream extends FilterOutputStream implements DataOutput {

  public AbstractDataOutputStream(OutputStream out) {
    super(out);
  }

  @Override
  public void writeBoolean(boolean v) throws IOException {
    throw new IOException();
  }

  @Override
  public void writeByte(int v) throws IOException {
    throw new IOException();
  }

  @Override
  public void writeShort(int v) throws IOException {
    throw new IOException();
  }

  @Override
  public void writeChar(int v) throws IOException {
    throw new IOException();
  }

  @Override
  public void writeInt(int v) throws IOException {
    throw new IOException();
  }

  @Override
  public void writeLong(long v) throws IOException {
    throw new IOException();
  }

  @Override
  public void writeFloat(float v) throws IOException {
    throw new IOException();
  }

  @Override
  public void writeDouble(double v) throws IOException {
    throw new IOException();
  }

  @Override
  public void writeBytes(String s) throws IOException {
    throw new IOException();
  }

  @Override
  public void writeChars(String s) throws IOException {
    throw new IOException();
  }

  @Override
  public void writeUTF(String s) throws IOException {
    throw new IOException();
  }

}
