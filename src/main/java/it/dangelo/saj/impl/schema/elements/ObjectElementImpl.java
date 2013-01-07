package it.dangelo.saj.impl.schema.elements;

import static it.dangelo.saj.impl.schema.SchemaQNameConsts.FINAL;
import static it.dangelo.saj.impl.schema.SchemaQNameConsts.TYPE;
import it.dangelo.saj.SAJException;
import it.dangelo.saj.impl.schema.ElementResolver;
import it.dangelo.saj.validation.schemas.types.ObjectElement;
import it.dangelo.saj.validation.schemas.types.SchemaElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ObjectElementImpl extends AbstractElement implements ObjectElement{

	private HashMap<String, SchemaElement> attributes = new HashMap<String, SchemaElement>();
	private boolean _final = false;
	
	public void setFinal(boolean _final) {
		this._final = _final;
	}
	
	public void addAttribute(String name, SchemaElement element){
		this.attributes.put(name, element);
	}
	
	public String[] getAttributeNames() {
		return this.attributes.keySet().toArray(new String[this.attributes.size()]);
	}

	public SchemaElement getElement(String attributeString) {
		return this.attributes.get(attributeString);
	}

	@Override
	protected boolean internalCheck(Object value) {
		return true;
	}

	public boolean existAttribute(String attributeName) {
		return this.attributes.containsKey(attributeName);
	}

	public boolean isFinal() {
		return this._final;
	}

	public String[] getMandatoryAttributeNames() {
		ArrayList<String> list = new ArrayList<String>();
		Set<String> keySet = this.attributes.keySet();
		for (String name : keySet) {
			if(!this.attributes.get(name).isOptional())
				list.add(name);
		}
		return list.toArray(new String[list.size()]);
	}

	public String getType() {
		return "object";
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("ObjectElment {");
		buffer.append(this.internalToString());
		buffer.append(", final : ").append(this._final);
		buffer.append(", attributes [");
		if(this.attributes != null) {
			Set<String> keySet = this.attributes.keySet();
			for (String string : keySet) {
				buffer.append('(').append(string).append(':').append(this.attributes.get(string)).append(')');
			}
		}
		buffer.append(']');
		buffer.append('}');
		return buffer.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void internalPopolateByMap(HashMap<String, Object> map, ElementResolver resolver) throws SAJException {
		if(map.containsKey(FINAL))
			this.setFinal((Boolean) map.get(FINAL));
		Object objType = map.get(TYPE);
		if (objType instanceof HashMap) {
			HashMap<String, Object> objMap = (HashMap<String, Object>) objType;
			Set<String> keySet = objMap.keySet();
			for (String string : keySet) {
//				HashMap<String, Object> value = (HashMap<String, Object>) objMap.get(string);
				this.addAttribute(string, resolver.resolve(objMap.get(string)));
			}
		}

	}
}
