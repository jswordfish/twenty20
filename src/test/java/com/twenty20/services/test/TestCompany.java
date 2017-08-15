package com.twenty20.services.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.twenty20.domain.Company;
import com.twenty20.services.CompanyService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:appContext.xml"})
@Transactional
public class TestCompany {
@Autowired	
CompanyService companyService;

	@Test
	@Rollback(value=false)
	public void testCompany() throws Exception{
		Company company = new Company();
		company.setCompanyName("Abc Inc");
		company.setCompanyRegistrationNumber("anxdswdwd");
		File fi = new File("testImages/download.jpg");
		byte[] fileContent = Files.readAllBytes(fi.toPath());
		company.setCompanyLogoExtension("jpg");
		company.setCompanyLogo(fileContent);
		companyService.saveOrUpdate(company);
	}

}
