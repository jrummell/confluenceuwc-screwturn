package com.atlassian.uwc.converters.screwturn;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.atlassian.uwc.converters.BaseConverter;
import com.atlassian.uwc.ui.Page;

public class UserDateConverter extends BaseConverter {

	Logger log = Logger.getLogger(this.getClass());
	public void convert(Page page) {
		log.debug("Adding User and Date metadata - Starting");
		
		String input = page.getOriginalText();
		String username = getUser(input);
		Date timestamp = getDate(input);
		log.debug("author: " + username);
		log.debug("timestamp: " + timestamp);
		
		if (username != null) page.setAuthor(username);
		if (timestamp != null) page.setTimestamp(timestamp);
		log.debug("page.getAuthor: " + page.getAuthor());
		log.debug("Adding User and Date metadata - Complete");
	}
	
	String lineSeparator = System.getProperty("line.separator");
	Pattern userAndDate = Pattern.compile("(\\w+)\\|(\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2})"+lineSeparator);
	
	protected String getUserAndDate(String input) {

		String[] lines = input.split("\n");
		
		if (lines.length > 2)
		{
			return lines[1];
		}
		
		return null;
	}
	
	protected String getUser(String input) {
		
		Matcher userFinder = userAndDate.matcher(input);
		if (userFinder.find()) {
			return userFinder.group(1);
		}
		return null;
	}
	
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
	protected Date getDate(String input) {

		Matcher dateFinder = userAndDate.matcher(input);
		if (dateFinder.find()) {
			String timestamp = dateFinder.group(2);
			try {
				return dateFormat.parse(timestamp);
			} catch (ParseException e) {
				log.error("Couldn't format date: " + timestamp);
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
}
