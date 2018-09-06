package com.twenty20.services.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.twenty20.domain.Request;
import com.twenty20.services.RequestService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:appContext.xml"})
@Transactional
public class TestRequest {
	@Autowired
	RequestService requestService;
	
	@Test
	@Rollback(value=false)
	public void testLazy() {
		Request r = (Request) requestService.find(82);
		Request r2 = new Request();
		r2.setRequestName(r.getRequestName());
		r2.setBuyer(r.getBuyer());
		r2.setCompany(r.getCompany());
		Hibernate.initialize(r);
		List<String> list = new ArrayList<>();
		list.add("444");
//		r2.getSupplierCompaniesSubset().clear();
//		r2.getSupplierCompaniesSubset().add("eee");
		r2.setSupplierCompaniesSubset(list);
		requestService.saveOrUpdate(r2);
	}

}
