package com.twenty20.domain;

public enum UserType {
	
	
	
	SUPPLIER("SUPPLIER"), BUYER("BUYER"), ADMIN("ADMIN");
	
	String userType;
	
	private UserType(String type){
		this.userType = type;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	

}
