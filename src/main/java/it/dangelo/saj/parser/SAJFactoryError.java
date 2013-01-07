package it.dangelo.saj.parser;

/**
 * This error is thrown when a factory error occurs (for example to instance a SAJReader).
 */
public class SAJFactoryError extends Error {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8444712867932588669L;

	public SAJFactoryError(String message) {
		super(message);
	}

	public SAJFactoryError(String message, Throwable cause) {
		super(message, cause);
	}

}
