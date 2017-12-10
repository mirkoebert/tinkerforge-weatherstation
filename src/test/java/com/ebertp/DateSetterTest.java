package com.ebertp;

import static org.junit.Assert.*;

import org.junit.Test;

public class DateSetterTest {

	@Test
	public void testFillWithBlankToLength() {
		DateSetter ds = new DateSetter(null);
		String r = ds.fillWithBlankToLength(10, "");
		assertEquals(10, r.length());
		//fail("Not yet implemented");
	}

}
