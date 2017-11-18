package com.twenty20.services;

import com.twenty20.common.Twenty20Exception;
import com.twenty20.domain.Project;

public interface ProjectService extends BaseService{

	public void saveOrUpdate(Project project) throws Twenty20Exception;
	
	
	 public Project getProject(String projectName, String buyer) throws Twenty20Exception;
	 
	 public void remove(long id) throws Twenty20Exception;
}