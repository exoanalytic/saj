package it.dangelo.saj.parser;

import it.dangelo.saj.SAJException;

import java.math.BigDecimal; 
/**
 * Receive notification of the logical content of a document.<BR/>
 * This is the main interface to receive basic parsing events.<BR/>
 * Developers must implement this interface and register the instance with the {@link JSONReader#setContentHandler}  method. 
 * The parser uses the instance to report basic document-related events like the start and end of elements and character data.<BR/>
 * The order of events in this interface is very important, and mirrors the order of information in the document itself.
 * 
 */
public interface ContentHandler {
	/**
	 * This is the first event called. This event suggest that a json document will read.
	 * @throws SAJException Any SAX exception, possibly wrapping another exception.
	 */
	void startJSON() throws SAJException;
	/**
	 * This is the last event called. This event suggest that a json document has been read.
	 * @throws SAJException Any SAX exception, possibly wrapping another exception.
	 */
	void endJSON() throws SAJException;

	/**
	 * This event suggest that an object will be read. This event is called when the parser meets a left brace.
	 * @throws SAJException Any SAX exception, possibly wrapping another exception.
	 */
	void startObject() throws SAJException;
	/**
	 * This event suggest that a reading of the object has been finished. This event is called when the parser meets a right brace.
	 * @throws SAJException Any SAX exception, possibly wrapping another exception.
	 */
	void endObject() throws SAJException;
	
	/**
	 * This event suggest that an array will be read. This event is called when the parser meets a left bracket.
	 * @throws SAJException Any SAX exception, possibly wrapping another exception.
	 */
	void startArray() throws SAJException;
	/**
	 * This event suggest that a reading of the array has been finished. This event is called when the parser meets a right bracket.
	 * @throws SAJException Any SAX exception, possibly wrapping another exception.
	 */
	void endArray() throws SAJException;
	
	/**
	 * This event is called when an attribute name has been read.
	 * @param name Attribute name. The string is encoded.
	 * @throws SAJException Any SAX exception, possibly wrapping another exception.
	 */
	void attribute(String name) throws SAJException;
	
	/**
	 * This event is called when a string value (not an attribute) has been read.
	 * @param value The string value. The string is encoded.
	 * @throws SAJException Any SAX exception, possibly wrapping another exception.
	 */
	void _string(String value) throws SAJException;

	/**
 	 * This event is called when a boolean value has been read.
	 * @param value The boolean value
	 * @throws SAJException Any SAX exception, possibly wrapping another exception.
	 */
	void _boolean(boolean value) throws SAJException;
	
	/**
 	 * This event is called when a numeric value has been read.
	 * @param value The numeric value. BigDecimal is the preferred type in order to manage all number types.
	 * @throws SAJException Any SAX exception, possibly wrapping another exception.
	 */
	void _number(BigDecimal value) throws SAJException;
	
	/**
 	 * This event is called when a null value has been read.
	 * @throws SAJException Any SAX exception, possibly wrapping another exception.
	 */
	void _null() throws SAJException;
}
