package it.dangelo.saj.impl.schema.elements;

import static it.dangelo.saj.impl.schema.SchemaQNameConsts.MAX_ITEMS;
import static it.dangelo.saj.impl.schema.SchemaQNameConsts.MIN_ITEMS;
import static it.dangelo.saj.impl.schema.SchemaQNameConsts.TYPE;
import it.dangelo.saj.SAJException;
import it.dangelo.saj.impl.schema.ElementResolver;
import it.dangelo.saj.validation.schemas.types.ArrayElement;
import it.dangelo.saj.validation.schemas.types.SchemaElement;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class ArrayElementImpl extends AbstractElement implements ArrayElement {

	private Integer maxItems;
	private Integer minItems;
	private AbstractElement element ;
	
	public ArrayElementImpl(AbstractElement element) {
		this.element = element;
	}
	
	public ArrayElementImpl() {
		this.element = new AnyElementImpl();
	}
	
	@Override
	protected boolean internalCheck(Object value) {
		return this.element.check(value);
	}

	public void setMaxItems(Integer maxItems) {
		this.maxItems = maxItems;
	}
	
	public Integer getMaxItems() {
		return this.maxItems;
	}

	public void setMinItems(Integer minItems) {
		this.minItems = minItems;
	}
	
	public Integer getMinItems() {
		return this.minItems;
	}

	public String getType() {
		return "array";
	}

	public SchemaElement getItemType() {
		return this.element;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("ArrayElment {");
		buffer.append(this.internalToString());
		buffer.append(", itemType : ").append(this.element);
		buffer.append(", minItems : ").append(this.minItems);
		buffer.append(", maxItems : ").append(this.maxItems);
		buffer.append('}');
		return buffer.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void internalPopolateByMap(HashMap<String, Object> map, ElementResolver resolver) throws SAJException {
		if(map.containsKey(MAX_ITEMS)) 
			this.setMaxItems(((BigDecimal)map.get(MAX_ITEMS)).intValue());
		if(map.containsKey(MIN_ITEMS)) 
			this.setMinItems(((BigDecimal)map.get(MIN_ITEMS)).intValue());
		Object type = map.get(TYPE);
		SchemaElement element = null;
		List list = (List) type;
		int size = list.size();
		if(size == 0)
			element = resolver.resolve("any");
		else
			element = resolver.resolve(resolver.cloneMap(map, list.get(0)));
		this.element = (AbstractElement) element;
	}
}
