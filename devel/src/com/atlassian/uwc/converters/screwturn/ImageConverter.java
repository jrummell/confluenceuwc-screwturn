package com.atlassian.uwc.converters.screwturn;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.atlassian.uwc.converters.BaseConverter;
import com.atlassian.uwc.ui.Page;

public class ImageConverter extends BaseConverter {
	
	Logger log = Logger.getLogger(this.getClass());

	public void convert(Page page) {
		log.info("Converting Images - start");
		
		String input = page.getOriginalText();
		String converted = convertImages(input);
		page.setConvertedText(converted);

		log.info("Converting Images - complete");
	}

	// Group 1: align, 2: namespace, 3: page name, 4: filename
	String image = 
		"\\[image(\\w+)?\\|\\|\\{UP\\((?:(\\w+)\\.)?([\\w-]+)\\)\\}([\\w- ]+\\.[\\w]+)\\]";

	Pattern imagePattern = Pattern.compile(image, Pattern.CASE_INSENSITIVE);

	protected String convertImages(String input) {
		Matcher imageFinder = imagePattern.matcher(input);
		StringBuffer sb = new StringBuffer();
		boolean found = false;
		while (imageFinder.find()) {
			found = true;
			
			// since we only used auto aligned images attached to the current page,
			// we only need to capture the filename
			String align = imageFinder.group(1);
			String namespace = imageFinder.group(2);
			String pagename = imageFinder.group(3);
			String filename = imageFinder.group(4);
			log.debug("image content = " + filename);
			String replacement = "!" + filename + "!";
			imageFinder.appendReplacement(sb, replacement);
		}
		if (found) {
			imageFinder.appendTail(sb);
			return sb.toString();
		}
		return input;
	}
}
