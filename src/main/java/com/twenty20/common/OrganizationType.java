package com.twenty20.common;

public enum OrganizationType {
	
	COMPANY_LIMITED_BY_SHARES("COMPANY_LIMITED_BY_SHARES"),	CORPORATION("CORPORATION"),	LIMITED_LIABILITY_PARTNERSHIP("LIMITED_LIABILITY_PARTNERSHIP");
	
	String orgType;
	
	private OrganizationType(String type){
		orgType = type;
	}

	public String getOrgType() {
		return orgType;
	}
	
	

}
