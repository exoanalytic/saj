package it.dangelo.saj.impl.schema.elements;

import java.text.SimpleDateFormat;

import it.dangelo.saj.validation.schemas.types.DateTimeElement;

public class DateTimeElementImpl extends DateElementImpl implements DateTimeElement{
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DateTimeElement.ISO8601_PATTERN);
	
	@Override
	protected SimpleDateFormat getFormatter() {
		return DateTimeElementImpl.dateFormat;
	}
	
}
