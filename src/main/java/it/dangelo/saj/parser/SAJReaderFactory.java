package it.dangelo.saj.parser;

import java.util.HashMap;
import java.util.Map;

import it.dangelo.factory.FactoryFinder;
import it.dangelo.saj.SAJConfigurationError;
import it.dangelo.saj.SAJException;
import it.dangelo.saj.validation.schemas.Schema;
import it.dangelo.saj.validation.schemas.SchemaReference;

/**
 * Factory class to retrieve a SAJReaderFactory implementation used to instance a {@link JSONReader}.<BR/>
 * The factory research use the {@link FactoryFinder} (see factoryfinder subproject in JON Tools).
 * The factory property used to retrieve the implementation is "it.dangelo.saj.SAJReaderFactory" (without double quotes)
 */
public abstract class SAJReaderFactory {

	private static final String SAJ_READER_FACTORY_PROPERTY = SAJReaderFactory.class.getName();

	private boolean validating = false;
	
	private HashMap<SchemaReference, Schema> schemas = new HashMap<SchemaReference, Schema>();
	
	protected SAJReaderFactory() {
		super();
	}
	
	/**
	 * Retrieve SAJReaderFactory
	 * See factoryfinder subproject in JON Tools.
	 * @return SAJReaderFactory implementation
	 */
	public static SAJReaderFactory newInstance() {
        try {
            return (SAJReaderFactory) FactoryFinder.find(
                SAJ_READER_FACTORY_PROPERTY,
                /* The fallback implementation class name */
                "it.dangelo.saj.impl.SAJReaderFactoryImpl");
        } catch (FactoryFinder.ConfigurationError e) {
            throw new SAJConfigurationError("Errore to retrieve the SAJReaderFactory implementation ", e);
        }
	}
	
	/**
	 * Create a new instance of {@link JSONReader}
	 * 
	 * @return {@link JSONReader} instance
	 * @throws SAJException
	 */
	public abstract JSONReader newJSONReader() throws SAJException;
	
	/**
	 * Set the validating flag. This flag indicate that the {@link JSONReader} will validate the 
	 * json stream using a schema
	 * 
	 * @param validating True if json stream must be validated, false otherwise
	 */
	public void setValidating(boolean validating) {
		this.validating = validating;
	}
	
	public void registerSchema(Schema schema) {
		this.schemas.put(schema.getSchemaReference(), schema);
	}
	
	public void removeAllSchemas() {
		this.schemas.clear();
	}
	
	public void removeSchema(String schemaReference) {
		this.schemas.remove(schemaReference);
	}
	
	protected Map<SchemaReference, Schema> getSchemaMaps() {
		return  this.schemas;
	}
	
	/**
	 * Indicates that the {@link JSONReader} will be instanced with validation support 
	 * 
	 * @return true if {@link JSONReader} will be instanced with validation support  
	 */
	public boolean isValidating() {
		return validating;
	}
	
	/**
	 * Set an implementation feature. This method must be used to set custom properties
	 * that the implementation can understand
	 * 
	 * @param name Feature name (generally a property name)
	 * @param value feature value
	 * @throws SAJNotSupportedException If the implementation don't support or don't understand the feature
	 */
	public abstract void setFeature(String name, Object value) throws SAJNotSupportedException;
	
	/**
	 * Return a feature value
	 * 
	 * @param name Feature name (generally a property name)
	 * @return Feature value
	 * @throws SAJNotSupportedException If the implementation don't support or don't understand the feature
	 */
	public abstract Object getFeature(String name) throws SAJNotSupportedException;
	
}
