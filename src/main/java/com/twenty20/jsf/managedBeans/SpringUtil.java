package com.twenty20.jsf.managedBeans;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringUtil {
	
	static ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("appContext.xml");
	
	public static  <T> T getService(Class<T> type){
		return ctx.getBean(type);
	}

}
