package it.dangelo.saj.impl.schema;

import it.dangelo.saj.validation.schemas.Schema;
import it.dangelo.saj.validation.schemas.SchemaReference;
import it.dangelo.saj.validation.schemas.types.ArrayElement;
import it.dangelo.saj.validation.schemas.types.ObjectElement;
import it.dangelo.saj.validation.schemas.types.SchemaElement;
import it.dangelo.saj.validation.schemas.types.UnionElement;

public class SchemaImpl implements Schema {

	private SchemaElement element;
	private SchemaReference reference;
	
	public SchemaImpl(SchemaReference reference, ObjectElement element) {
		this.reference = reference;
		this.element = element;
	}
	
	public SchemaImpl(SchemaReference reference, ArrayElement element) {
		this.reference = reference;
		this.element = element;
	}
	
	public SchemaImpl(SchemaReference reference, UnionElement element) {
		this.reference = reference;
		this.element = element;
	}
	
	public SchemaElement getPrincipalElement() {
		return this.element;
	}

	public boolean isArray() {
		return (this.element instanceof ArrayElement);
	}

	public boolean isObject() {
		return (this.element instanceof ObjectElement);
	}

	public SchemaReference getSchemaReference() {
		return this.reference;
	}

}
