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
import com.twenty20.dao.ProjectSubTypeDao;
import com.twenty20.domain.Company;
import com.twenty20.domain.ProjectSubType;
import com.twenty20.domain.Rebate;
import com.twenty20.services.ProjectSubTypeService;
import com.twenty20.util.ConfUtil;

@Service("projectSubTypeService")
@org.springframework.transaction.annotation.Transactional(propagation= Propagation.REQUIRED, rollbackFor=Twenty20Exception.class)
public class ProjectSubTypeServiceImpl extends BaseServiceImpl<Long, ProjectSubType> implements ProjectSubTypeService{
	@Autowired
    protected ProjectSubTypeDao dao;
	
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
	public void saveOrUpdate(ProjectSubType projectSubType) throws Twenty20Exception {
		// TODO Auto-generated method stub
		if(projectSubType.getProjecttype() == null){
			throw new Twenty20Exception("CAN_NOT_BE_NULL");
		}
		
		ProjectSubType projectSubType2 = getProjectSubType(projectSubType.getProjecttype());
			if(projectSubType2 == null){
				dao.persist(projectSubType);
			}
			else{
				projectSubType.setId(projectSubType2.getId());
				projectSubType2.setSubTypes(projectSubType.getSubTypes());
				dao.merge(projectSubType2);
			}
	}

	@Override
	public ProjectSubType getProjectSubType(String project) throws Twenty20Exception {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("projecttype", project);
		
		List<ProjectSubType> subTypes = findByNamedQueryAndNamedParams(
				"ProjectSubType.getProjectSubType", queryParams);
		if(subTypes.size() > 1){
			throw new Twenty20Exception("MULTIPLE_Project_SubTypes_With_SAME_IDENTITY");
		}
		
		if(subTypes.size() == 0){
			return null;
		}
		return subTypes.get(0);
	}

	
}
