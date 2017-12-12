package com.twenty20.jsf.managedBeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.primefaces.context.RequestContext;

import com.twenty20.domain.Response;
import com.twenty20.domain.ResponseStatus;
import com.twenty20.services.ResponseService;

@ManagedBean(name = "myResponseManager", eager = true)
@SessionScoped
public class MyResponseManager {


	
	transient ResponseService responseService;
	
	@ManagedProperty(value="#{tab}") 
	 TabManager tabManager;
	
	@ManagedProperty(value="#{userManager}") 
	 UserManager userManager;
	
	List<Response> myResponses = new ArrayList<>();
	
	@PostConstruct
	public void init() {
		
		responseService = SpringUtil.getService(ResponseService.class);
		myResponses = responseService.getResponsesByCompany(userManager.getUsr().getCompany().getCompanyName());
		
	}

	public List<Response> getMyResponses() {
		return myResponses;
	}

	public void setMyResponses(List<Response> myResponses) {
		this.myResponses = myResponses;
	}
	
	public String goBack() {
		tabManager.setDisplayTab("Responses");
		return "supplierTabs.xhtml?faces-redirect=true";
	}
	
	public void markResponseAsVoid(Response response) {
		response.setResponseStatus(ResponseStatus.CANCEL.getStatus());
		responseService.saveOrUpdate(response);
		myResponses = responseService.getResponsesByCompany(userManager.getUsr().getCompany().getCompanyName());
		RequestContext.getCurrentInstance().update("myResponseForm:myResponses");
	}

	public TabManager getTabManager() {
		return tabManager;
	}

	public void setTabManager(TabManager tabManager) {
		this.tabManager = tabManager;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	
}
