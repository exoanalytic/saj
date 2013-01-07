package it.dangelo.saj.impl.parser;


import java.io.IOException;

public abstract class CharStream {
	public static final char EOF = (char)-1; 
	protected int bufferSize = 10;
	private CharElement[] buffer = null;
	private int line = 0;
	private int charPosition = 0;
	private int charPositionInLine = 0;
	private int mark = -1;
	private static final CharElement EOF_ELEMENT = new CharElement(EOF, 0, 0, 0);
	public char getChar() {
		return this.getCharElement().getChar();
	}
	
	protected abstract char readNextChar() throws IOException;
	
	public void mark(int mark) throws IOException {
		if(mark > this.bufferSize)
			throw new IOException("Mark is greater of buffer ");
		if(this.mark == 0) return;
		this.mark = mark;
	}
	
	public char consume() throws IOException{
		if(mark > 0) {
			this.mark--;
			CharElement element = this.buffer[this.mark];
			char c = EOF;
			if(element != null)
				c = element.getChar();
			if(this.mark == 0) this.mark = -1;
			return c;
		}
		char c = this.readNextChar();
		if(c == EOF) return EOF;
		if(this.line == 0 || c == '\n') {
			this.line++;
			this.charPositionInLine = 0;
		}else
			this.charPositionInLine++;
		this.charPosition++;
		CharElement element = new CharElement(c, this.line, this.charPosition, this.charPositionInLine);
		this.addInBuffer(element);
		return c;
	}
	
	public CharElement getCharElement() {
		CharElement element = null;
		if(this.mark > -1)
			element = this.buffer[this.mark];
		else
			element = this.buffer[0];
		if(element == null)
			return EOF_ELEMENT;
		return element;
	}
	
	public char prev(int i) {
		if(this.mark > -1)
			i += this.mark;
		if(i > this.bufferSize)
			throw new IllegalArgumentException("Too many steps");
		CharElement charElement = this.buffer[i];
		if(charElement == null)
			return EOF;
		return charElement.getChar();
	}
	
	public CharElement prevCharacterElement(int i) {
		if(this.mark > -1)
			i += this.mark;
		if(i > this.bufferSize)
			throw new IllegalArgumentException("Too many steps");
		CharElement charElement = this.buffer[i];
		if(charElement == null)
			return EOF_ELEMENT;
		return charElement;
	}
	
	private void addInBuffer(CharElement element) {
		if(this.buffer == null)
			this.buffer = new CharElement[this.bufferSize];
		for (int i = this.bufferSize-1; i > 0; i--) {
			this.buffer[i] = this.buffer[i-1]; 
		}
		
//		for (int i = 0; i < (this.buffer.length -1); i++) {
//			this.buffer[i+1] = this.buffer[i];
//		}
		this.buffer[0] = element;
	}
	
	public abstract void close() throws IOException;

	public int getOffset() {
		return this.charPosition;
	}
}
