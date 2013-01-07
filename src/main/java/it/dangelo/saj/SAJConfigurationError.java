package it.dangelo.saj;

import it.dangelo.saj.parser.SAJReaderFactory;


/**
 * Error class that represent a configuration error. 
 * If this error is throwed a {@link SAJReaderFactory} can't be instanced.
 */
@SuppressWarnings("serial")
public class SAJConfigurationError extends Error {

	public SAJConfigurationError(String message) {
		super(message);
	}

	public SAJConfigurationError(String message, Throwable cause) {
		super(message, cause);
	}

}
