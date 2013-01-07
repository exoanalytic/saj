package it.dangelo.saj.impl.parser;

import it.dangelo.saj.Consts;
import it.dangelo.saj.parser.SAJParserException;

import java.io.IOException;

public class CharTokener {
	private CharStream charStream;
	
    private StringBuffer tokenBuffer = new StringBuffer();
    private JsonToken token = JsonToken.EOF;

    public CharTokener(CharStream charStream) {
    	this.charStream = charStream;
	}
    
    private void mWS() throws SAJParserException {
    	this.token = JsonToken.WS;
    	try {
        	boolean cont = true;
        	while(cont) {
        		char c = this.charStream.consume();
        		switch (c) {
    			case ' ':
    			case '\t':
    			case '\n':
    			case '\r':
    				continue;
    			default:
    				cont = false;
    			}
        	}
        	this.charStream.mark(1);
        	this.mTokens();
		} catch (IOException e) {
			throw new SAJParserException("Error to read the stream ", this.charStream.getCharElement().getLine(), this.charStream.getCharElement().getCharPositionInLine());
		}
    }
    
    private void mCOMMENT() throws SAJParserException {
    	try {
        	char c = this.charStream.getChar();
        	if(c == '*') {
        		boolean asterisk = false;
        		while(true) {
        			c = this.charStream.consume();
        			if(c == '/' && asterisk) {
        				break;
        			}
        			if(c == '*') {
        				asterisk = true;
        			} else
        				asterisk = false;
        		}
        	}
        	if(c=='/') {
        		while(true) {
        			c = this.charStream.consume();
        			if(c == '\n') {
        				break;
        			}
        		}
        	}
		} catch (Exception e) {
			throw new SAJParserException("Error to read the stream ", this.charStream.getCharElement().getLine(), this.charStream.getCharElement().getCharPositionInLine(), e);
		}
    	this.mTokens();
    	
    }
    
    private void mSTART_OBJECT() throws SAJParserException {
    	this.token = JsonToken.START_OBJECT;
    	this.match(Consts.START_OBJECT);
    }
    private void mSTART_ARRAY() throws SAJParserException {
    	this.token = JsonToken.START_ARRAY;
    	this.match(Consts.START_ARRAY);
    }
    private void mEND_OBJECT() throws SAJParserException {
    	this.token = JsonToken.END_OBJECT;
    	this.match(Consts.END_OBJECT);
    }
    private void mEND_ARRAY() throws SAJParserException {
    	this.token = JsonToken.END_ARRAY;
    	this.match(Consts.END_ARRAY);
    }
    
    private void mCOMMA() throws SAJParserException {
    	this.token = JsonToken.COMMA;
    	this.match(Consts.MEMBER_SEPARATOR);
    }
    
    private void mPAIR_SEPARATOR() throws SAJParserException {
    	this.token = JsonToken.PAIR_SEPARATOR;
    	this.match(Consts.PAIR_SEPARATOR);
    }
    
    private void mSTRING() throws SAJParserException{
    	this.token = JsonToken.STRING;
    	try  {
	    	this.cleanTokenBuffer();
	    	this.tokenBuffer.append(this.charStream.getChar());
	    	boolean escape = false;
	    	while(true) {
	    		char c = this.charStream.consume();
	    		if(c == CharStream.EOF)
	    			throw new SAJParserException("Unexpected EOF in string", 0,0);
	    		this.tokenBuffer.append(c);
	    		if(c == '"' && !escape) 
	    			break;
	    	}
    	}catch(SAJParserException exception) {
    		throw exception;
    	}catch(Exception exception) {
    		CharElement element = this.charStream.prevCharacterElement(1);
    		throw new SAJParserException("Error in stream ", element.getLine(), element.getCharPositionInLine(), exception);
    	}
    	
    }
    
    private void mBOOLEAN_FALSE() throws SAJParserException {
    	this.token = JsonToken.BOOLEAN_FALSE;
    	this.match(Consts.BOOLEAN_FALSE_VALUE);
    }
    
    private void mBOOLEAN_TRUE() throws SAJParserException {
    	this.token = JsonToken.BOOLEAN_TRUE;
    	this.match(Consts.BOOLEAN_TRUE_VALUE);
    }
    
    private void mNULL_VALUE() throws SAJParserException {
    	this.token = JsonToken.NULL_VALUE;
    	this.match(Consts.NULL_VALUE);
    }
    
