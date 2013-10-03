package com.atlassian.uwc.converters.screwturn;

import java.util.Date;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.atlassian.uwc.ui.Page;

public class UserDateConverterTest extends TestCase {

	Logger log = Logger.getLogger(this.getClass());
	UserDateConverter tester = null;
	protected void setUp() throws Exception {
		PropertyConfigurator.configure("log4j.properties");
		tester = new UserDateConverter();
	}
	
	String lineSeparator = System.getProperty("line.separator");
	public void testConvert() {
		String input, expected, actual;
		input = "Main Page" + lineSeparator +
		"foobar|2010/06/03 12:09:48" + lineSeparator +
		"Testing 123";
		Page page = new Page(null);
		page.setOriginalText(input);
		tester.convert(page);
		
		actual = page.getAuthor();
		expected = "foobar";
		assertNotNull(actual);
		assertEquals(expected, actual);
		
		Date timestamp = page.getTimestamp();
		Date expTime = new Date(2010-1900, 6-1, 3, 12, 9, 48);
		assertEquals(expTime.getTime(), timestamp.getTime());
	}
}
