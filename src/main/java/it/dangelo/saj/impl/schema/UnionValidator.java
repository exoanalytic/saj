package it.dangelo.saj.impl.schema;

import it.dangelo.saj.SAJException;
import it.dangelo.saj.impl.schema.elements.AbstractElement;
import it.dangelo.saj.impl.schema.elements.ArrayElementImpl;
import it.dangelo.saj.impl.schema.elements.ObjectElementImpl;
import it.dangelo.saj.impl.schema.elements.UnionElementImpl;
import it.dangelo.saj.parser.ContentHandler;
import it.dangelo.saj.validation.schemas.types.SchemaElement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UnionValidator implements ContentHandler {

	private HashMap<String, List<AbstractElement>> validators = new HashMap<String, List<AbstractElement>>();
	private SchemaValidatorContext context; 
	private UnionContext unionContext = new UnionContext();
	
	public UnionValidator(UnionElementImpl unionElement, SchemaValidatorContext context) {
//		this.unionElement = unionElement;
		this.context = context;
		SchemaElement[] types = unionElement.getTypes();
		for (SchemaElement schemaElement : types) {
			this.addElement(schemaElement.getType(), (AbstractElement)schemaElement);
		}
	}
	
	
	private void addElement(String type, AbstractElement element) {
		List<AbstractElement> list = this.validators.get(type);
		if(list == null) {
			list = new ArrayList<AbstractElement>();
			this.validators.put(type, list);
		}
		list.add(element);
	}

	public void _boolean(boolean value) throws SAJException {
		this.check(value, "boolean");
		this.context.removeHandler();
	}

	public void _null() throws SAJException {
		this.check(null, "null");
		this.context.removeHandler();
	}

	public void _number(BigDecimal value) throws SAJException {
		boolean valid = false;
		if(!this.validators.containsKey("integer") && !this.validators.containsKey("float"))
			throw new SAJException("Type number is not permetted");
		if(this.validators.containsKey("integer"))
			valid = this.checkType(value, "integer");
		if(valid) {
			this.context.removeHandler();
			return;
		}
		if(this.validators.containsKey("float"))
			valid = this.checkType(value, "float");
		if(!valid)
			throw new SAJException("Invalid value : " + value);
		this.context.removeHandler();
	}

	public void _string(String value) throws SAJException {
		this.check(value, "string");
		this.context.removeHandler();
	}
	/**
	 * 
	 * @param value
	 * @return 0=valid, 1=not permitted, 2=not valid
	 */
	private void check(Object value, String type) throws SAJException {
		/*
		AbstractElement[] elements = (AbstractElement[]) this.unionElement.getTypes();
		boolean exist = false;
		boolean valid = false;
		for (AbstractElement abstractElement : elements) {
			if(abstractElement.getType().equals(type)) {
				exist = true;
				valid = abstractElement.check(value);
				if(valid)
					break;
			}
		}
		return (exist?(valid?0:-2):-1);
		*/
		if(!this.validators.containsKey(type)) 
			throw new SAJException("Type " + type + " is not permetted!");
		boolean valid = checkType(value, type);
		if(!valid)
			throw new SAJException("Invalid value : " + value);
	}


	private boolean checkType(Object value, String type) {
		List<AbstractElement> list = this.validators.get(type);
		boolean valid = false;
		for (AbstractElement element : list) {
			if(element.check(value)) {
				valid = true;
				break;
			}
		}
		return valid;
	}

	public void attribute(String name) throws SAJException {}

	public void endArray() throws SAJException {}

	public void endJSON() throws SAJException {}

	public void endObject() throws SAJException {}

	public void startArray() throws SAJException {
		if(!this.validators.containsKey("array"))
			throw new SAJException("Type array is not permitted");
		List<AbstractElement> list = this.validators.get("array");
		this.context.removeHandler();
		for (AbstractElement abstractElement : list) {
			ArrayElementImpl impl = (ArrayElementImpl) abstractElement;
			SchemaValidatorContext ictx = new SchemaValidatorContext();
			ictx.addHandler(new ArrayValidator(impl,ictx));
			this.unionContext.addIntenalContext(ictx);
		}
		this.context.addHandler(this.unionContext);
	}

	public void startJSON() throws SAJException {}

	public void startObject() throws SAJException {
		if(!this.validators.containsKey("object"))
			throw new SAJException("Type object is not permitted");
		List<AbstractElement> list = this.validators.get("object");
		this.context.removeHandler();
		for (AbstractElement abstractElement : list) {
			ObjectElementImpl impl = (ObjectElementImpl) abstractElement;
			SchemaValidatorContext ictx = new SchemaValidatorContext();
			ictx.addHandler(new ObjectValidator(impl,ictx));
			this.unionContext.addIntenalContext(ictx);
		}
		this.context.addHandler(this.unionContext);

	}
	
	class UnionContext implements ContentHandler {
		private ArrayList<SchemaValidatorContext> contexts = new ArrayList<SchemaValidatorContext>();
		
		public void addIntenalContext(SchemaValidatorContext ctx) {
			this.contexts.add(ctx);
		}
		
		public void _boolean(boolean value) throws SAJException {
			ArrayList<Exception> list = new ArrayList<Exception>();
			ArrayList<SchemaValidatorContext> copy = new ArrayList<SchemaValidatorContext>(this.contexts);
			for (SchemaValidatorContext ctx : copy) {
				try {
					ctx._boolean(value);
				} catch (SAJException e) {
					this.contexts.remove(ctx);
					list.add(e);
				}
			}
			if(this.contexts.size() == 0)
				throw new UnionSAJException(list);
		}
		
		public void _null() throws SAJException {
			ArrayList<Exception> list = new ArrayList<Exception>();
			ArrayList<SchemaValidatorContext> copy = new ArrayList<SchemaValidatorContext>(this.contexts);
			for (SchemaValidatorContext ctx : copy) {
				try {
					ctx._null();
				} catch (SAJException e) {
					this.contexts.remove(ctx);
					list.add(e);
				}
			}
			if(this.contexts.size() == 0)
				throw new UnionSAJException(list);
		}
		
		public void _number(BigDecimal value) throws SAJException {
			ArrayList<Exception> list = new ArrayList<Exception>();
			ArrayList<SchemaValidatorContext> copy = new ArrayList<SchemaValidatorContext>(this.contexts);
			for (SchemaValidatorContext ctx : copy) {
				try {
					ctx._number(value);
				} catch (SAJException e) {
					this.contexts.remove(ctx);
					list.add(e);
				}
			}
			if(this.contexts.size() == 0)
				throw new UnionSAJException(list);
		}
		
		public void _string(String value) throws SAJException {
			ArrayList<Exception> list = new ArrayList<Exception>();
			ArrayList<SchemaValidatorContext> copy = new ArrayList<SchemaValidatorContext>(this.contexts);
			for (SchemaValidatorContext ctx : copy) {
				try {
					ctx._string(value);
				} catch (SAJException e) {
					this.contexts.remove(ctx);
					list.add(e);
				}
			}
			if(this.contexts.size() == 0)
				throw new UnionSAJException(list);
		}
		
		public void attribute(String name) throws SAJException {
			ArrayList<Exception> list = new ArrayList<Exception>();
			ArrayList<SchemaValidatorContext> copy = new ArrayList<SchemaValidatorContext>(this.contexts);
			for (SchemaValidatorContext ctx : copy) {
				try {
					ctx.attribute(name);
				} catch (SAJException e) {
					this.contexts.remove(ctx);
					list.add(e);
				}
			}
			if(this.contexts.size() == 0)
				throw new UnionSAJException(list);
		}
		
		public void endArray() throws SAJException {
			ArrayList<Exception> list = new ArrayList<Exception>();
			ArrayList<SchemaValidatorContext> copy = new ArrayList<SchemaValidatorContext>(this.contexts);
			for (SchemaValidatorContext ctx : copy) {
				try {
					ctx.endArray();
					if(ctx.isEmpty())
						this.contexts.remove(ctx);
				} catch (SAJException e) {
					this.contexts.remove(ctx);
					list.add(e);
				}
			}
			if(this.contexts.isEmpty() && !list.isEmpty())
				throw new UnionSAJException(list);
			if(this.contexts.isEmpty())
				context.removeHandler();
		}
		
		public void endJSON() throws SAJException {	}
		
		public void endObject() throws SAJException {
			ArrayList<Exception> list = new ArrayList<Exception>();
			ArrayList<SchemaValidatorContext> copy = new ArrayList<SchemaValidatorContext>(this.contexts);
			for (SchemaValidatorContext ctx : copy) {
				try {
					ctx.endObject();
					if(ctx.isEmpty())
						this.contexts.remove(ctx);
				} catch (SAJException e) {
					this.contexts.remove(ctx);
					list.add(e);
				}
			}
			if(this.contexts.size() == 0&& !list.isEmpty())
				throw new UnionSAJException(list);
			if(this.contexts.isEmpty())
				context.removeHandler();
		}
		
		public void startArray() throws SAJException {
			ArrayList<Exception> list = new ArrayList<Exception>();
			ArrayList<SchemaValidatorContext> copy = new ArrayList<SchemaValidatorContext>(this.contexts);
			for (SchemaValidatorContext ctx : copy) {
				try {
					ctx.startArray();
				} catch (SAJException e) {
					this.contexts.remove(ctx);
					list.add(e);
				}
			}
			if(this.contexts.size() == 0)
				throw new UnionSAJException(list);
		}
		
		public void startJSON() throws SAJException {
			ArrayList<Exception> list = new ArrayList<Exception>();
			ArrayList<SchemaValidatorContext> copy = new ArrayList<SchemaValidatorContext>(this.contexts);
			for (SchemaValidatorContext ctx : copy) {
				try {
					ctx.startJSON();
				} catch (SAJException e) {
					this.contexts.remove(ctx);
					list.add(e);
				}
			}
			if(this.contexts.size() == 0)
				throw new UnionSAJException(list);
		}
		
		public void startObject() throws SAJException {
			ArrayList<Exception> list = new ArrayList<Exception>();
			ArrayList<SchemaValidatorContext> copy = new ArrayList<SchemaValidatorContext>(this.contexts);
			for (SchemaValidatorContext ctx : copy) {
				try {
					ctx.startObject();
				} catch (SAJException e) {
					this.contexts.remove(ctx);
					list.add(e);
				}
			}
			if(this.contexts.size() == 0)
				throw new UnionSAJException(list);
		}
		
		
	}

}
