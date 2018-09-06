package com.twenty20.jsf.managedBeans;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;

import com.mysql.jdbc.Util;
import com.twenty20.domain.Project;
import com.twenty20.domain.Rebate;
import com.twenty20.domain.Request;
import com.twenty20.domain.RequestDescription;
import com.twenty20.domain.Response;
import com.twenty20.domain.ResponseStatus;
import com.twenty20.domain.User;
import com.twenty20.domain.UserType;
import com.twenty20.jsf.charts.barchart.model.RebatesChart;
import com.twenty20.jsf.reports.model.RebateModel;
import com.twenty20.jsf.reports.model.TableModel;
import com.twenty20.services.ProjectService;
import com.twenty20.services.RebateService;
import com.twenty20.services.RequestService;
import com.twenty20.services.ResponseService;
import com.twenty20.services.UserService;

@ManagedBean(name = "requestManager", eager = true)
@SessionScoped
public class RequestManager {
	
	String title;

	List<Request> requests;
	
	transient RequestService requestService;
	
	transient ProjectService projectService;
	
	Request request = new Request();
	
	UploadedFile uploadedFile;
	
	String requestCategories[] = {"Cement", "Stone Chips", "Sand", "ReadyMix", "Steel ReBar", "FormWork", "Other"};
	
	String projects[] ;
	
	User user;
	
	String requestOpenDate = "";
	
	String requestCloseDate = "";
	
	String deliveryStartDate = "";
	
	 @ManagedProperty(value="#{userManager}") 
	 UserManager userManager;
	 
	 @ManagedProperty(value="#{tab}") 
	 TabManager tabManager;
	 
	 Boolean enableMinTurnOverTextField = false;
	 
	 Boolean enableReferencesRequired = false;
	 
	 Boolean enableTradeAssociationRequired = false;
	 
	 Boolean enableCertificateRequired = false;
	 
	 Boolean enableInsurancesRequired = false;
	 
	 transient ResponseService responseService;
	 
	 transient RebateService rebateService;
	 
	 transient UserService userService;
	 
	 List<Response> responsesToRequest = new ArrayList<>();
	 
	 Set<String> supplierCompanies = new HashSet();
	 
	 Response response;
	 
	 RebatesChart  rebatesChart;
	 
	 TableModel pricingModel;
	
	@PostConstruct
	public void init() {
		requestService = SpringUtil.getService(RequestService.class);
		projectService = SpringUtil.getService(ProjectService.class);
		responseService = SpringUtil.getService(ResponseService.class);
		rebateService = SpringUtil.getService(RebateService.class);
		userService = SpringUtil.getService(UserService.class);
		//requests = requestService.findAll();
		reload();
		//projectService.getProjectsByCompany(user)
	}
	
	public Set<String> getSupplierCompanies() {
		return supplierCompanies;
	}

	public void setSupplierCompanies(Set<String> supplierCompanies) {
		this.supplierCompanies = supplierCompanies;
	}

	public void reload() {
		//requests = requestService.getAllOpenRequests();
		user = userManager.getUsr();
		requests = requestService.getAllOpenRequestsByBuyer(user.getUserName(), user.getCompany().getCompanyName());
		supplierCompanies = userService.getCompaniesByType(UserType.SUPPLIER.getUserType());
	}
	
	public String edit(Request request) {
		setTitle("Update "+request.getRequestName());
		this.request = request;
		
		setDates();
		setQualificationFields();
		/**
		 * Its always better to reload projects incase if any new is added
		 */
		getProjects();
		return "request.xhtml?faces-redirect=false";
	}
	
