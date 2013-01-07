package it.dangelo.saj.validation.schemas.types;


/**
 * Interface that represents an array element 
 *
 */
public interface ArrayElement extends SchemaElement {
	/**
	 * Gets the minimum items of array
	 * 
	 * @return The minimum number of array length, null if don't exists the constraint
	 */
	Integer getMinItems();
	/**
	 * Gets the maximum items of array
	 * 
	 * @return The maximum number of array length, null if don't exists the constraint
	 */
	Integer getMaxItems();
	
	/**
	 * Return the type of the array
	 * 
	 * @return {@link SchemaElement} that represents the array type
	 */
	SchemaElement getItemType();
}
