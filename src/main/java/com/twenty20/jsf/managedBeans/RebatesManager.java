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

import org.primefaces.context.RequestContext;

import com.twenty20.domain.Rebate;
import com.twenty20.domain.User;
import com.twenty20.services.RebateService;
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
	 
	 
	 String title = "Your Main Rebate Offer";
	 
	 Boolean saveDisabled = true;
	 
	 
	 @ManagedProperty(value="#{tab}") 
	 TabManager tabManager;
	
	@PostConstruct
	public void init() {
		user = userManager.getUsr();
		rebateService = SpringUtil.getService(RebateService.class);
		createNew();
		//rebates = rebateService.getActiveRebatesBySupplierAndCompany(getUser().getUserName(), getUser().getCompany().getCompanyName());
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
		Rebate r = rebateService.getUniqueRebateByNameAndCompany(this.user.getCompany().getCompanyName()+"-MainRebate", this.user.getCompany().getCompanyName());
			if(r == null) {
				this.rebate = new Rebate();
				this.rebate.setRebateName(this.user.getCompany().getCompanyName()+"-MainRebate");
				this.rebate.setSupplier(this.user.getUserName());
				this.rebate.setCompany(this.user.getCompany().getCompanyName());
				setDates();
			}
			else {
				this.rebate = r;
				setDates();
			}
		
		
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
	
	public void validate() {
		try {
			if(getRebate().getBaseOfferRebateOfferInPercent() >=100) {
				RequestContext.getCurrentInstance().execute("bootbox.alert('Invalid Base Offer rebate percentage ');");	
				setSaveDisabled(true);
			}
			else if(getRebate().getTier1OfferRebateOfferInPercent() >= 100) {
				RequestContext.getCurrentInstance().execute("bootbox.alert('Invalid Tier 1 Rebate Percentage');");	
				setSaveDisabled(true);
			}
			else if(getRebate().getTier2OfferRebateOfferInPercent() >= 100) {
				RequestContext.getCurrentInstance().execute("bootbox.alert('Invalid Tier 2 Rebate Percentage');");	
				setSaveDisabled(true);
			}
			else if(getRebate().getTier3OfferRebateOfferInPercent() >= 100) {
				RequestContext.getCurrentInstance().execute("bootbox.alert('Invalid Tier 3 Rebate Percentagee');");	
				setSaveDisabled(true);
			}
			else if(!(getRebate().getTier1OfferSalesValue() > getRebate().getBaseOfferSalesValue())) {
				RequestContext.getCurrentInstance().execute("bootbox.alert('Tier 1 Sales value should be greater than Base Sales value');");	
				setSaveDisabled(true);
			}
			else if(!(getRebate().getTier2OfferSalesValue() > getRebate().getTier1OfferSalesValue())) {
				RequestContext.getCurrentInstance().execute("bootbox.alert('Tier 2 Sales value should be greater than Tier 1 Sales value');");	
				setSaveDisabled(true);
			}
			else if(!(getRebate().getTier3OfferSalesValue() > getRebate().getTier2OfferSalesValue())) {
				RequestContext.getCurrentInstance().execute("bootbox.alert('Tier 3 Sales value should be greater than Tier 2 Sales value');");	
				setSaveDisabled(true);
			}
			else if(!(getRebate().getTier1OfferRebateOfferInPercent() > getRebate().getBaseOfferRebateOfferInPercent())) {
				RequestContext.getCurrentInstance().execute("bootbox.alert('Tier 1 Rebate Percentage should be greater than Base Level Rebate Percent');");	
				setSaveDisabled(true);
			}
			else if(!(getRebate().getTier2OfferRebateOfferInPercent() > getRebate().getTier1OfferRebateOfferInPercent())) {
				RequestContext.getCurrentInstance().execute("bootbox.alert('Tier 2 Rebate Percentage should be greater than Tier 1 Rebate Percent');");	
				setSaveDisabled(true);
			}
			else if(!(getRebate().getTier3OfferRebateOfferInPercent() > getRebate().getTier2OfferRebateOfferInPercent())) {
				RequestContext.getCurrentInstance().execute("bootbox.alert('Tier 3 Rebate Percentage should be greater than Tier 2 Rebate Percent');");	
				setSaveDisabled(true);
			}
			else {
				RequestContext.getCurrentInstance().execute("bootbox.alert('Validation Succesful. Now you can save Rebate');");	
				setSaveDisabled(false);
			}
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			RequestContext.getCurrentInstance().execute("bootbox.alert('Fill all the fields before trying to validate');");	
			setSaveDisabled(true);
		}
		
	}

	public Boolean getSaveDisabled() {
		return saveDisabled;
	}

	public void setSaveDisabled(Boolean saveDisabled) {
		this.saveDisabled = saveDisabled;
	}
	
	
}
