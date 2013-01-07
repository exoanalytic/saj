package it.dangelo.saj;

/**
 * Interface to associate a SAJ event with a document location 
 *
 */
public interface Locator {
	
	/**
	 * Return a column number where the SAJ event is fired
	 * 
	 * @return column number, or -1 if not available
	 */
	public int getColumnNumber() ;

	/**
	 * Return a line number where the SAJ event is fired
	 * 
	 * @return line number, or -1 if not available
	 */
	public int getLineNumber() ;
	

}
