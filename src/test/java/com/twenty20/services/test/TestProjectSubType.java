package com.twenty20.services.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.twenty20.domain.ProjectSubType;
import com.twenty20.services.ProjectSubTypeService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:appContext.xml"})
@Transactional
public class TestProjectSubType {
	@Autowired
	ProjectSubTypeService  projectSubTypeService;

	@Test
	@Rollback(value=false)
	public void testCreateProjectSubTypesMasterData(){
		ProjectSubType projectSubType = new ProjectSubType();
		projectSubType.setProjecttype("Residential");
		List<String> subtypes = new ArrayList<String>();
		subtypes.add("Building > 10 floors");
		subtypes.add("Building 4 to 10 floors");
		subtypes.add("Building < 4 floors");
		projectSubType.setSubTypes(subtypes);
		projectSubTypeService.saveOrUpdate(projectSubType);
		
		projectSubType = new ProjectSubType();
		projectSubType.setProjecttype("Commercial");
		subtypes = new ArrayList<String>();
		subtypes.add("> xm2");
		subtypes.add("< xm2");
		projectSubType.setSubTypes(subtypes);
		projectSubTypeService.saveOrUpdate(projectSubType);
		
		projectSubType = new ProjectSubType();
		projectSubType.setProjecttype("Infrastructure");
		subtypes = new ArrayList<String>();
		subtypes.add("Roads & highways");
		subtypes.add("Airport");
		subtypes.add("Railway");
		subtypes.add("Port");
		subtypes.add("Water & waste management");
		subtypes.add("Energy");
		projectSubType.setSubTypes(subtypes);
		projectSubTypeService.saveOrUpdate(projectSubType);
	}
}
