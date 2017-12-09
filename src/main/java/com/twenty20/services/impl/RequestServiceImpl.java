package com.twenty20.services.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.twenty20.dao.RequestDao;
import com.twenty20.domain.Project;
import com.twenty20.domain.Request;
import com.twenty20.services.ProjectService;
import com.twenty20.services.RequestService;
import com.twenty20.util.ConfUtil;
import com.twenty20.util.SearchParams;
@Service("requestService")
@org.springframework.transaction.annotation.Transactional(propagation= Propagation.REQUIRED, rollbackFor=Twenty20Exception.class)
public class RequestServiceImpl extends BaseServiceImpl<Long, Request> implements RequestService{
	@Autowired
    protected RequestDao dao;
	
	
//	ValidatorFactor
	@Autowired
	ConfUtil confService;
	
	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	Validator validator = factory.getValidator();
	
	@Autowired
	ProjectService projectService;

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
	public void saveOrUpdate(Request request) throws Twenty20Exception {
		// TODO Auto-generated method stub
		Set<ConstraintViolation<Request>> violations = validator.validate(request);
			if(violations.size() > 0){
				throw new Twenty20Exception("MISSING_MANDATORY_PARAMS");
			}
			
		request.setProjectLocation(getLocation(request.getProject(), request.getBuyer()));
			
		request = saveAndGenerateLogoUrl(request);
		Request request2 = getUniqueRequest(request.getRequestName(), request.getBuyer(), request.getCompany());
			if(request2 == null){
				request.setCreatedDate(new Date());
				dao.persist(request);
			}
			else{
				if(request.getCreatedDate() == null) {
					request.setCreatedDate(new Date());
				}
				
				request.setLastModifiedDate(new Date());
				
				request.setId(request2.getId());
				org.dozer.Mapper mapper = new DozerBeanMapper();
				mapper.map(request, request2);
				request2.setRequestDescriptions(request.getRequestDescriptions());
				dao.merge(request2);
			}
	}
	
	private String getLocation(String projectName, String buyer) {
		Project project =  projectService.getProject(projectName, buyer);
			if(project == null) {
				return "";
			}
			else {
				return project.getAddress().getTown();
			}
	}

	@Override
	public Request getUniqueRequest(String requestName,	String buyer, String company) throws Twenty20Exception {
		//Company.getUniqueCompany
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("company", company);
		queryParams.put("requestName", requestName);
		queryParams.put("buyer", buyer);
		
		List<Request> requests = findByNamedQueryAndNamedParams(
				"Request.getUniqueRequest", queryParams);
		if(requests.size() > 1){
			throw new Twenty20Exception("TOO_MANY_COMPANIES_BY_SAME_NAME");
		}
		if(requests.size() == 0){
			return null;
		}
		return requests.get(0);
	}
    
   
	private Request saveAndGenerateLogoUrl(Request request){
    	try {
			if(request.getRequestSpecificationFileExtension() != null && request.getRequestSpecificationFileExtension().trim().length() != 0){
				FileUtils.writeByteArrayToFile(new File(confService.getDocumentsLocation()+File.separator+""+request.getCompany()+"-"+request.getBuyer()+"-"+request.getRequestName()+"."+request.getRequestSpecificationFileExtension()), request.getRequestSpecification());
				request.setRequestSpecificationUrl(confService.getDocumentsServerBaseUrl()+request.getCompany()+"-"+request.getBuyer()+"-"+request.getRequestName()+"."+request.getRequestSpecificationFileExtension());
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new Twenty20Exception("CAN_NOT_CREATE_LOGO_FILE", e);
		}
    	return request;
    }

	@Override
	public void delete(Long id) throws Twenty20Exception {
		// TODO Auto-generated method stub
		super.delete(id);
	}

	@Override
	public List<Request> getRequests(SearchParams searchParams) throws Twenty20Exception {
		// TODO Auto-generated method stub
		return dao.getRequests(searchParams);
	}
	
	
}
