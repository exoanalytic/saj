package it.dangelo.saj.impl.schema.elements;

import static it.dangelo.saj.impl.schema.SchemaQNameConsts.DEFAULT;
import static it.dangelo.saj.impl.schema.SchemaQNameConsts.MAX_LENGTH;
import static it.dangelo.saj.impl.schema.SchemaQNameConsts.MIN_LENGTH;
import static it.dangelo.saj.impl.schema.SchemaQNameConsts.OPTIONS;
import static it.dangelo.saj.impl.schema.SchemaQNameConsts.PATTERN;
import static it.dangelo.saj.impl.schema.SchemaQNameConsts.UNCONSTRAINED;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.dangelo.saj.SAJException;
import it.dangelo.saj.impl.schema.ElementResolver;
import it.dangelo.saj.validation.schemas.types.StringElement;

public class StringElementImpl extends AbstractElement implements StringElement {

	private String defaultValue;
	private Integer minLength;
	private Integer maxLength;
	private Pattern pattern;
	private boolean unconstrained = true;
	private String[] options;
	
	
	public void setDefault(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public String getDefault() {
		return this.defaultValue;
	}
	
	public void setMinLength(Integer length) {
		this.minLength = length;
	}

	public Integer getMinLength() {
		return this.minLength;
	}
	
	public void setMaxLength(Integer length) {
		this.maxLength = length;
	}

	public Integer getMaxLength() {
		return this.maxLength;
	}

	public void setPattern(String pattern) {
		if(pattern != null)
			this.pattern = Pattern.compile(pattern);
	}
	
	public String getPattern() {
		return this.pattern.toString();
	}

	public void setUnconstrained(boolean unconstrained) {
		this.unconstrained = unconstrained;
	}
	
	public boolean isUnconstrained() {
		return this.unconstrained;
	}

	public void setOptions(String[] options) {
		this.options = options;
	}
	
	public void addOption(String option) {
		if(this.options == null) this.options = new String[0];
		int newSize = this.options.length+1;
		String[] newOptions = new String[newSize];
		System.arraycopy(this.options, 0, newOptions, 0, this.options.length);
		newOptions[this.options.length] = option;
		this.options = newOptions;
	}
	
	public String[] options() {
		return this.options;
	}

	@Override
	protected boolean internalCheck(Object value) {
		if(!(value instanceof String))
			return false;
		String string = (String) value;
		boolean result = true;
		if(this.minLength != null)
			result = (string.length() >= this.minLength);
		if(result && this.maxLength != null)
			result = (string.length() <= this.maxLength);
		if(result && this.options != null) {
			boolean exists = false;
			for (String option : this.options) {
				if(string.equals(option)) {
					exists = true;
					break;
				}
			}
			result = !(!exists && !this.unconstrained);
		}
		if(result && this.pattern != null) {
			Matcher matcher = this.pattern.matcher(string);
			result = matcher.matches();
		}
		return result;
		
	}

	public String getType() {
		return "string";
	}


	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("StringElment {");
		buffer.append(this.internalToString());
		buffer.append(", defaultValue : ").append(this.defaultValue);
		buffer.append(", pattern : ").append(this.pattern);
		buffer.append(", minLength : ").append(this.minLength);
		buffer.append(", maxLength : ").append(this.maxLength);
		buffer.append(", options [");
		if(this.options != null) {
			for (String string : this.options) {
				buffer.append('(').append(string).append(')');
			}
		}
		buffer.append(']');
		buffer.append(", unconstrained : ").append(this.unconstrained);
		buffer.append('}');
		return buffer.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void internalPopolateByMap(HashMap<String, Object> map,
			ElementResolver resolver) throws SAJException {
		this.setDefault((String)map.get(DEFAULT));
		if(map.containsKey(MAX_LENGTH))
			this.setMaxLength(((BigDecimal) map.get(MAX_LENGTH)).intValue());
		if(map.containsKey(MIN_LENGTH))
			this.setMinLength(((BigDecimal) map.get(MIN_LENGTH)).intValue());
		if(map.containsKey(OPTIONS)) {
			List<String> opts = (List<String>) map.get(OPTIONS);
			this.setOptions(opts.toArray(new String[opts.size()]));
		}
		this.setPattern((String) map.get(PATTERN));
		if(map.containsKey(UNCONSTRAINED))
			this.setUnconstrained((Boolean) map.get(UNCONSTRAINED));

	}
}
