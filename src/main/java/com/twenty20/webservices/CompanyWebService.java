package com.twenty20.webservices;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import com.twenty20.common.Twenty20Exception;
import com.twenty20.domain.Company;

@Path("/companyService")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CompanyWebService {

	@Autowired
	com.twenty20.services.CompanyService companyService;
	
	@POST
	@Path("/company/token/{token}")
	@Produces("application/json")
	@Consumes("application/json")
	public javax.ws.rs.core.Response saveOrUpdateCompany(Company company, @PathParam("token") String token){
		try {
			companyService.saveOrUpdate(company);
			return javax.ws.rs.core.Response.ok().build();
		} catch (Twenty20Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return javax.ws.rs.core.Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
	
	@POST
	@Path("/delete/company/token/{token}")
	@Produces("application/json")
	@Consumes("application/json")
	public javax.ws.rs.core.Response deleteCompany(Company company, @PathParam("token") String token){
		try {
			Company company2 = companyService.getUniqueCompany(company.getCompanyName(), company.getCompanyRegistrationNumber());
				if(company2 != null){
					companyService.delete(company2.getId());
				}
			return javax.ws.rs.core.Response.ok().build();
		} catch (Twenty20Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return javax.ws.rs.core.Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
	
	@POST
	@Path("/companies/token/{token}")
	@Produces("application/json")
	@Consumes("application/json")
	public javax.ws.rs.core.Response getAllCompanies(Company company, @PathParam("token") String token){
		try {
			List<Company> companies = companyService.findAll();
			return javax.ws.rs.core.Response.ok(companies).build();
		} catch (Twenty20Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return javax.ws.rs.core.Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
}
