grammar JSON;

@header:lexer {
package it.dangelo.saj.impl;
}
@header {
package it.dangelo.saj.impl;

import it.dangelo.saj.NumberValueCodec;
import it.dangelo.saj.StringValueCodec;
import it.dangelo.saj.ContentHandler;
import it.dangelo.saj.ErrorHandler;
import it.dangelo.saj.SAJException;
import it.dangelo.saj.SAJParserException;
import it.dangelo.saj.impl.LocatorImpl;
import java.math.BigDecimal;
}

@members {
  ContentHandler handler = new DefaultContentHandler();
  ErrorHandler errorHandler = new DefaultErrorHandler();
  SAJException lastEx = null;
  
  public void setContentHandler(ContentHandler handler) {
  	this.handler = handler;
  }
  
  public void setErrorHandler(ErrorHandler handler) {
  	this.errorHandler = handler;
  }
  
  private void manageException(SAJException ex) throws RecognitionException{
    this.lastEx = ex;
    throw new RecognitionException();
    
  }
  
}
@rulecatch {
catch (Exception exc) {
   if(exc instanceof SAJException) {
    SAJException exception = (SAJException) exc;
    this.lastEx = exception;
    throw new RecognitionException();
   }
   if(exc instanceof RecognitionException) {
    RecognitionException exception = (RecognitionException) exc;
    Token t = this.getTokenStream().get(this.getTokenStream().index());
    int line = t.getLine();
    int cl = t.getCharPositionInLine();
    this.lastEx = new SAJParserException(exception.getMessage(), line, cl,     exception);
    throw exception;
   }
   throw new RuntimeException(exc);
}
}


COMMA		:	',';
START_OBJECT	:	'{' ;
START_ARRAY	:	'[';
END_OBJECT		:	'}';
END_ARRAY		:	']';
PAIR_SEPARATOR	:	':';
BOOLEAN_TRUE	:	'true';
BOOLEAN_FALSE	:	'false';
NULL_VALUE		:	'null';
fragment Digit		: 	'0'..'9';
fragment HexDigit	: 	'0'..'9' | 'A'..'F' | 'a'..'f';
fragment UnicodeEscape	:  	'u' HexDigit HexDigit HexDigit HexDigit;
fragment EscapeSequence    	:  	 '\\' (UnicodeEscape |'b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\');
WS		: 	(' '|'\n'|'\r'|'\t')+ {$channel=HIDDEN;} ; // ignore whitespace 
STRING 		:	'"' ( EscapeSequence | ~('\u0000'..'\u001f' | '\\' | '\"' ) )* '"';
EXPONENT		: 	('e'|'E') '-'? Digit+;
NUMBER		: 	'-'? Digit+ ( '.' Digit+)?;

json		:	array|object;
value	 	:	 string 
			| number
			| object
			| array
			| BOOLEAN_TRUE {this.handler._boolean(true);}
			| BOOLEAN_FALSE {this.handler._boolean(false);}
			| NULL_VALUE {this.handler._null();}
			;
string 		:	 s=STRING {
				try {
					String value = StringValueCodec.extractString($s.text);
					this.handler._string(value);
				} catch(Exception ex) {
					this.errorHandler.error(new SAJParserException("Error to decode string value " + $s.text, 0,0,ex));
				}
			};

number 
		: 	n=NUMBER e=EXPONENT? { 
				StringBuffer buffer = new StringBuffer($n.text);
				if(e != null)
					buffer.append($e.text);
				BigDecimal num = NumberValueCodec.convert(buffer.toString());
				this.handler._number(num);
			};

object		: 	START_OBJECT {this.handler.startObject();}
			(members)* 
			END_OBJECT {this.handler.endObject();};
array		: 	START_ARRAY {this.handler.startArray();}
			(elements)* 
			END_ARRAY {this.handler.endObject();};

elements		: 	value (COMMA value)*;
	
members		: 	pair (COMMA pair)*;
	 
pair		: 	attribute=STRING {this.handler.attribute($attribute.text);}
			PAIR_SEPARATOR 
			value ;
