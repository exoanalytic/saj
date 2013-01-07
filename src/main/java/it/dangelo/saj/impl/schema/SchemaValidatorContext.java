package it.dangelo.saj.impl.schema;

import it.dangelo.saj.SAJException;
import it.dangelo.saj.parser.ContentHandler;

import java.math.BigDecimal;
import java.util.ArrayList;

public class SchemaValidatorContext implements ContentHandler{

	private ArrayList<ContentHandler> list = new ArrayList<ContentHandler>();

	public ContentHandler getHandler() {
		int size = list.size();
		ContentHandler handler = null;
		if(size > 0)
			handler = list.get(size-1);
		return handler;
	}
	
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	public void removeHandler() {
		int size = list.size();
		if(size > 0)
			list.remove(size-1);
	}
	
	public void addHandler(ContentHandler handler) {
		list.add(handler);
	}

	public void _boolean(boolean value) throws SAJException {
		try {
			this.getHandler()._boolean(value);
		} catch (SAJException e) {
			this.removeHandler();
			throw e;
		}
	}

	public void _null() throws SAJException {
		try {
			this.getHandler()._null();
		} catch (SAJException e) {
			this.removeHandler();
			throw e;
		}
	}

	public void _number(BigDecimal value) throws SAJException {
		try {
			this.getHandler()._number(value);
		} catch (SAJException e) {
			this.removeHandler();
			throw e;
		}
	}

	public void _string(String value) throws SAJException {
		try {
			this.getHandler()._string(value);
		} catch (SAJException e) {
			this.removeHandler();
			throw e;
		}
	}

	public void attribute(String name) throws SAJException {
		try {
			this.getHandler().attribute(name);
		} catch (SAJException e) {
			this.removeHandler();
			throw e;
		}
	}

	public void endArray() throws SAJException {
		try {
			this.getHandler().endArray();
		} catch (SAJException e) {
			this.removeHandler();
			throw e;
		}
	}

	public void endJSON() throws SAJException {
//		try {
//			this.getHandler().endJSON();
//		} catch (SAJException e) {
//			this.removeHandler();
//			throw e;
//		}
	}

	public void endObject() throws SAJException {
		try {
			this.getHandler().endObject();
		} catch (SAJException e) {
			this.removeHandler();
			throw e;
		}
	}

	public void startArray() throws SAJException {
		try {
			this.getHandler().startArray();
		} catch (SAJException e) {
			this.removeHandler();
			throw e;
		}
		
	}

	public void startJSON() throws SAJException {
		try {
			this.getHandler().startJSON();
		} catch (SAJException e) {
			this.removeHandler();
			throw e;
		}
	}

	public void startObject() throws SAJException {
		try {
			this.getHandler().startObject();
		} catch (SAJException e) {
			this.removeHandler();
			throw e;
		}
	}
}