	private void setQualificationFields() {
		if(this.request.getMinimumTurnOverRequiredText() != null && this.request.getMinimumTurnOverRequiredText() == 0.0) {
			this.enableMinTurnOverTextField = true;
		}
		else {
			this.enableMinTurnOverTextField = false;
		}
		
		if(this.request.getReferencesRequiredText() != null && this.request.getReferencesRequiredText().trim().length() > 0) {
			this.enableReferencesRequired = true;
		}
		else {
			this.enableReferencesRequired = false;
		}
		
		if(this.request.getTradeAssociationRequiredText() != null && this.request.getTradeAssociationRequiredText().trim().length() > 0) {
			this.enableTradeAssociationRequired = true;
		}
		else {
			this.enableTradeAssociationRequired = false;
		}
		
		if(this.request.getCertificateRequiredText() != null && this.request.getCertificateRequiredText().trim().length() > 0) {
			this.enableCertificateRequired = true;
		}
		else {
			this.enableCertificateRequired = false;
		}
		
		if(this.request.getInsurancesRequiredText() != null && this.request.getInsurancesRequiredText().trim().length() > 0) {
			this.enableInsurancesRequired = true;
		}
		else {
			this.enableInsurancesRequired = false;
		}
	}
	
	private void setDates() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if(this.request.getRequestOpenDate() != null){
			String dt = df.format(this.request.getRequestOpenDate());
			this.requestOpenDate = dt;
		}
		
		if(this.request.getRequestCloseDate() != null){
			String dt = df.format(this.request.getRequestCloseDate());
			this.requestCloseDate = dt;
		}
		
