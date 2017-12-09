package com.twenty20.services;

import java.util.List;

import com.twenty20.common.Twenty20Exception;
import com.twenty20.domain.Project;

public interface ProjectService extends BaseService{

	public void saveOrUpdate(Project project) throws Twenty20Exception;
	
	
	 public Project getProject(String projectName, String buyer) throws Twenty20Exception;
	 
	 public void remove(long id) throws Twenty20Exception;
	 
	 public List<Project> getProjectsByCompany(String company)throws Twenty20Exception;
}