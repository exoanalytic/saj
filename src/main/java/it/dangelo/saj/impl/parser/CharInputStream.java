package it.dangelo.saj.impl.parser;

import java.io.IOException; 
import java.io.InputStream;

public class CharInputStream extends CharStream {
	
	private InputStream stream;
	
	public CharInputStream(InputStream stream) {
		super();
		this.stream = stream;
	}

	@Override
	protected char readNextChar() throws IOException{
		return (char) stream.read();
	}

	@Override
	public void close() throws IOException {
		this.stream.close();
	}

}
