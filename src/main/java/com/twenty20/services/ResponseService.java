package com.twenty20.services;

import java.util.List;

import com.twenty20.common.Twenty20Exception;
import com.twenty20.domain.Response;

public interface ResponseService extends BaseService{

	public void saveOrUpdate(Response response) throws Twenty20Exception;
	
	
	 public Response getResponse(String requestName, String buyer, String buyerCompany, String supplier, String supplierCompany) throws Twenty20Exception;
	 
	 public void remove(long id) throws Twenty20Exception;
	 
	 public List<Response> getResponsesByCompany(String suplierCompany)throws Twenty20Exception;
	 
	 public List<Response> getResponsesBySupplierNameAndCompany(String supplierName, String suplierCompany)throws Twenty20Exception;
	 
	 public List<Response> getResponsesForRequentNameAndBuyerCompany(String requestName, String buyerCompany)throws Twenty20Exception;
	 
	 
}