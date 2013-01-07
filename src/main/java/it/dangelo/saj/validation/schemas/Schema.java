package it.dangelo.saj.validation.schemas;

import it.dangelo.saj.validation.schemas.types.SchemaElement;

public interface Schema {
	SchemaReference getSchemaReference();
	boolean isArray();
	boolean isObject();
	SchemaElement getPrincipalElement();
	
}
