package com.twenty20.domain;

public enum ProjectValue {

	LESS_THAN_ONE_MILLION("LESS_THAN_ONE_MILLION", "< 1 Million INR"), BETWEEN_ONE_AND_FIVE_MILLION("BETWEEN_ONE_AND_FIVE_MILLION", "1 TO 5 Million INR"),
	BETWEEN_FIVE_AND_FIFTY_MILLION("BETWEEN_FIVE_AND_FIFTY_MILLION", "5+ TO 50 Million INR"), GREATER_THAN_FIFTY_MILLION("GREATER_THAN_FIFTY_MILLION", "> 50 Million INR");
	
	String value;
	
	String display;
	
	private ProjectValue(String value, String display){
		this.value = value;
		this.display = display;
	}

	public String getValue() {
		return value;
	}

	public String getDisplay() {
		return display;
	}
	
	
}
