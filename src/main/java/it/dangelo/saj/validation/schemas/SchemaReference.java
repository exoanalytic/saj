package it.dangelo.saj.validation.schemas;

import java.net.URI;

public class SchemaReference implements Comparable<SchemaReference>{

	private URI schemaURI;
	public static final SchemaReference DEFAULT_REFERENCE; 
	static {
		try {
			DEFAULT_REFERENCE = new SchemaReference(new URI("http://jontools.sourceforge.net/SAJ/schema"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
		
	
	private SchemaReference(URI schemaURI) {
		this.schemaURI = schemaURI;
	}
	
	public static SchemaReference newInstance(URI schemaURI) {
		if(schemaURI == null || !schemaURI.isAbsolute())
			throw new IllegalArgumentException("Schema uri " + schemaURI + " not permitted");
		return new SchemaReference(schemaURI);
	}
	
	public URI getSchemaURI() {
		return schemaURI;
	}
	
	@Override
	public int hashCode() {
		return this.schemaURI.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean result;
		if (obj instanceof SchemaReference) {
			SchemaReference reference = (SchemaReference) obj;
			result = reference.schemaURI.equals(this.schemaURI);
		}else
			result = false;
		return result;
	}

	public int compareTo(SchemaReference o) {
		return o.schemaURI.compareTo(this.schemaURI);
	}
	
	@Override
	public String toString() {
		return this.schemaURI.toString();
	}
}
