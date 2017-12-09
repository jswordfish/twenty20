package com.twenty20.services.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.io.FileUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import com.twenty20.common.Twenty20Exception;
import com.twenty20.dao.JPADAO;
import com.twenty20.dao.RebateDao;
import com.twenty20.dao.UserDao;
import com.twenty20.domain.Company;
import com.twenty20.domain.Rebate;
import com.twenty20.domain.User;
import com.twenty20.services.CompanyService;
import com.twenty20.services.RebateService;
import com.twenty20.services.UserService;
import com.twenty20.util.ConfUtil;

@Service("rebateService")
@org.springframework.transaction.annotation.Transactional(propagation= Propagation.REQUIRED, rollbackFor=Twenty20Exception.class)
public class RebateServiceImpl extends BaseServiceImpl<Long, Rebate> implements RebateService{
	@Autowired
    protected RebateDao dao;
	
	@Autowired
	protected CompanyService companyService;
	

	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	Validator validator = factory.getValidator();

	@PostConstruct
    public void init() throws Exception {
	 super.setDAO((JPADAO) dao);
    }
    
    @PreDestroy
    public void destroy() {
    }
    
    @Override
    public void setEntityManagerOnDao(EntityManager entityManager){
    	dao.setEntityManager(entityManager);
    }

	@Override
	public void saveOrUpdate(Rebate rebate) throws Twenty20Exception {
		Set<ConstraintViolation<Rebate>> violations = validator.validate(rebate);
		if(violations.size() > 0){
			throw new Twenty20Exception("INVALID_REBATE_PARAMS");
		}
		
		Rebate rebate2 = getUniqueRebateByNameAndCompany(rebate.getRebateName(), rebate.getCompany());
		if(rebate2 == null){
			//create mode
		
			dao.persist(rebate);
		}
		else{
			
			org.dozer.Mapper mapper = new DozerBeanMapper();
			mapper.map(rebate, rebate2);
			dao.merge(rebate2);
		}
	}

	@Override
	 public Rebate getUniqueRebateByNameAndCompany(String rebateName, String company) throws Twenty20Exception{
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("rebateName", rebateName);
		queryParams.put("company", company);
		
		List<Rebate> rebates = findByNamedQueryAndNamedParams(
				"Rebate.getUniqueRebateByNameAndCompany", queryParams);
		if(rebates.size() > 1){
			throw new Twenty20Exception("MULTIPLE_REBATES_SAME_IDENTITY");
		}
		if(rebates.size() == 0){
			return null;
		}
		return rebates.get(0);
	}

	@Override
	public List<Rebate> getActiveRebates() throws Twenty20Exception {
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("rebateActive", true);
		
		List<Rebate> rebates = findByNamedQueryAndNamedParams(
				"Rebate.getRebates", queryParams);
		return rebates;
	}

	@Override
	public List<Rebate> getActiveRebatesBySupplierAndCompany(String supplierNo, String companyNo)
			throws Twenty20Exception {
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("supplier", supplierNo);
		queryParams.put("company", companyNo);
		
		List<Rebate> rebates = findByNamedQueryAndNamedParams(
				"Rebate.getActiveRebatesBySupplierNoAndCompanyNo", queryParams);
		return rebates;
	}

	@Override
	public List<Rebate> getActiveRebatesByCompany(String companyNo) throws Twenty20Exception {
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("company", companyNo);
		
		List<Rebate> rebates = findByNamedQueryAndNamedParams(
				"Rebate.getActiveRebatesByCompanyNo", queryParams);
		return rebates;
	}

	@Override
	public void deleteById(Long rebateId) throws Twenty20Exception {
		// TODO Auto-generated method stub
		super.delete(rebateId);
	}

	

	
}
