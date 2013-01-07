package it.dangelo.saj.impl.schema.elements;

import java.util.HashMap;

import it.dangelo.saj.impl.schema.ElementResolver;
import it.dangelo.saj.validation.schemas.types.AnyElement;

public class AnyElementImpl extends AbstractElement implements AnyElement {

	@Override
	protected boolean internalCheck(Object value) {
		return true;
	}

	public String getType() {
		return "any";
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("AnyElment {");
		buffer.append(this.internalToString()).append('}');
		return buffer.toString();
	}

	@Override
	protected void internalPopolateByMap(HashMap<String, Object> map, ElementResolver resolver) {
	}
	
}
