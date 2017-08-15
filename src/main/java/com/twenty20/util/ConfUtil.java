package com.twenty20.util;

public class ConfUtil {
	/**
	 * Default location
	 */
	String documentsLocation ="/opt/hirebuddy/documents";
	
	/**
	 * Default location
	 */
	String documentsServerBaseUrl = "http://localhost/hirebuddy/documents/";

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
