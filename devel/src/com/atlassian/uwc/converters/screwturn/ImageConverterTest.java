package com.atlassian.uwc.converters.screwturn;

import java.io.File;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.atlassian.uwc.ui.Page;

public class ImageConverterTest extends TestCase {

	ImageConverter tester = null;
	Logger log = Logger.getLogger(this.getClass());
	protected void setUp() throws Exception {
		
		PropertyConfigurator.configure("log4j.properties");
		tester = new ImageConverter();
	}
	
	public void testExistingImageConversion() {
		String input = "[imageauto||{UP(namespace.topic)}filename.JPG]"; 
		String expected = "!filename.JPG!";
		String actual = tester.convertImages(input);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}
	
	public void testImageConvertWithContext() {
		String input = 
			"uwc-101: Mediawiki image conversion syntax needs to be case insensitive\n" +
			"[imageauto||{UP(namespace.topic)}filename.JPG]\n" +
					"After\n";
		String expected = "uwc-101: Mediawiki image conversion syntax needs to be case insensitive\n" +
			"!filename.JPG!\n" +
			"After\n";
		String actual = tester.convertImages(input);
		assertNotNull(actual);
		assertEquals(expected, actual);

		Page page = new Page(new File(""));
		page.setOriginalText(input);
		tester.convert(page);
		actual = page.getConvertedText();
		assertNotNull(actual);
		assertEquals(expected, actual);
	}
}
