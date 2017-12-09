package com.twenty20.services;

import java.util.List;

import com.twenty20.common.Twenty20Exception;
import com.twenty20.domain.Rebate;

public interface RebateService extends BaseService{

	public void saveOrUpdate(Rebate rebate) throws Twenty20Exception;
	
	
	 public Rebate getUniqueRebateByNameAndCompany(String rebateName, String company) throws Twenty20Exception;
	 
	 public List<Rebate> getActiveRebates() throws Twenty20Exception;
	 
	 public List<Rebate> getActiveRebatesBySupplierAndCompany(String supplierNo, String companyNo) throws Twenty20Exception;
	 
	 public List<Rebate> getActiveRebatesByCompany(String companyNo) throws Twenty20Exception;
	 
	 public void deleteById(Long rebateId)throws Twenty20Exception;
	 
	
}
