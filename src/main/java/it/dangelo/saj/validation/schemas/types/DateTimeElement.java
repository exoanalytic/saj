package it.dangelo.saj.validation.schemas.types;


public interface DateTimeElement extends SchemaElement {
	
	String ISO8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'ZZ";
	
	String getPattern();
}
