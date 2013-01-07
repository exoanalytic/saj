package it.dangelo.saj.impl.schema;

import it.dangelo.saj.impl.schema.elements.StringElementImpl;

import org.junit.Test;
import static junit.framework.Assert.*;

public class StringElementImplTest {
	@Test
	public void checkNullable() {
		StringElementImpl impl = new StringElementImpl();
		impl.setNullable(false);
		assertFalse(impl.check(null));
		assertTrue(impl.check("test"));
		impl.setNullable(true);
		assertTrue(impl.check(null));
	}
	
	@Test
	public void checkOptions() {
		StringElementImpl impl = new StringElementImpl();
		impl.setOptions(new String[] {"test1", "test2"});
		assertTrue(impl.check("test2"));
		assertTrue(impl.check("test3"));
		impl.setUnconstrained(false);
		assertFalse(impl.check("test3"));
	}
}
