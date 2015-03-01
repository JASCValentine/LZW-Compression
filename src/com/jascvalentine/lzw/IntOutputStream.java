package com.jascvalentine.lzw;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class IntOutputStream extends DataOutputStreamAdapter {	
	private int buffer; // only use the 4 rightmost bits
	private boolean occupied; // whether the buffer is full or empty
	
	public IntOutputStream(OutputStream out) {
		super(out);
	}
	
	@Override
	public void writeInt(int v) throws IOException {
		if (v < 0 || v > 0xFFF)
			throw new IOException("invalid value");
		
		if (!occupied) {
			// write the first 8 bits
			write(v >>> 4);
			
			buffer = v & 0xF;
			occupied = true;
		} else {
			// write the buffer and the first 4 bits of next value
			write(buffer << 4 | v >>> 8);
			// write the last 8 bits of the next value;
			write(v & 0xFF);
			occupied = false;
		}
	}

	@Override
	public void flush() throws IOException {
		if (occupied) {
			out.write(buffer << 4); // implicitly pad 0's
		}
		out.flush();
	}
	
	public static void main(String[] args) throws Exception {
		int[] input = new int[] { 97 };
		byte[] expected = new byte[] { 0x06, 0x10 };
		
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		IntOutputStream out = new IntOutputStream(bout);
		for (int i : input) {
			out.writeInt(i);
		}
		out.close();
		
		System.out.println(Arrays.equals(bout.toByteArray(), expected));		
	}
}
