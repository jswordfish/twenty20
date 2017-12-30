package com.twenty20.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import com.twenty20.lucene.bridges.RequestFieldBridge;
@Entity
@NamedQueries({
	@NamedQuery(name="Request.getUniqueRequest", 
			query="SELECT p FROM Request p WHERE p.requestName=:requestName AND p.buyer=:buyer AND p.company=:company"),
	@NamedQuery(name="Request.getAllOpenRequests", 
	query="SELECT p FROM Request p WHERE p.closed=false ")
})
@Indexed
public class Request extends Base{
	
	@org.hibernate.search.annotations.Field(analyze = Analyze.YES, store=Store.YES)
	@NotNull
	String buyer;
	
	@org.hibernate.search.annotations.Field(analyze = Analyze.YES, store=Store.YES)
	@NotNull
	String company;
	
	@org.hibernate.search.annotations.Field(analyze = Analyze.YES, store=Store.YES)
	@NotNull
	String requestName;
	
	@org.hibernate.search.annotations.Field(analyze = Analyze.YES, store=Store.YES)
	String requestType;
	
	@org.hibernate.search.annotations.Field(analyze = Analyze.YES, store=Store.YES)
	@NotNull
	String project = "";
	
	String projectLocation = "";
	
	@ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(joinColumns=@JoinColumn(name = "RequestDescription_ID"))
	@FieldBridge(impl = RequestFieldBridge.class )
	@org.hibernate.search.annotations.Field(analyze = Analyze.YES, store=Store.YES)
    private List<RequestDescription> requestDescriptions = new ArrayList<>();
	
	
	@Transient
	byte[] requestSpecification;
	
	String requestSpecificationUrl;
	
	@Transient
	String requestSpecificationFileExtension;
	
	
	@Temporal(value = TemporalType.DATE)
	Date requestOpenDate = new Date();
	
	@Temporal(value = TemporalType.DATE)
	Date requestCloseDate;
	
	Double approxValueFixed = 0d;
	
	String approxValueInRange;
	
	Boolean minimumTurnOverRequired = false;
	
	Boolean referencesRequired = false;
	
	Boolean tradeAssociationRequired = false;
	
	Boolean certificateRequired = false;
	
	Boolean insurancesRequired = false;
	
	@Column(columnDefinition="Decimal(20,4) default '0.00'")
	Double minimumTurnOverRequiredText;
	
	String referencesRequiredText;
	
	String tradeAssociationRequiredText;
	
	String certificateRequiredText;
	
	String insurancesRequiredText;
	
	@Temporal(value = TemporalType.DATE)
	Date deliveryStartDate ;
	
	
	Boolean termsAndConditionsAccepted = true;
	
	Boolean closed = false;


