package it.dangelo.saj;

import java.math.BigDecimal;
/**
 * Utility class to manage numeric elements
 */
public class NumberValueCodec {
	
	/**
	 * COnvert a string that represent a json numeric value in BigDecimal instance.
	 * @param value A string that contains a json numeric value
	 * @return Instance of BigDecimal
	 */
	public static BigDecimal convert(String value) throws Exception {
		return new BigDecimal(value);
	}
	
}
