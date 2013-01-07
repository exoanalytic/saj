package it.dangelo.saj.impl.parser;

import it.dangelo.saj.NumberValueCodec;
import it.dangelo.saj.SAJException;
import it.dangelo.saj.StringValueCodec;
import it.dangelo.saj.parser.ContentHandler;
import it.dangelo.saj.parser.ErrorHandler;
import it.dangelo.saj.parser.SAJParserException;
import static it.dangelo.saj.impl.parser.JsonToken.*;

import java.math.BigDecimal;

public class Parser {
	
	private ContentHandler contentHandler;
	private ErrorHandler errorHandler;
	private CharTokener charTokener;
	
	public Parser(CharStream charStream, ContentHandler contentHandler, ErrorHandler errorHandler) {
		this.charTokener = new CharTokener(charStream);
		this.contentHandler = contentHandler;
		this.errorHandler = errorHandler;
	}
	
	public void parse() throws SAJException {
		try {
			this.json();
		} catch (WrapperErrorException e) {
			throw new SAJException("Error in parsing of json stream at " + charTokener.getTokenLine()+":"+charTokener.getTokenCharInLine(), e.getSAJException());
		} catch (SAJParserException e) {
			throw e;
		} catch (SAJException e) {
			throw new SAJException("Error in parsing of json stream at " + charTokener.getTokenLine()+":"+charTokener.getTokenCharInLine(), e);
		}
	}
	
	private void json() throws SAJException {
		try {
			this.charTokener.mTokens();
			JsonToken token = this.charTokener.getToken();
			contentHandler.startJSON();
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
			this.contentHandler.endJSON();
		} catch (SAJParserException e) {
			try {
				errorHandler.fatalError(e);
			} catch (SAJException se) {
				throw new WrapperErrorException(se);
			}
		} 
	}
	
	private void array() throws SAJException {
		try {
			if(this.charTokener.getToken() != START_ARRAY)
				throw new SAJParserException("Expected start of array but found " + this.charTokener.getToken(), this.line(), this.charInLine());
			this.contentHandler.startArray();
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
			this.contentHandler.endArray();
		} catch (SAJParserException e) {
			try {
				errorHandler.fatalError(e);
			} catch (SAJException se) {
				throw new WrapperErrorException(se);
			}
		}
	}
	
	private void elements() throws SAJException {
		try {
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
		} catch (SAJParserException e) {
			try {
				errorHandler.fatalError(e);
			} catch (SAJException se) {
				throw new WrapperErrorException(se);
			}
		}
	}
	
	private void members() throws SAJException {
		try {
			this.pair();
			this.charTokener.mTokens();
			JsonToken token = this.charTokener.getToken();
			while(token == COMMA) {
				this.charTokener.mTokens();
				pair();
				this.charTokener.mTokens();
				token = this.charTokener.getToken();
			}
		} catch (SAJParserException e) {
			try {
				errorHandler.fatalError(e);
			} catch (SAJException se) {
				throw new WrapperErrorException(se);
			}
		}
	}
	
	private int line() {
		return this.charTokener.getTokenLine();
	}
	private int charInLine() {
		return this.charTokener.getTokenCharInLine();
	}
	
	private void pair() throws SAJException {
		try {
			if(this.charTokener.getToken() != STRING) 
				throw new SAJParserException("Expecting a json string how attribute name but found " + this.charTokener.getToken(), this.line(), this.charInLine());
			String attribute = string();
			this.contentHandler.attribute(attribute);
			this.charTokener.mTokens();
			if(this.charTokener.getToken() != PAIR_SEPARATOR)
				throw new SAJParserException("Expected a pair separator but found " + this.charTokener.getToken(), this.line(), this.charInLine());
			this.charTokener.mTokens();
			this.value();
		} catch (SAJParserException e) {
			try {
				errorHandler.fatalError(e);
			} catch (SAJException se) {
				throw new WrapperErrorException(se);
			}
		}
		
	}

	private String string() throws SAJException {
		if(this.charTokener.getToken() != STRING)
			throw new SAJParserException("Expecting a json string but found " + this.charTokener.getToken() , this.line(), this.charInLine());
		try {
			String attribute = StringValueCodec.extractString(this.charTokener.getTokenValue());
			attribute = attribute.substring(1, attribute.length()-1);
			return attribute;
		} catch (Exception e) {
			this.errorHandler.error(new SAJParserException("Error in string conversion", this.line(), this.charInLine(), e));
			return "";
		}
		
	}
	
	private BigDecimal number() throws SAJException {
		if(this.charTokener.getToken() != NUMBER)
			throw new SAJParserException("Expecting a json number but found " + this.charTokener.getToken(), this.line(), this.charInLine());
		try {
			return NumberValueCodec.convert(this.charTokener.getTokenValue());
		} catch (Exception e) {
			this.errorHandler.error(new SAJParserException("Error in number conversion", this.line(), this.charInLine(), e));
			return new BigDecimal(0);
		}
	}
	
	private void value() throws SAJException {
		try {
			JsonToken token = this.charTokener.getToken();
			switch (token) {
			case START_ARRAY:
				this.array();
				break;
			case START_OBJECT:
				this.object();
				break;
			case STRING:
				this.contentHandler._string(this.string());
				break;
			case NULL_VALUE:
				this.contentHandler._null();
				break;
			case NUMBER:
				BigDecimal number = this.number();
				this.contentHandler._number(number);
				break;
			case BOOLEAN_FALSE:
				this.contentHandler._boolean(false);
				break;
			case BOOLEAN_TRUE:
				this.contentHandler._boolean(true);
				break;
			default:
				throw new SAJParserException("Unexpected token. Found " + token, this.line(), this.charInLine());
			}
		} catch (SAJParserException e) {
			try {
				errorHandler.fatalError(e);
			} catch (SAJException se) {
				throw new WrapperErrorException(se);
			}
		}
	}
	
	private void object() throws SAJException {
		try {
			if(this.charTokener.getToken() != START_OBJECT)
				throw new SAJParserException("Expected a start object but found " + this.charTokener.getToken(), this.charTokener.getTokenLine(), this.charTokener.getTokenCharInLine());
			this.contentHandler.startObject();
			this.charTokener.mTokens();
			JsonToken token = this.charTokener.getToken();
			if(token == STRING) {
				this.members();
			}
			token = this.charTokener.getToken();
			if(token != END_OBJECT) 
				throw new SAJParserException("Expecting end of object but found " + this.charTokener.getToken(), this.line(), this.charInLine());
			this.contentHandler.endObject();
		} catch (SAJParserException e) {
			try {
				errorHandler.fatalError(e);
			} catch (SAJException se) {
				throw new WrapperErrorException(se);
			}
		}
	}
}
