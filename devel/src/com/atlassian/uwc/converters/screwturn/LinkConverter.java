package com.atlassian.uwc.converters.screwturn;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.atlassian.uwc.converters.BaseConverter;
import com.atlassian.uwc.ui.Page;

public class LinkConverter extends BaseConverter {

	Logger log = Logger.getLogger(this.getClass());
	@Override
	public void convert(Page page) {

		log.info("Converting Links - start");
		
		String input = page.getOriginalText();
		String converted = convertLinks(input);
		page.setConvertedText(converted);
		
		log.info("Converting Links - complete");
	}
	
	Pattern linkPattern = Pattern.compile("\\[(.*)\\]");

	protected String convertLinks(String input) {
		Matcher linkFinder = linkPattern.matcher(input);
		StringBuffer sb = new StringBuffer();
		boolean found = false;
		while (linkFinder.find()) {
			String match = linkFinder.group(1);
			String[] parts = match.split("\\|");
			String link = parts.length > 0 ? parts[0] : match;
			String alias = parts.length == 2 ? parts[1] : null;
			
			String replacement = convertLink(link, alias);
			
			if (replacement != null) {
				
				linkFinder.appendReplacement(sb, replacement);
			
				found = true;
			}
		}
		if (found) {
			linkFinder.appendTail(sb);
			return sb.toString();
		}
		return input;
	}
	
	protected String convertLink(String link, String alias) {
		// ignore images
		if (link.startsWith("image")) {
			return null;
		}				
		
		link = convertInternalLink(link);
		
		if (alias == "" || alias == null) {
			return String.format("[%1$s]", link);
		}
		
		return String.format("[%1$s|%2$s]", alias, link);
	}

	Pattern externalPattern = Pattern.compile("^http|https|file|\\\\.*");
	private String convertInternalLink(String link) {
		
		Matcher externalFinder = externalPattern.matcher(link);
		
		if (!externalFinder.find()) {
			
			// remove the namespace
			if (link.contains(".")) {
				link = link.substring(link.indexOf(".")+1);
			}
			
			// replace hyphens with spaces
			return link.replace("-", " ");
		}

		return link;
	}
}
