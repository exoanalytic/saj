package it.dangelo.saj.validation.schemas.types;



public interface ObjectElement extends SchemaElement {
	String[] getAttributeNames();

	String[] getMandatoryAttributeNames();
	
	SchemaElement getElement(String attribute);
	
	boolean existAttribute(String attributeName);
	
	boolean isFinal();
}
