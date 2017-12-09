package com.twenty20.jsf.managedBeans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.primefaces.context.RequestContext;

import com.twenty20.domain.Request;
import com.twenty20.domain.RequestDescription;
import com.twenty20.domain.Response;
import com.twenty20.domain.ResponseToRequestDescription;
import com.twenty20.services.RequestService;
import com.twenty20.services.ResponseService;
import com.twenty20.util.SearchParams;
@ManagedBean(name = "responseManager", eager = true)
@SessionScoped
public class ResponseManager {
	
	List<Request> requests = new ArrayList<>();
	

	
	transient RequestService  requestService;
	
	transient ResponseService responseService;
	
	SearchParams searchParams = new SearchParams();
	
	@ManagedProperty(value="#{tab}") 
	 TabManager tabManager;
	
	Response response;
	
	
	String responsePageTitle = "";
	
	Boolean error = false;
	
	@ManagedProperty(value="#{userManager}") 
	 UserManager userManager;
	 
	 
	
	@PostConstruct
	public void init() {
		requestService = SpringUtil.getService(RequestService.class);
		requests = requestService.findAll();
		responseService = SpringUtil.getService(ResponseService.class);
	}

	public List<Request> getRequests() {
		return requests;
	}

	public void setRequests(List<Request> requests) {
		this.requests = requests;
	}

	public SearchParams getSearchParams() {
		return searchParams;
	}

	public void setSearchParams(SearchParams searchParams) {
		this.searchParams = searchParams;
	}

	
	public String fetchResponses() {
		return "";
	}
	
	public void searchRequests() {
		requests = requestService.getRequests(getSearchParams());
		RequestContext.getCurrentInstance().update("responsesForm:reqs");
		tabManager.setDisplayTab("Responses");
	}
	
	public void allRequests() {
		requests = requestService.findAll();
		RequestContext.getCurrentInstance().update("responsesForm:reqs");
	}

	public TabManager getTabManager() {
		return tabManager;
	}

	public void setTabManager(TabManager tabManager) {
		this.tabManager = tabManager;
	}

	

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public String respond(Request request) {
		//step 1 check if request is alive
		int dt = new Date().compareTo(request.getRequestCloseDate());
			if(dt > 0) {
				setError(true);
				setResponsePageTitle("This request is closed! You can respond to it.");
				return "responseError.xhtml?faces-redirect=false";
			}
		
		//step 2 check if responses exist
		Response response = responseService.getResponse(request.getRequestName(), request.getBuyer(), request.getCompany(), userManager.getUser(), userManager.getUsr().getCompany().getCompanyName());
		//step 3 - Assosicate request with response
		
		if(response == null) {
				//create
				this.response = new Response();
				this.response.setRequestName(request.getRequestName());
				this.response.setBuyer(request.getBuyer());
				this.response.setBuyerCompany(request.getCompany());
				this.response.setSupplier(userManager.getUser());
				this.response.setSupplierCompany(userManager.getUsr().getCompany().getCompanyName());
				List<ResponseToRequestDescription> descriptions = new ArrayList<>();
					for(RequestDescription requestDescription : request.getRequestDescriptions()) {
						ResponseToRequestDescription description = new ResponseToRequestDescription(requestDescription.getSubProduct(), requestDescription.getVolumeRequired(), requestDescription.getUnit(), "");
						descriptions.add(description);
					}
				this.response.setResponseToRequestDescriptions(descriptions);
				setResponsePageTitle("You are submitting a response to following request - "+request.getRequestName());
			}
			else {
				setError(false);
				DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				String time = df.format(response.getLastModifiedDate());
				setResponsePageTitle("You are updating the response submited on "+time+". If you don't want to update click on Back Button");
				setResponse(response);
			}
		this.response.setRequest(request);
		return "response.xhtml?faces-redirect=false";
	}

	public String getResponsePageTitle() {
		return responsePageTitle;
	}

	public void setResponsePageTitle(String responsePageTitle) {
		this.responsePageTitle = responsePageTitle;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	
	
}
