package com.twenty20.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.twenty20.common.Twenty20Exception;

public class ConfUtil {
	/**
	 * Default location
	 */
	String documentsLocation ="/opt/hirebuddy/documents";
	
	/**
	 * Default location
	 */
	String documentsServerBaseUrl = "http://localhost/hirebuddy/documents/";
	
	static Properties properties = new Properties();
	
	static {
		try {
			
			FileInputStream fileInputStream = new FileInputStream("config.properties");
			properties.load(fileInputStream);
			fileInputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Twenty20Exception("Can't load config properties", e);
		} 
	}
	
	public static String get(String key) {
		return properties.getProperty(key);
	}

	public String getDocumentsLocation() {
		return documentsLocation;
	}

	public void setDocumentsLocation(String documentsLocation) {
		this.documentsLocation = documentsLocation;
	}

	public String getDocumentsServerBaseUrl() {
		return documentsServerBaseUrl;
	}

	public void setDocumentsServerBaseUrl(String documentsServerBaseUrl) {
		this.documentsServerBaseUrl = documentsServerBaseUrl;
	}
	
	
}
