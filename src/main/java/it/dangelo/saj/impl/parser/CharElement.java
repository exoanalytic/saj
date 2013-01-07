package it.dangelo.saj.impl.parser;

public class CharElement {
	private char c;
	private int line;
	private int charPosition;
	private int charPositionInLine;
	
	public CharElement() {
	}

	
	
	public CharElement(char c, int line, int charPosition,
			int charPositionInLine) {
		super();
		this.c = c;
		this.line = line;
		this.charPosition = charPosition;
		this.charPositionInLine = charPositionInLine;
	}



	public char getChar() {
		return c;
	}
	public void setChar(char c) {
		this.c = c;
	}
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public int getCharPosition() {
		return charPosition;
	}
	public void setCharPosition(int charPosition) {
		this.charPosition = charPosition;
	}
	public int getCharPositionInLine() {
		return charPositionInLine;
	}
	public void setCharPositionInLine(int charPositionInLine) {
		this.charPositionInLine = charPositionInLine;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{ char : ").append(this.c).
		append(", line : ").append(this.line).
		append(", charPosition : ").append(this.charPosition).
		append(", charPositionInLine : ").append(this.charPositionInLine);
		return buffer.toString();
	}
}
