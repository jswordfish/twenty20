package com.twenty20.services.test;

import java.io.File;
import java.nio.file.Files;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.twenty20.domain.Company;
import com.twenty20.domain.User;
import com.twenty20.domain.UserForgotQuestions;
import com.twenty20.domain.UserType;
import com.twenty20.services.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:appContext.xml"})
@Transactional
public class TestUser {
	@Autowired
	UserService userService;
	
		
	@Test
	@Rollback(value=false)
	public void testUser() throws Exception{
		User user = new User();
		user.setFirstName("Mike");
		user.setLastName("Glover");
		user.setUserName("mglover");
		user.setPassword("secret");
		user.setEmail("mglover@twenty20.com");
		user.setUserType(UserType.BUYER);
		UserForgotQuestions userForgotQuestions = new UserForgotQuestions();
		userForgotQuestions.setQuestionChosen("question2");
		userForgotQuestions.setAnswer("Nilesh");
		user.setUserForgotQuestions(userForgotQuestions);
		Company company = new Company("Abc Inc", "anxdswdwd");
		user.setCompany(company);
		userService.saveOrUpdate(user);
	}
	
	@Test
	@Rollback(value=false)
	public void testUserSupplier() throws Exception{
		User user = new User();
		user.setFirstName("Jatin");
		user.setLastName("Sutaria");
		user.setUserName("jsutaria");
		user.setPassword("secret");
		user.setEmail("jatin.sutaria@thev2technologies.com");
		user.setUserType(UserType.SUPPLIER);
		UserForgotQuestions userForgotQuestions = new UserForgotQuestions();
		userForgotQuestions.setQuestionChosen("question2");
		userForgotQuestions.setAnswer("Nilesh");
		user.setUserForgotQuestions(userForgotQuestions);
		Company company = new Company("V2 Tech", "anxdswdwd");
		user.setCompany(company);
		userService.saveOrUpdate(user);
	}
	
	
	


}
