package com.twenty20.domain;

public enum ResponseStatus {
	HOLD("HOLD"), NEGOTIATE("NEGOTIATE"), ACCEPT("ACCEPT"), DECLINE("DECLINE"), CANCEL("CANCEL");
	public String status;
	
	private ResponseStatus(String status){
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
	
	static String[] allStatus(){
		String tps[] = new String[ResponseStatus.values().length];
		int count = 0;
		for(ResponseStatus responseStatus:ResponseStatus.values()){
			tps[count] = responseStatus.getStatus();
			count++;
		}
		return tps;
	}

}
