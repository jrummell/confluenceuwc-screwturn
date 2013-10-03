package com.atlassian.uwc.converters.screwturn;

import org.apache.log4j.PropertyConfigurator;

import junit.framework.TestCase;

public class MetaDataCleanerTest extends TestCase {

	MetaDataCleaner tester = null;
	String lineSeparator = System.getProperty("line.separator");
	protected void setUp() throws Exception {
		tester = new MetaDataCleaner();
		PropertyConfigurator.configure("log4j.properties");
	}

	public void testConvert() {
		String input = "something"+lineSeparator+"SYSTEM|2010/06/03 12:09:48"+lineSeparator+"##PAGE##"+lineSeparator+"This is the main page of the namespace, created for you by the system.";
		String expected = "This is the main page of the namespace, created for you by the system.";
		String actual = tester.removeMeta(input);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}

}
