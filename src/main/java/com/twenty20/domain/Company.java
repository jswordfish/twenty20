package com.twenty20.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.twenty20.common.OrganizationType;
@Entity
@NamedQueries({
	@NamedQuery(name="Company.getUniqueCompany", 
			query="SELECT c FROM Company c WHERE c.companyName=:companyName AND c.companyRegistrationNumber=:companyRegistrationNumber")
	
})
public class Company extends com.twenty20.domain.Base{
	@NotNull
	String companyName;
	
	@NotNull
	String companyRegistrationNumber;
	
	String tradingAs;
	
	String yearFounded;
	
	@Enumerated(EnumType.STRING)
	OrganizationType orgType = OrganizationType.CORPORATION;
	
	String typeOConstrunctionWorkUndertaken = "";
	
	Integer numberOfPRojectsUnderConstruction;
	
	Double companySize;
	
	@Transient
	byte[] companyLogo;
	
	String companyLogoUrl;
	
	@Transient
	String companyLogoExtension;
	
	public String getCompanyLogoExtension() {
		return companyLogoExtension;
	}


	public void setCompanyLogoExtension(String companyLogoExtension) {
		this.companyLogoExtension = companyLogoExtension;
	}


	String telephoneNumber;
	
	String website;
	
	@Embedded
	Address address;
	
	String headQuarters;
	
	
	String sourceofReferal;


	public String getCompanyName() {
		return companyName;
	}


	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}


	public String getCompanyRegistrationNumber() {
		return companyRegistrationNumber;
	}


	public void setCompanyRegistrationNumber(String companyRegistrationNumber) {
		this.companyRegistrationNumber = companyRegistrationNumber;
	}


	public String getTradingAs() {
		return tradingAs;
	}


	public void setTradingAs(String tradingAs) {
		this.tradingAs = tradingAs;
	}


	public String getYearFounded() {
		return yearFounded;
	}


	public void setYearFounded(String yearFounded) {
		this.yearFounded = yearFounded;
	}


	public OrganizationType getOrgType() {
		return orgType;
	}


	public void setOrgType(OrganizationType orgType) {
		this.orgType = orgType;
	}


	public String getTypeOConstrunctionWorkUndertaken() {
		return typeOConstrunctionWorkUndertaken;
	}


	public void setTypeOConstrunctionWorkUndertaken(
			String typeOConstrunctionWorkUndertaken) {
		this.typeOConstrunctionWorkUndertaken = typeOConstrunctionWorkUndertaken;
	}


	public Integer getNumberOfPRojectsUnderConstruction() {
		return numberOfPRojectsUnderConstruction;
	}


	public void setNumberOfPRojectsUnderConstruction(
			Integer numberOfPRojectsUnderConstruction) {
		this.numberOfPRojectsUnderConstruction = numberOfPRojectsUnderConstruction;
	}


	public Double getCompanySize() {
		return companySize;
	}


	public void setCompanySize(Double companySize) {
		this.companySize = companySize;
	}


	public byte[] getCompanyLogo() {
		return companyLogo;
	}


	public void setCompanyLogo(byte[] companyLogo) {
		this.companyLogo = companyLogo;
	}


	public String getCompanyLogoUrl() {
		return companyLogoUrl;
	}


	public void setCompanyLogoUrl(String companyLogoUrl) {
		this.companyLogoUrl = companyLogoUrl;
	}


	public String getTelephoneNumber() {
		return telephoneNumber;
	}


	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}


	public String getWebsite() {
		return website;
	}


	public void setWebsite(String website) {
		this.website = website;
	}


	public Address getAddress() {
		return address;
	}


	public void setAddress(Address address) {
		this.address = address;
	}


	public String getHeadQuarters() {
		return headQuarters;
	}


	public void setHeadQuarters(String headQuarters) {
		this.headQuarters = headQuarters;
	}


	public String getSourceofReferal() {
		return sourceofReferal;
	}


	public void setSourceofReferal(String sourceofReferal) {
		this.sourceofReferal = sourceofReferal;
	}
	
	

}
