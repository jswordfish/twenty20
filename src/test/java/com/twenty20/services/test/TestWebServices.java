package com.twenty20.services.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.twenty20.domain.Rebate;

public class TestWebServices {

	
	@Test
	public void testCreateBuddy() throws Exception{
		Client client = ClientBuilder.newBuilder().newClient().register(new JacksonJsonProvider());
		//client.
		WebTarget target = client.target("http://localhost:8080/twenty20-1.0/ws/rest/rebateService/rebate/token/test");
		
		 
		Invocation.Builder builder = target.request();
		//Buddy buddy = getBuddy();
		Rebate rebate = getRebate();
		Response res = builder.post(Entity.entity(rebate, MediaType.APPLICATION_JSON));
		System.out.println(res.getStatus());		
	}
	
	private Rebate getRebate(){
		Rebate rebate = new Rebate();
//		rebate.setSuppilierNo("Supp_Test");
//		rebate.setCompany("Comp_Test");
//		rebate.setBaseOfferSalesValue(100000d);
//		rebate.setRebateUniqueId("Supp_Test-Comp_Test-1503917379063");
//		rebate.setBaseOfferRebateOfferInPercent(23.5f);
		return rebate;
	}
}
