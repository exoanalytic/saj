package it.dangelo.saj.impl.schema.elements;

import java.util.HashMap;

import it.dangelo.saj.impl.schema.ElementResolver;
import it.dangelo.saj.validation.schemas.types.NullElement;

public class NullElementImpl extends AbstractElement implements NullElement {

	@Override
	protected boolean internalCheck(Object value) {
		return (value == null);
	}

	public String getType() {
		return "null";
	}
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("NullElment {");
		buffer.append(this.internalToString());
		buffer.append('}');
		return buffer.toString();
	}

	@Override
	protected void internalPopolateByMap(HashMap<String, Object> map, ElementResolver resolver) {
	}

	

}
