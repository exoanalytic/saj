package it.dangelo.saj.impl.schema.elements;

import it.dangelo.saj.impl.schema.ElementResolver;
import it.dangelo.saj.validation.schemas.types.DateElement;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class DateElementImpl extends AbstractElement implements DateElement {

	private final static SimpleDateFormat dateFormat = new SimpleDateFormat(ISO8601_PATTERN);
	
	@Override
	protected boolean internalCheck(Object value) {
		if(!(value instanceof String))
			return false;
		String string = (String) value;
		try {
			SimpleDateFormat dateFormat = this.getFormatter();
			dateFormat.setLenient(false);
			dateFormat.parse(string);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	protected SimpleDateFormat getFormatter() {
		return DateElementImpl.dateFormat;
	}
	
	public String getPattern() {
		return this.getFormatter().toPattern();
	}

	public String getType() {
		return "string";
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("DateElment {");
		buffer.append(this.internalToString());
		buffer.append('}');
		return buffer.toString();
	}

	@Override
	protected void internalPopolateByMap(HashMap<String, Object> map, ElementResolver resolver) {
	}

}
