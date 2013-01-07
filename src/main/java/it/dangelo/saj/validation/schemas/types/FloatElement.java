package it.dangelo.saj.validation.schemas.types;


import java.math.BigDecimal;

public interface FloatElement extends SchemaElement {
	BigDecimal getMaximum();
	BigDecimal getMinimum();
	Integer getMaxDecimal();
}
