package it.dangelo.saj.impl;

import it.dangelo.saj.SAJException;
import it.dangelo.saj.impl.parser.CharInputStream;
import it.dangelo.saj.impl.parser.CharReaderStream;
import it.dangelo.saj.impl.parser.CharStream;
import it.dangelo.saj.impl.parser.CharStringStream;
import it.dangelo.saj.impl.parser.DocumentHandler;
import it.dangelo.saj.parser.ContentHandler;
import it.dangelo.saj.parser.ErrorHandler;
import it.dangelo.saj.parser.JSONReader;
import it.dangelo.saj.parser.ResourceResolver;
import it.dangelo.saj.validation.schemas.Schema;
import it.dangelo.saj.validation.schemas.SchemaReference;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class JSONReaderImpl implements JSONReader{
	
	private HashMap<String, Object> properties = new HashMap<String, Object>();
	private ParserConfiguration parserConfiguration = new ParserConfiguration();
	
	public JSONReaderImpl(HashMap<String, Object> properties) {
		if(properties != null)
			this.properties = properties;
			
	}
	
	public JSONReaderImpl() {
		this(null);
	}

	public ContentHandler getContentHandler() {
		return this.parserConfiguration.resolveHandlers(properties).getContentHandler();
	}

	public ErrorHandler getErrorHandler() {
		return this.parserConfiguration.resolveHandlers(properties).getErrorHandler();
	}
	
	public void setValidate(boolean validate) {
		this.properties.put("validation.schema", validate);
	}

	public void parse(InputStream inputStream, URI uri) throws IOException, SAJException{
		this.parseStream(new CharInputStream(inputStream), uri);
	}

	public void parse(Reader reader, URI uri) throws IOException, SAJException {
		parseStream(new CharReaderStream(reader), uri);
	}

	DocumentHandler handler; 
	public void parseStream(CharStream charStream, URI uri) throws SAJException {
		handler = new DocumentHandler(charStream, 
				this.parserConfiguration.buildEventPipeline(properties, uri));
		handler.setJsonReference(uri);
		handler.parse();
		/*
		Parser parser = new Parser(charStream, this.getInternalContentHandler(), this.errorHandler);
		parser.parse();
		*/
		
	}
	
	public int getOffset() {
		return handler.getOffset();
	}
	
	/*
	private ContentHandler getInternalContentHandler() throws SAJException{
		if(this.validate) {
			Schema schema = null;
			if(this.schemas == null || !this.schemas.containsKey(SchemaReference.DEFAULT_REFERENCE))
				throw new SAJParserException("No default schema reference for validation", 0, 0);
			schema = this.schemas.get(SchemaReference.DEFAULT_REFERENCE);
			SchemaValidator schemaValidator = new SchemaValidator((SchemaImpl) schema, this.contentHandler);
			return schemaValidator;
		}else
			return this.contentHandler;
			
	}
	*/
	public void setSchemas(Map<SchemaReference, Schema> schemas) {
		this.properties.put("validation.schema.reference", schemas);
	}
	
	public void setContentHandler(ContentHandler contentHandler) {
		this.properties.put(ParserConfiguration.CONTENT_HANDLER_PROPERTY, contentHandler);
	}

	public void setErrorHandler(ErrorHandler errorHandler) {
		this.properties.put(ParserConfiguration.ERROR_HANDLER_PROPERTY, errorHandler);
	}

	public void parse(String jsonString, URI uri) throws SAJException {
		this.parseStream(new CharStringStream(jsonString), uri);
	}

	public ResourceResolver getResourceResolver() {
		return (ResourceResolver) this.properties.get(ParserConfiguration.RESOURCE_RESOLVER_PROPERTY);
	}

	public void setResourceResolver(ResourceResolver resolver) {
		this.properties.put(ParserConfiguration.RESOURCE_RESOLVER_PROPERTY, resolver);
	}

	public void parse(String jsonString) throws SAJException {
		this.parse(jsonString, null);
	}

	public void parse(InputStream inputStream) throws IOException, SAJException {
		this.parse(inputStream, null);
	}

	public void parse(Reader reader) throws IOException, SAJException {
		this.parse(reader, null);
	}
	

}
