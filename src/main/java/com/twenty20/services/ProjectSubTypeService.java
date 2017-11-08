package com.twenty20.services;

import com.twenty20.common.Twenty20Exception;
import com.twenty20.domain.ProjectSubType;

public interface ProjectSubTypeService extends BaseService{

	public void saveOrUpdate(ProjectSubType projectSubType) throws Twenty20Exception;
	
	
	 public ProjectSubType getProjectSubType(String project) throws Twenty20Exception;
	 
	 
}
