package com.twenty20.services.test;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.twenty20.domain.Project;
import com.twenty20.domain.ProjectSubType;
import com.twenty20.domain.ProjectType;
import com.twenty20.services.ProjectService;
import com.twenty20.services.ProjectSubTypeService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:appContext.xml"})
@Transactional
public class TestProjectService {
	@Autowired
	ProjectService projectService;
	
	@Autowired
	ProjectSubTypeService  projectSubTypeService;
	
	@Test
	@Rollback(value=false)
	public void testProject(){
		Project p = new Project();
		p.setActive(true);
		p.setBuyer("Buyer 1");
		p.setCompany("Company 1");
		p.setCreatedDate(new Date());
		p.setApproximateProjectDuration(23.3f);
		p.setProjectName("Mega Arts");
		p.setProjectType(ProjectType.COMMERCIAL);
		ProjectSubType projectSubType = projectSubTypeService.getProjectSubType(ProjectType.COMMERCIAL.getType());
		p.setProjectSubTye(projectSubType.getSubTypes().get(0));
		p.setProjectCommenceDate(new Date());
		p.setProjectValue("1234567");
		projectService.saveOrUpdate(p);
		//p.set
	}

}
