package com.twenty20.jsf.managedBeans;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import com.twenty20.domain.Rebate;
import com.twenty20.domain.User;
import com.twenty20.services.RebateService;
import com.twenty20.services.RequestService;
@ManagedBean(name = "rebateManager", eager = true)
@SessionScoped
public class RebatesManager {
	
	List<Rebate> rebates = new ArrayList<Rebate>();
	
	transient RebateService rebateService;
	
	User user;
	
	Rebate rebate;
	
	 @ManagedProperty(value="#{userManager}") 
	 UserManager userManager;
	 
	 String fromDate;
	 
	 String toDate;
	 
	 
	 String title = "Create New Rebate Offer";
	 
	 
	 @ManagedProperty(value="#{tab}") 
	 TabManager tabManager;
	
	@PostConstruct
	public void init() {
		rebateService = SpringUtil.getService(RebateService.class);
		rebates = rebateService.getActiveRebatesBySupplierAndCompany(getUser().getUserName(), getUser().getCompany().getCompanyName());
	}

	public List<Rebate> getRebates() {
		return rebates;
	}

	public void setRebates(List<Rebate> rebates) {
		this.rebates = rebates;
	}

	public User getUser() {
			if(user == null) {
				user = userManager.getUsr();
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
	
	public String createNew() {
		this.rebate = new Rebate();
		this.rebate.setSupplier(this.user.getUserName());
		this.rebate.setCompany(this.user.getCompany().getCompanyName());
		setDates();
		return "rebate.xhtml?faces-redirect=true";
	}
	
	public String edit(Rebate rebate) {
		this.rebate = rebate;
		setTitle("Edit Rebate - "+rebate.getRebateName());
		setDates();
		return "rebate.xhtml?faces-redirect=true";
	}
	
	public void delete(Rebate rebate) {
		this.rebate = rebate;
		rebateService.deleteById(rebate.getId());
		this.rebates = rebateService.findAll();
	}
	
	public String saveOrUpdate() {
		rebateService.saveOrUpdate(rebate);
		this.rebates = rebateService.findAll();
		tabManager.setDisplayTab("Rebates");
		return "supplierTabs.xhtml?faces-redirect=true";
	}
	
	private void setDates() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if(this.rebate.getRebateValidFrom() != null){
			String dt = df.format(this.rebate.getRebateValidFrom());
			this.fromDate = dt;
		}
		
		if(this.rebate.getRebateValidTo() != null){
			String dt = df.format(this.rebate.getRebateValidTo());
			this.toDate = dt;
		}
	}

	public Rebate getRebate() {
		return rebate;
	}

	public void setRebate(Rebate rebate) {
		this.rebate = rebate;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) throws ParseException {
		this.fromDate = fromDate;
		if(this.fromDate != null || this.fromDate.trim().length() != 0){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = formatter.parse(fromDate);
			getRebate().setRebateValidFrom(date);
		}
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) throws ParseException{
		this.toDate = toDate;
		if(this.toDate != null || this.toDate.trim().length() != 0){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = formatter.parse(toDate);
			getRebate().setRebateValidTo(date);
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public TabManager getTabManager() {
		return tabManager;
	}

	public void setTabManager(TabManager tabManager) {
		this.tabManager = tabManager;
	}
	
	
}