		if(this.request.getDeliveryStartDate() != null){
			String dt = df.format(this.request.getDeliveryStartDate());
			this.deliveryStartDate = dt;
		}
	}
	
	public void close(Request request) {
		setTitle("Create New Request");
		//request.setr
		request.setClosed(true);
		requestService.saveOrUpdate(request);
		//requests = requestService.getAllOpenRequests();
		reload();
	}
	
	public String createNew() {
		this.request = new Request();
		setTitle("New Request");
		setDates();
		setQualificationFields();
		this.request.setBuyer(getUser().getUserName());
		this.request.setCompany(getUser().getCompany().getCompanyName());
		return "request.xhtml?faces-redirect=false";
	}
	
	public void upload() {
		if(uploadedFile != null){
			String fileName = uploadedFile.getFileName();
		    String contentType = uploadedFile.getContentType();
		    byte[] contents = uploadedFile.getContents(); // Or getInputStream()
		    this.request.setRequestSpecification(contents);
		    this.request.setRequestSpecificationFileExtension(fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()));
		    
		}
	    
	    // ... Save it, now!
	}
	
	public String saveOrUpdate() {
		requestService.saveOrUpdate(request);
		
		//requests = requestService.getAllOpenRequests();
		reload();
		tabManager.setDisplayTab("Requests");
		return "bootstrapTabs.xhtml?faces-redirect=true";
	}

	public List<Request> getRequests() {
		return requests;
	}

	public void setRequests(List<Request> requests) {
		this.requests = requests;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String[] getRequestCategories() {
		return requestCategories;
	}

	public void setRequestCategories(String[] requestCategories) {
		this.requestCategories = requestCategories;
	}

	public String[] getProjects() {
			//if(this.projects == null || this.projects.length == 0) {
				List<Project> ps= projectService.getProjectsByCompany(getUserManager().getUsr().getCompany().getCompanyName());
				this.projects = new String[ps.size()];
				for(int i=0;i<ps.size();i++) {
					
					this.projects[i] = ps.get(i).getProjectName();
				}
			//}
		return projects;
	}

	public void setProjects(String[] projects) {
		this.projects = projects;
	}

	public User getUser() {
			if(this.user == null) {
				this.user = userManager.getUsr();
			}
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRequestOpenDate() {
		return requestOpenDate;
	}

	public void setRequestOpenDate(String requestOpenDate) throws ParseException{
		this.requestOpenDate = requestOpenDate;
		if(this.requestOpenDate != null || this.requestOpenDate.trim().length() != 0){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = formatter.parse(requestOpenDate);
			getRequest().setRequestOpenDate(date);
		}
	}

	public String getRequestCloseDate() {
		return requestCloseDate;
	}

	public void setRequestCloseDate(String requestCloseDate) throws ParseException{
		this.requestCloseDate = requestCloseDate;
		if(this.requestCloseDate != null || this.requestCloseDate.trim().length() != 0){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = formatter.parse(requestCloseDate);
			getRequest().setRequestCloseDate(date);
		}
	}

	public String getDeliveryStartDate() {
		return deliveryStartDate;
	}

	public void setDeliveryStartDate(String deliveryStartDate) throws ParseException{
		this.deliveryStartDate = deliveryStartDate;
		if(this.deliveryStartDate != null || this.deliveryStartDate.trim().length() != 0){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = formatter.parse(deliveryStartDate);
			getRequest().setDeliveryStartDate(date);
		}
	}

	public Boolean getEnableMinTurnOverTextField() {
		return enableMinTurnOverTextField;
	}

	public void setEnableMinTurnOverTextField(AjaxBehaviorEvent abe) {
		boolean b = getRequest().getMinimumTurnOverRequired();
		this.enableMinTurnOverTextField = b;
		RequestContext.getCurrentInstance().update("requestForm:minimumTurnOverRequiredText");
	}

	public Boolean getEnableReferencesRequired() {
		return enableReferencesRequired;
	}

	public void setEnableReferencesRequired(AjaxBehaviorEvent abe) {
		boolean b = getRequest().getReferencesRequired();
		this.enableReferencesRequired = b;
		RequestContext.getCurrentInstance().update("requestForm:referencesRequiredText");
	}

	public Boolean getEnableTradeAssociationRequired() {
		return enableTradeAssociationRequired;
	}

	public void setEnableTradeAssociationRequired(AjaxBehaviorEvent abe) {
		boolean b = getRequest().getTradeAssociationRequired();
		this.enableTradeAssociationRequired = b;
		RequestContext.getCurrentInstance().update("requestForm:tradeAssociationRequiredText");
	}

	public Boolean getEnableCertificateRequired() {
		return enableCertificateRequired;
	}

	public void setEnableCertificateRequired(AjaxBehaviorEvent abe) {
		boolean b = getRequest().getCertificateRequired();
		this.enableCertificateRequired = b;
		RequestContext.getCurrentInstance().update("requestForm:certificateRequiredText");
	}

	public Boolean getEnableInsurancesRequired() {
		return enableInsurancesRequired;
	}

	public void setEnableInsurancesRequired(AjaxBehaviorEvent abe) {
		boolean b = getRequest().getInsurancesRequired();
		this.enableInsurancesRequired = b;
		RequestContext.getCurrentInstance().update("requestForm:insurancesRequiredText");
	}

	public TabManager getTabManager() {
		return tabManager;
	}

	public void setTabManager(TabManager tabManager) {
		this.tabManager = tabManager;
	}
	
	public String navigateToHomePage() {
		tabManager.setDisplayTab("Requests");
		return "bootstrapTabs.xhtml?faces-redirect=true";
	}
	
	public void deleteRequestDescription(RequestDescription desc) {
		getRequest().getRequestDescriptions().remove(desc);
		RequestContext.getCurrentInstance().update("requestForm:reqDescsTable");
	}
	
	public void addRequestDesc() {
		getRequest().getRequestDescriptions().add(new RequestDescription());
		RequestContext.getCurrentInstance().update("requestForm:reqDescsTable");
	}
	
	public String seeResponsesToRequest(Request request) {
		tabManager.setDisplayTab("Requests");
		setRequest(request);
		responsesToRequest = responseService.getResponsesForRequentNameAndBuyerCompany(request.getRequestName(), request.getCompany());
		return "responsesToRequest.xhtml?faces-redirect=true";
	}
	
	public void rejectResponse(Response res) {
		responseService.rejectResponse(res);
		res.setResponseStatus(ResponseStatus.DECLINE.getStatus());
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success - Status Changed to", "Decline");
		RequestContext.getCurrentInstance().showMessageInDialog(msg);
		RequestContext.getCurrentInstance().update("responseForm:responsesToRequest");
	}
	
	public void negotiateResponse(Response res) {
		responseService.negotiateResponse(res);
		res.setResponseStatus(ResponseStatus.NEGOTIATE.getStatus());
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success - Status Changed to", "Negotiate");
		RequestContext.getCurrentInstance().showMessageInDialog(msg);
		RequestContext.getCurrentInstance().update("responseForm:responsesToRequest");
	}	

	public void acceptResponse(Response res) {
		responseService.acceptResponse(res);
		res.setResponseStatus(ResponseStatus.ACCEPT.getStatus());
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success - Status Changed to", "Accept");
		RequestContext.getCurrentInstance().showMessageInDialog(msg);
		RequestContext.getCurrentInstance().update("responseForm:responsesToRequest");
	}
	
	public String showRebateOffer(Response res) {
		tabManager.setDisplayTab("Requests");
		setResponse(res);
		Rebate rebate = rebateService.getUniqueRebateByNameAndCompany(response.getRebateName(), response.getSupplierCompany());
		res.setRebate(rebate);
		return "showRebateDetails.xhtml?faces-redirect=false";
	}

	public List<Response> getResponsesToRequest() {
		return responsesToRequest;
	}

	public void setResponsesToRequest(List<Response> responsesToRequest) {
		this.responsesToRequest = responsesToRequest;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}
	
	public String seeResponseDetails(Response response) {
		tabManager.setDisplayTab("Requests");
		this.response = response;
		return "showResponseDetails.xhtml?faces-redirect=false";
	}
	
	
	public void showReport() {
		Map<String,Object> options = new HashMap<String, Object>();
		 options.put("draggable", false);
         options.put("modal", true);
         options.put("position", "top"); // <--- not work
         options.put("width", "90%");
         options.put("contentWidth", "90%");
         options.put("height", "500");
         options.put("contentheight", "90%");
         options.put("size", "auto");

        rebatesChart = new RebatesChart(responsesToRequest, "Comparing Rebate Offers by Suppliers");
        Map<String, List<String>> params = new HashMap<String, List<String>>();

        RequestContext.getCurrentInstance().openDialog("barReport",options, params);
      
	}

	public void showRebateAmountReport() {
		Map<String,Object> options = new HashMap<String, Object>();
		 options.put("draggable", false);
        options.put("modal", true);
        options.put("position", "top"); // <--- not work
        options.put("width", "90%");
        options.put("contentWidth", "90%");
        options.put("height", "500");
        options.put("contentheight", "90%");
        options.put("size", "auto");

       rebatesChart = new RebatesChart();
       rebatesChart.setTitle("Compare Rebate Trigger Amounts by Suppliers");
       rebatesChart.setResponsesToRequest(responsesToRequest);
       rebatesChart.initForValues();
       Map<String, List<String>> params = new HashMap<String, List<String>>();

       RequestContext.getCurrentInstance().openDialog("barReport",options, params);
	}
	
	public void showExampleSavingsReport() {
		Map<String,Object> options = new HashMap<String, Object>();
		 options.put("draggable", false);
       options.put("modal", true);
       options.put("position", "top"); // <--- not work
       options.put("width", "90%");
       options.put("contentWidth", "90%");
       options.put("height", "500");
       options.put("contentheight", "90%");
       options.put("size", "auto");

      rebatesChart = new RebatesChart();
      rebatesChart.setTitle("YOUR EXAMPLE SAVINGS");
      rebatesChart.setResponsesToRequest(responsesToRequest);
      rebatesChart.initForExampleSavings();
      Map<String, List<String>> params = new HashMap<String, List<String>>();

      RequestContext.getCurrentInstance().openDialog("barReport",options, params);
	}
	
	
	public void showPriceComparisionReport() {
		Map<String,Object> options = new HashMap<String, Object>();
		 options.put("draggable", false);
       options.put("modal", true);
       options.put("position", "top"); // <--- not work
       options.put("width", "90%");
       options.put("contentWidth", "90%");
       options.put("height", "500");
       options.put("contentheight", "90%");
       options.put("size", "auto");

     
      Map<String, List<String>> params = new HashMap<String, List<String>>();
      pricingModel = new TableModel();
      pricingModel.build(responsesToRequest);
      RequestContext.getCurrentInstance().openDialog("tableModel",options, params);
	}
	
	public RebatesChart getRebatesChart() {
		return rebatesChart;
	}

	public void setRebatesChart(RebatesChart rebatesChart) {
		this.rebatesChart = rebatesChart;
	}

	public TableModel getPricingModel() {
		return pricingModel;
	}

	public void setPricingModel(TableModel pricingModel) {
		this.pricingModel = pricingModel;
	}

	

	
	
	
}
