package com.twenty20.common;

public class Twenty20Exception extends RuntimeException{

	public Twenty20Exception(){
		super();
	}
	
	public Twenty20Exception(String message){
		super(message);
	}
	
	public Twenty20Exception(String message, Throwable t){
		super(message, t);
	}

}
