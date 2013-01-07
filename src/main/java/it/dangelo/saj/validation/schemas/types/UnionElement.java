package it.dangelo.saj.validation.schemas.types;

public interface UnionElement extends SchemaElement {
	SchemaElement[] getTypes();
}
