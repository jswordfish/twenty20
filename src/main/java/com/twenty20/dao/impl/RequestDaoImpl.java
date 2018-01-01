package com.twenty20.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.EntityContext;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.twenty20.common.Twenty20Exception;
import com.twenty20.dao.RequestDao;
import com.twenty20.domain.Request;
import com.twenty20.util.SearchParams;
@Repository("requestDAO")
public class RequestDaoImpl extends JpaDAOImpl<Long, Request> implements RequestDao{
	@Autowired
    EntityManagerFactory entityManagerFactory;
	
	@PersistenceContext(unitName="V2CRM_PersistenceUnit")
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager em) {
		this.entityManager = em;
		super.setEntityManager(entityManager);
		}
    
    @PostConstruct
    public void init() {
        super.setEntityManagerFactory(entityManagerFactory);
        super.setEntityManager(entityManager);
    }

    

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public List<Request> getRequests(SearchParams params) {
		// TODO Auto-generated method stub
		boolean isLuceneQueryNeeded = false;
		if(!params.getAllProducts() ) {
			if(params.getCement() || params.getFormWork() || params.getSteelBar() || params.getStoneChips() || params.getSand() || params.getReadyMix()) {
				isLuceneQueryNeeded = true;
			}
		}
		else {
			params.setCement(true);
			params.setFormWork(true);
			params.setSteelBar(true);
			params.setStoneChips(true);
			params.setSand(true);
			params.setReadyMix(true);
			isLuceneQueryNeeded = true;
		}
		
		boolean isAnySearchParamSelected = false;
		List<Request> reqsLucene = null;
		if(isLuceneQueryNeeded) {
			reqsLucene = getRequestFromLucene(params);
			isAnySearchParamSelected = true;
		}
		
		List<Request> requestsDb =null;
			try {
				requestsDb = getRequestsFromDatabase(params);
				isAnySearchParamSelected = true;
			}
			catch(Twenty20Exception e) {
				if(e.getMessage()!= null && e.getMessage().equalsIgnoreCase("NO_SEARCH_PARAMS_SELECTED")) {
					if(!isAnySearchParamSelected) {
						isAnySearchParamSelected = false;
					}
				}
				else {
					throw e;
				}
			}
			
		if(reqsLucene == null && requestsDb == null) {
			//No Search params around. Fetch all Requests
				if(!isAnySearchParamSelected) {
					return findAll();
				}
				else {
					return new ArrayList<Request>();
				}
			
			//return findAll();
		}
		
		if(reqsLucene != null && requestsDb != null) {
			//return intersection
			Set<Request> r1 = new HashSet<Request>();
			r1.addAll(reqsLucene);
			
			Set<Request> r2 = new HashSet<Request>();
			r2.addAll(requestsDb);
			SetView<Request> rs = Sets.intersection(r1, r2);
			List<Request> list = new ArrayList<>();
			list.addAll(rs);
			return getOpenRequests(list);
		}
		
		if(reqsLucene != null) {
			return getOpenRequests(reqsLucene);
		}
		
		if(requestsDb != null) {
			return getOpenRequests(requestsDb);
		}
		return null;
	}
	
	private List<Request> getOpenRequests(List<Request> list){
		List<Request> ret = new ArrayList<>();
		for(Request r : list) {
			if(!r.getClosed()) {
				ret.add(r);
			}
		}
	return ret;
	}
	
	private List<Request> getRequestsFromDatabase(SearchParams params){
		try {
			Double reqFromAmount = null;
			Double reqToAmount = null;
			
			String queryMoreThan1Crore = null;
			Double amountMoreThan1Crore = null;
			if(params.getMoreThan1Crore()) {
				//reqToAmount = 10000000d;
				amountMoreThan1Crore = 10000000d;
				queryMoreThan1Crore = "(req.approxValueFixed > :amountMoreThan1Crore)";
			}
			
			String queryBetween1And10Lacs = null;
			Double amount1Lac = null;
			Double amount10Lacs = null;
			if(params.getBetween1And10Lacs()){
				//reqFromAmount = 100000d;
				//reqToAmount = 1000000d;
				amount1Lac = 100000d;
				amount10Lacs = 1000000d;
				queryBetween1And10Lacs = "(req.approxValueFixed BETWEEN :amount1Lac AND :amount10Lacs)";
			}
			
			String queryBetween10LacsAnd1Crore = null;
			Double amount10Lacs2 = null;
			Double amount1Crore = null;
			if(params.getBetween10LacsAnd1Crore()){
				//reqFromAmount = 1000000d;
				//reqToAmount = 10000000d;
				amount10Lacs2 = 1000000d;
				amount1Crore = 10000000d;
				queryBetween10LacsAnd1Crore = "(req.approxValueFixed BETWEEN :amount10Lacs2 AND :amount1Crore)";
			}
			
			Date fromDate = convertToDate(params.getFromDate());
			Date toDate = convertToDate(params.getToDate());
			
			Double turnOverAmount = null;
			String queryTurnoverGreaterThan100Crore = null;
			Double amountTurnoverGreaterThan100Crore = null;
			if(params.getTurnoverGreaterThan100Crore()) {
				//turnOverAmount = 1000000000d;
				queryTurnoverGreaterThan100Crore = "(req.minimumTurnOverRequiredText > :amountTurnoverGreaterThan100Crore)";
				amountTurnoverGreaterThan100Crore = 1000000000d;
			}
			
			String queryTurnoverGreaterThan10Crore = null;
			Double amountTurnoverGreaterThan10Crore = null;
			if(params.getTurnoverGreaterThan10Crore()) {
				//turnOverAmount = 100000000d;
				queryTurnoverGreaterThan100Crore = "(req.minimumTurnOverRequiredText > :amountTurnoverGreaterThan10Crore)";
				amountTurnoverGreaterThan10Crore = 100000000d;
			}
			
			String queryTurnoverGreaterThan1Crore = null;
			Double amountTurnoverGreaterThan1Crore = null;
			if(params.getTurnoverGreaterThan1Crore()) {
				//turnOverAmount = 10000000d;
				queryTurnoverGreaterThan100Crore = "(req.minimumTurnOverRequiredText > :amountTurnoverGreaterThan1Crore)";
				amountTurnoverGreaterThan1Crore = 10000000d;
			}
			
			String query = null;
			boolean valQuery = false;
			boolean fromAmnt = false;
			boolean toAmt = false;
			boolean fromDt = false;
			boolean toDt = false;
			boolean turnoverReqd = false;
			
//			if(reqFromAmount != null && reqToAmount != null) {
//				//req amount in range
//				query = "select req from Request req where req.approxValueFixed BETWEEN :approxValueFixed1 AND :approxValueFixed2";
//				valQuery = true;
//				fromAmnt = true;
//				toAmt = true;
//			}
//			else if(reqFromAmount == null && reqToAmount != null) {
//				//req amount > value
//				query = "select req from Request req where req.approxValueFixed > :approxValueFixed2";
//				valQuery = true;
//				toAmt = true;
//			}
//			else {
//				//no req amount query
//			}
			
			if(queryMoreThan1Crore != null) {
				valQuery = true;
				query = "select req from Request req where "+queryMoreThan1Crore;
				
			}
			
			if(queryBetween1And10Lacs != null) {
				valQuery = true;
				if(query != null) {
					query = query + " OR "+queryBetween1And10Lacs;
				}
				else {
					query = "select req from Request req where "+queryBetween1And10Lacs;
				}
			}
			
			if(queryBetween10LacsAnd1Crore != null) {
				valQuery = true;
				if(query != null) {
					query = query + " OR "+queryBetween10LacsAnd1Crore;
				}
				else {
					query = "select req from Request req where "+queryBetween10LacsAnd1Crore;
				}
			}
			
			if(valQuery) {
				if(fromDate != null && toDate != null) {
					query += " AND req.createdDate BETWEEN :date1 AND :date2";
					fromDt = true;
					toDt = true;
				}
			}
			else {
				if(fromDate != null && toDate != null) {
					query = "select req from Request req where req.createdDate BETWEEN :date1 AND :date2";
					fromDt = true;
					toDt = true;
					valQuery = true;
				}
				
			}
			
//			if(valQuery) {
//				if(turnOverAmount != null) {
//					query += " AND req.minimumTurnOverRequiredText > :turnOverAmount";
//					turnoverReqd = true;
//				}
//			}
//			else {
//				if(turnOverAmount != null) {
//					query = "select req from Request req where req.minimumTurnOverRequiredText > :turnOverAmount";
//					turnoverReqd = true;
//				}
//				
//			}
			
			String turnOverQry = null;
				if(queryTurnoverGreaterThan100Crore != null) {
				
					turnOverQry = queryTurnoverGreaterThan100Crore;
				}
				
				if(queryTurnoverGreaterThan10Crore != null) {
				
					if(turnOverQry == null) {
						turnOverQry = queryTurnoverGreaterThan10Crore;
					}
					else {
						turnOverQry += " OR "+ queryTurnoverGreaterThan10Crore;
					}
				}
				
				if(queryTurnoverGreaterThan1Crore != null) {
				
					if(turnOverQry == null) {
						turnOverQry = queryTurnoverGreaterThan1Crore;
					}
					else {
						turnOverQry += " OR "+ queryTurnoverGreaterThan1Crore;
					}
				}
			
			if(valQuery) {
				if(turnOverQry != null) {
					query += " AND ( "+turnOverQry+" )";
				}
				
			}
			else {
				if(turnOverQry != null) {
					query = "select req from Request req where "+turnOverQry;
				}
			}
			
			
			if(query == null || query.trim().length() == 0) {
					throw new Twenty20Exception("NO_SEARCH_PARAMS_SELECTED");
					//return null;
				}
			
			javax.persistence.Query query2 = getEntityManager().createQuery(query);
//				if(fromAmnt) {
//					query2 = query2.setParameter("approxValueFixed1", reqFromAmount);
//				}
//				if(toAmt) {
//					query2 = query2.setParameter("approxValueFixed2", reqToAmount);
//				}
//				if(fromDt) {
//					query2 = query2.setParameter("date1", fromDate);
//				}
//				if(toDt) {
//					query2 = query2.setParameter("date2", toDate);
//				}
//				if(turnoverReqd) {
//					query2 = query2.setParameter("turnOverAmount", turnOverAmount);
//				}
			
				if(amountMoreThan1Crore != null) {
					query2 = query2.setParameter("amountMoreThan1Crore", amountMoreThan1Crore);
				}
				
				if(amount1Lac != null) {
					query2 = query2.setParameter("amount1Lac", amount1Lac);
				}
				
				if(amount10Lacs != null) {
					query2 = query2.setParameter("amount10Lacs", amount10Lacs);
				}
				
				if(amount10Lacs2 != null) {
					query2 = query2.setParameter("amount10Lacs2", amount10Lacs2);
				}
				
				if(amount1Crore != null) {
					query2 = query2.setParameter("amount1Crore", amount1Crore);
				}
				
				if(fromDate != null) {
					query2 = query2.setParameter("date1", fromDate);
				}
				
				if(toDate != null) {
					query2 = query2.setParameter("date2", toDate);
				}
				
				if(amountTurnoverGreaterThan100Crore != null) {
					query2 = query2.setParameter("amountTurnoverGreaterThan100Crore", amountTurnoverGreaterThan100Crore);
				}
				
				if(amountTurnoverGreaterThan10Crore != null) {
					query2 = query2.setParameter("amountTurnoverGreaterThan10Crore", amountTurnoverGreaterThan10Crore);
				}
				
				if(amountTurnoverGreaterThan1Crore != null) {
					query2 = query2.setParameter("amountTurnoverGreaterThan1Crore", amountTurnoverGreaterThan1Crore);
				}
				
				
				List<Request> requests = query2.getResultList();
			return requests;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new Twenty20Exception("INVALID_DATES_PASSED");
		}
	}
	
	private Date convertToDate(String date) throws ParseException {
			if(date == null || date.trim().length() == 0) {
				return null;
			}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date d = formatter.parse(date);
		return d;
	}
	
	private List<Request> getRequestFromLucene(SearchParams params){
		FullTextEntityManager fullTextEm = Search.getFullTextEntityManager(getEntityManager());
		EntityContext context = fullTextEm.getSearchFactory().buildQueryBuilder().forEntity(Request.class);
		QueryBuilder builder = context.get();
					
			BooleanJunction bool = builder.bool();
			BooleanQuery.Builder q1 = new BooleanQuery.Builder();
			//q1.setMinimumNumberShouldMatch(1);
				if(params.getCement()) {
					org.apache.lucene.search.Query q = builder.keyword().onField("requestType").matching("Cement").createQuery();
					q1.add(q, Occur.SHOULD);
				}
				if(params.getStoneChips()) {
					org.apache.lucene.search.Query q = builder.keyword().onField("requestType").matching("Stone Chips").createQuery();
					q1.add(q, Occur.SHOULD);
				}
				if(params.getSand()) {
					org.apache.lucene.search.Query q = builder.keyword().onField("requestType").matching("Sand").createQuery();
					q1.add(q, Occur.SHOULD);
				}
				if(params.getReadyMix()) {
					org.apache.lucene.search.Query q = builder.keyword().onField("requestType").matching("Ready Mix").createQuery();
					q1.add(q, Occur.SHOULD);
				}
				if(params.getSteelBar()) {
					org.apache.lucene.search.Query q = builder.keyword().onField("requestType").matching("Steel ReBar").createQuery();
					q1.add(q, Occur.SHOULD);
				}
				if(params.getFormWork()) {
					org.apache.lucene.search.Query q = builder.keyword().onField("requestType").matching("Formwork").createQuery();
					q1.add(q, Occur.SHOULD);
				}
				
				//builder.
				org.apache.lucene.search.Query q = bool.should(q1.build()).createQuery();
			System.out.println(q.toString());
			List<Request> reqs = fullTextEm.createFullTextQuery(q, Request.class).getResultList();
				
				
			return reqs;
	}
	
	
}