package com.twenty20.jsf.managedBeans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

@ManagedBean(name="tabtest")
@SessionScoped 
public class TabManager {
	String displayTab = "Companies";

	public String getDisplayTab() {
		return displayTab;
	}

	public void setDisplayTab(String displayTab) {
		this.displayTab = displayTab;
	}
	
	public void activateTab(ActionEvent ae)
	{
	    String componentId = ae.getComponent().getId();
	    ae.getComponent().getAttributes();
	    setDisplayTab(componentId);
	}
			
}
