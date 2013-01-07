package it.dangelo.saj.validation.schemas;

import it.dangelo.saj.SAJException;

@SuppressWarnings("serial")
public class SAJSchemaValidationError extends SAJException {

	public SAJSchemaValidationError(String message, Throwable cause) {
		super(message, cause);
	}

	public SAJSchemaValidationError(String message) {
		super(message);
	}

}
