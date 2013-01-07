package it.dangelo.saj.impl.schema;

import it.dangelo.saj.SAJException;
import it.dangelo.saj.parser.ContentHandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class SchemaContentHandler implements ContentHandler {

	private ArrayList<Object> list = new ArrayList<Object>();
	private String attribute = null;
	private Object rootElement;
	
	private void push(Object obj) {
		this.list.add(obj);
	}
	
	private Object pop(boolean remove) {
		int size = this.list.size();
		if(size == 0)
			return null;
		size--;
		Object result = null;
		if(remove)
			result = this.list.remove((size));
		else
			result = this.list.get(size);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public void _boolean(boolean value) throws SAJException {
		addValue(value);
	}

	public void _null() throws SAJException {
		addValue(null);
	}

	public void _number(BigDecimal value) throws SAJException {
		addValue(value);
	}

	@SuppressWarnings("unchecked")
	private void addValue(Object value) {
		Object object = this.pop(false);
		if (object instanceof HashMap) {
			HashMap<String, Object> map = (HashMap<String, Object>) object;
			map.put(this.attribute, value);
		}
		if (object instanceof ArrayList) {
			ArrayList<Object> list = (ArrayList<Object>) object;
			list.add(value);
		}
		this.attribute = null;
	}

	public void _string(String value) throws SAJException {
		this.addValue(value);
	}

	public void attribute(String name) throws SAJException {
		this.attribute = name;
		
	}

	public void endArray() throws SAJException {
		this.pop(true);
		
	}

	public void endJSON() throws SAJException {
	}

	public void endObject() throws SAJException {
		this.pop(true);
	}

	public void startArray() throws SAJException {
		ArrayList<Object> list = new ArrayList<Object>();
		if(this.rootElement == null)
			this.rootElement = list;
		if(this.pop(false) != null)
			this.addValue(list);
		this.push(list);
	}

	public void startJSON() throws SAJException {
		
	}

	public void startObject() throws SAJException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(this.rootElement == null)
			this.rootElement = map;
		if(this.pop(false) != null)
			this.addValue(map);
		this.push(map);
	}

	public Object getRootElement() {
		return rootElement;
	}

}
