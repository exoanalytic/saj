package it.dangelo.saj.impl;

import it.dangelo.saj.SAJException;
import it.dangelo.saj.impl.parser.Component;
import it.dangelo.saj.impl.parser.Handlers;

import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;

public class ContentHandlerCaller implements Component {

	private Handlers handlers;
	
	public void _boolean(boolean value) throws SAJException {
		this.handlers.getContentHandler()._boolean(value);
	}

	public void _null() throws SAJException {
		this.handlers.getContentHandler()._null();
	}

	public void _number(BigDecimal value) throws SAJException {
		this.handlers.getContentHandler()._number(value);
	}

	public void _string(String value) throws SAJException {
		this.handlers.getContentHandler()._string(value);
	}

	public void attribute(String name) throws SAJException {
		this.handlers.getContentHandler().attribute(name);
	}

	public void endArray() throws SAJException {
		this.handlers.getContentHandler().endArray();
	}

	public void endJSON() throws SAJException {
		this.handlers.getContentHandler().endJSON();
	}

	public void endObject() throws SAJException {
		this.handlers.getContentHandler().endObject();
	}

	public void startArray() throws SAJException {
		this.handlers.getContentHandler().startArray();
	}

	public void startJSON() throws SAJException {
		this.handlers.getContentHandler().startJSON();
	}

	public void startObject() throws SAJException {
		this.handlers.getContentHandler().startObject();
	}

	public void init(Handlers handlers, URI uri, HashMap<String, Object> properties)
			throws SAJException {
		this.handlers = handlers;
	}

	public boolean enabled(HashMap<String, Object> properties) {
		return true;
	}

}
