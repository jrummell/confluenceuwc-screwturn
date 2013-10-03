package com.atlassian.uwc.converters.screwturn;

import java.io.File;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class AttachmentConverterTest extends TestCase {

	AttachmentConverter tester = null;
	Logger log = Logger.getLogger(this.getClass());
	protected void setUp() throws Exception {
		PropertyConfigurator.configure("log4j.properties");
		tester = new AttachmentConverter();
	}

	static final String publicPath = "C:\\Projects\\Confluence\\UWC\\devel\\sampleData\\screwturn\\Public";
	public void testGetPageFiles() {
		File publicDir = new File(publicPath);
		File attachmentDir = new File(publicDir, "Attachments");
		File pageDir = new File(publicDir, "Pages");
		File pageFile = new File(pageDir, "Test-Page.cs");
		
		File[] files = tester.getPageFiles(attachmentDir.getPath(), pageFile.getPath());
		
		assertNotNull(files);
		// the first file is .svn
		assertEquals(2, files.length);
		assertEquals("attachment1.txt", files[1].getName());
	}
	
	public void testGetNamespacePageFiles() {
		File publicDir = new File(publicPath);
		File attachmentDir = new File(publicDir, "Attachments");
		File pagesDir = new File(publicDir, "Pages\\Namespace");
		File pageFile = new File(pagesDir, "Test-Page2.cs");
		
		File[] files = tester.getPageFiles(attachmentDir.getPath(), pageFile.getPath());
		
		assertNotNull(files);
		// the first file is .svn		
		assertEquals(2, files.length);
		assertEquals("attachment2.txt", files[1].getName());
	}
}
