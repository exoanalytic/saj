package it.dangelo.saj.impl;

import it.dangelo.saj.SAJException;
import it.dangelo.saj.parser.ErrorHandler;
import it.dangelo.saj.parser.SAJParserException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class DefaultErrorHandler implements ErrorHandler {

	public void error(SAJException parserException) throws SAJException{
		System.out.println("Error");
		throw parserException;
	}

	public void fatalError(SAJParserException parserException) throws SAJException {
		System.out.println("Fatal Error");
		throw parserException;
	}

	public void warnin(SAJException parserException) throws SAJException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		parserException.printStackTrace(new PrintStream(out));
		System.out.println(out.toString());
	}

}
