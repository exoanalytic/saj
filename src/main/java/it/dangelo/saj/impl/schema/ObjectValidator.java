package it.dangelo.saj.impl.schema;

import it.dangelo.saj.SAJException;
import it.dangelo.saj.impl.schema.elements.AbstractElement;
import it.dangelo.saj.impl.schema.elements.AnyElementImpl;
import it.dangelo.saj.impl.schema.elements.ArrayElementImpl;
import it.dangelo.saj.impl.schema.elements.ObjectElementImpl;
import it.dangelo.saj.impl.schema.elements.UnionElementImpl;
import it.dangelo.saj.parser.ContentHandler;
import it.dangelo.saj.validation.schemas.SAJSchemaValidationError;
import it.dangelo.saj.validation.schemas.types.SchemaElement;

import java.math.BigDecimal;
import java.util.ArrayList;



public class ObjectValidator implements ContentHandler{

	private ObjectElementImpl element;
	private String attribute = null;
	private SchemaElement actualElement = null;
	private ArrayList<String> checkedAttributes = new ArrayList<String>();
	private SchemaValidatorContext context;
	
	public ObjectValidator(ObjectElementImpl impl, SchemaValidatorContext context) {
		this.element = impl;
		this.context = context;
	}
	
	/* (non-Javadoc)
	 * @see it.dangelo.saj.impl.schema.IObjectValidator#_boolean(boolean)
	 */
	public void _boolean(boolean value) throws SAJException {
		if(this.attribute == null)
			return;
		AbstractElement element = (AbstractElement) this.actualElement;
		if(!element.check((value?Boolean.TRUE:Boolean.FALSE)))
			throw new SAJSchemaValidationError("Invalid attribute type " + this.attribute+ " with value " + value);
	}

	/* (non-Javadoc)
	 * @see it.dangelo.saj.impl.schema.IObjectValidator#_null()
	 */
	public void _null() throws SAJException {
		if(this.attribute == null)
			return;
		AbstractElement element = (AbstractElement) this.actualElement;
		System.out.println(element);
		if(!element.check(null))
			throw new SAJSchemaValidationError("Invalid attribute type " + this.attribute+ " with value null");
	}

	/* (non-Javadoc)
	 * @see it.dangelo.saj.impl.schema.IObjectValidator#_number(java.math.BigDecimal)
	 */
	public void _number(BigDecimal value) throws SAJException {
		if(this.attribute == null)
			return;
		AbstractElement element = (AbstractElement) this.actualElement;
		if(!element.check(value))
			throw new SAJSchemaValidationError("Invalid attribute type " + this.attribute+ " with value " + value);
	}

	/* (non-Javadoc)
	 * @see it.dangelo.saj.impl.schema.IObjectValidator#_string(java.lang.String)
	 */
	public void _string(String value) throws SAJException {
		if(this.attribute == null)
			return;
		AbstractElement element = (AbstractElement) this.actualElement;
		if(!element.check(value))
			throw new SAJSchemaValidationError("Invalid attribute type " + this.attribute + " with value " + value);
	}

	/* (non-Javadoc)
	 * @see it.dangelo.saj.impl.schema.IObjectValidator#attribute(java.lang.String)
	 */
	public void attribute(String name) throws SAJException {
		if(!this.element.existAttribute(name)) {
			if(this.element.isFinal())
				throw new SAJSchemaValidationError("Attribute not permitted :" + name);
		} else { 
			this.actualElement = this.element.getElement(name);
			this.attribute = name;
		}
		this.checkedAttributes.add(name);
	}

	/* (non-Javadoc)
	 * @see it.dangelo.saj.impl.schema.IObjectValidator#startObject(it.dangelo.saj.impl.schema.SchemaContext)
	 */
	public void startObject() throws SAJException {
		if(this.attribute == null) return;
		if (actualElement instanceof ObjectElementImpl) {
			ObjectElementImpl impl = (ObjectElementImpl) this.actualElement;
			context.addHandler(new ObjectValidator(impl, this.context));
		} else if (actualElement instanceof AnyElementImpl) {
			ObjectElementImpl impl = new ObjectElementImpl();
			impl.setFinal(false);
			context.addHandler(new ObjectValidator(impl, this.context));
		}else if (actualElement instanceof UnionElementImpl) {
			UnionElementImpl impl = (UnionElementImpl) actualElement;
			context.addHandler(new UnionValidator(impl, this.context));
			context.startObject();
		}else
			throw new SAJSchemaValidationError("Invalid attribute type " + this.attribute + " with value object");
	}
	
	/* (non-Javadoc)
	 * @see it.dangelo.saj.impl.schema.IObjectValidator#endObject()
	 */
	public void endObject() throws SAJException {
		String[] man = this.element.getMandatoryAttributeNames();
		for (String string : man) {
			if(!this.checkedAttributes.contains(string))
				throw new SAJSchemaValidationError("Mandatory attribute not setted :" + string);
		}
		this.context.removeHandler();
	}

	public void startArray() throws SAJException {
		if (actualElement instanceof ArrayElementImpl) {
			ArrayElementImpl impl = (ArrayElementImpl) this.actualElement;
			context.addHandler(new ArrayValidator(impl, this.context));
		} else if (actualElement instanceof AnyElementImpl) {
			ArrayElementImpl impl = new ArrayElementImpl(new AnyElementImpl());
			context.addHandler(new ArrayValidator(impl, this.context));
		}else if (actualElement instanceof UnionElementImpl) {
			UnionElementImpl impl = (UnionElementImpl) actualElement;
			context.addHandler(new UnionValidator(impl, this.context));
			context.startArray();
		}else 
			throw new SAJSchemaValidationError("Invalid attribute type " + this.attribute + " with value array");
	}

	public void endArray() throws SAJException {
	}

	public void endJSON() throws SAJException {
	}

	public void startJSON() throws SAJException {
	}

}
