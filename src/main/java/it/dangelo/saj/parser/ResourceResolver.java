package it.dangelo.saj.parser;

import it.dangelo.saj.SAJException;

import java.io.InputStream;

public interface ResourceResolver {

	InputStream resolve(String uri) throws SAJException;
}
