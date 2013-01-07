package it.dangelo.saj.validation.schemas.types;

/**
 * Interface that represents a schema element.<br/>
 * A schema element is the object that is link to a json attribute.
 */
public interface SchemaElement {
	
	/**
	 * Indicate that the element is optional
	 * 
	 * @return true if the element is optional, false otherwise. Default true
	 */
	boolean isOptional();
	
	/**
	 * Indicate that the element can be null 
	 * 
	 * @return true if the element can be null, false otherwise. Default true
	 */
	boolean isNullable();
	
	/**
	 * Return element description. Useless in validation. 
	 * 
	 * @return element description or null if it wasn't setted.
	 */
	String getDescription();

	/**
	 * Return the string representation of the element.
	 * Actual values are:<br/>
	 * <li>
	 * 	<ul>string</ul>
	 * 	<ul>array</ul>
	 * 	<ul>object</ul>
	 * 	<ul>integer</ul>
	 * 	<ul>float</ul>
	 * 	<ul>null</ul>
	 * 	<ul>union</ul>
	 * 	<ul>any</ul>
	 * 	<ul>boolean</ul>
	 * </li>
	 * @return the string representation of the element
	 */
	String getType();
}
