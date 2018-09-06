package com.twenty20.services.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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
import com.twenty20.dao.CompanyDao;
import com.twenty20.dao.JPADAO;
import com.twenty20.domain.Company;
import com.twenty20.domain.Rebate;
import com.twenty20.services.CompanyService;
import com.twenty20.services.RebateService;
import com.twenty20.util.ConfUtil;


@Service("companyService")
@org.springframework.transaction.annotation.Transactional(propagation= Propagation.REQUIRED, rollbackFor=Twenty20Exception.class)
public class CompanyServiceImpl extends BaseServiceImpl<Long, Company> implements CompanyService{
	@Autowired
    protected CompanyDao dao;
	
//	ValidatorFactor
	@Autowired
	ConfUtil confService;
	
	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	Validator validator = factory.getValidator();
	
	@Autowired
	RebateService rebateService;

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
	public void saveOrUpdate(Company company) throws Twenty20Exception {
		// TODO Auto-generated method stub
		Set<ConstraintViolation<Company>> violations = validator.validate(company);
			if(violations.size() > 0){
				throw new Twenty20Exception("MISSING_MANDATORY_PARAMS");
			}
		company = saveAndGenerateLogoUrl(company);
		Company company2 = getUniqueCompany(company.getCompanyName(), company.getCompanyRegistrationNumber());
			if(company2 == null){
				dao.persist(company);
				
				
			}
			else{
				company.setId(company2.getId());
				company.setUploadedFile(null);
				org.dozer.Mapper mapper = new DozerBeanMapper();
				mapper.map(company, company2);
				dao.merge(company2);
			}
	}

	@Override
	public Company getUniqueCompany(String companyName,
			String companyRegistrationNumber) throws Twenty20Exception {
		//Company.getUniqueCompany
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("companyName", companyName);
		queryParams.put("companyRegistrationNumber", companyRegistrationNumber);
		
		List<Company> companies = findByNamedQueryAndNamedParams(
				"Company.getUniqueCompany", queryParams);
		if(companies.size() > 1){
			throw new Twenty20Exception("TOO_MANY_COMPANIES_BY_SAME_NAME");
		}
		if(companies.size() == 0){
			return null;
		}
		return companies.get(0);
	}
    
   
	private Company saveAndGenerateLogoUrl(Company company){
    	try {
			if(company.getCompanyLogoExtension() != null && company.getCompanyLogoExtension().trim().length() != 0){
				FileUtils.writeByteArrayToFile(new File(confService.getDocumentsLocation()+File.separator+""+company.getCompanyName()+"-"+company.getCompanyRegistrationNumber()+"."+company.getCompanyLogoExtension()), company.getCompanyLogo());
				company.setCompanyLogoUrl(confService.getDocumentsServerBaseUrl()+company.getCompanyName()+"-"+company.getCompanyRegistrationNumber()+"."+company.getCompanyLogoExtension());
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new Twenty20Exception("CAN_NOT_CREATE_LOGO_FILE", e);
		}
    	return company;
    }

	@Override
	public List<Company> getAllCompaniesSortedByName() throws Twenty20Exception {
		Map<String, String> queryParams = new HashMap<String, String>();
	
		
		List<Company> companies = findByNamedQueryAndNamedParams(
				"Company.getAllCompanies", queryParams);
		return companies;
	}
	
	public List<Company> getCompaniesByName(String companyName) throws Twenty20Exception{
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("companyName", companyName);
		
		List<Company> companies = findByNamedQueryAndNamedParams(
				"Company.getCompaniesByName", queryParams);
		return companies;
	}
}
