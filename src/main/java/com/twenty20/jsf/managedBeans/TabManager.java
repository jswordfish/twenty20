package com.twenty20.jsf.managedBeans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name="tab", eager = true)
@SessionScoped 
public class TabManager implements Serializable {
	String displayTab = "Dashboard";

	public String getDisplayTab() {
		return displayTab;
	}

	public void setDisplayTab(String displayTab) {
		this.displayTab = displayTab;
	}
	

			
}
