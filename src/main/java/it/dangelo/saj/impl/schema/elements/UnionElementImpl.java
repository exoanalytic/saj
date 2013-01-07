package it.dangelo.saj.impl.schema.elements;

import static it.dangelo.saj.impl.schema.SchemaQNameConsts.TYPE;

import java.util.HashMap;
import java.util.List;

import it.dangelo.saj.SAJException;
import it.dangelo.saj.impl.schema.ElementResolver;
import it.dangelo.saj.validation.schemas.types.NullElement;
import it.dangelo.saj.validation.schemas.types.SchemaElement;
import it.dangelo.saj.validation.schemas.types.UnionElement;

public class UnionElementImpl extends AbstractElement implements UnionElement {

	private AbstractElement[] elements = new AbstractElement[0];
	
	public void addElement(AbstractElement element) {
		int size = this.elements.length;
		AbstractElement[] newElements = new AbstractElement[size+1];
		System.arraycopy(this.elements, 0, newElements, 0, size);
		newElements[size] = element;
		this.elements = newElements;
		if(element instanceof NullElement)
			this.setNullable(true);
	}
	
	@Override
	protected boolean internalCheck(Object value) {
		boolean result = false;
		for (AbstractElement element : this.elements) {
			result = element.check(value);
			if(result)
				break;
		}
		return result;
	}

	public SchemaElement[] getTypes() {
		return this.elements;
	}

	public String getType() {
		return "union";
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("UnionElment {");
		buffer.append(this.internalToString());
		buffer.append(", [");
		for (AbstractElement element : elements) {
			buffer.append('(').append(element).append(')');
		}
		buffer.append(']');
		buffer.append('}');
		return buffer.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void internalPopolateByMap(HashMap<String, Object> map,
			ElementResolver resolver) throws SAJException {
		List<Object> listType = (List<Object>) map.get(TYPE);
		for (Object object : listType) {
			if (object instanceof String) {
				String type = (String) object;
				SchemaElement element = resolver.resolve(resolver.cloneMap(map, type));
				this.addElement((AbstractElement) element);
			} else if (object instanceof HashMap) {
				HashMap<String, Object> imap = (HashMap<String, Object>) object;
				SchemaElement element = resolver.resolve(imap);
				this.addElement((AbstractElement) element);
			} else if (object instanceof List) {
				List<Object> list = (List<Object>) object;
				SchemaElement element = resolver.resolve(resolver.cloneMap(map, list));
				this.addElement((AbstractElement) element);
			}
		}
	}
	

}
