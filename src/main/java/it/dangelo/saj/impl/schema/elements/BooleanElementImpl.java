package it.dangelo.saj.impl.schema.elements;

import java.util.HashMap;

import it.dangelo.saj.impl.schema.ElementResolver;
import it.dangelo.saj.validation.schemas.types.BooleanElement;

public class BooleanElementImpl extends AbstractElement implements
		BooleanElement {

	@Override
	protected boolean internalCheck(Object value) {
		if(!(value instanceof Boolean))
			return false;
		return true;
	}

	public String getType() {
		return "boolean";
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("BooleanElment {");
		buffer.append(this.internalToString());
		buffer.append('}');
		return buffer.toString();
	}

	@Override
	protected void internalPopolateByMap(HashMap<String, Object> map, ElementResolver resolver) {
	}


}
