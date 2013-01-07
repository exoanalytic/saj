package it.dangelo.saj.impl.schema;

import it.dangelo.saj.SAJException;
import it.dangelo.saj.validation.schemas.types.SchemaElement;

import java.util.HashMap;

public interface ElementResolver {
	
	/**
	 * 
	 * @param type [], {}, string
	 * @return
	 * @throws SAJException
	 */
	SchemaElement resolve(Object type) throws SAJException;
	
	HashMap<String, Object> cloneMap(HashMap<String, Object> map, Object type) ;

}
