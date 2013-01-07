package it.dangelo.saj.impl.parser;

import java.io.IOException;
import java.io.Reader;

public class CharReaderStream extends CharStream {
	
	private Reader reader;
	
	public CharReaderStream(Reader reader) {
		super();
		this.reader = reader;
	}

	@Override
	protected char readNextChar() throws IOException{
		return (char) reader.read();
	}

	@Override
	public void close() throws IOException {
		this.reader.close();
	}

}
