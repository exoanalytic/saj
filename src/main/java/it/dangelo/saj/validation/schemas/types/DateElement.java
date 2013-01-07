package it.dangelo.saj.validation.schemas.types;

/**
 * Represents a date element with format yyyy-MM-dd  
 *
 */
public interface DateElement extends SchemaElement {
	
	/**
	 * ISO 8601 pattern for the date
	 * 
	 */
	String ISO8601_PATTERN = "yyyy-MM-dd";
	
	/**
	 * Return the pattern used
	 * 
	 * @return date pattern
	 */
	String getPattern();
}
