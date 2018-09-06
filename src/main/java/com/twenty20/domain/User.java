package com.twenty20.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Entity
@NamedQueries({
	@NamedQuery(name="User.getUniqueUser", 
			query="SELECT u FROM User u WHERE u.userName=:userName"),
	
	@NamedQuery(name="User.getUsersByCompanyType", 
	query="SELECT u FROM User u WHERE u.userType=:userType AND u.validated=true")
})
public class User extends Base{

	@NotNull
	String firstName;
	
	@NotNull
	String lastName;
	
	@NotNull
	@Size(min=4, max=40)
	String userName;
	
	@Size(min=4, max=30)
	String password;
	
	//@Email
	String email;
	
	Boolean validated = false;
	
	@Enumerated(EnumType.STRING)
	UserType userType = UserType.SUPPLIER;
	
	@Embedded
	UserForgotQuestions userForgotQuestions = new UserForgotQuestions();
	
	@NotNull
	@ManyToOne(fetch=FetchType.EAGER)
	Company company;
	
	String token;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getValidated() {
		return validated;
	}

	public void setValidated(Boolean validated) {
		this.validated = validated;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public UserForgotQuestions getUserForgotQuestions() {
		return userForgotQuestions;
	}

	public void setUserForgotQuestions(UserForgotQuestions userForgotQuestions) {
		this.userForgotQuestions = userForgotQuestions;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	

	
	
	
}
