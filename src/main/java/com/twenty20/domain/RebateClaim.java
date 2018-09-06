package com.twenty20.domain;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Entity
@NamedQueries({
	@NamedQuery(name="RebateClaim.getClaimByBuyerCompany", 
			query="SELECT r FROM RebateClaim r WHERE r.buyerCompany=:buyerCompany"),
	
	@NamedQuery(name="RebateClaim.getClaimBySupplierCompany", 
	query="SELECT r FROM RebateClaim r WHERE r.supplierCompany=:supplierCompany"),

	
	@NamedQuery(name="RebateClaim.getClaimByBuyerSupplierAndRequest", 
	query="SELECT r FROM RebateClaim r WHERE r.supplierCompany=:supplierCompany AND r.buyerCompany=:buyerCompany  AND r.requestName=:requestName")
	
})

@Indexed
public class RebateClaim extends Base{
	@Transient
	String responseId;
	
	@org.hibernate.search.annotations.Field(analyze = Analyze.YES, store=Store.YES)
	@NotNull
	String requestName;
	
	
	@org.hibernate.search.annotations.Field(analyze = Analyze.YES, store=Store.YES)
	@NotNull
	String buyerCompany;
	
	
	
	@org.hibernate.search.annotations.Field(analyze = Analyze.YES, store=Store.YES)
	@NotNull
	String supplierCompany;
	
	@Transient
	Response response;
	
	String supplier;
	
	String buyer;
	
	Date invoiceDate;
	
	@Transient
	String invDate;
	
	String invoiceNo;
	
	Double invoiceAmount;
	
	Boolean accept = false;
	
	String note;

	public String getRequestName() {
		return requestName;
	}

	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}

	
	public String getBuyerCompany() {
		return buyerCompany;
	}

	public void setBuyerCompany(String buyerCompany) {
		this.buyerCompany = buyerCompany;
	}

	

	public String getSupplierCompany() {
		return supplierCompany;
	}

	public void setSupplierCompany(String supplierCompany) {
		this.supplierCompany = supplierCompany;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Double getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(Double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public Boolean getAccept() {
		return accept;
	}

	public void setAccept(Boolean accept) {
		this.accept = accept;
	}

	public String getResponseId() {
		return responseId;
	}

	public void setResponseId(String responseId) {
		this.responseId = responseId;
	}

	public String getInvDate() {
			if(getInvoiceDate() != null) {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				return df.format(getInvoiceDate());
			}
		return invDate;
	}

	public void setInvDate(String invDate) throws ParseException {
		this.invDate = invDate;
		if(this.invDate != null && this.invDate.trim().length() != 0) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			setInvoiceDate(df.parse(invDate));
		}
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	
	

}
