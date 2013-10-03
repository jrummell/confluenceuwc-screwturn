package com.atlassian.uwc.converters.screwturn;

import java.io.File;

import org.apache.log4j.Logger;

import com.atlassian.uwc.converters.BaseConverter;
import com.atlassian.uwc.ui.ConfluenceSettingsForm;
import com.atlassian.uwc.ui.Page;

public class AttachmentConverter extends BaseConverter {
	Logger log = Logger.getLogger(this.getClass()); 
	ConfluenceSettingsForm confSettings = null;

	public void convert(Page page) {
		log.info("Converting Attachments -- starting");
        // scan the page and create a list of attachments
        addAttachmentsToPage(page, this.getAttachmentDirectory());
		log.info("Converting Attachments -- complete");

	}
	
    /**
     * determines which attachments are sought by the page, and attaches them
     * @param page object to attach pages to
     */
    protected void addAttachmentsToPage(Page page, String attachmentPath) {
   	
    	//get filelist from the attachment directory
    	String pagePath = page.getFile().getPath();
		log.debug("Examining File Directory for page " + pagePath);
    	        
        File files[] = getPageFiles(attachmentPath, pagePath);
        if (files==null) {
            log.debug("no attachment files found in directory: "+attachmentPath);
            return;
        }
        
        // add attachments
        log.debug("Attaching files to page");
        
        for (File file : files) {
        	//attach the file
            log.debug("adding attachment: " + file.getName());
            page.addAttachment(file);
        }
    }

	protected File[] getPageFiles(String attachmentPath, String pagePath) {
        
		log.debug("getting attachments for " + pagePath);
		
		File pageFile = new File(pagePath);
		
		String pageFileName = pageFile.getName();
		String pageName = pageFileName.substring(0, pageFileName.indexOf(".cs"));
		
		String namespace = null;
		if (!pageFile.getParent().endsWith("Pages")) {
			namespace = pageFile.getParentFile().getName();
		}
		
		if (namespace != null) {
			pageName = namespace + "." + pageName;
		}
		
		File attachmentDir = new File(attachmentPath);
		File attachmentPageDir = new File(attachmentDir, pageName);
        
        return attachmentPageDir.listFiles();
	}
}
