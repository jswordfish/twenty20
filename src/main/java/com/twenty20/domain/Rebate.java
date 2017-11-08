package com.twenty20.domain;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
@Entity
@NamedQueries({
	@NamedQuery(name="Rebate.getRebateUniqueId", 
			query="SELECT r FROM Rebate r WHERE r.rebateUniqueId=:rebateUniqueId"),
	
	@NamedQuery(name="Rebate.getRebates", 
	query="SELECT r FROM Rebate r WHERE r.rebateActive=:rebateActive"),
	
	@NamedQuery(name="Rebate.getActiveRebatesBySupplierNoAndCompanyNo", 
	query="SELECT r FROM Rebate r WHERE r.rebateActive=true AND r.suppilierNo=:suppilierNo AND r.company=:company"),
	
	@NamedQuery(name="Rebate.getActiveRebatesByCompanyNo", 
	query="SELECT r FROM Rebate r WHERE r.rebateActive=true AND r.company=:company")
})

public class Rebate extends Base{
	
	//@NotNull
	@Column(unique=true)
	String rebateUniqueId;
	
	@NotNull
	String suppilierNo;
	
	@NotNull
	String company;
	
	String rebateReference;
	
	@Temporal(TemporalType.DATE)
	Date rebateValidFrom;
	
	@Temporal(TemporalType.DATE)
	Date rebateValidTo;
	
	Double baseOfferSalesValue;
	
	Float baseOfferRebateOfferInPercent;
	
	Double tier1OfferSalesValue;
	
	Float tier1OfferRebateOfferInPercent;
	
	Double tier2OfferSalesValue;
	
	Float tier2OfferRebateOfferInPercent;
	
	Double tier3OfferSalesValue;
	
	Float tier3OfferRebateOfferInPercent;
	
	Boolean rebateActive = true;
	
	public Rebate(){
		this.rebateValidFrom = new Date();
		this.rebateValidTo = new Date();
		Calendar myCal = Calendar.getInstance();
	    myCal.setTime(this.rebateValidFrom);    
	    myCal.add(Calendar.MONTH, +1);    
	    this.rebateValidTo = myCal.getTime();
	}
	
	public String getSuppilierNo() {
		return suppilierNo;
	}

	public void setSuppilierNo(String suppilierNo) {
		this.suppilierNo = suppilierNo;
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

	public String getRebateUniqueId() {
		return rebateUniqueId;
	}

	public void setRebateUniqueId(String rebateUniqueId) {
		this.rebateUniqueId = rebateUniqueId;
	}
	
	
	
	
	
}
