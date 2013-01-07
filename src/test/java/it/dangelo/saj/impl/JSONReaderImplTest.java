package it.dangelo.saj.impl;

import static org.junit.Assert.*;

import it.dangelo.saj.parser.JSONReader;
import it.dangelo.saj.parser.SAJReaderFactory;

import java.io.InputStream;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

public class JSONReaderImplTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testParseReader() throws Exception{
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream("test1.json");
		SAJReaderFactory factory = SAJReaderFactory.newInstance();
		JSONReader reader = factory.newJSONReader();
		reader.parse(stream);
	}

}
