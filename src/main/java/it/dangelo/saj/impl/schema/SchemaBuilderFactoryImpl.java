package it.dangelo.saj.impl.schema;

import it.dangelo.saj.SAJException;
import it.dangelo.saj.impl.parser.CharInputStream;
import it.dangelo.saj.impl.parser.CharReaderStream;
import it.dangelo.saj.impl.parser.CharStream;
import it.dangelo.saj.impl.parser.CharStringStream;
import it.dangelo.saj.impl.parser.Parser;
import it.dangelo.saj.impl.schema.elements.AbstractElement;
import it.dangelo.saj.impl.schema.elements.ArrayElementImpl;
import it.dangelo.saj.impl.schema.elements.ObjectElementImpl;
import it.dangelo.saj.impl.schema.elements.UnionElementImpl;
import it.dangelo.saj.parser.ErrorHandler;
import it.dangelo.saj.parser.SAJParserException;
import it.dangelo.saj.validation.schemas.Schema;
import it.dangelo.saj.validation.schemas.SchemaBuilderFactory;
import it.dangelo.saj.validation.schemas.SchemaReference;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SchemaBuilderFactoryImpl extends SchemaBuilderFactory {

	private static final ElementResolver resolver = new ElementResolverImpl();
	
	@Override
	public Schema[] parse(String schemaStream) throws SAJException {
		return this.parseSchema(new CharStringStream(schemaStream));
	}

	@Override
	public Schema[] parse(File schemaStream) throws SAJException, IOException {
		return this.parse(new FileReader(schemaStream));
	}

	@Override
	public Schema[] parse(InputStream schemaStream) throws SAJException, IOException {
		return this.parseSchema(new CharInputStream(schemaStream));
	}

	@Override
	public Schema[] parse(Reader schemaStream) throws SAJException, IOException {
		return this.parseSchema(new CharReaderStream(schemaStream));
	}

	@SuppressWarnings("unchecked")
	private Schema[] parseSchema(CharStream stream) throws SAJException {
		SchemaContentHandler contentHandler = new SchemaContentHandler();
		Parser parser = new Parser(stream, contentHandler , new ErrorHandler(){

			public void error(SAJException parserException)
					throws SAJException {
				throw parserException;
			}

			public void fatalError(SAJParserException parserException)
					throws SAJException {
				throw parserException;
			}

			public void warnin(SAJException parserException)
					throws SAJException {
				throw parserException;
			}});
		parser.parse();
		Object object = contentHandler.getRootElement();
		ArrayList<Schema> schemas = new ArrayList<Schema>();
		if (object instanceof HashMap) {
			HashMap<String, Object> map = (HashMap<String, Object>) object;
			URI schemaURI = this.getSchemaURI(map);
			schemas.add(this.getSchema(map, schemaURI));
		} else {
			List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) object;
			for (HashMap<String, Object> map : list) {
				URI schemaURI = this.getSchemaURI(map);
				schemas.add(this.getSchema(map, schemaURI));
			}
		}
		return schemas.toArray(new Schema[schemas.size()]);
	}
	
	private URI getSchemaURI(HashMap<String, Object> map) throws SAJException{
		String uri = (String) map.get("schemaNamespace");
		if(uri == null)
			throw new SAJException("All schema must have the schemaNamespace");
		try {
			return new URI(uri);
		} catch (URISyntaxException e) {
			throw new SAJException("Invalid schemaNamespace " + uri, e);
		}
	}
	
	private Schema getSchema(HashMap<String, Object> map, URI schemaURI) throws SAJException {
		SchemaImpl schema = null;
		AbstractElement element = (AbstractElement) SchemaBuilderFactoryImpl.resolver.resolve(map);
		if (element instanceof ObjectElementImpl) {
			ObjectElementImpl impl = (ObjectElementImpl) element;
			schema = new SchemaImpl(SchemaReference.newInstance(schemaURI), impl);
		} else if(element instanceof ArrayElementImpl){
			ArrayElementImpl impl = (ArrayElementImpl) element;
			schema = new SchemaImpl(SchemaReference.newInstance(schemaURI), impl);
		} else {
			UnionElementImpl impl = (UnionElementImpl) element;
			schema = new SchemaImpl(SchemaReference.newInstance(schemaURI), impl);
		}
		return schema;
	}
	
	
}
