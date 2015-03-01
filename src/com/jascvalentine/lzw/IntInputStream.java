package com.jascvalentine.lzw;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class IntInputStream extends DataInputStreamAdapter {
	private int buffer; // only use the 4 rightmost bits
	private boolean occupied; // whether the buffer is full or empty

	public IntInputStream(InputStream in) {
		super(in);
	}

	@Override
	public int readInt() throws IOException {
		if (!occupied) {
			// read next 8 bits (total 16 bits)
			int ch1 = in.read();
			int ch2 = in.read();
			if ((ch1 | ch2) < 0) {
				// incomplete 12 bit codeword
				throw new EOFException();
			}

			// put last 4 bits to buffer
			buffer = ch2 & 0xF;
			occupied = true;

			// return first 12 bits as integer
			return ch1 << 4 | ch2 >>> 4;
		} else {
			int ch1 = in.read();
			if (ch1 < 0) {
				// incomplete 12 bit codeword
				throw new EOFException();
			}
			
			occupied = false;

			// buffer as first 4 bits of returns, remaining bits from read value
			return buffer << 8 | ch1;
		}
	}

	public static void main(String[] args) throws Exception {
		byte[] b = new byte[] { 0x06, 0x10 };
		IntInputStream in = new IntInputStream(new ByteArrayInputStream(b));
		try {
			while (true) {
				System.out.print(in.readInt() + ", ");
			}
		} catch (EOFException ex) {
			System.out.println("end");
		}
	}
}
