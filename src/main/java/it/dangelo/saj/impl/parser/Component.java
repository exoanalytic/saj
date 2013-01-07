package it.dangelo.saj.impl.parser;

import it.dangelo.saj.SAJException;

import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;

public interface Component {
	
	boolean enabled(HashMap<String, Object> properties);
	void init(Handlers handlers, URI uri, HashMap<String, Object> properties) throws SAJException;
	
	void startJSON() throws SAJException;
	void endJSON() throws SAJException;

	void startObject() throws SAJException;
	void endObject() throws SAJException;
	
	void startArray() throws SAJException;
	void endArray() throws SAJException;
	
	void attribute(String name) throws SAJException;
	
	void _string(String value) throws SAJException;

	void _boolean(boolean value) throws SAJException;
	
	void _number(BigDecimal value) throws SAJException;
	
	void _null() throws SAJException;

}
