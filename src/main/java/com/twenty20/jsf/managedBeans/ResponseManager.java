package com.twenty20.jsf.managedBeans;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.Part;

import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;

import com.twenty20.domain.Rebate;
import com.twenty20.domain.Request;
import com.twenty20.domain.RequestDescription;
import com.twenty20.domain.Response;
import com.twenty20.domain.ResponseStatus;
import com.twenty20.domain.ResponseToRequestDescription;
import com.twenty20.services.RebateService;
import com.twenty20.services.RequestService;
import com.twenty20.services.ResponseService;
import com.twenty20.util.SearchParams;
@ManagedBean(name = "responseManager", eager = true)
@SessionScoped
public class ResponseManager {
	
	List<Request> requests = new ArrayList<>();
	
	List<Request> requestsDirect = new ArrayList<>();
	
	transient RequestService  requestService;
	
	transient ResponseService responseService;
	
	transient RebateService rebateService;
	
	SearchParams searchParams = new SearchParams();
	
	@ManagedProperty(value="#{tab}") 
	 TabManager tabManager;
	
	Response response;
	
	
	String responsePageTitle = "";
	
	Boolean error = false;
	
	@ManagedProperty(value="#{userManager}") 
	 UserManager userManager;
	 
	 String[] rebates;
	 
	 UploadedFile uploadedFile;
	 
	 Part part;
	 
	 String responseCreationDt = "";
		
	String responseValidFromDt = "";
		
		String responseValidToDt = "";
		
		String fileAttached;
		
		List<Response> myResponses = new ArrayList<>();
	
	@PostConstruct
	public void init() {
		rebateService = SpringUtil.getService(RebateService.class);
			
		
		requestService = SpringUtil.getService(RequestService.class);
		
		requests = requestService.findAll();
		requestsDirect = requestService.getAllOpenRequestsByBuyerForSupplier(userManager.getUsr().getCompany().getCompanyName());
		responseService = SpringUtil.getService(ResponseService.class);
		
		reloadRebates();
	}
	
	public void refresh() {
		myResponses = responseService.getResponsesByCompany(userManager.getUsr().getCompany().getCompanyName());
		
		reloadRebates();
	}
	
	public void directRequests() {
		requestsDirect = requestService.getAllOpenRequestsByBuyerForSupplier(userManager.getUsr().getCompany().getCompanyName());
	}
	
	private void reloadRebates() {
		List<Rebate> rbs = rebateService.getActiveRebatesByCompany(userManager.getUsr().getCompany().getCompanyName());
		rebates = new String[rbs.size()];
			for(int i=0;i<rbs.size();i++) {
				rebates[i] = rbs.get(i).getRebateName();
			}
	}
	
	private byte[] getBytes(Part pt) throws IOException{
		 InputStream partInputStream=pt.getInputStream();
		    ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
		    byte[] chunk=new byte[8192];
		    int amountRead;
		    while ((amountRead=partInputStream.read(chunk)) != -1) {
		      outputStream.write(chunk,0,amountRead);
		    }
		    if (outputStream.size() == 0) {
		      return null;
		    }
		 else {
		      return outputStream.toByteArray();
		    }
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
	
	private void setDates() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if(this.response.getResponseCreationDate() != null){
			String dt = df.format(this.response.getResponseCreationDate());
			this.responseCreationDt = dt;
		}
		
		if(this.response.getResponseValidFromDate() != null){
			String dt = df.format(this.response.getResponseValidFromDate());
			this.responseValidFromDt = dt;
		}
		
		if(this.response.getResponseValidToDate() != null){
			String dt = df.format(this.response.getResponseValidToDate());
			this.responseValidToDt = dt;
		}
	}

