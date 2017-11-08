package com.twenty20.domain;

public enum ProjectType {
	
	RESIDENTIAL("RESIDENTIAL"), COMMERCIAL("COMMERCIAL"), INFRASTRUCTURE("INFRASTRUCTURE");
	String type;
	
	private ProjectType(String type){
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	static String[] allTypes(){
		String tps[] = new String[ProjectType.values().length];
		int count = 0;
		for(ProjectType projectType:ProjectType.values()){
			tps[count] = projectType.getType();
			count++;
		}
		return tps;
	}

}
