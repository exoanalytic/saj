package it.dangelo.saj.parser;

import it.dangelo.saj.SAJException;

/**
 * Indicate an unknown or unsupported feature 
 * 
 *
 */
@SuppressWarnings("serial")
public class SAJNotSupportedException extends SAJException {

	public SAJNotSupportedException(String message) {
		super(message);
	}

	public SAJNotSupportedException(String message, Throwable cause) {
		super(message, cause);
	}

}
