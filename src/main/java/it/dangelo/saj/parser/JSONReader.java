package it.dangelo.saj.parser;

import it.dangelo.saj.SAJException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;

/**
 * Interface for reading an JSON document using callbacks.
 *
 */
public interface JSONReader {
	/**
	 * Parse a json document
	 * 
	 * @param jsonString
	 * @throws IOException
	 * @throws SAJException
	 */
	void parse(String jsonString) throws SAJException;
	/**
	 * Parse a json document
	 * 
	 * @param jsonString
	 * @param uri URI of the json stream. Used to identify the stream, Can be null
	 * @throws IOException
	 * @throws SAJException
	 */
	void parse(String jsonString, URI uri) throws SAJException;

	/**
	 * Parse a json document
	 * 
	 * @param inputStream
	 * @throws IOException
	 * @throws SAJException
	 */
	void parse(InputStream inputStream) throws IOException, SAJException;
	/**
	 * Parse a json document
	 * 
	 * @param inputStream
	 * @param uri URI of the json stream. Used to identify the stream. Can be null
	 * @throws IOException
	 * @throws SAJException
	 */
	void parse(InputStream inputStream, URI uri) throws IOException, SAJException;

	/**
	 * Parse a json document
	 * 
	 * @param reader
	 * @throws IOException
	 * @throws SAJException
	 */
	void parse(Reader reader) throws IOException, SAJException;
	/**
	 * Parse a json document
	 * 
	 * @param reader
	 * @param uri URI of the json stream. Used to identify the stream. Can be null
	 * @throws IOException
	 * @throws SAJException
	 */
	void parse(Reader reader, URI uri) throws IOException, SAJException;
	
	/**
	 * Set the {@link ContentHandler} that will receive parser's events.
	 * @param contentHandler Instance of ContentHandler that manage events
	 */
	void setContentHandler(ContentHandler contentHandler);
	/**
	 * Set the {@link ErrorHandler} that will receive all parsing error events.
	 */
	void setErrorHandler(ErrorHandler errorHandler);
	/**
	 * Return ContentHandler
	 * @return Instance of the ContentHandler that receive parsing events 
	 */
	ContentHandler getContentHandler();
	/**
	 * Return ErrorHAndler
	 * @return Instance of ErrorHAndler that receive parse error events.
	 */
	ErrorHandler getErrorHandler();
	
	/**
	 * Set the implementation of {@link ResourceResolver} used to resolve external link
	 * @param resolver {@link ResourceResolver}
	 */
	void setResourceResolver(ResourceResolver resolver);
	
	/**
	 * Get the resolver used for external link
	 * @return {@link ResourceResolver} used.
	 */
	ResourceResolver getResourceResolver();
}
