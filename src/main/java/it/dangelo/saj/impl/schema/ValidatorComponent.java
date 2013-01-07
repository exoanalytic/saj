package it.dangelo.saj.impl.schema;

import it.dangelo.saj.SAJException;
import it.dangelo.saj.impl.parser.Component;
import it.dangelo.saj.impl.parser.Handlers;
import it.dangelo.saj.impl.schema.elements.ArrayElementImpl;
import it.dangelo.saj.impl.schema.elements.ObjectElementImpl;
import it.dangelo.saj.impl.schema.elements.UnionElementImpl;
import it.dangelo.saj.parser.ContentHandler;
import it.dangelo.saj.parser.ErrorHandler;
import it.dangelo.saj.validation.schemas.Schema;
import it.dangelo.saj.validation.schemas.SchemaReference;
import it.dangelo.saj.validation.schemas.types.SchemaElement;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;

public class ValidatorComponent implements Component {

	private static final String VALIDATION_SCHEMA_REFERENCE = "validation.schema.reference";
	private static final String VALIDATION_SCHEMA = "validation.schema";
	private SchemaValidatorContext context = new SchemaValidatorContext(); 
	private ErrorHandler errorHandler;
	
	
	public void _boolean(boolean value) throws SAJException {
		try {
			this.context._boolean(value);
		} catch (SAJException e) {
			this.errorHandler.error(e);
		}
	}

	public void _null() throws SAJException {
		try {
			this.context._null();
		} catch (SAJException e) {
			this.errorHandler.error(e);
		}
	}

	public void _number(BigDecimal value) throws SAJException {
		try {
			this.context._number(value);
		} catch (SAJException e) {
			this.errorHandler.error(e);
		}
	}

	public void _string(String value) throws SAJException {
		try {
			this.context._string(value);
		} catch (SAJException e) {
			this.errorHandler.error(e);
		}
	}

	public void attribute(String name) throws SAJException {
		try {
			this.context.attribute(name);
		} catch (SAJException e) {
			this.errorHandler.error(e);
		}
	}

	public void endArray() throws SAJException {
		try {
			this.context.endArray();
		} catch (SAJException e) {
			this.errorHandler.error(e);
		}
	}

	public void endJSON() throws SAJException {
		try {
			this.context.endJSON();
		} catch (SAJException e) {
			this.errorHandler.error(e);
		}
	}

	public void endObject() throws SAJException {
		try {
			this.context.endObject();
		} catch (SAJException e) {
			this.errorHandler.error(e);
		}
	}

	@SuppressWarnings("unchecked")
	public void init(Handlers handler, URI uri, HashMap<String, Object> properties)
			throws SAJException {
		HashMap<SchemaReference, Schema> schemas = null;
		if(properties.containsKey(VALIDATION_SCHEMA_REFERENCE)) {
			schemas = (HashMap<SchemaReference, Schema>) properties.get(VALIDATION_SCHEMA_REFERENCE);
		}else
			schemas = new HashMap<SchemaReference, Schema>();
		SchemaReference reference = SchemaReference.DEFAULT_REFERENCE;
//		if(properties.containsKey(VALIDATION_SCHEMA_NAMESPACE))
//			reference = (SchemaReference) properties.get(VALIDATION_SCHEMA_NAMESPACE);
		if(uri != null)
			reference = SchemaReference.newInstance(uri);
		Schema schema = schemas.get(reference);
		if(schema == null) {
			resolveInternalSchema(handler, uri, schemas);
			schema = schemas.get(reference);
		}
		if(schema == null)
			throw new SAJException("No schema namespace present with " + reference);
		SchemaElement principalElement = schema.getPrincipalElement();
		ContentHandler elementHandler = null;
		if (principalElement instanceof ObjectElementImpl) {
			ObjectElementImpl impl = (ObjectElementImpl) principalElement;
			elementHandler = new ObjectValidator(impl, this.context);
		} else if (principalElement instanceof ArrayElementImpl) {
			ArrayElementImpl impl = (ArrayElementImpl) principalElement;
			elementHandler = new ArrayValidator(impl, this.context);
		} else if (principalElement instanceof UnionElementImpl) {
			UnionElementImpl impl = (UnionElementImpl) principalElement;
			elementHandler = new UnionValidator(impl, this.context);
		}
		this.errorHandler = handler.getErrorHandler();
		this.context.addHandler(elementHandler);
	}

	private void resolveInternalSchema(Handlers handler, URI uri,
			HashMap<SchemaReference, Schema> schemas) throws SAJException {
		try {
			Schema schema;
			InputStream stream = handler.getResourceResolver().resolve(uri.toString());
			if(stream == null) return;
			SchemaBuilderFactoryImpl impl = new SchemaBuilderFactoryImpl();
			Schema[] resovedSchemas;
			resovedSchemas = impl.parse(stream);
			stream.close();
			schema = resovedSchemas[0];
			for (Schema ischema : resovedSchemas) {
				schemas.put(ischema.getSchemaReference(), ischema);
			}
		} catch (IOException e) {
			throw new SAJException("Error to read the stream in " + uri, e);
		}
	}

	public void startArray() throws SAJException {
		try {
			this.context.startArray();
		} catch (SAJException e) {
			this.errorHandler.error(e);
		}
	}

	public void startJSON() throws SAJException {
		try {
			this.context.startJSON();
		} catch (SAJException e) {
			this.errorHandler.error(e);
		}
	}

	public void startObject() throws SAJException {
		try {
			this.context.startObject();
		} catch (SAJException e) {
			this.errorHandler.error(e);
		}
	}

	public boolean enabled(HashMap<String, Object> properties) {
		if(properties.containsKey(VALIDATION_SCHEMA)) {
			return (Boolean) properties.get(VALIDATION_SCHEMA);
		} else return false;
	}

}
