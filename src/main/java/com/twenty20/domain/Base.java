package com.twenty20.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;


@MappedSuperclass
public class Base implements Serializable { //TODO - Revisit all the attributes and remove the ones which are not really needed
	
	private static final long serialVersionUID = 1701926931204630606L;

	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    
    @Version
    protected Integer version;
    
    @Column(nullable = true)
    private String description;
    
    @Column(length = 55, nullable = true)
    protected String createdBy;
    
    @Column(length = 55, nullable = true)
    protected String deprecatedBy;
    
    @Column(length = 55, nullable = true)
    protected String lastModifiedBy;
    
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    protected Date deprecatedDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastModifiedDate;
    
    @Basic
    Boolean deprecated;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getDeprecatedBy() {
		return deprecatedBy;
	}

	public void setDeprecatedBy(String deprecatedBy) {
		this.deprecatedBy = deprecatedBy;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getDeprecatedDate() {
		return deprecatedDate;
	}

	public void setDeprecatedDate(Date deprecatedDate) {
		this.deprecatedDate = deprecatedDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Boolean getDeprecated() {
		return deprecated;
	}

	public void setDeprecated(Boolean deprecated) {
		this.deprecated = deprecated;
	}
    
    
    

}

