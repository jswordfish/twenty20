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
import com.twenty20.dao.CompanyDao;
import com.twenty20.dao.JPADAO;
import com.twenty20.dao.UserDao;
import com.twenty20.domain.Company;
import com.twenty20.domain.User;
import com.twenty20.services.CompanyService;
import com.twenty20.services.UserService;
import com.twenty20.util.ConfUtil;

@Service("userService")
@org.springframework.transaction.annotation.Transactional(propagation= Propagation.REQUIRED, rollbackFor=Twenty20Exception.class)
public class UserServiceImpl extends BaseServiceImpl<Long, User> implements UserService{
	@Autowired
    protected UserDao dao;
	
	@Autowired
	protected CompanyService companyService;
	
//	ValidatorFactor
	@Autowired
	ConfUtil confService;
	
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
	public void saveOrUpdate(User user) throws Twenty20Exception {
		// TODO Auto-generated method stub
		Company company = companyService.getUniqueCompany(user.getCompany().getCompanyName(), user.getCompany().getCompanyRegistrationNumber());
		user.setCompany(company);
		
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		if(violations.size() > 0){
			throw new Twenty20Exception("INVALID_USER_PARAMS");
		}
			
		User user2 = getUniqueUser(user.getUserName());
			if(user2 == null){
				dao.persist(user);
			}
			else{
				user.setId(user2.getId());
				org.dozer.Mapper mapper = new DozerBeanMapper();
				mapper.map(user, user2);
				dao.merge(user2);
			}
	}

	@Override
	public User getUniqueUser(String userName) throws Twenty20Exception {
		//Company.getUniqueCompany
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("userName", userName);
		
		List<User> users = findByNamedQueryAndNamedParams(
				"User.getUniqueUser", queryParams);
		if(users.size() > 1){
			throw new Twenty20Exception("TOO_MANY_USERS_BY_SAME_NAME");
		}
		if(users.size() == 0){
			return null;
		}
		return users.get(0);
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
}
