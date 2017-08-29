package com.twenty20.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
public class Project extends Base{

	@NotNull
	String buyer;
	
	@NotNull
	String company;
	
	@NotNull
	String projectName;
	
	@Temporal(value = TemporalType.DATE)
	Date projectCommenceDate = new Date();
	
	@Enumerated(EnumType.STRING)
	ProjectType projectType = ProjectType.RESIDENTIAL;
	
	@Transient
	String projectTypes[] = ProjectType.allTypes();
	
	String projectSubTye;
	
	String projectValue;
	
	Float approximateProjectDuration = 5f;
	
	Boolean active = true;

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

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Date getProjectCommenceDate() {
		return projectCommenceDate;
	}

	public void setProjectCommenceDate(Date projectCommenceDate) {
		this.projectCommenceDate = projectCommenceDate;
	}

	public ProjectType getProjectType() {
		return projectType;
	}

	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}

	public String[] getProjectTypes() {
		return projectTypes;
	}

	public void setProjectTypes(String[] projectTypes) {
		this.projectTypes = projectTypes;
	}

	public String getProjectSubTye() {
		return projectSubTye;
	}

	public void setProjectSubTye(String projectSubTye) {
		this.projectSubTye = projectSubTye;
	}

	public String getProjectValue() {
		return projectValue;
	}

	public void setProjectValue(String projectValue) {
		this.projectValue = projectValue;
	}

	public Float getApproximateProjectDuration() {
		return approximateProjectDuration;
	}

	public void setApproximateProjectDuration(Float approximateProjectDuration) {
		this.approximateProjectDuration = approximateProjectDuration;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	
}
