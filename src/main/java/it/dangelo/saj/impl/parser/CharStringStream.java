package it.dangelo.saj.impl.parser;


import java.io.IOException;

public class CharStringStream extends CharStream {
	
	private String stream;
	private int index = 0;
	private int length = 0;
	
	public CharStringStream(String stream) {
		super();
		this.stream = stream;
		this.length = this.stream.length();
	}

	@Override
	protected char readNextChar() throws IOException{
		if(this.index < this.length)
			return this.stream.charAt(this.index++);
		return EOF;
	}

	@Override
	public void close() throws IOException {
	}

}
