package com.atlassian.uwc.converters.screwturn;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import junit.framework.TestCase;

public class LinkConverterTest extends TestCase {

	LinkConverter tester = null;
	Logger log = Logger.getLogger(this.getClass());
	protected void setUp() throws Exception {
		
		PropertyConfigurator.configure("log4j.properties");
		tester = new LinkConverter();
	}
	
	public void testConvertExternalLinks() {
		
		String input = "asdf asdf [http://google.com|Google] asdf asdf";
		String expected = "asdf asdf [Google|http://google.com] asdf asdf";
		String actual = tester.convertLinks(input);
		assertEquals(expected, actual);
		
		input = "asdf asdf [http://google.com] asdf asdf";
		expected = "asdf asdf [http://google.com] asdf asdf";
		actual = tester.convertLinks(input);
		assertEquals(expected, actual);
	}

	public void testConvertExternalLink() {
		
		String expected = "[Google|http://google.com]";
		String actual = tester.convertLink("http://google.com", "Google");
		assertEquals(expected, actual);
		
		expected = "[http://google.com]";
		actual = tester.convertLink("http://google.com", null);
		assertEquals(expected, actual);
	}
	
	public void testConvertInternalLinks() {
		
		String input = "asdf asdf [A-wiki-page|A wiki page] asdf asdf";
		String expected = "asdf asdf [A wiki page|A wiki page] asdf asdf";
		String actual = tester.convertLinks(input);
		assertEquals(expected, actual);
		
		input = "asdf asdf [A-wiki-page] asdf asdf";
		expected = "asdf asdf [A wiki page] asdf asdf";
		actual = tester.convertLinks(input);
		assertEquals(expected, actual);
	}
	
	public void testConvertInternalLink() {
		
		String expected = "[A wiki page|A wiki page]";
		String actual = tester.convertLink("A-wiki-page", "A wiki page");
		assertEquals(expected, actual);
		
		expected = "[A wiki page]";
		actual = tester.convertLink("A-wiki-page", null);
		assertEquals(expected, actual);
	}
	
	public void testConvertInternalSpaceLinks() {
		
		String input = "asdf asdf [NS.A-wiki-page|A wiki page] asdf asdf";
		String expected = "asdf asdf [A wiki page|A wiki page] asdf asdf";
		String actual = tester.convertLinks(input);
		assertEquals(expected, actual);
		
		input = "asdf asdf [A-wiki-page] asdf asdf";
		expected = "asdf asdf [A wiki page] asdf asdf";
		actual = tester.convertLinks(input);
		assertEquals(expected, actual);
	}

	public void testConvertInternalSpaceLink() {
		
		String expected = "[A wiki page|A wiki page]";
		String actual = tester.convertLink("NS.A-wiki-page", "A wiki page");
		assertEquals(expected, actual);
		
		expected = "[A wiki page]";
		actual = tester.convertLink("NS.A-wiki-page", null);
		assertEquals(expected, actual);
	}
	
	public void testConvertLinksIgnoreImages() {
		String input = "asdf [imageauto||{UP(Export.Migrating-Topics-From-SimpleWiki-to-ScrewTurn-Wiki)}1.JPG] asdf";
		String actual = tester.convertLinks(input);
		assertEquals(input, actual);
	}
}