	public String getBuyer() {
		return buyer;
	}


	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}


	public String getCompany() {
		return company;
	}


	public void setCompany(String company) {
		this.company = company;
	}


	public String getRequestName() {
		return requestName;
	}


	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}


	public String getProject() {
		return project;
	}


	public void setProject(String project) {
		this.project = project;
	}


	public List<RequestDescription> getRequestDescriptions() {
		return requestDescriptions;
	}


	public void setRequestDescriptions(List<RequestDescription> requestDescriptions) {
		this.requestDescriptions = requestDescriptions;
	}


	public byte[] getRequestSpecification() {
		return requestSpecification;
	}


	public void setRequestSpecification(byte[] requestSpecification) {
		this.requestSpecification = requestSpecification;
	}


	public String getRequestSpecificationUrl() {
		return requestSpecificationUrl;
	}


	public void setRequestSpecificationUrl(String requestSpecificationUrl) {
		this.requestSpecificationUrl = requestSpecificationUrl;
	}


	public String getRequestSpecificationFileExtension() {
		return requestSpecificationFileExtension;
	}


	public void setRequestSpecificationFileExtension(String requestSpecificationFileExtension) {
		this.requestSpecificationFileExtension = requestSpecificationFileExtension;
	}


	public Date getRequestOpenDate() {
		return requestOpenDate;
	}


	public void setRequestOpenDate(Date requestOpenDate) {
		this.requestOpenDate = requestOpenDate;
	}


	public Date getRequestCloseDate() {
		if(this.requestCloseDate == null) {
			
			Calendar myCal = Calendar.getInstance();
			    myCal.setTime(this.requestOpenDate);    
			myCal.add(Calendar.MONTH, +1);    
			this.requestCloseDate = myCal.getTime();
		}
		return requestCloseDate;
	}


	public void setRequestCloseDate(Date requestCloseDate) {
		this.requestCloseDate = requestCloseDate;
	}


	


	public Double getApproxValueFixed() {
		return approxValueFixed;
	}


	public void setApproxValueFixed(Double approxValueFixed) {
		this.approxValueFixed = approxValueFixed;
	}


	public String getApproxValueInRange() {
		return approxValueInRange;
	}


	public void setApproxValueInRange(String approxValueInRange) {
		this.approxValueInRange = approxValueInRange;
	}


	public Boolean getMinimumTurnOverRequired() {
		return minimumTurnOverRequired;
	}


	public void setMinimumTurnOverRequired(Boolean minimumTurnOverRequired) {
		this.minimumTurnOverRequired = minimumTurnOverRequired;
	}


	public Boolean getReferencesRequired() {
		return referencesRequired;
	}


	public void setReferencesRequired(Boolean referencesRequired) {
		this.referencesRequired = referencesRequired;
	}


	public Boolean getTradeAssociationRequired() {
		return tradeAssociationRequired;
	}


	public void setTradeAssociationRequired(Boolean tradeAssociationRequired) {
		this.tradeAssociationRequired = tradeAssociationRequired;
	}


	public Boolean getCertificateRequired() {
		return certificateRequired;
	}


	public void setCertificateRequired(Boolean certificateRequired) {
		this.certificateRequired = certificateRequired;
	}


	public Boolean getInsurancesRequired() {
		return insurancesRequired;
	}


	public void setInsurancesRequired(Boolean insurancesRequired) {
		this.insurancesRequired = insurancesRequired;
	}


	


	public Double getMinimumTurnOverRequiredText() {
		return minimumTurnOverRequiredText;
	}


	public void setMinimumTurnOverRequiredText(Double minimumTurnOverRequiredText) {
		this.minimumTurnOverRequiredText = minimumTurnOverRequiredText;
	}


	public String getReferencesRequiredText() {
		return referencesRequiredText;
	}


	public void setReferencesRequiredText(String referencesRequiredText) {
		this.referencesRequiredText = referencesRequiredText;
		if(referencesRequiredText != null && referencesRequiredText.trim().length() > 0) {
			setReferencesRequired(true);
		}
	}


	public String getTradeAssociationRequiredText() {
		return tradeAssociationRequiredText;
	}


	public void setTradeAssociationRequiredText(String tradeAssociationRequiredText) {
		this.tradeAssociationRequiredText = tradeAssociationRequiredText;
		if(tradeAssociationRequiredText != null && tradeAssociationRequiredText.trim().length() > 0) {
			setTradeAssociationRequired(true);
		}
	}


	public String getCertificateRequiredText() {
		return certificateRequiredText;
	}


	public void setCertificateRequiredText(String certificateRequiredText) {
		this.certificateRequiredText = certificateRequiredText;
		if(certificateRequiredText != null && certificateRequiredText.trim().length() > 0) {
			setCertificateRequired(true);
		}
	}


	public String getInsurancesRequiredText() {
		return insurancesRequiredText;
	}


	public void setInsurancesRequiredText(String insurancesRequiredText) {
		this.insurancesRequiredText = insurancesRequiredText;
		if(insurancesRequiredText != null && insurancesRequiredText.trim().length() > 0) {
			setInsurancesRequired(true);
		}
	}


	public Date getDeliveryStartDate() {
		if(this.deliveryStartDate == null) {
			this.deliveryStartDate = getRequestCloseDate();
		}
		return deliveryStartDate;
	}


	public void setDeliveryStartDate(Date deliveryStartDate) {
		this.deliveryStartDate = deliveryStartDate;
	}


	public Boolean getTermsAndConditionsAccepted() {
		return termsAndConditionsAccepted;
	}


	public void setTermsAndConditionsAccepted(Boolean termsAndConditionsAccepted) {
		this.termsAndConditionsAccepted = termsAndConditionsAccepted;
	}


	public String getRequestType() {
		return requestType;
	}


	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	
	@Override
	public int hashCode() {
		return (getRequestName()+"-"+getBuyer()+"-"+getCompany()).hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Request)) {
			return false;
		}
		if(this.hashCode() == obj.hashCode()) {
			return true;
		}
		return false;
	}


	public String getProjectLocation() {
		return projectLocation;
	}


	public void setProjectLocation(String projectLocation) {
		this.projectLocation = projectLocation;
	}


	public Boolean getClosed() {
		return closed;
	}


	public void setClosed(Boolean closed) {
		this.closed = closed;
	}
	
	

}
