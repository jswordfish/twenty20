package com.twenty20.services;

import java.util.List;

import org.hibernate.search.jpa.Search;

import com.twenty20.common.Twenty20Exception;
import com.twenty20.domain.Request;
import com.twenty20.util.SearchParams;

public interface RequestService extends BaseService{
	
	public void saveOrUpdate(Request request) throws Twenty20Exception;

	public Request getUniqueRequest(String requestName,	String buyer, String company) throws Twenty20Exception;
	
	public void delete(Long id)throws Twenty20Exception;
	
	public List<Request> getRequests(SearchParams searchParams) throws Twenty20Exception;
	
	public List<Request> getAllOpenRequests() throws Twenty20Exception;
	
	public List<Request> getAllOpenRequestsByBuyer(String buyer, String company) throws Twenty20Exception;
	
	public List<Request> getAllOpenRequestsByBuyerForSupplier(String supplierCompany) throws Twenty20Exception;
	
	public void sendCommunicationToSuppliers(Request req);
}
