package com.twenty20.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
@Entity
@NamedQueries({
	@NamedQuery(name="Rebate.getUniqueRebateByNameAndCompany", 
			query="SELECT r FROM Rebate r WHERE r.rebateName=:rebateName AND r.company=:company AND r.rebateActive=true"),
	
	@NamedQuery(name="Rebate.getRebates", 
	query="SELECT r FROM Rebate r WHERE r.rebateActive=:rebateActive"),
	
	@NamedQuery(name="Rebate.getActiveRebatesBySupplierNoAndCompanyNo", 
	query="SELECT r FROM Rebate r WHERE r.rebateActive=true AND r.supplier=:supplier AND r.company=:company"),
	
	@NamedQuery(name="Rebate.getActiveRebatesByCompanyNo", 
	query="SELECT r FROM Rebate r WHERE r.rebateActive=true AND r.company=:company")
})

public class Rebate extends Base{
	
	@NotNull
	@Column(unique=true)
	String rebateName;
	
	@NotNull
	String supplier;
	
	@NotNull
	String company;
	
	String rebateReference;
	
	@Temporal(TemporalType.DATE)
	Date rebateValidFrom = new Date();
	
	@Temporal(TemporalType.DATE)
	Date rebateValidTo;
	
	@Transient
	String fromDt;
	
	@Transient
	String toDt;
	
	Double baseOfferSalesValue = 0d;
	
	Float baseOfferRebateOfferInPercent = 3f;
	
	Double tier1OfferSalesValue;
	
	Float tier1OfferRebateOfferInPercent = 5f;
	
	Double tier2OfferSalesValue;
	
	Float tier2OfferRebateOfferInPercent = 7f;
	
	Double tier3OfferSalesValue;
	
	Float tier3OfferRebateOfferInPercent = 10f;
	
	Boolean rebateActive = true;
	
	public Rebate(){
		this.rebateValidFrom = new Date();
		this.rebateValidTo = new Date();
		Calendar myCal = Calendar.getInstance();
	    myCal.setTime(this.rebateValidFrom);    
	    myCal.add(Calendar.MONTH, +1);    
	    this.rebateValidTo = myCal.getTime();
	}
	


	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getRebateReference() {
		return rebateReference;
	}

	public void setRebateReference(String rebateReference) {
		this.rebateReference = rebateReference;
	}

	public Date getRebateValidFrom() {
		return rebateValidFrom;
	}

	public void setRebateValidFrom(Date rebateValidFrom) {
		this.rebateValidFrom = rebateValidFrom;
	}

	public Date getRebateValidTo() {
		if(rebateValidTo == null) {
			Calendar myCal = Calendar.getInstance();
			myCal.setTime(this.rebateValidFrom);    
			myCal.add(Calendar.MONTH, +1);   
			rebateValidTo = myCal.getTime();
		}
		return rebateValidTo;
	}

	public void setRebateValidTo(Date rebateValidTo) {
		this.rebateValidTo = rebateValidTo;
	}

	public Double getBaseOfferSalesValue() {
		return baseOfferSalesValue;
	}

	public void setBaseOfferSalesValue(Double baseOfferSalesValue) {
		this.baseOfferSalesValue = baseOfferSalesValue;
	}

	public Float getBaseOfferRebateOfferInPercent() {
		return baseOfferRebateOfferInPercent;
	}

	public void setBaseOfferRebateOfferInPercent(Float baseOfferRebateOfferInPercent) {
		this.baseOfferRebateOfferInPercent = baseOfferRebateOfferInPercent;
	}

	public Double getTier1OfferSalesValue() {
		return tier1OfferSalesValue;
	}

	public void setTier1OfferSalesValue(Double tier1OfferSalesValue) {
		this.tier1OfferSalesValue = tier1OfferSalesValue;
	}

	public Float getTier1OfferRebateOfferInPercent() {
		return tier1OfferRebateOfferInPercent;
	}

	public void setTier1OfferRebateOfferInPercent(Float tier1OfferRebateOfferInPercent) {
		this.tier1OfferRebateOfferInPercent = tier1OfferRebateOfferInPercent;
	}

	public Double getTier2OfferSalesValue() {
		return tier2OfferSalesValue;
	}

	public void setTier2OfferSalesValue(Double tier2OfferSalesValue) {
		this.tier2OfferSalesValue = tier2OfferSalesValue;
	}

	public Float getTier2OfferRebateOfferInPercent() {
		return tier2OfferRebateOfferInPercent;
	}

	public void setTier2OfferRebateOfferInPercent(Float tier2OfferRebateOfferInPercent) {
		this.tier2OfferRebateOfferInPercent = tier2OfferRebateOfferInPercent;
	}

	public Double getTier3OfferSalesValue() {
		return tier3OfferSalesValue;
	}

	public void setTier3OfferSalesValue(Double tier3OfferSalesValue) {
		this.tier3OfferSalesValue = tier3OfferSalesValue;
	}

	public Float getTier3OfferRebateOfferInPercent() {
		return tier3OfferRebateOfferInPercent;
	}

	public void setTier3OfferRebateOfferInPercent(Float tier3OfferRebateOfferInPercent) {
		this.tier3OfferRebateOfferInPercent = tier3OfferRebateOfferInPercent;
	}

	public Boolean getRebateActive() {
		return rebateActive;
	}

	public void setRebateActive(Boolean rebateActive) {
		this.rebateActive = rebateActive;
	}



	public String getRebateName() {
		return rebateName;
	}



	public void setRebateName(String rebateName) {
		this.rebateName = rebateName;
	}



	public String getSupplier() {
		return supplier;
	}



	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}



	public String getFromDt() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if(getRebateValidFrom() != null){
			String dt = df.format(getRebateValidFrom());
			this.fromDt = dt;
			return dt;
		}
		return "";
	}



	public void setFromDt(String fromDt) {
		this.fromDt = fromDt;
	}



	public String getToDt() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if(getRebateValidTo() != null){
			String dt = df.format(getRebateValidTo());
			this.toDt = dt;
			return dt;
		}
		return "";
	}



	public void setToDt(String toDt) {
		this.toDt = toDt;
	}
	
	
	
}
