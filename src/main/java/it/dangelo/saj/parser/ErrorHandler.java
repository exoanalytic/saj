package it.dangelo.saj.parser;

import it.dangelo.saj.SAJException;


/**
 * Basic interface for SAJ error handler 
 *
 */
public interface ErrorHandler {
	
	/**
	 * Receive notification of a recoverable error.
	 * 
	 * @param parserException The error information encapsulated in a SAJ parse exception.
	 */
	void error(SAJException parserException) throws SAJException;
	
	/**
	 * Receive notification of a non-recoverable error.
	 * 
	 * @param parserException The error information encapsulated in a SAX parse exception.
	 */
	void fatalError(SAJParserException parserException) throws SAJException;
	
	/**
	 * Receive notification of a warning.
	 * 
	 * @param parserException The warning information encapsulated in a SAX parse exception.
	 */
	void warnin(SAJException parserException) throws SAJException;
	
}
