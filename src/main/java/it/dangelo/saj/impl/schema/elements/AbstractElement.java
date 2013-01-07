package it.dangelo.saj.impl.schema.elements;

import static it.dangelo.saj.impl.schema.SchemaQNameConsts.*;

import java.util.HashMap;

import it.dangelo.saj.SAJException;
import it.dangelo.saj.impl.schema.ElementResolver;
import it.dangelo.saj.validation.schemas.types.SchemaElement;

public abstract class AbstractElement implements SchemaElement {

	private String description;
	private boolean nullable = false;
	private boolean optional = true;
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
	
	public boolean isNullable() {
		return this.nullable;
	}
	
	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	public boolean isOptional() {
		return this.optional;
	}
	
	public boolean check(Object value) {
		boolean result = false;
		if(this.nullable && value == null)
			result = true;
		else if(value != null)
			result = this.internalCheck(value);
		return result;
	}
	
	protected abstract boolean internalCheck(Object value);
	
	protected String internalToString() {
		StringBuffer buffer = new StringBuffer("description : ").append(this.description);
		buffer.append(", nullable : ").append(this.nullable);
		buffer.append(", optional : ").append(this.optional);
		return buffer.toString();
	}
	
	protected abstract void internalPopolateByMap(HashMap<String, Object> map, ElementResolver resolver) throws SAJException;
	
	public void popolateByMap(HashMap<String, Object> map, ElementResolver resolver) throws SAJException{
		this.setDescription((String) map.get(DESCRIPTION));
		if(map.containsKey(NULLABLE))
			this.setNullable((Boolean)map.get(NULLABLE));
		if(map.containsKey(OPTIONAL))
			this.setOptional((Boolean) map.get(OPTIONAL));
		this.internalPopolateByMap(map, resolver);
	}
	
}
