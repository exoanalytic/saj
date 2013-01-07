package it.dangelo.saj.impl;

import java.math.BigDecimal;

import it.dangelo.saj.SAJException;
import it.dangelo.saj.parser.ContentHandler;

public class DefaultContentHandler implements ContentHandler{

	public void endArray() throws SAJException {
		System.out.println("endArray");
	}

	public void endJSON() throws SAJException {
		System.out.println("endJSON");
	}

	public void endObject() throws SAJException {
		System.out.println("endObject");
	}

	public void member(String name, Object value) throws SAJException {
		System.out.println("member");
	}

	public void startArray() throws SAJException {
		System.out.println("startArray");
	}

	public void startJSON() throws SAJException {
		System.out.println("startJSON");
	}

	public void startObject() throws SAJException {
		System.out.println("startObject");
	}

	public void _boolean(boolean value) throws SAJException {
		System.out.println("Boolean value : " + value);
		
	}

	public void _null() throws SAJException {
		System.out.println("Null value");
	}

	public void _number(BigDecimal value) throws SAJException {
		System.out.println("Number value : " + value);
	}

	public void _string(String value) throws SAJException {
		System.out.println("String value : " + value);
	}

	public void attribute(String name) throws SAJException {
		System.out.println("Attribute name : " + name);
	}

}
