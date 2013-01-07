package it.dangelo.saj.impl;

import it.dangelo.saj.Locator;

public class LocatorImpl implements Locator{

	private int columnNumber;
	private int lineNumber;
	public LocatorImpl(int columnNumber, int lineNumber) {
		super();
		this.columnNumber = columnNumber;
		this.lineNumber = lineNumber;
	}
	public LocatorImpl() {
		super();
		this.columnNumber = 1;
		this.lineNumber = 1;
	}
	public int getColumnNumber() {
		return columnNumber;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	void incrementColumnNumber() {
		this.columnNumber++;
	}
	
	void incrementLineNumber() {
		this.lineNumber++;
		this.columnNumber = 1;
	}
	void decrementColumnNumber() {
		this.columnNumber--;
	}
	
	void decrementLineNumber() {
		this.lineNumber--;
	}

}
