package it.dangelo.saj.impl.schema.elements;

import java.math.BigDecimal;
import static it.dangelo.saj.impl.schema.SchemaQNameConsts.*;
import java.util.HashMap;

import it.dangelo.saj.impl.schema.ElementResolver;
import it.dangelo.saj.validation.schemas.types.FloatElement;

public class FloatElementImpl extends AbstractElement implements
		FloatElement {

	private Integer maxDecimal;
	private BigDecimal maximum;
	private BigDecimal minimum;
	
	public void setMaxDecimal(Integer maxDecimal) {
		this.maxDecimal = maxDecimal;
	}
	
	public Integer getMaxDecimal() {
		return this.maxDecimal;
	}

	public void setMaximum(BigDecimal maximum) {
		this.maximum = maximum;
	}
	
	public BigDecimal getMaximum() {
		return this.maximum;
	}

	public void setMinimum(BigDecimal minimum) {
		this.minimum = minimum;
	}
	
	public BigDecimal getMinimum() {
		return this.minimum;
	}

	@Override
	protected boolean internalCheck(Object value) {
		if(!(value instanceof BigDecimal))
			return false;
		BigDecimal num = (BigDecimal) value;
		boolean result = true;
		if(this.maximum != null)
			result = num.compareTo(this.maximum)<=0;
		if(result && this.minimum != null)
			result = num.compareTo(this.minimum)>=0;
		if(result && this.maxDecimal != null) {
			String val_ = Double.valueOf(num.toString()).toString();
			int indexOf = val_.indexOf('.');
			int numDecs = (indexOf != -1? val_.substring(indexOf+1).length():0);
			result = numDecs <= this.maxDecimal;
		}
		return result;
	}

	public String getType() {
		return "float";
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("FloatElment {");
		buffer.append(this.internalToString());
		buffer.append(", minimum : ").append(this.minimum);
		buffer.append(", maximum : ").append(this.maximum);
		buffer.append(", maxDecimal : ").append(this.maxDecimal);
		buffer.append('}');
		return buffer.toString();
	}
	
	@Override
	protected void internalPopolateByMap(HashMap<String, Object> map, ElementResolver resolver) {
		if(map.containsKey(MINIMUM))
			this.setMinimum(((BigDecimal)map.get(MINIMUM)));
		if(map.containsKey(MAXIMUM))
			this.setMaximum(((BigDecimal)map.get(MAXIMUM)));
		if(map.containsKey(MAX_DECIMAL))
			this.setMaxDecimal(((BigDecimal)map.get(MAX_DECIMAL)).intValue());
	}

}
