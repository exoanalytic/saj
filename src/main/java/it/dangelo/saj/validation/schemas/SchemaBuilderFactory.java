package it.dangelo.saj.validation.schemas;

import it.dangelo.factory.FactoryFinder;
import it.dangelo.saj.SAJConfigurationError;
import it.dangelo.saj.SAJException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public abstract class SchemaBuilderFactory {
	
	public static final String SAJ_SCHEMA_BUILDER_FACTORY_PROPERTY = SchemaBuilderFactory.class.getName();
	
	public static SchemaBuilderFactory newInstance() {
        try {
            return (SchemaBuilderFactory) FactoryFinder.find(
            		SAJ_SCHEMA_BUILDER_FACTORY_PROPERTY,
                /* The fallback implementation class name */
                "it.dangelo.saj.impl.schema.SchemaBuilderFactoryImpl");
        } catch (FactoryFinder.ConfigurationError e) {
            throw new SAJConfigurationError("Errore to retrieve the SchemaBuilderFactory implementation ", e);
        }
	}
	
	public abstract Schema[] parse(String schemaStream) throws SAJException;
	public abstract Schema[] parse(File schemaStream) throws SAJException, IOException;
	public abstract Schema[] parse(InputStream schemaStream) throws SAJException, IOException;
	public abstract Schema[] parse(Reader schemaStream) throws SAJException, IOException;
	
}
