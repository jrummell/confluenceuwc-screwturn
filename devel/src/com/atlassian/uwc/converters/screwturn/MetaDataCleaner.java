package com.atlassian.uwc.converters.screwturn;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.atlassian.uwc.converters.BaseConverter;
import com.atlassian.uwc.ui.Page;

public class MetaDataCleaner extends BaseConverter {

	Logger log = Logger.getLogger(this.getClass());
	public void convert(Page page) {
	
		log.info("Cleaning metadata - start");
		String convertedText = removeMeta(page.getConvertedText());
		
		page.setConvertedText(convertedText);
		log.info("Cleaning metadata - complete");
	}
	
	Pattern metaData = Pattern.compile("##PAGE##"+System.getProperty("line.separator"));
	
	public String removeMeta(String input)
	{
		Matcher metaFinder = metaData.matcher(input);
		if (metaFinder.find()) {
			int index = metaFinder.end();
			input = input.substring(index);
		}
		return input;
	}

}
