package com.twenty20.jsf.managedBeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import com.twenty20.domain.RebateClaim;
import com.twenty20.domain.Response;
import com.twenty20.domain.User;
import com.twenty20.services.RebateClaimService;
import com.twenty20.services.ResponseService;

//supplierClaimManager
@ManagedBean(name = "supplierClaimManager", eager = true)
@SessionScoped
public class SupplierClaimManager {
	 @ManagedProperty(value="#{userManager}") 
	 UserManager userManager;
	 
	 @ManagedProperty(value="#{tab}") 
	 TabManager tabManager;
	 
	 User user = null;
	 
	 RebateClaimService rebateClaimService;
	 
	 ResponseService responseService;
	 
	 List<RebateClaim> claims = new ArrayList<>();
	 
	 Boolean cannotedit = false;
	 
	 RebateClaim rebateClaim;
	 
	 String title = "Accept the Claim";
	 
	 Boolean saveDisabled = true;
	
	@PostConstruct
	public void init() {
		user = userManager.getUsr();
		rebateClaimService = SpringUtil.getService(RebateClaimService.class);
		responseService = SpringUtil.getService(ResponseService.class); 
		
		//rebates = rebateService.getActiveRebatesBySupplierAndCompany(getUser().getUserName(), getUser().getCompany().getCompanyName());
	}
	
	public void reload() {
		/**
		 * Claims for supplier
		 */
		claims = rebateClaimService.getRebateClaimsForSupplier(user.getCompany().getCompanyName());
		
	}
	
	public String edit(RebateClaim claim) {
		this.rebateClaim = claim;
		tabManager.setDisplayTab("SupplierClaims");
		Response response = responseService.getResponse(claim.getRequestName(), claim.getBuyer(), claim.getBuyerCompany(), claim.getSupplier(), claim.getSupplierCompany());
		rebateClaim.setResponse(response);
		rebateClaim.setResponseId(""+response.getId());
		
		setTitle("Accept/Reject Rebate Claim for Request - "+rebateClaim.getRequestName());
		//setSaveDisabled(false);
		return "claimSupplier.xhtml?faces-redirect=true";
	}
	
	public String accept() {
		rebateClaimService.accept(getRebateClaim());
		tabManager.setDisplayTab("SupplierClaims");
		return "supplierTabs.xhtml?faces-redirect=true";
	}
	
	public String reject() {
		rebateClaimService.reject(getRebateClaim());
		tabManager.setDisplayTab("SupplierClaims");
		return "supplierTabs.xhtml?faces-redirect=true";
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

	public Boolean getCannotedit() {
		return cannotedit;
	}

	public void setCannotedit(Boolean cannotedit) {
		this.cannotedit = cannotedit;
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

	public Boolean getSaveDisabled() {
		return saveDisabled;
	}

	public void setSaveDisabled(Boolean saveDisabled) {
		this.saveDisabled = saveDisabled;
	}
	
	
	
}
