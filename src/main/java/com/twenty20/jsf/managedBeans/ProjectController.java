package com.twenty20.jsf.managedBeans;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.context.RequestContext;

import com.twenty20.domain.Address;
import com.twenty20.domain.Project;
import com.twenty20.domain.ProjectSubType;
import com.twenty20.domain.User;
import com.twenty20.services.ProjectService;
import com.twenty20.services.ProjectSubTypeService;
import com.twenty20.services.UserService;

@ManagedBean(name = "projectsManager", eager = true)
@SessionScoped
public class ProjectController {
	
	public ProjectSubType getProjectSubType() {
		return projectSubType;
	}

	public void setProjectSubType(ProjectSubType projectSubType) {
		this.projectSubType = projectSubType;
	}

	Project project = new Project();
	
	User user;
	
	transient ProjectService  projectService;
	
	List<Project> projects = new ArrayList<Project>();
	
	String title = "New Project";
	
	String projectDate;
	
	ProjectSubType  projectSubType;
	
	transient ProjectSubTypeService projectSubTypeService;
	
	String source = "first_tab";
	
	@PostConstruct
	public void init(){
		projectService = (ProjectService) SpringUtil.getService(ProjectService.class);
		this.projects = projectService.findAll();
		//test user
		UserService userService = (UserService) SpringUtil.getService(UserService.class);
		user = userService.getUniqueUser("mglover");
		getProject().setBuyer(user.getUserName());
		getProject().setCompany(user.getCompany().getCompanyName());
		projectSubTypeService = (ProjectSubTypeService)SpringUtil.getService(ProjectSubTypeService.class);
		projectSubType = projectSubTypeService.getProjectSubType("Residential");
		if(getProject().getAddress() == null){
			getProject().setAddress(new Address());
		}
	}
	
	public void changeSubTypes(AjaxBehaviorEvent abe){
		  //do what you want with your favCoffee3 variable here
		System.out.println(abe.getSource());
		
		String tp = getProject().getType();
		projectSubType = projectSubTypeService.getProjectSubType(tp);
		RequestContext.getCurrentInstance().update("projectForm:projectSubTypes");
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	public String saveProject(){
		projectService.saveOrUpdate(project);
		this.projects = projectService.findAll();
		setSource("third_tab");
		return "bootstrapTabs.xhtml?faces-redirect=false&fromSource=Project";
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public String edit(Project project1){
		setTitle("Update Project- "+project1.getProjectValue()+" And Type- "+project1.getProjectType());
		//RequestContext.getCurrentInstance().update("form:displayTitle");
		this.project = project1;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if(this.project.getProjectCommenceDate() != null){
			String dt = df.format(this.project.getProjectCommenceDate());
			this.projectDate = dt;
		}
		
		if(this.project.getProjectSubTye() != null){
			String tp = this.project.getType();
			projectSubType = projectSubTypeService.getProjectSubType(tp);
		}
		
		return "/project.xhtml?faces-redirect=false";
	}
	
	
	
	public void delete(Long id){
		projectService.remove(id);
		this.projects = projectService.findAll();
	}
	
	public String createNew(){
		setProject(new Project());
		setTitle("New Project Creation");UserService userService = (UserService) SpringUtil.getService(UserService.class);
		user = userService.getUniqueUser("mglover");
		getProject().setBuyer(user.getUserName());
		getProject().setCompany(user.getCompany().getCompanyName());
		projectSubTypeService = (ProjectSubTypeService)SpringUtil.getService(ProjectSubTypeService.class);
		projectSubType = projectSubTypeService.getProjectSubType("Residential");
		if(getProject().getAddress() == null){
			getProject().setAddress(new Address());
		}
		
		return "/project.xhtml";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getProjectDate() {
		return projectDate;
	}

	public void setProjectDate(String projectDate) throws ParseException {
		this.projectDate = projectDate;
		if(this.projectDate != null || this.projectDate.trim().length() != 0){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = formatter.parse(projectDate);
			getProject().setProjectCommenceDate(date);
		}
		
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	
}
