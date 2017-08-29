package com.twenty20.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries({
	@NamedQuery(name="ProjectSubType.getProjectSubType", 
			query="SELECT p FROM ProjectSubType p WHERE p.projecttype=:projecttype")
})
public class ProjectSubType extends Base{
	@Column(unique=true)
	String projecttype;
	
	@ElementCollection
	List<String> subTypes = new ArrayList<String>();

	public String getProjecttype() {
		return projecttype;
	}

	public void setProjecttype(String projecttype) {
		this.projecttype = projecttype;
	}

	public List<String> getSubTypes() {
		return subTypes;
	}

	public void setSubTypes(List<String> subTypes) {
		this.subTypes = subTypes;
	}
	
	
}

