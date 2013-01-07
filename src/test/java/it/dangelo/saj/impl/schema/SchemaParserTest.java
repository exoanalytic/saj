package it.dangelo.saj.impl.schema;

import it.dangelo.saj.SAJException;
import it.dangelo.saj.impl.schema.elements.AnyElementImpl;
import it.dangelo.saj.impl.schema.elements.ArrayElementImpl;
import it.dangelo.saj.impl.schema.elements.BooleanElementImpl;
import it.dangelo.saj.impl.schema.elements.IntegerElementImpl;
import it.dangelo.saj.impl.schema.elements.ObjectElementImpl;
import it.dangelo.saj.impl.schema.elements.StringElementImpl;
import it.dangelo.saj.impl.schema.elements.UnionElementImpl;
import it.dangelo.saj.parser.JSONReader;
import it.dangelo.saj.parser.ResourceResolver;
import it.dangelo.saj.parser.SAJReaderFactory;
import it.dangelo.saj.validation.schemas.Schema;
import it.dangelo.saj.validation.schemas.SchemaBuilderFactory;
import it.dangelo.saj.validation.schemas.types.AnyElement;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;

import org.junit.Test;

public class SchemaParserTest {
	

	
	public void parseSchema() throws Exception {
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream("schema/schema1.json");
		SchemaBuilderFactory factory = SchemaBuilderFactory.newInstance();
		Schema[] schemas = factory.parse(stream);
		for (Schema schema : schemas) {
			System.out.println(schema.getPrincipalElement());
		}
//		stream = this.getClass().getClassLoader().getResourceAsStream("test1.json");
//		SAJReaderFactory readerFactory = SAJReaderFactory.newInstance();
//		readerFactory.setValidating(true);
//		readerFactory.registerSchema(schemas[0]);
//		JSONReader reader = readerFactory.newJSONReader();
//		reader.parse(stream);
		
	}
	
	public void testObjectValidator() throws Exception {
		ObjectElementImpl impl = new ObjectElementImpl();
		impl.setFinal(true);
		impl.addAttribute("name", new StringElementImpl());
		impl.addAttribute("surname", new StringElementImpl());
		UnionElementImpl ua = new UnionElementImpl();
		ua.addElement(new IntegerElementImpl());
		ua.addElement(new BooleanElementImpl());
		ObjectElementImpl obj = new ObjectElementImpl();
		obj.addAttribute("pippo", new StringElementImpl());
//		ArrayElementImpl array = new ArrayElementImpl(new IntegerElementImpl());
		ArrayElementImpl array = new ArrayElementImpl(ua);
		array.setNullable(false);
		array.setOptional(false);
		array.setMinItems(3);
		array.setMaxItems(5);
		ObjectElementImpl impl2 = new ObjectElementImpl();
		impl2.addAttribute("name", new BooleanElementImpl());

		UnionElementImpl union = new UnionElementImpl();
		union.addElement(array);
		union.addElement(impl2);
		ua.addElement(obj);
		impl.addAttribute("boo", union);
		SchemaValidatorContext handler = new SchemaValidatorContext();
		System.out.println(impl);
		ObjectValidator validator = new ObjectValidator(impl,handler);
		handler.addHandler(validator);
		handler.startObject();
		handler.attribute("name");
		handler._string("Claudio");
		handler.attribute("boo");
		handler.startObject();
		handler.attribute("name");
		handler._boolean(true);
		handler.endObject();
		/*
		handler.startArray();
		handler._number(new BigDecimal(10));
		handler._number(new BigDecimal(10));
//		handler._null();
		handler._boolean(true);
		handler.startObject();
		handler.attribute("pippo");
		handler._string("pippo");
		handler.endObject();
		handler._string("ciao");
//		handler._number(new BigDecimal(10));
		handler.endArray();
		*/
		handler.attribute("surname");
		handler._string("D'Angelo");
		handler.endObject();
		
	}
	
	@Test
	public void testParseReader() throws Exception{
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream("test2.json");
		SAJReaderFactory factory = SAJReaderFactory.newInstance();
		factory.setValidating(true);
		InputStream schemastream = this.getClass().getClassLoader().getResourceAsStream("schema/schema2.json");
		SchemaBuilderFactory schemafactory = SchemaBuilderFactory.newInstance();
		Schema[] schemas = schemafactory.parse(schemastream);
		factory.registerSchema(schemas[0]);
		System.out.println(schemas[0].getPrincipalElement());
		JSONReader reader = factory.newJSONReader();
//		reader.setResourceResolver(new ResourceResolver() {
//
//			public InputStream resolve(String uri) throws SAJException {
//				if(uri.equals("http://example.org/1"))
//					return this.getClass().getResourceAsStream("/schema/schema2.json");
//				return null;
//			}
//			
//		});
		reader.parse(stream, new URI("http://example.org/1"));
	}
	
}
