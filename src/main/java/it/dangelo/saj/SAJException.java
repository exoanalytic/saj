package it.dangelo.saj;

/**
 * Encapsulate a general SAJ error or warning  
 *
 */
public class SAJException extends Exception { 

	/**
	 * 
	 */
	private static final long serialVersionUID = 2990697092671699471L;

	public SAJException(String message, Throwable cause) {
		super(message, cause);
	}

	public SAJException(String message) {
		super(message);
	}

}