    private void match(String value) throws SAJParserException {
    	try {
    		char[] cs = value.toCharArray();
    		if(this.charStream.getChar() != cs[0])
    			throw new SAJParserException("Invalid match " + value, this.charStream.getCharElement().getLine(), this.charStream.getCharElement().getCharPositionInLine());
    		for (int i = 1; i < cs.length; i++) {
        		if(this.charStream.consume() != cs[i])
        			throw new SAJParserException("Invalid match " + value, this.charStream.getCharElement().getLine(), this.charStream.getCharElement().getCharPositionInLine());
			}
		} catch (IOException e) {
			throw new SAJParserException("IO error in stream", this.charStream.getCharElement().getLine(), this.charStream.getCharElement().getCharPositionInLine(), e);
		}
    }
    
    private void match(char value) throws SAJParserException {
		if(this.charStream.getChar() != value)
			throw new SAJParserException("Invalid match " + this.charStream.getChar(), this.charStream.getCharElement().getLine(), this.charStream.getCharElement().getCharPositionInLine());
    }
    
    private void mNUMBER() throws SAJParserException{
    	this.token = JsonToken.NUMBER;
    	try {
	    	this.cleanTokenBuffer();
	    	this.tokenBuffer.append(this.charStream.getChar());
	    	boolean contin = true;
	    	while(contin) {
	    		char c = this.charStream.consume();
	    		if(c == CharStream.EOF)
	    			throw new SAJParserException("Unexpected EOF in number", 0,0);
	    		
	    		switch (c) {
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				case '.':
				case '-':
				case '+':
				case 'e':
				case 'E':
					this.tokenBuffer.append(c);
					break;
				default:
					this.charStream.mark(1);
					contin = false;
				}
	    	}
		} catch(SAJParserException exception) {
    		throw exception;
    	}catch(Exception exception) {
    		CharElement element = this.charStream.prevCharacterElement(1);
    		throw new SAJParserException("Error in stream ", element.getLine(), element.getCharPositionInLine(), exception);
    	}
    }
    
    public JsonToken getToken() {
		return token;
	}
    
    public String getTokenValue() {
		return tokenBuffer.toString();
	}
    
    private void cleanTokenBuffer() {
    	this.tokenBuffer.delete(0, this.tokenBuffer.length());
    }
    

    public void mTokens() throws SAJParserException {
    	char c = CharStream.EOF;
    	try {
        	c = this.charStream.consume();
		} catch (Exception e) {
			throw new SAJParserException("Error to read the stream ", this.charStream.getCharElement().getLine(), this.charStream.getCharElement().getCharPositionInLine());
		}
    	switch (c) {
		case Consts.START_ARRAY:
			this.mSTART_ARRAY();
			break;
		case Consts.START_OBJECT:
			this.mSTART_OBJECT();
			break;
		case Consts.END_ARRAY:
			this.mEND_ARRAY();
			break;
		case Consts.END_OBJECT:
			this.mEND_OBJECT();
			break;
		case Consts.PAIR_SEPARATOR:
			this.mPAIR_SEPARATOR();
			break;
		case Consts.MEMBER_SEPARATOR:
			this.mCOMMA();
			break;
		case Consts.STRING_BOUNDARY:
			this.mSTRING();
			break;
		case 'f':
			this.mBOOLEAN_FALSE();
			break;
		case 't':
			this.mBOOLEAN_TRUE();
			break;
		case 'n':
			this.mNULL_VALUE();
			break;
		case ' ':
		case '\n':
		case '\t':
		case '\r':
			this.mWS();
			break;
		case '-':
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			this.mNUMBER();
			break;
		case '/':
	    	try {
	        	c = this.charStream.consume();
			} catch (Exception e) {
				throw new SAJParserException("Error to read the stream ", this.charStream.getCharElement().getLine(), this.charStream.getCharElement().getCharPositionInLine());
			}
			if(c != '/' && c != '*')
				throw new SAJParserException("Unexpected character " + c, this.charStream.getCharElement().getLine(), this.charStream.getCharElement().getCharPositionInLine());
			this.mCOMMENT();
			break;
		case CharStream.EOF:
			this.token = JsonToken.EOF;
			break;
		default:
			throw new SAJParserException("Unexpected character " + c, this.charStream.getCharElement().getLine(), this.charStream.getCharElement().getCharPositionInLine());
		}
    }
    
    public int getTokenLine() {
    	return this.charStream.getCharElement().getLine();
    }
    
    public int getTokenCharInLine() {
    	return this.charStream.getCharElement().getCharPositionInLine();
    }

	public int getOffset() {
		return charStream.getOffset();
	}
	
}
