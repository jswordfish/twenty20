package com.twenty20.services;

import java.util.List;

import com.twenty20.common.Twenty20Exception;
import com.twenty20.domain.Rebate;
import com.twenty20.domain.RebateClaim;

public interface RebateClaimService {

	public void saveOrUpdate(RebateClaim rebate) throws Twenty20Exception;
	
	
	 public RebateClaim getUniqueRebateClaimByNameAndCompany(String buyerCompany, String requestName ,String suplierCompany) throws Twenty20Exception;
	 
	 public List<RebateClaim> getRebateClaims(String buyerCompany) throws Twenty20Exception;
	 
	 public List<RebateClaim> getRebateClaimsForSupplier(String supplierCompany) throws Twenty20Exception;
	 
	 public void deleteById(Long rebateClaimId) throws Twenty20Exception ;
	 
	 public void accept(RebateClaim claim) throws Twenty20Exception ;
	 
	 public void reject(RebateClaim claim) throws Twenty20Exception ;
}
