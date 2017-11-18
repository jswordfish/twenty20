package com.twenty20.services.impl;

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

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import com.twenty20.common.Twenty20Exception;
import com.twenty20.dao.JPADAO;
import com.twenty20.dao.ProjectDao;
import com.twenty20.dao.ProjectSubTypeDao;
import com.twenty20.domain.Company;
import com.twenty20.domain.Project;
import com.twenty20.domain.ProjectSubType;
import com.twenty20.services.ProjectService;
import com.twenty20.services.ProjectSubTypeService;
import com.twenty20.util.ConfUtil;
@Service("projectService")
@org.springframework.transaction.annotation.Transactional(propagation= Propagation.REQUIRED, rollbackFor=Twenty20Exception.class)
public class ProjectServiceImpl extends BaseServiceImpl<Long, Project> implements ProjectService{
	@Autowired
    protected ProjectDao dao;
	
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
	public void saveOrUpdate(Project project) throws Twenty20Exception {
		// TODO Auto-generated method stub
		Set<ConstraintViolation<Project>> violations = validator.validate(project);
		if(violations.size() > 0){
			throw new Twenty20Exception("MISSING_MANDATORY_PARAMS");
		}	
		
		Project project2 = getProject(project.getProjectName(), project.getBuyer());
			if(project2 == null){
				//create
				dao.persist(project);
			}
			else{
				//update
				project.setId(project2.getId());
				org.dozer.Mapper mapper = new DozerBeanMapper();
				mapper.map(project, project2);
				dao.merge(project2);
			}
	}

	@Override
	public Project getProject(String projectName, String buyer)
			throws Twenty20Exception {
		//Company.getUniqueCompany
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("projectName", projectName);
		queryParams.put("buyer", buyer);
		
		List<Project> projects = findByNamedQueryAndNamedParams(
				"Project.getProject", queryParams);
		if(projects.size() > 1){
			throw new Twenty20Exception("TOO_MANY_PROJECTS_BY_SAME_NAME");
		}
		if(projects.size() == 0){
			return null;
		}
		return projects.get(0);
	}

	@Override
	public void remove(long id) throws Twenty20Exception {
		// TODO Auto-generated method stub
		super.delete(id);
	}

	
}
