package com.twenty20.services;

import com.twenty20.common.Twenty20Exception;
import com.twenty20.domain.Company;

public interface CompanyService extends BaseService{

	public void saveOrUpdate(Company company) throws Twenty20Exception;
	
	
	 public Company getUniqueCompany(String companyName, String companyRegistrationNumber) throws Twenty20Exception;
	 
	 
}
