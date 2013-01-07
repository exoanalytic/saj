package it.dangelo.saj.impl.schema.elements;

import static it.dangelo.saj.impl.schema.SchemaQNameConsts.MAXIMUM;
import static it.dangelo.saj.impl.schema.SchemaQNameConsts.MINIMUM;

import java.math.BigDecimal;
import java.util.HashMap;

import it.dangelo.saj.impl.schema.ElementResolver;
import it.dangelo.saj.validation.schemas.types.IntegerElement;

public class IntegerElementImpl extends AbstractElement implements IntegerElement {

	private Integer minimum;
	private Integer maximum;
	public Integer getMinimum() {
		return minimum;
	}
	public void setMinimum(Integer minimum) {
		this.minimum = minimum;
	}
	public Integer getMaximum() {
		return maximum;
	}
	public void setMaximum(Integer maximum) {
		this.maximum = maximum;
	}
	@Override
	protected boolean internalCheck(Object value) {
		boolean result = false;
		if (value instanceof Integer) 
			result = true;
		else if (value instanceof BigDecimal) {
			BigDecimal decimal = (BigDecimal) value;
			String val_ = Double.valueOf(decimal.toString()).toString();
			int indexOf = val_.indexOf('.');
			int decs = (indexOf != -1? Integer.parseInt(val_.substring(indexOf+1)):0);
			if(decs == 0)
				result = true;
			else
				result = false;
		}
		return result;
	}
	public String getType() {
		return "integer";
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("IntegerElment {");
		buffer.append(this.internalToString());
		buffer.append(", minimum : ").append(this.minimum);
		buffer.append(", maximum : ").append(this.maximum);
		buffer.append('}');
		return buffer.toString();
	}
	@Override
	protected void internalPopolateByMap(HashMap<String, Object> map, ElementResolver resolver) {
		if(map.containsKey(MINIMUM))
			this.setMinimum(((BigDecimal)map.get(MINIMUM)).intValue());
		if(map.containsKey(MAXIMUM))
			this.setMaximum(((BigDecimal)map.get(MAXIMUM)).intValue());
	}

	
}
