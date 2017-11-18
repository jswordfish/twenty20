package com.twenty20.jsf.managedBeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.UploadedFile;
import org.springframework.stereotype.Service;

import com.twenty20.common.OrganizationType;
import com.twenty20.domain.Company;
import com.twenty20.services.CompanyService;

@ManagedBean(name = "companiesManager", eager = true)
@ViewScoped
@Service
public class CompanyManager  {
	//@Autowired
	transient CompanyService  companyService;
	
	List<Company> companies = new ArrayList();
	
	String companyName;
	
	String companyRegistrationNumber;
	
	//String companyLogoUrl;
	
	String companyLogoExt;
	
	byte[] logo;
	
	String orgType;
	
	UploadedFile uploadedFile;
	
	String typeOConstrunctionWorkUndertaken;
	
	Integer numberOfPRojectsUnderConstruction;
	
	Company currentCompany;
	
	boolean company = false;
	
	private Map<String,Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap(); 
	
	@PostConstruct
    private void init() {
//        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//        ServletContext servletContext = (ServletContext) externalContext.getContext();
//    WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext).
//                                   getAutowireCapableBeanFactory().
//                                   autowireBean(this);
		companyService = SpringUtil.getService(CompanyService.class);
		companies = companyService.findAll();
		System.out.println(companyService);
    }
	
	public List<Company> companies(){
		return this.companies;
	}
	


	public String addCompany(){
		Company company = new Company();
		company.setCompanyName(companyName);
		company.setCompanyRegistrationNumber(companyRegistrationNumber);
		company.setCompanyLogo(getLogo());
		company.setCompanyLogoExtension(getCompanyLogoExt());
		company.setOrgType(OrganizationType.valueOf(getOrgType()));
		companyService.saveOrUpdate(company);
		companies.add(company);
		return "tabPanel.xhtml?faces-redirect=true";
	}
	
	public void upload() {
		if(uploadedFile != null){
			String fileName = uploadedFile.getFileName();
		    String contentType = uploadedFile.getContentType();
		    byte[] contents = uploadedFile.getContents(); // Or getInputStream()
		    setLogo(contents);
		    setCompanyLogoExt(fileName.substring(fileName.lastIndexOf("."), fileName.length()));
		}
	    
	    // ... Save it, now!
	}

	public CompanyService getCompanyService() {
		return companyService;
	}

	public void setCompanyService(CompanyService companyService) {
		this.companyService = companyService;
	}

	public List<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyRegistrationNumber() {
		return companyRegistrationNumber;
	}

	public void setCompanyRegistrationNumber(String companyRegistrationNumber) {
		this.companyRegistrationNumber = companyRegistrationNumber;
	}

	public String getCompanyLogoExt() {
		return companyLogoExt;
	}

	public void setCompanyLogoExt(String companyLogoExt) {
		this.companyLogoExt = companyLogoExt;
	}

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
	
	public String edit(int id){  
		Company editCompany = null;
		for(Company c : getCompanies()){
			if(c.getId() == id){
				editCompany = c;
				break;
			}
		}
		sessionMap.put("editCompany", editCompany);  
		return "/edit.xhtml?faces-redirect=true";  
	}
	
	public String delete(int id){  
		companyService.delete(id);
		Company remove = null;
		for(Company c : getCompanies()){
			if(c.getId() == id){
				remove = c;
				break;
			}
		}
		companies.remove(remove);
		return "/index.xhtml?faces-redirect=true";  
	}

	public String getTypeOConstrunctionWorkUndertaken() {
		return typeOConstrunctionWorkUndertaken;
	}

	public void setTypeOConstrunctionWorkUndertaken(
			String typeOConstrunctionWorkUndertaken) {
		this.typeOConstrunctionWorkUndertaken = typeOConstrunctionWorkUndertaken;
	}

	public Integer getNumberOfPRojectsUnderConstruction() {
		return numberOfPRojectsUnderConstruction;
	}

	public void setNumberOfPRojectsUnderConstruction(
			Integer numberOfPRojectsUnderConstruction) {
		this.numberOfPRojectsUnderConstruction = numberOfPRojectsUnderConstruction;
	}

	public Company getCurrentCompany() {
		return currentCompany;
	}

	public void setCurrentCompany(Company currentCompany) {
		this.currentCompany = currentCompany;
	}
	
	public void showCreateCompany(){
		this.company = true;
	}

	public boolean isCompany() {
		return company;
	}

	public void setCompany(boolean company) {
		this.company = company;
	}
	
	
}
