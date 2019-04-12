package com.feeye.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropUtil {
	private static final Logger logger = Logger.getLogger(PropUtil.class);
	
	public static String getPropertiesValue(String properties, String key) {
		Properties prop = new Properties();
		InputStream in = PropUtil.class.getResourceAsStream("/"+properties+".properties");
		try {
			prop.load(in);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		String url = (String) prop.get(key);
		return url;
	}
}
