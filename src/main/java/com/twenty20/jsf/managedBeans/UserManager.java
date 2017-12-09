package com.twenty20.jsf.managedBeans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.stereotype.Service;

import com.twenty20.domain.User;
import com.twenty20.domain.UserType;
import com.twenty20.services.UserService;

@ManagedBean(name = "userManager", eager = true)
@SessionScoped
@Service
public class UserManager {
	
	private String user;
	
	private String password;
	transient UserService userService;
	
	String errorTitle;
	
	String errorDetails;
	
	User usr;
	
	@PostConstruct
    private void init() {
		userService = SpringUtil.getService(UserService.class);
		
    }
	
	public String login() {
		User user = userService.getUniqueUser(getUser());
			if(user == null) {
				setErrorTitle("User Does not Exist");
				setErrorDetails("If you think you have already registered ,try clicking 'Forgot User or Password' button on login page");
				return "Error.xhtml?faces-redirect=true";
			}
			if(user.getPassword().equals(getPassword())) {
				usr = user;
				if(usr.getUserType() == UserType.BUYER) {
					return "bootstrapTabs.xhtml?faces-redirect=true";
				}
				else {
					return "supplierTabs.xhtml?faces-redirect=true";
				}
				
			}
		else {
			setErrorTitle("Invalid Password");
			setErrorDetails("If you don't recollect your password ,try clicking 'Forgot User or Password' button on login page");
			return "Error.xhtml?faces-redirect=true";
		}
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getErrorTitle() {
		return errorTitle;
	}

	public void setErrorTitle(String errorTitle) {
		this.errorTitle = errorTitle;
	}

	public String getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}

	public User getUsr() {
		return usr;
	}

	public void setUsr(User usr) {
		this.usr = usr;
	}
	
	

}
