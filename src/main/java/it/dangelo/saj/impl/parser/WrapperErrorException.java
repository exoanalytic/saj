package it.dangelo.saj.impl.parser;

import it.dangelo.saj.SAJException;

@SuppressWarnings("serial")
class WrapperErrorException extends RuntimeException {

	WrapperErrorException(Throwable cause) {
		super(cause);
	}
	
	SAJException getSAJException() {
		return (SAJException) this.getCause();
		
	}
	
}
