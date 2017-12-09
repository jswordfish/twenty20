package com.twenty20.dao;

import java.util.List;

import com.twenty20.common.Twenty20Exception;
import com.twenty20.domain.Request;
import com.twenty20.util.SearchParams;

public interface RequestDao extends JPADAO<Request, Long>{
	
	public List<Request> getRequests(SearchParams searchParams);
	
}


