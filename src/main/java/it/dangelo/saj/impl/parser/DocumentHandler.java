package it.dangelo.saj.impl.parser;

import static it.dangelo.saj.impl.parser.JsonToken.COMMA;
import static it.dangelo.saj.impl.parser.JsonToken.END_ARRAY;
import static it.dangelo.saj.impl.parser.JsonToken.END_OBJECT;
import static it.dangelo.saj.impl.parser.JsonToken.NUMBER;
import static it.dangelo.saj.impl.parser.JsonToken.PAIR_SEPARATOR;
import static it.dangelo.saj.impl.parser.JsonToken.START_ARRAY;
import static it.dangelo.saj.impl.parser.JsonToken.START_OBJECT;
import static it.dangelo.saj.impl.parser.JsonToken.STRING;
import it.dangelo.saj.NumberValueCodec;
import it.dangelo.saj.SAJException;
import it.dangelo.saj.StringValueCodec;
import it.dangelo.saj.parser.SAJParserException;

import java.math.BigDecimal;
import java.net.URI;

public class DocumentHandler {

	private EventPipeline pipeline;
	private CharTokener charTokener;
	private Handlers handlers;
	private URI jsonReference;
	
	public DocumentHandler(CharStream charStream, EventPipeline pipeline) {
		this.charTokener = new CharTokener(charStream);
		this.pipeline = pipeline;
		this.handlers = pipeline.getHandlers();
	}
	
	public void setJsonReference(URI jsonReference) {
		this.jsonReference = jsonReference;
	}
	
	public URI getJsonReference() {
		return jsonReference;
	}
	
	public Handlers getHandlers() {
		return handlers;
	}
	
	public void parse() throws SAJException {
		try {
			this.json();
		} catch (SAJParserException e) {
			this.handlers.getErrorHandler().fatalError(e);
		} catch(SAJException e) {
			throw new SAJException("Error in line " + this.line() + " : position " + this.charInLine() + " see below : ", e);
		}
	}
	
	private void json() throws SAJException {
		this.charTokener.mTokens();
		JsonToken token = this.charTokener.getToken();
		this.pipeline.startJSON();
		switch (token) {
		case START_ARRAY:
			this.array();
			break;
		case START_OBJECT:
			this.object();
			break;
		default:
			throw new SAJParserException("JSON stream must start with array or object. Found " +token , this.charTokener.getTokenLine(), this.charTokener.getTokenCharInLine());
		}
		this.pipeline.endJSON();
	}
	
	private void array() throws SAJException {
		if(this.charTokener.getToken() != START_ARRAY)
			throw new SAJParserException("Expected start of array but found " + this.charTokener.getToken(), this.line(), this.charInLine());
		this.pipeline.startArray();
		this.charTokener.mTokens();
		JsonToken token = this.charTokener.getToken();
		switch (token) {
		case BOOLEAN_FALSE:
		case BOOLEAN_TRUE:
		case START_ARRAY:
		case START_OBJECT:
		case NULL_VALUE:
		case NUMBER:
		case STRING:
			this.elements();
		default:
		}
		token = this.charTokener.getToken();
		if(token != END_ARRAY)
			throw new SAJParserException("Expected end of array but found " + token, this.line(), this.charInLine());
		this.pipeline.endArray();
	}
	
	private void elements() throws SAJException {
		switch (this.charTokener.getToken()) {
		case BOOLEAN_FALSE:
		case BOOLEAN_TRUE:
		case START_ARRAY:
		case START_OBJECT:
		case NULL_VALUE:
		case NUMBER:
		case STRING:
			break;
		default:
			throw new SAJParserException("Unexpected value. Found " + this.charTokener.getToken(), this.line(), this.charInLine());
		}
		this.value();
		this.charTokener.mTokens();
		JsonToken token = this.charTokener.getToken();
		while(token == COMMA) {
			this.charTokener.mTokens();
			value();
			this.charTokener.mTokens();
			token = this.charTokener.getToken();
		}
	}
	
	private void members() throws SAJException {
		this.pair();
		this.charTokener.mTokens();
		JsonToken token = this.charTokener.getToken();
		while(token == COMMA) {
			this.charTokener.mTokens();
			pair();
			this.charTokener.mTokens();
			token = this.charTokener.getToken();
		}
	}
	
	private int line() {
		return this.charTokener.getTokenLine();
	}
	private int charInLine() {
		return this.charTokener.getTokenCharInLine();
	}
	
	private void pair() throws SAJException {
		if(this.charTokener.getToken() != STRING) 
			throw new SAJParserException("Expecting a json string how attribute name but found " + this.charTokener.getToken(), this.line(), this.charInLine());
		String attribute = string();
		this.pipeline.attribute(attribute);
		this.charTokener.mTokens();
		if(this.charTokener.getToken() != PAIR_SEPARATOR)
			throw new SAJParserException("Expected a pair separator but found " + this.charTokener.getToken(), this.line(), this.charInLine());
		this.charTokener.mTokens();
		this.value();
	}

	private String string() throws SAJException {
		if(this.charTokener.getToken() != STRING)
			throw new SAJParserException("Expecting a json string but found " + this.charTokener.getToken() , this.line(), this.charInLine());
		String tokenValue = this.charTokener.getTokenValue();
		try {
			String attribute = StringValueCodec.extractString(tokenValue);
			attribute = attribute.substring(1, attribute.length()-1);
			return attribute;
		} catch (Exception e) {
			throw new SAJParserException("Invalid string " +tokenValue +" in " + this.charTokener.getToken() , this.line(), this.charInLine());
		}
	}
	
	private BigDecimal number() throws SAJException {
		if(this.charTokener.getToken() != NUMBER)
			throw new SAJParserException("Expecting a json number but found " + this.charTokener.getToken(), this.line(), this.charInLine());
		String tokenValue = this.charTokener.getTokenValue();
		try {
			return NumberValueCodec.convert(tokenValue);
		} catch (Exception e) {
			throw new SAJParserException("Invalid number " +tokenValue+" in " + this.charTokener.getToken(), this.line(), this.charInLine());
		}
	}
	
	private void value() throws SAJException {
		JsonToken token = this.charTokener.getToken();
		switch (token) {
		case START_ARRAY:
			this.array();
			break;
		case START_OBJECT:
			this.object();
			break;
		case STRING:
			this.pipeline._string(this.string());
			break;
		case NULL_VALUE:
			this.pipeline._null();
			break;
		case NUMBER:
			BigDecimal number = this.number();
			this.pipeline._number(number);
			break;
		case BOOLEAN_FALSE:
			this.pipeline._boolean(false);
			break;
		case BOOLEAN_TRUE:
			this.pipeline._boolean(true);
			break;
		default:
			throw new SAJParserException("Unexpected token. Found " + token, this.line(), this.charInLine());
		}
	}
	
	private void object() throws SAJException {
		if(this.charTokener.getToken() != START_OBJECT)
			throw new SAJParserException("Expected a start object but found " + this.charTokener.getToken(), this.charTokener.getTokenLine(), this.charTokener.getTokenCharInLine());
		this.pipeline.startObject();
		this.charTokener.mTokens();
		JsonToken token = this.charTokener.getToken();
		if(token == STRING) {
			this.members();
		}
		token = this.charTokener.getToken();
		if(token != END_OBJECT) 
			throw new SAJParserException("Expecting end of object but found " + this.charTokener.getToken(), this.line(), this.charInLine());
		this.pipeline.endObject();
	}
}
