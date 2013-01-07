package it.dangelo.saj.impl;

import java.util.HashMap;

import it.dangelo.saj.SAJException;
import it.dangelo.saj.parser.JSONReader;
import it.dangelo.saj.parser.SAJNotSupportedException;
import it.dangelo.saj.parser.SAJReaderFactory;

public class SAJReaderFactoryImpl extends SAJReaderFactory {

	private HashMap<String, Object> properties = new HashMap<String, Object>();
	
	@Override
	public Object getFeature(String name) throws SAJNotSupportedException {
		throw new SAJNotSupportedException("NO feature supported");
	}

	@Override
	public JSONReader newJSONReader() throws SAJException {
		JSONReaderImpl impl = new JSONReaderImpl(this.properties);
		impl.setValidate(this.isValidating());
		impl.setSchemas(this.getSchemaMaps());
		return impl;
	}

	@Override
	public void setFeature(String name, Object value)
			throws SAJNotSupportedException {
		throw new SAJNotSupportedException("NO feature supported");
	}

}