	public String respond(Request request) {
		//step 1 check if request is alive
		int dt = new Date().compareTo(request.getRequestCloseDate());
			if(dt > 0) {
				setError(true);
				setResponsePageTitle("This request is closed! You can respond to it.");
				return "responseError.xhtml?faces-redirect=false";
			}
			else {
				setError(false);
				setResponsePageTitle("Responding to - "+request.getRequestName());
				reloadRebates();
				tabManager.setDisplayTab("Responses");
			}
		
		//step 2 check if responses exist
		Response response = responseService.getResponse(request.getRequestName(), request.getBuyer(), request.getCompany(), userManager.getUser(), userManager.getUsr().getCompany().getCompanyName());
		//step 3 - Assosicate request with response
		
		/**
		 * Always better to reload request incase it changed.
		 */
		request = requestService.getUniqueRequest(request.getRequestName(), request.getBuyer(), request.getCompany());
		
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
				setFileAttached("");
			}
			else {
				this.response = response;
				setError(false);
				DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				String time = df.format(response.getLastModifiedDate());
				setResponsePageTitle("You are updating the response submited on "+time+". If you don't want to update click on Back Button");
				//List<ResponseToRequestDescription> descriptions = new ArrayList<>();
//				for(RequestDescription requestDescription : request.getRequestDescriptions()) {
//					ResponseToRequestDescription description = new ResponseToRequestDescription(requestDescription.getSubProduct(), requestDescription.getVolumeRequired(), requestDescription.getUnit(), requestDescription);
//					descriptions.add(description);
//				}
				//this.response.setResponseToRequestDescriptions(descriptions);
				List<ResponseToRequestDescription> descriptions = figure(response, request);
				response.setResponseToRequestDescriptions(descriptions);
				
				setResponse(response);
				setFileAttached(response.getAdditionalDocumentUrl());
			}
		setDates();
		
		
		this.response.setRequest(request);
		return "response.xhtml?faces-redirect=false";
	}
	
	private List<ResponseToRequestDescription> figure(Response response, Request request){
		/**
		 * Step 1 Find Response descrs that are not mapped to Req Descs and discard them
		 */
		Map<String,RequestDescription> map = new HashMap<>();
		for(RequestDescription description : request.getRequestDescriptions()) {
			map.put(description.getSubProduct()+description.getUnit()+description.getVolumeRequired(), description);
		}
		
		for(Iterator<ResponseToRequestDescription> itr = response.getResponseToRequestDescriptions().iterator(); itr.hasNext();) {
			ResponseToRequestDescription des = itr.next();
			if(map.get(des.getSubProduct()+des.getUnit()+des.getVolumeRequired()) == null ) {
				itr.remove();
			}
		}
		
		/**
		 * Step 2 Find out those req descs who does not have a coreesponding res desc and then create response desc for it.
		 */
		Map<String,ResponseToRequestDescription> map2 = new HashMap<>();
		for(ResponseToRequestDescription responseToRequestDescription : response.getResponseToRequestDescriptions()) {
			map2.put(responseToRequestDescription.getSubProduct()+responseToRequestDescription.getUnit()+responseToRequestDescription.getVolumeRequired(), responseToRequestDescription);
			
		}
		
		for(RequestDescription requestDescription : request.getRequestDescriptions()) {
			if(map2.get(requestDescription.getSubProduct()+requestDescription.getUnit()+requestDescription.getVolumeRequired()) == null) {
				ResponseToRequestDescription description = new ResponseToRequestDescription(requestDescription.getSubProduct(), requestDescription.getVolumeRequired(), requestDescription.getUnit(), "");
				response.getResponseToRequestDescriptions().add(description);
			}
		}
		
	return response.getResponseToRequestDescriptions();
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

	public String[] getRebates() {
		return rebates;
	}

	public void setRebates(String[] rebates) {
		this.rebates = rebates;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
	
	
	public void upload() {
		if(uploadedFile != null){
			String fileName = uploadedFile.getFileName();
		    String contentType = uploadedFile.getContentType();
		    byte[] contents = uploadedFile.getContents(); // Or getInputStream()
		    this.response.setAdditionalDocument(contents);
		    this.response.setAdditionalDocumentExtension(fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()));
		    
		}
	    
	    // ... Save it, now!
	}
	
	public void handleFileUpload(AjaxBehaviorEvent event) throws IOException {
	   if(part != null) {
		   String fileName = getFileName(part);
		   // String contentType = uploadedFile.getContentType();
		    byte[] contents = getBytes(part); // Or getInputStream()
		    this.response.setAdditionalDocument(contents);
		    this.response.setAdditionalDocumentExtension(fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()));
		    setFileAttached(fileName);
		    RequestContext.getCurrentInstance().update("responseForm:fileAttached");
	   }
	   else {
		   setFileAttached("");
		    RequestContext.getCurrentInstance().update("responseForm:fileAttached");
	   }
	}
	
	private String getFileName(Part part) {
		for (String cd : part.getHeader("content-disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {
				return cd.substring(cd.indexOf('=') + 1).trim()
						.replace("\"", "");
			}
		}
		return null;
	}
	
	public String saveOrUpdate() {
		responseService.saveOrUpdate(response);
		tabManager.setDisplayTab("Responses");
		return "supplierTabs.xhtml?faces-redirect=true";
	}
	
	public String goBack() {
		tabManager.setDisplayTab("Responses");
		return "supplierTabs.xhtml?faces-redirect=true";
	}

	public String getResponseCreationDt() {
		return responseCreationDt;
	}

	public void setResponseCreationDt(String responseCreationDt) throws ParseException {
		this.responseCreationDt = responseCreationDt;
		
		if(this.responseCreationDt != null || this.responseCreationDt.trim().length() != 0){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = formatter.parse(responseCreationDt);
			getResponse().setResponseCreationDate(date);
		}
	}

	public String getResponseValidFromDt() {
		return responseValidFromDt;
	}

	public void setResponseValidFromDt(String responseValidFromDt) throws ParseException {
		this.responseValidFromDt = responseValidFromDt;
		if(this.responseValidFromDt != null || this.responseValidFromDt.trim().length() != 0){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = formatter.parse(responseValidFromDt);
			getResponse().setResponseValidFromDate(date);
		}
	}

	public String getResponseValidToDt() {
		return responseValidToDt;
	}

	public void setResponseValidToDt(String responseValidToDt) throws ParseException {
		this.responseValidToDt = responseValidToDt;
		if(this.responseValidToDt != null || this.responseValidToDt.trim().length() != 0){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = formatter.parse(responseValidToDt);
			getResponse().setResponseValidToDate(date);
		}
	}

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public String getFileAttached() {
		return fileAttached;
	}

	public void setFileAttached(String fileAttached) {
		this.fileAttached = fileAttached;
	}
	
	public String myResponses() {
		tabManager.setDisplayTab("Responses");
		myResponses = responseService.getResponsesByCompany(userManager.getUsr().getCompany().getCompanyName());
		return "myResponses.xhtml?faces-redirect=false";
	}
	
