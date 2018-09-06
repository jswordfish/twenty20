package com.twenty20.jsf.managedBeans;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.twenty20.services.RebateService;

@ManagedBean(name="suplierTabManager", eager = true)
@SessionScoped 
public class SupplierTabManager {
//	
//	String settings = "active";
//	String dashboard = "";
//	
//	String rebate = "";
//	String responseToBuyerRequests = "";
//	String buyerRequestsOnlyForYou = "";
//	String rebateReadyReckoner = "";
//	String meetingInvites = "";
//	String notifications = "";
	
	String tab=  "settings";
	
	public String goToSettings() {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		TabManager tabManager = application.evaluateExpressionGet(context, "#{tab}", TabManager.class);
		tabManager.setDisplayTab("Settings");
		return "supplierTabs.xhtml?faces-redirect=true";
	}
	
	public String goToDashboard() {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		TabManager tabManager = application.evaluateExpressionGet(context, "#{tab}", TabManager.class);
		tabManager.setDisplayTab("Dashboard");
		return "supplierTabs.xhtml?faces-redirect=true";
	}
	
	public String goToDirectResponses() {
//		FacesContext context = FacesContext.getCurrentInstance();
//		Application application = context.getApplication();
//		TabManager tabManager = application.evaluateExpressionGet(context, "#{tab}", TabManager.class);
//		tabManager.setDisplayTab("DirectResponses");
//		return "supplierTabs.xhtml?faces-redirect=true";
		RebateService rebateService = SpringUtil.getService(RebateService.class);
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		TabManager tabManager = application.evaluateExpressionGet(context, "#{tab}", TabManager.class);
		UserManager userManager = application.evaluateExpressionGet(context, "#{userManager}", UserManager.class);
		RebatesManager rebatesManager = application.evaluateExpressionGet(context, "#{rebateManager}", RebatesManager.class);
		//As a Supplier you need to have your main Rebate offer with all tier values created and saved before you can proceed further
		if(rebateService.getUniqueRebateByNameAndCompany(userManager.getUsr().getCompany().getCompanyName()+"-MainRebate", userManager.getUsr().getCompany().getCompanyName()) == null) {
			
			
			rebatesManager.setTitle("As a Supplier you need to have your main Rebate offer with all tier values created and saved before you can proceed further to this tab");
			tabManager.setDisplayTab("Rebates");
			
			//ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			return null;
		}
		
		tabManager.setDisplayTab("DirectResponses");
		return "supplierTabs.xhtml?faces-redirect=true";
	}
	
	public String goToReadyReckoner() {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		TabManager tabManager = application.evaluateExpressionGet(context, "#{tab}", TabManager.class);
		tabManager.setDisplayTab("ReadyReckoner");
		return "supplierTabs.xhtml?faces-redirect=true";
	}
	
	public String goToMeetingInvites() {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		TabManager tabManager = application.evaluateExpressionGet(context, "#{tab}", TabManager.class);
		tabManager.setDisplayTab("MeetingInvites");
		return "supplierTabs.xhtml?faces-redirect=true";
	}
	
	public String goToSupplierClaims() {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		TabManager tabManager = application.evaluateExpressionGet(context, "#{tab}", TabManager.class);
		tabManager.setDisplayTab("SupplierClaims");
		return "supplierTabs.xhtml?faces-redirect=true";
	}
	
//	public String goToNotifications() {
//		FacesContext context = FacesContext.getCurrentInstance();
//		Application application = context.getApplication();
//		TabManager tabManager = application.evaluateExpressionGet(context, "#{tab}", TabManager.class);
//		tabManager.setDisplayTab("Notifications");
//		return "supplierTabs.xhtml?faces-redirect=true";
//	}
	
	
	public String goToRespondBuyerRequests() {
		RebateService rebateService = SpringUtil.getService(RebateService.class);
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		TabManager tabManager = application.evaluateExpressionGet(context, "#{tab}", TabManager.class);
		UserManager userManager = application.evaluateExpressionGet(context, "#{userManager}", UserManager.class);
		RebatesManager rebatesManager = application.evaluateExpressionGet(context, "#{rebateManager}", RebatesManager.class);
		//As a Supplier you need to have your main Rebate offer with all tier values created and saved before you can proceed further
		if(rebateService.getUniqueRebateByNameAndCompany(userManager.getUsr().getCompany().getCompanyName()+"-MainRebate", userManager.getUsr().getCompany().getCompanyName()) == null) {
			
			

			rebatesManager.setTitle("As a Supplier you need to have your main Rebate offer with all tier values created and saved before you can proceed further to this tab");
			tabManager.setDisplayTab("Rebates");
			
			//ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			return null;
		}
		
		tabManager.setDisplayTab("Responses");
		return "supplierTabs.xhtml?faces-redirect=true";
	}
	

	
	public String goToRebates() {
		RebateService rebateService = SpringUtil.getService(RebateService.class);
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		TabManager tabManager = application.evaluateExpressionGet(context, "#{tab}", TabManager.class);
		UserManager userManager = application.evaluateExpressionGet(context, "#{userManager}", UserManager.class);
		RebatesManager rebatesManager = application.evaluateExpressionGet(context, "#{rebateManager}", RebatesManager.class);
		//As a Supplier you need to have your main Rebate offer with all tier values created and saved before you can proceed further
		if(rebateService.getUniqueRebateByNameAndCompany(userManager.getUsr().getCompany().getCompanyName()+"-MainRebate", userManager.getUsr().getCompany().getCompanyName()) == null) {
			
			
		
			rebatesManager.setTitle("Please create your rebate offer before you proceed further");
			tabManager.setDisplayTab("Rebates");
			//ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			return "supplierTabs.xhtml?faces-redirect=true";
		}
		else {
			tabManager.setDisplayTab("Rebates");
			rebatesManager.setTitle("Update your Rebate offer");
			return "supplierTabs.xhtml?faces-redirect=true";
		}
		
		
	}



	public String getTab() {
		return tab;
	}



	public void setTab(String tab) {
		this.tab = tab;
	}

	
	
	

}
