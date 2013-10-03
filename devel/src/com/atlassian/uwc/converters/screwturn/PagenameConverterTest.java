package com.atlassian.uwc.converters.screwturn;

import org.apache.log4j.PropertyConfigurator;

import junit.framework.TestCase;

public class PagenameConverterTest extends TestCase {

	PagenameConverter tester = null;
	String lineSeparator = System.getProperty("line.separator");
	protected void setUp() throws Exception {
		tester = new PagenameConverter();
		PropertyConfigurator.configure("log4j.properties");
	}

	public void testConvertPagename() {
		String input = "something"+lineSeparator+"SYSTEM|2010/06/03 12:09:48"+lineSeparator+"##PAGE##";
		String expected = "something";
		String actual = tester.convertPagename(input);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}
	
	public void testHandleSpace() {
		String input, expected, actual;
		input = "some thing"+lineSeparator+"SYSTEM|2010/06/03 12:09:48"+lineSeparator+"##PAGE##"+lineSeparator+"asdf asdf asdf";
		expected = "some thing";
		actual = tester.convertPagename(input);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}

}