//	public String myResponsesToDirectRequests() {
//		tabManager.setDisplayTab("Responses");
//		myResponses = responseService.getResponsesByCompany(userManager.getUsr().getCompany().getCompanyName());
//		return "myResponses.xhtml?faces-redirect=false";
//	}
	
	public void markResponseAsVoid(Response response) {
		response.setResponseStatus(ResponseStatus.CANCEL.getStatus());
		responseService.saveOrUpdate(response);
		myResponses = responseService.getResponsesByCompany(userManager.getUsr().getCompany().getCompanyName());
		RequestContext.getCurrentInstance().update("myResponseForm:myResponses");
	}
	
	public String editResponse(Response response) {
		this.response = response;
		setError(false);
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String time = df.format(response.getLastModifiedDate());
		setResponsePageTitle("You are updating the response submited on "+time+". If you don't want to update click on Back Button");
		setResponse(response);
		setFileAttached(response.getAdditionalDocumentUrl());
		Request request = requestService.getUniqueRequest(response.getRequestName(), response.getBuyer(), response.getBuyerCompany());
		this.response.setRequest(request);
		setDates();
		return "response.xhtml?faces-redirect=false";
	}

	public List<Response> getMyResponses() {
		return myResponses;
	}

	public void setMyResponses(List<Response> myResponses) {
		this.myResponses = myResponses;
	}

	public List<Request> getRequestsDirect() {
		return requestsDirect;
	}

	public void setRequestsDirect(List<Request> requestsDirect) {
		this.requestsDirect = requestsDirect;
	}
	
	
}
