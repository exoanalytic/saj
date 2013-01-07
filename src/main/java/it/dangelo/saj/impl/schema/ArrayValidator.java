package it.dangelo.saj.impl.schema;

import it.dangelo.saj.SAJException;
import it.dangelo.saj.impl.schema.elements.AnyElementImpl;
import it.dangelo.saj.impl.schema.elements.ArrayElementImpl;
import it.dangelo.saj.impl.schema.elements.ObjectElementImpl;
import it.dangelo.saj.impl.schema.elements.UnionElementImpl;
import it.dangelo.saj.parser.ContentHandler;
import it.dangelo.saj.validation.schemas.SAJSchemaValidationError;

import java.math.BigDecimal;

public class ArrayValidator implements ContentHandler {

	private ArrayElementImpl arrayElement;
	private int elements = 0;
	private SchemaValidatorContext context;
	
	public ArrayValidator(ArrayElementImpl impl, SchemaValidatorContext context) {
		this.arrayElement = impl;
		this.context = context;
	}
	
	public void _boolean(boolean value) throws SAJException {
		this.checkMaxItems();
		if(!this.arrayElement.check(value))
			throw new SAJException("Invalid value " + value);
		
	}

	private void checkMaxItems() throws SAJSchemaValidationError {
		this.elements++;
		if(this.arrayElement.getMaxItems() != null) {
			if(this.elements > this.arrayElement.getMaxItems())
				throw new SAJSchemaValidationError("Max items error");
		}
	}

	public void _null() throws SAJException {
		this.checkMaxItems();
		if(!this.arrayElement.check(null))
			throw new SAJException("Invalid value null");
	}

	
	
	public void _number(BigDecimal value) throws SAJException {
		this.checkMaxItems();
		if(!checkArrayElement(value))
			throw new SAJException("Invalid value " + value);

	}

	private boolean checkArrayElement(Object value) {
		return this.arrayElement.check(value);
	}

	public void _string(String value) throws SAJException {
		this.checkMaxItems();
		if(!this.arrayElement.check(value))
			throw new SAJException("Invalid value " + value);

	}

	public void startObject() throws SAJException {
		this.checkMaxItems();
		if(this.arrayElement.getItemType() instanceof AnyElementImpl)
			context.addHandler(new ObjectValidator(new ObjectElementImpl(), this.context));
		else if(this.arrayElement.getItemType() instanceof ObjectElementImpl)
			context.addHandler(new ObjectValidator((ObjectElementImpl) this.arrayElement.getItemType(), this.context));
		else if (this.arrayElement.getItemType() instanceof UnionElementImpl) {
			context.addHandler(new UnionValidator((UnionElementImpl) this.arrayElement.getItemType(), this.context));
			context.startObject();
		}
		else 
			throw new SAJSchemaValidationError("Invalid type in array");
	}

	public void startArray() throws SAJException {
		this.checkMaxItems();
//		if(this.arrayElement.getItemType() instanceof AnyElement)
//			context.addHandler(new DummyArrayValidator());
		if(this.arrayElement.getItemType() instanceof ArrayElementImpl)
			context.addHandler(new ArrayValidator((ArrayElementImpl) this.arrayElement.getItemType(), this.context));
		else if(this.arrayElement.getItemType() instanceof AnyElementImpl)
				context.addHandler(new ArrayValidator(new ArrayElementImpl(new AnyElementImpl()), this.context));
		else if (this.arrayElement.getItemType() instanceof UnionElementImpl) { 
			context.addHandler(new UnionValidator((UnionElementImpl) this.arrayElement.getItemType(), this.context));
			context.startArray();
		}
		else
			throw new SAJSchemaValidationError("Invalid type in array");
	}
	
	public void endArray() throws SAJException {
		if(this.arrayElement.getMinItems() != null &&
			(this.elements < this.arrayElement.getMinItems()))
				throw new SAJSchemaValidationError("Min elements error");
		this.context.removeHandler();
	}

	public void attribute(String name) throws SAJException {}

	public void endJSON() throws SAJException {}

	public void endObject() throws SAJException {}


	public void startJSON() throws SAJException {}



}
