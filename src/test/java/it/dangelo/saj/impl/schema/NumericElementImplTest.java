package it.dangelo.saj.impl.schema;

import static org.junit.Assert.*;

import it.dangelo.saj.impl.schema.elements.FloatElementImpl;

import java.math.BigDecimal;

import org.junit.Test;

public class NumericElementImplTest {

	@Test
	public void checkMaxDigit() {
		FloatElementImpl impl = new FloatElementImpl();
		impl.setMaxDecimal(3);
		BigDecimal bigDecimal = new BigDecimal(12345.1229999999);
		assertFalse(impl.check(bigDecimal));
		bigDecimal = new BigDecimal(12345.123);
		assertTrue(impl.check(bigDecimal));
		bigDecimal = new BigDecimal(12345.12);
		assertTrue(impl.check(bigDecimal));
	}
	
	@Test
	public void checkBoundary() {
		BigDecimal bigDecimal = new BigDecimal(33.45);
		FloatElementImpl impl = new FloatElementImpl();
		impl.setMaximum(new BigDecimal(33));
		assertFalse(impl.check(bigDecimal));
		impl.setMaximum(new BigDecimal(33.45));
		assertTrue(impl.check(bigDecimal));
		impl.setMaximum(new BigDecimal(34));
		assertTrue(impl.check(bigDecimal));
		impl.setMinimum(new BigDecimal(34));
		assertFalse(impl.check(bigDecimal));
		impl.setMinimum(new BigDecimal(32));
		assertTrue(impl.check(bigDecimal));
		impl.setMaximum(new BigDecimal(33.45));
		impl.setMinimum(new BigDecimal(33.45));
		assertTrue(impl.check(bigDecimal));
	}

}
