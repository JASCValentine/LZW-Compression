package com.jascvalentine.lzw;

import java.io.DataInput;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class LZWInputStream extends FilterInputStream {
	public static final int DICTIONARY_SIZE = 4096; // requirement
	public static final int INITIAL_SIZE = 256; // requirement

	private Map<Integer, byte[]> dictionary;
	private int dictionaryIndex;
	private Queue<Byte> cache;
	private boolean first;
	private int cW;	

	public LZWInputStream(DataInput in, Map<Integer, byte[]> dictionary) {
		super((InputStream)in);
		this.dictionary = dictionary;
		this.dictionaryIndex = INITIAL_SIZE;
		this.cache = new ArrayDeque<Byte>();
		this.first = true;
	}
	
	public static Map<Integer, byte[]> getDefaultDictionary() {
		return new HashMap<Integer, byte[]>(DICTIONARY_SIZE - INITIAL_SIZE);
	}

	private byte[] lookupPhase(int index) {		
		return index < INITIAL_SIZE ? new byte[] { (byte) index } : dictionary.get(Integer.valueOf(index));
	}

	private void appendToCache(byte[] buffer) {
		for (byte b : buffer) {
			boolean offer = cache.offer(Byte.valueOf(b));
			assert offer;
		}
	}

	@Override
	public int read() throws IOException {
		Byte b = cache.poll();
		if (b != null) {
			return b.intValue();
		}

		if (first) {
			first = false;
			try {
				cW = ((DataInput)in).readInt();
				return cW;
			} catch (EOFException ex) {
				return -1;
			}
		}

		// fill the buffer
		int pW = cW;
		try {
			cW = ((DataInput)in).readInt();	
		} catch (EOFException ex) {
			// if no more input, return buffer content
			b = cache.poll();
			return b == null ? -1 : b.intValue(); 
		}
		
		byte[] s = lookupPhase(cW);

		byte c;
		byte[] p;
		if (s != null) {
			appendToCache(s);
			c = s[0];
			p = lookupPhase(pW);
		} else {
			p = lookupPhase(pW);
			c = p[0];
			appendToCache(p);
			cache.offer(c);
		}

		// p + c
		byte[] pc = Arrays.copyOf(p, p.length + 1);		
		pc[p.length] = c;

		dictionary.put(Integer.valueOf(dictionaryIndex++), pc);
		if (dictionary.size() >= DICTIONARY_SIZE)
			dictionary.clear();

		return cache.poll().intValue();
	}
}
