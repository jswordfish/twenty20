package com.twenty20.services;

import com.twenty20.common.Twenty20Exception;
import com.twenty20.domain.User;

public interface UserService extends BaseService{

	public void saveOrUpdate(User user) throws Twenty20Exception;
	
	public void createUserWithMailSent(User user) throws Twenty20Exception;
	
	 public User getUniqueUser(String userName) throws Twenty20Exception;
	 
	 public void validateUser(User user) throws Twenty20Exception;
}
