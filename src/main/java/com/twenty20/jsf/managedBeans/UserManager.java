package com.twenty20.jsf.managedBeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.primefaces.context.RequestContext;
import org.springframework.stereotype.Service;

import com.twenty20.common.Twenty20Exception;
import com.twenty20.domain.Company;
import com.twenty20.domain.User;
import com.twenty20.domain.UserType;
import com.twenty20.services.CompanyService;
import com.twenty20.services.UserService;
import com.twenty20.util.EmailUtil;

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
	
	@ManagedProperty(value="#{tab}") 
	TabManager tabManager;
	
	transient CompanyService  companyService;
	
	List<Company> companies = new ArrayList();
	
	
	
	String companyNameSelected;
	
	String styleIfNotSelected = "text-align:left;font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 12px;font-weight: bold;color:red";
	
	boolean companyCreateMode = true;
	
	String token="";
	
	 
//	public void reload() {
//		this.companies = companyService.getAllCompaniesSortedByName();
//	
//		for(Company comp : getCompanies()) {
//			if(comp.getCompanyName().equals("Not Listed")) {
//				usr.setCompany(comp);
//				break;
//			}
//		}
//	
//	setCompanyNameSelected("Not Listed");
//	String stl = getStyleIfNotSelected();
//	stl = stl.replace("color:green", "color:red");
//	setStyleIfNotSelected(stl);
//	}

	
	@PostConstruct
    public void init() {
		userService = SpringUtil.getService(UserService.class);
		companyService = SpringUtil.getService(CompanyService.class);
		companies = companyService.getAllCompaniesSortedByName();
		Company notSelected = new Company();
		notSelected.setCompanyName("Not Listed");
		companies.add(0, notSelected);
		//companiesStr.
    }
	
	
	
	public String login() {
		User user = userService.getUniqueUser(getUser());
			if(user == null) {
				setErrorTitle("User Does not Exist");
				setErrorDetails("If you think you have already registered ,try clicking 'Forgot User or Password' button on login page");
				setUsr(null);
				return "Error.xhtml?faces-redirect=true";
			}
			
			if(!user.getValidated()) {
				setErrorTitle("User Not Validated");
				setErrorDetails("If you think you have already registered and do not have the email holding the token, try registering yourself again");
				setUsr(null);
				return "Error.xhtml?faces-redirect=true";
			}
			if(user.getPassword().equals(getPassword())) {
				usr = user;
				if(usr.getUserType() == UserType.BUYER) {
					tabManager.setDisplayTab("Dashboard");
					//RequestContext.getCurrentInstance().update("buyerDashboardForm:pieChartRegion");
					return "bootstrapTabs.xhtml?faces-redirect=true";
				}
				else {
					tabManager.setDisplayTab("Responses");
					return "supplierTabs.xhtml?faces-redirect=true";
				}
				
			}
		else {
			setErrorTitle("Invalid Password");
			setErrorDetails("If you don't recollect your password ,try clicking 'Forgot User or Password' button on login page");
			return "Error.xhtml?faces-redirect=true";
		}
	}
	
	public void upload() {
		if(usr.getCompany().getUploadedFile() != null){
			String fileName = usr.getCompany().getUploadedFile().getFileName();
		    String contentType = usr.getCompany().getUploadedFile().getContentType();
		    byte[] contents = usr.getCompany().getUploadedFile().getContents(); // Or getInputStream()
		    usr.getCompany().setCompanyLogo(contents);
		    usr.getCompany().setCompanyLogoExtension(fileName.substring(fileName.lastIndexOf("."), fileName.length()));
		}
	    
	    // ... Save it, now!
	}
	
	public String updateCompany(String companyName) {
		for(Company company : getCompanies()) {
			if(company.getCompanyName().equals(companyName)) {
				usr.setCompany(company);
				setCompanyCreateMode(false);
				break;
			}
		}
		return "create.xhtml?faces-redirect=true";
	}
	
	public void saveCompany(){
		Company company = usr.getCompany();
			if(company.getCompanyName().trim().equalsIgnoreCase("")) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Company Name", "Change Company Name");
				RequestContext.getCurrentInstance().showMessageInDialog(message);
				return;
			}
			
			if(isCompanyCreateMode()) {
				Company temp = companyService.getUniqueCompany(company.getCompanyName().trim(), company.getCompanyRegistrationNumber().trim());
				if(temp != null) {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Company Already Exists", "To update go back and click on 'Click to view and edit Company Details' to proceed. Just making sure you are not duplicating by mistake. ");
					RequestContext.getCurrentInstance().showMessageInDialog(message);
					return;
				}
				
				if(companyService.getCompaniesByName(company.getCompanyName()).size() > 0) {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Company already exists with name provided - "+company.getCompanyName(), "You need to log in and then update the Company!");
					RequestContext.getCurrentInstance().showMessageInDialog(message);
					return;
				}
			}
			
		companyService.saveOrUpdate(company);
		companies.add(company);
		usr.setCompany(company);
		//RequestContext.getCurrentInstance().update("userForm:pType");
		
		setCompanyNameSelected(company.getCompanyName());
		String stl = getStyleIfNotSelected();
		stl = stl.replace("color:red", "color:green");
		setStyleIfNotSelected(stl);
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Company Saved. Click on Go Back Button");
		RequestContext.getCurrentInstance().showMessageInDialog(message);
		//return "registration.xhtml?faces-redirect=true";
	}
	
	public String registerUser() {
		usr = new User();
		Company company = new Company();
		usr.setCompany(company);
		
			for(Company comp : getCompanies()) {
				if(comp.getCompanyName().equals("Not Listed")) {
					usr.setCompany(comp);
				}
			}
		setCompanyNameSelected("Not Listed");
		String stl = getStyleIfNotSelected();
		stl = stl.replace("color:green", "color:red");
		setStyleIfNotSelected(stl);
		return "registration.xhtml?faces-redirect=true";
	}
	
	public void checkValidUser() {
		String u = usr.getUserName();
		User t = userService.getUniqueUser(u);
			if(t != null) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "User Name Exists", "Try something different");
				RequestContext.getCurrentInstance().showMessageInDialog(message);
				usr.setUserName(null);
			}
	}
	
	public void updateCompanyDetails() {
		if(getCompanyNameSelected() != null &&  !getCompanyNameSelected().equalsIgnoreCase("Not Listed")) {
			for(Company company : getCompanies()) {
				if(company.getCompanyName().equals(getCompanyNameSelected())) {
						if(usr == null) {
							usr = new User();
						}
					usr.setCompany(company);
					String stl = getStyleIfNotSelected();
					stl = stl.replace("color:red", "color:green");
					setStyleIfNotSelected(stl);
					RequestContext.getCurrentInstance().update("userForm:companyName-id");
					RequestContext.getCurrentInstance().update("userForm:companyRegistrationNumber-id");
					RequestContext.getCurrentInstance().update("userForm:tradingAs-id");
//					RequestContext.getCurrentInstance().update("userForm:yearFounded-id");
//					RequestContext.getCurrentInstance().update("userForm:orgType-id");
					RequestContext.getCurrentInstance().update("userForm:companyLogoUrl-id");
					//RequestContext.getCurrentInstance().update("userForm:compDetails");
					RequestContext.getCurrentInstance().update("userForm:saveUser");
					break;
				}
			}
		}
		else {
			if(usr == null) {
				usr = new User();
			}
			Company notSelected = new Company();
			notSelected.setCompanyName("Not Listed");
			usr.setCompany(notSelected);
			String stl = getStyleIfNotSelected();
			stl = stl.replace("color:green", "color:red");
			setStyleIfNotSelected(stl);
			RequestContext.getCurrentInstance().update("userForm:companyName-id");
			RequestContext.getCurrentInstance().update("userForm:companyRegistrationNumber-id");
			RequestContext.getCurrentInstance().update("userForm:tradingAs-id");
			
			RequestContext.getCurrentInstance().update("userForm:companyLogoUrl-id");
			
			//RequestContext.getCurrentInstance().update("userForm:compDetails");
			RequestContext.getCurrentInstance().update("userForm:saveUser");
		}
		RequestContext.getCurrentInstance().update("userForm:newComp");
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

	public TabManager getTabManager() {
		return tabManager;
	}

	public void setTabManager(TabManager tabManager) {
		this.tabManager = tabManager;
	}

	public List<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}

	public String getCompanyNameSelected() {
		return companyNameSelected;
	}

	public void setCompanyNameSelected(String companyNameSelected) {
		this.companyNameSelected = companyNameSelected;
	}

	public String getStyleIfNotSelected() {
		return styleIfNotSelected;
	}

	public void setStyleIfNotSelected(String styleIfNotSelected) {
		this.styleIfNotSelected = styleIfNotSelected;
	}
	
	public String saveUser() {
		User temp = userService.getUniqueUser(usr.getUserName());
			if(temp != null) {
				if(temp.getValidated()) {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "User Name Exists", "Try something different");
					RequestContext.getCurrentInstance().showMessageInDialog(message);
					usr.setUserName(null);
					return "";
				}
				else {
					Random random = new Random();
					String token = String.format("%04d", random.nextInt(10000));
					usr.setToken(token);
					usr.setValidated(false);
					userService.saveOrUpdate(usr);
					EmailUtil emailUtil = new EmailUtil(usr);
					Thread th = new Thread(emailUtil);
					th.start();
				}
				
				
			
			}
			else {
				Random random = new Random();
				String token = String.format("%04d", random.nextInt(10000));
				usr.setToken(token);
				usr.setValidated(false);
				userService.createUserWithMailSent(usr);
			}
		
		return "userSaved.xhtml?faces-redirect=true";
	}

	public String validateUser() {
		if(getToken() == null || getToken().trim().length() == 0) {
			setErrorTitle("Invalid Token");
			setErrorDetails("Token can't be blank. Go back!");
			return "Error.xhtml?faces-redirect=true";
		}
		
		if(usr.getToken().equals(getToken())) {
			userService.validateUser(usr);
			setToken("");
			if(usr.getUserType() == UserType.BUYER) {
				tabManager.setDisplayTab("Dashboard");
				//RequestContext.getCurrentInstance().update("buyerDashboardForm:pieChartRegion");
				return "bootstrapTabs.xhtml?faces-redirect=true";
			}
			else {
				tabManager.setDisplayTab("Responses");
				return "supplierTabs.xhtml?faces-redirect=true";
			}
		}
		else {
			token = "";
			setErrorTitle("Invalid Token");
			setErrorDetails("Wrong Token. Go back by clicking the back button of your browser and try adding again. Go back!");
			return "Error.xhtml?faces-redirect=true";
		}
		
	}

	public boolean isCompanyCreateMode() {
		return companyCreateMode;
	}



	public void setCompanyCreateMode(boolean companyCreateMode) {
		this.companyCreateMode = companyCreateMode;
	}
	
	public void setCheck() {
		setCompanyCreateMode(true);
	//	reload();
	}



	public String getToken() {
		return token;
	}



	public void setToken(String token) {
		this.token = token;
	}



	
	

}
