package com.twenty20.jsf.managedBeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.context.RequestContext;

import com.twenty20.domain.RebateClaim;
import com.twenty20.domain.Response;
import com.twenty20.domain.User;
import com.twenty20.services.RebateClaimService;
import com.twenty20.services.ResponseService;

@ManagedBean(name = "rebateClaimManager", eager = true)
@SessionScoped
public class RebateClaimManager {
	 @ManagedProperty(value="#{userManager}") 
	 UserManager userManager;
	 
	 @ManagedProperty(value="#{tab}") 
	 TabManager tabManager;
	 
	 User user = null;
	 
	 RebateClaimService rebateClaimService;
	 
	 ResponseService responseService;
	 
	 List<RebateClaim> claims = new ArrayList<>();
	 
	 List<Response> acceptedResponses = new ArrayList<>();
	 
	 RebateClaim rebateClaim;
	 
	 String title = "Claim your Rebate";
	 
	 Boolean saveDisabled = true;
	 
	 Boolean cannotedit = false;
	
	@PostConstruct
	public void init() {
		user = userManager.getUsr();
		rebateClaimService = SpringUtil.getService(RebateClaimService.class);
		responseService = SpringUtil.getService(ResponseService.class); 
		
		//rebates = rebateService.getActiveRebatesBySupplierAndCompany(getUser().getUserName(), getUser().getCompany().getCompanyName());
	}
	
	public void reload() {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		UserManager userManager = application.evaluateExpressionGet(context, "#{userManager}", UserManager.class);
		user = userManager.getUsr();
		claims = rebateClaimService.getRebateClaims(user.getCompany().getCompanyName());
		acceptedResponses = responseService.getAcceptedResponses(user.getCompany().getCompanyName());
	}
	
	public void setResponseValues(Response res) {
		//System.out.println(abe.getSource());
		String requestName = res.getRequestName();
		String supplierCompany = res.getSupplierCompany();
		String supplierEmail = res.getSupplierEmail();
	}
	
	public void change(AjaxBehaviorEvent abe){
		  //do what you want with your favCoffee3 variable here
		String responseId = getRebateClaim().getResponseId();
		try {
			Long id = Long.parseLong(responseId);
			Response response = (Response) responseService.find(id);
			getRebateClaim().setRequestName(response.getRequestName());
			getRebateClaim().setSupplierCompany(response.getSupplierCompany());
			getRebateClaim().setBuyerCompany(response.getBuyerCompany());
			getRebateClaim().setBuyer(response.getBuyer());
			getRebateClaim().setSupplier(response.getSupplier());
			getRebateClaim().setResponse(response);
			setSaveDisabled(false);
			RequestContext.getCurrentInstance().update("rebateClaimForm:supplierCompany-id");
			RequestContext.getCurrentInstance().update("rebateClaimForm:saveRebateClaim");
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			getRebateClaim().setRequestName("Select Response");
			getRebateClaim().setSupplierCompany("Select Company");
			getRebateClaim().setBuyerCompany("Select Company");
			setSaveDisabled(true);
		}
		
		
		//RequestContext.getCurrentInstance().update("projectForm:projectSubTypes");
	}
	
	public String saveOrUpdate() {
		rebateClaimService.saveOrUpdate(getRebateClaim());
		tabManager.setDisplayTab("Claims");
		return "bootstrapTabs.xhtml?faces-redirect=true";
	}
	
	public String createNewClaim() {
		setTitle("Create a New Claim");
		rebateClaim = new RebateClaim();
		rebateClaim.setBuyerCompany(user.getCompany().getCompanyName());
		tabManager.setDisplayTab("Claims");
		setCannotedit(false);
		return "claim.xhtml?faces-redirect=true";
	}
	
	public String edit(RebateClaim claim) {
		this.rebateClaim = claim;
		tabManager.setDisplayTab("Claims");
		Response response = responseService.getResponse(claim.getRequestName(), claim.getBuyer(), claim.getBuyerCompany(), claim.getSupplier(), claim.getSupplierCompany());
		claim.setResponse(response);
		claim.setResponseId(""+response.getId());
		if(this.rebateClaim.getAccept()) {
			setTitle("This claim has been accepted and as such can not be modified");
			setCannotedit(true);
		}
		else {
			setTitle("Edit Rebate Claim for Request - "+rebateClaim.getRequestName());
			setSaveDisabled(false);
		}
		
		return "claim.xhtml?faces-redirect=true";
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public TabManager getTabManager() {
		return tabManager;
	}

	public void setTabManager(TabManager tabManager) {
		this.tabManager = tabManager;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<RebateClaim> getClaims() {
		return claims;
	}

	public void setClaims(List<RebateClaim> claims) {
		this.claims = claims;
	}

	public RebateClaim getRebateClaim() {
		return rebateClaim;
	}

	public void setRebateClaim(RebateClaim rebateClaim) {
		this.rebateClaim = rebateClaim;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Response> getAcceptedResponses() {
		return acceptedResponses;
	}

	public void setAcceptedResponses(List<Response> acceptedResponses) {
		this.acceptedResponses = acceptedResponses;
	}

	public Boolean getSaveDisabled() {
		return saveDisabled;
	}

	public void setSaveDisabled(Boolean saveDisabled) {
		this.saveDisabled = saveDisabled;
	}

	public Boolean getCannotedit() {
		return cannotedit;
	}

	public void setCannotedit(Boolean cannotedit) {
		this.cannotedit = cannotedit;
	}
	
	public void close(RebateClaim claim) {
		tabManager.setDisplayTab("Claims");
		rebateClaimService.deleteById(claim.getId());
		//this.projects = projectService.findAll();
		reload();
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Status - Claim Close Status", "Success");
		RequestContext.getCurrentInstance().showMessageInDialog(msg);
	}
	
	
}
