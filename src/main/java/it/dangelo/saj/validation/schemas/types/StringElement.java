package it.dangelo.saj.validation.schemas.types;


public interface StringElement extends SchemaElement{
	String getPattern();
	Integer getMinLength();
	Integer getMaxLength();
	String[] options();
	boolean isUnconstrained();
	String getDefault();
}
