package com.twenty20.dao;

import java.util.List;

import com.twenty20.domain.Company;
import com.twenty20.domain.User;

public interface UserDao extends JPADAO<User, Long>{
	
	public List<User> fetchValidatedUsersForCompanies(String companyName);

}
