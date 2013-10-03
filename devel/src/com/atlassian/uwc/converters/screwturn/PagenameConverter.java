package com.atlassian.uwc.converters.screwturn;

import org.apache.log4j.Logger;

import com.atlassian.uwc.converters.BaseConverter;
import com.atlassian.uwc.ui.Page;

/**
 * removes the extension from the filename, so that the pages
 * are imported with the same pagename that the links will use
 */
public class PagenameConverter extends BaseConverter {

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	Logger log = Logger.getLogger(this.getClass());
	public void convert(Page page) {
		log.info("Converting Pagenames - start");
		
		String input = page.getOriginalText();
		String convertedName = convertPagename(input);
		page.setName(convertedName);
		
		log.info("Converting Pagenames - complete");
	}

	protected String convertPagename(String input) {

		String[] lines = input.split(LINE_SEPARATOR);
		
		if (lines.length > 0)
		{
			return lines[0];
		}
		
		return null;
	}

}
