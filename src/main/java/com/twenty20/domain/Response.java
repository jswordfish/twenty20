package com.twenty20.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
@Entity
@NamedQueries({
	@NamedQuery(name="Response.getUniqueResponse", 
			query="SELECT p FROM Response p WHERE p.requestName=:requestName AND p.buyer=:buyer AND p.buyerCompany=:buyerCompany AND p.supplier=:supplier AND p.supplierCompany=:supplierCompany"),
	@NamedQuery(name="Response.getResponseByCompany", 
	query="SELECT p FROM Response p WHERE p.supplierCompany=:supplierCompany"),
	@NamedQuery(name="Response.getResponseBySupplierNameAndCompany", 
	query="SELECT p FROM Response p WHERE p.supplier=:supplier AND p.supplierCompany=:supplierCompany")
})
@Indexed
public class Response extends Base{
	
	@org.hibernate.search.annotations.Field(analyze = Analyze.YES, store=Store.YES)
	@NotNull
	String requestName;
	
	@org.hibernate.search.annotations.Field(analyze = Analyze.YES, store=Store.YES)
	@NotNull
	String buyer;
	
	@org.hibernate.search.annotations.Field(analyze = Analyze.YES, store=Store.YES)
	@NotNull
	String buyerCompany;
	
	@org.hibernate.search.annotations.Field(analyze = Analyze.YES, store=Store.YES)
	@NotNull
	String supplier;
	
	@org.hibernate.search.annotations.Field(analyze = Analyze.YES, store=Store.YES)
	@NotNull
	String supplierCompany;
	
	@Transient
	Request request;
	
	String supplierEmail;
	
	Date responseCreationDate  = new Date();
	
	Date responseValidFromDate = new Date();
	
	
	
	Date responseValidToDate;
	
	String responseAuthority;
	
	@ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(joinColumns=@JoinColumn(name = "ResponseToRequestDescription_ID"))
	List<ResponseToRequestDescription> responseToRequestDescriptions = new ArrayList<ResponseToRequestDescription>();
    
	String rebateName;
	
	Boolean requestSpecificRebateOffer = false;
	
	@Transient
	byte[] additionalDocument;
	
	String additionalDocumentUrl;
	
	String additionalDocumentExtension;
	
	String responseStatus = ResponseStatus.HOLD.getStatus();

	public String getRequestName() {
		return requestName;
	}

	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getBuyerCompany() {
		return buyerCompany;
	}

	public void setBuyerCompany(String buyerCompany) {
		this.buyerCompany = buyerCompany;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getSupplierCompany() {
		return supplierCompany;
	}

	public void setSupplierCompany(String supplierCompany) {
		this.supplierCompany = supplierCompany;
	}

	public String getSupplierEmail() {
		return supplierEmail;
	}

	public void setSupplierEmail(String supplierEmail) {
		this.supplierEmail = supplierEmail;
	}

	public Date getResponseCreationDate() {
		return responseCreationDate;
	}

	public void setResponseCreationDate(Date responseCreationDate) {
		this.responseCreationDate = responseCreationDate;
	}

	public Date getResponseValidFromDate() {
		return responseValidFromDate;
	}

	public void setResponseValidFromDate(Date responseValidFromDate) {
		this.responseValidFromDate = responseValidFromDate;
	}

	public Date getResponseValidToDate() {
		if(responseValidToDate == null) {
			Calendar myCal = Calendar.getInstance();
		    myCal.setTime(this.responseCreationDate);    
			myCal.add(Calendar.MONTH, +1);    
			this.responseValidToDate = myCal.getTime();
		}
		return this.responseValidToDate;
	}

	public void setResponseValidToDate(Date responseValidToDate) {
		this.responseValidToDate = responseValidToDate;
	}

	public String getResponseAuthority() {
		return responseAuthority;
	}

	public void setResponseAuthority(String responseAuthority) {
		this.responseAuthority = responseAuthority;
	}

	public List<ResponseToRequestDescription> getResponseToRequestDescriptions() {
		return responseToRequestDescriptions;
	}

	public void setResponseToRequestDescriptions(List<ResponseToRequestDescription> responseToRequestDescriptions) {
		this.responseToRequestDescriptions = responseToRequestDescriptions;
	}

	public String getRebateName() {
		return rebateName;
	}

	public void setRebateName(String rebateName) {
		this.rebateName = rebateName;
	}

	public Boolean getRequestSpecificRebateOffer() {
		return requestSpecificRebateOffer;
	}

	public void setRequestSpecificRebateOffer(Boolean requestSpecificRebateOffer) {
		this.requestSpecificRebateOffer = requestSpecificRebateOffer;
	}

	public byte[] getAdditionalDocument() {
		return additionalDocument;
	}

	public void setAdditionalDocument(byte[] additionalDocument) {
		this.additionalDocument = additionalDocument;
	}

	public String getAdditionalDocumentUrl() {
		return additionalDocumentUrl;
	}

	public void setAdditionalDocumentUrl(String additionalDocumentUrl) {
		this.additionalDocumentUrl = additionalDocumentUrl;
	}

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	public String getAdditionalDocumentExtension() {
		return additionalDocumentExtension;
	}

	public void setAdditionalDocumentExtension(String additionalDocumentExtension) {
		this.additionalDocumentExtension = additionalDocumentExtension;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}
	
	
}
