package com.twenty20.webservices;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import com.twenty20.common.Twenty20Exception;
import com.twenty20.domain.Rebate;

@Path("/rebateService")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RebateWebService {
	
	@Autowired
	com.twenty20.services.RebateService	rebateService;
	
	@POST
	@Path("/rebate/token/{token}")
	@Produces("application/json")
	@Consumes("application/json")
	public javax.ws.rs.core.Response saveOrUpdateCompany(Rebate rebate, @PathParam("token") String token){
		try {
			rebateService.saveOrUpdate(rebate);
			return javax.ws.rs.core.Response.ok().build();
		} catch (Twenty20Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return javax.ws.rs.core.Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
	
	@POST
	@Path("/delete/rebate/token/{token}")
	@Produces("application/json")
	@Consumes("application/json")
	public javax.ws.rs.core.Response deleteRebate(Rebate rebate, @PathParam("token") String token){
		try {
			Rebate rebate2 = rebateService.getUniqueRebate(rebate.getRebateUniqueId());
			if(rebate2 != null){
				rebate2.setRebateActive(false);
				rebateService.saveOrUpdate(rebate2);
				return javax.ws.rs.core.Response.ok().build();
			}
			else{
				return javax.ws.rs.core.Response.status(Status.BAD_REQUEST).build();
			}
			
		} catch (Twenty20Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return javax.ws.rs.core.Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
	
	@GET
	@Path("/rebates/supplier/{supplier}/company/{company}/token/{token}")
	@Produces("application/json")
	@Consumes("application/json")
	public javax.ws.rs.core.Response getAllRebatesBySupplierNoAndCompany(@PathParam("supplier") String supplier, @PathParam("company") String company, @PathParam("token") String token){
		try {
			List<Rebate> rebates = rebateService.getActiveRebatesBySupplierAndCompany(supplier, company);
			return javax.ws.rs.core.Response.ok(rebates).build();
		} catch (Twenty20Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return javax.ws.rs.core.Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
	
	@GET
	@Path("/rebates/company/{company}/token/{token}")
	@Produces("application/json")
	@Consumes("application/json")
	public javax.ws.rs.core.Response getAllRebatesByCompany(@PathParam("company") String company, @PathParam("token") String token){
		try {
			List<Rebate> rebates = rebateService.getActiveRebatesByCompany(company);
			return javax.ws.rs.core.Response.ok(rebates).build();
		} catch (Twenty20Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return javax.ws.rs.core.Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

}
