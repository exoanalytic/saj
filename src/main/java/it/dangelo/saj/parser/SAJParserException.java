package it.dangelo.saj.parser;

import it.dangelo.saj.Locator;
import it.dangelo.saj.SAJException;

/**
 * Encapsulate a json parser error or warning 
 *
 */
public class SAJParserException extends SAJException {

	private Locator locator;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8032952754484746632L;

	public SAJParserException(String message, Locator locator) {
		super(message);
		this.locator = locator;
	}

	public SAJParserException(String message, Locator locator, Throwable cause) {
		super(message, cause);
		this.locator = locator;
	}
	
	public SAJParserException(String message, int lineNumber, int columnNumber) {
		super(message);
		final int columnNumber_ = columnNumber;
		final int lineNumber_ = lineNumber;
		this.locator = new Locator() {

			public int getColumnNumber() {
				return columnNumber_;
			}

			public int getLineNumber() {
				return lineNumber_;
			}};
	}

	public SAJParserException(String message, int lineNumber, int columnNumber, Throwable cause) {
		super(message, cause);
		final int columnNumber_ = columnNumber;
		final int lineNumber_ = lineNumber;
		this.locator = new Locator() {

			public int getColumnNumber() {
				return columnNumber_;
			}

			public int getLineNumber() {
				return lineNumber_;
			}};
	}

	public Locator getLocator() {
		return locator;
	}
	
	@Override
	public String getMessage() {
		StringBuffer buffer = new StringBuffer();
		if(super.getMessage() != null) buffer.append(super.getMessage());
		else buffer.append("<Null message>");
		if(this.locator != null ) {
			buffer.append(" Location : row = ").append(this.locator.getLineNumber() );
			buffer.append(", col = ").append(this.locator.getColumnNumber());
		}
		return buffer.toString();
	}
	
}
