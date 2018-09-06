package com.twenty20.services.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import com.twenty20.common.Twenty20Exception;
import com.twenty20.dao.JPADAO;
import com.twenty20.dao.RebateClaimDao;
import com.twenty20.domain.RebateClaim;
import com.twenty20.domain.User;
import com.twenty20.services.CompanyService;
import com.twenty20.services.RebateClaimService;
import com.twenty20.services.ResponseService;
import com.twenty20.services.UserService;
import com.twenty20.util.EmailGeneralUtil;

@Service("rebateClaimService")
@org.springframework.transaction.annotation.Transactional(propagation= Propagation.REQUIRED, rollbackFor=Twenty20Exception.class)
public class RebateClaimServiceImpl extends BaseServiceImpl<Long, RebateClaim> implements RebateClaimService{
	@Autowired
    protected RebateClaimDao dao;
	
	@Autowired
	protected CompanyService companyService;
	
	@Autowired
	ResponseService responseService;
	

	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	Validator validator = factory.getValidator();
	
	@Autowired
	UserService userService;

	@PostConstruct
    public void init() throws Exception {
	 super.setDAO((JPADAO) dao);
    }
    
    @PreDestroy
    public void destroy() {
    }
    
    @Override
    public void setEntityManagerOnDao(EntityManager entityManager){
    	dao.setEntityManager(entityManager);
    }

	@Override
	public void saveOrUpdate(RebateClaim claim) throws Twenty20Exception {
		Set<ConstraintViolation<RebateClaim>> violations = validator.validate(claim);
		if(violations.size() > 0){
			throw new Twenty20Exception("INVALID_REBATECLAIM_PARAMS");
		}
		//rebate.setRebateName(rebate.getSupplier()+"-Main-Rebate");
		String subject = "";
		RebateClaim claim2 = null;
			if(claim.getId() != null) {
				
				
				claim2 = dao.findById(claim.getId());
			}
		String message = "";
		
		if(claim2 == null){
			//create mode
			message = claimSubmitted;
			subject = "Rebate Claim submitted by "+claim.getBuyerCompany();
			dao.persist(claim);
		}
		else{
			if(claim2.getAccept()) {
				return ;
			}
			message = claimModified;
			subject = "Rebate Claim modified by "+claim.getBuyerCompany();
			org.dozer.Mapper mapper = new DozerBeanMapper();
			mapper.map(claim, claim2);
			dao.merge(claim2);
		}
		message = message.replace("&SUPPLIER&", claim.getResponse().getSupplier());
		message = message.replace("&BUYER&", claim.getResponse().getBuyer());
		message = message.replace("&REQUESTNAME&", claim.getResponse().getRequestName());
		message = message.replace("&INVOICENO&", claim.getInvoiceNo());
		message = message.replace("&INVOICEDATE&", claim.getInvDate());
		message = message.replace("&INVOICEAMOUNT&", new BigDecimal(claim.getInvoiceAmount()).toPlainString());
		
		String to[] = {claim.getResponse().getSupplierEmail()};
		String bcc[] = {"jatin.sutaria@thev2technologies.com"};
		
		Thread th = new Thread(new EmailGeneralUtil(to, null, bcc, subject, message));
		th.start();
	}

	@Override
	public RebateClaim getUniqueRebateClaimByNameAndCompany(String buyerCompany, String requestName ,String suplierCompany) throws Twenty20Exception{
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("requestName", requestName);
		queryParams.put("buyerCompany", buyerCompany);
		queryParams.put("supplierCompany", suplierCompany);
		
		List<RebateClaim> claims = findByNamedQueryAndNamedParams(
				"RebateClaim.getClaimByBuyerSupplierAndRequest", queryParams);
		if(claims.size() > 1){
			throw new Twenty20Exception("MULTIPLE_REBATES_CLAIMS_SAME_IDENTITY");
		}
		if(claims.size() == 0){
			return null;
		}
		return claims.get(0);
	}

	@Override
	public List<RebateClaim> getRebateClaims(String buyerCompany) throws Twenty20Exception {
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("buyerCompany", buyerCompany);
		
		List<RebateClaim> rebates = findByNamedQueryAndNamedParams(
				"RebateClaim.getClaimByBuyerCompany", queryParams);
		return rebates;
	}

	@Override
	public List<RebateClaim> getRebateClaimsForSupplier(String supplierCompany) throws Twenty20Exception {
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("supplierCompany", supplierCompany);
		
		List<RebateClaim> rebates = findByNamedQueryAndNamedParams(
				"RebateClaim.getClaimBySupplierCompany", queryParams);
		return rebates;
	}

	

	@Override
	public void deleteById(Long rebateClaimId) throws Twenty20Exception {
		// TODO Auto-generated method stub
		super.delete(rebateClaimId);
	}

	private static String claimSubmitted = "<p>Hello &SUPPLIER&! The Buyer - &BUYER& has submitted a rebate claim for the request - &REQUESTNAME&. </p>\r\n" + 
			"<p>Claim details are -  </p>\r\n" + 
			"<p>Invoice No - &INVOICENO&</p>\r\n" + 
			"<p>Invoice Date - &INVOICEDATE&</p>\r\n" + 
			"<p>Invoice Amount - &INVOICEAMOUNT&</p>\r\n" + 
			"<p>Thanks and Regards,</p>\r\n" + 
			"<p>Twenty20 Admin</p>\r\n" + 
			"<p>&nbsp;</p>\r\n" + 
			"<p>&nbsp;</p>\r\n" + 
			"<p>&nbsp;</p>\r\n" + 
			"<p>&nbsp;</p>";
	
	private static String claimModified = "<p>Hello &SUPPLIER&! The Buyer - &BUYER& has modified a rebate claim for the request - &REQUESTNAME&. </p>\r\n" + 
			"<p>Claim details are -  </p>\r\n" + 
			"<p>Invoice No - &INVOICENO&</p>\r\n" + 
			"<p>Invoice Date - &INVOICEDATE&</p>\r\n" + 
			"<p>Invoice Amount - &INVOICEAMOUNT&</p>\r\n" + 
			"<p>Thanks and Regards,</p>\r\n" + 
			"<p>Twenty20 Admin</p>\r\n" + 
			"<p>&nbsp;</p>\r\n" + 
			"<p>&nbsp;</p>\r\n" + 
			"<p>&nbsp;</p>\r\n" + 
			"<p>&nbsp;</p>";
	
	private static String ACCEPT_CLAIM="<p>Hello &BUYER&! The Supplier - &SUPPLIER& has accepted your rebate claim for the request - &REQUESTNAME&. </p>\r\n" + 
			"<p>Claim details are -  </p>\r\n" + 
			"<p>Invoice No - &INVOICENO&</p>\r\n" + 
			"<p>Invoice Date - &INVOICEDATE&</p>\r\n" + 
			"<p>Invoice Amount - &INVOICEAMOUNT&</p>\r\n" + 
			"<p>Note - &NOTE&</p>\r\n" + 
			"<p>Thanks and Regards,</p>\r\n" + 
			"<p>Twenty20 Admin</p>\r\n" + 
			"<p>&nbsp;</p>\r\n" + 
			"<p>&nbsp;</p>\r\n" + 
			"<p>&nbsp;</p>\r\n" + 
			"<p>&nbsp;</p>";
	
	private static String REJECT_CLAIM="<p>Hello &BUYER&! The Supplier - &SUPPLIER& has rejected your rebate claim for the request - &REQUESTNAME&. </p>\r\n" + 
			"<p>Claim details are -  </p>\r\n" + 
			"<p>Invoice No - &INVOICENO&</p>\r\n" + 
			"<p>Invoice Date - &INVOICEDATE&</p>\r\n" + 
			"<p>Invoice Amount - &INVOICEAMOUNT&</p>\r\n" + 
			"<p>Note - &NOTE&</p>\r\n" + 
			"<p>Thanks and Regards,</p>\r\n" + 
			"<p>Twenty20 Admin</p>\r\n" + 
			"<p>&nbsp;</p>\r\n" + 
			"<p>&nbsp;</p>\r\n" + 
			"<p>&nbsp;</p>\r\n" + 
			"<p>&nbsp;</p>";

	@Override
	public void accept(RebateClaim claim) throws Twenty20Exception {
		// TODO Auto-generated method stub
		RebateClaim claim2 = dao.findById(claim.getId());
		claim2.setAccept(true);
		dao.merge(claim2);
		String message = ACCEPT_CLAIM;
		message = message.replace("&SUPPLIER&", claim.getResponse().getSupplier());
		message = message.replace("&BUYER&", claim.getResponse().getBuyer());
		message = message.replace("&REQUESTNAME&", claim.getResponse().getRequestName());
		message = message.replace("&INVOICENO&", claim.getInvoiceNo());
		message = message.replace("&INVOICEDATE&", claim.getInvDate());
		message = message.replace("&INVOICEAMOUNT&", new BigDecimal(claim.getInvoiceAmount()).toPlainString());
		message = message.replace("&NOTE&", claim.getNote()==null?"":claim.getNote());
		User b = userService.getUniqueUser(claim.getResponse().getBuyer());
		String to[] = {b.getEmail()};
		String bcc[] = {"jatin.sutaria@thev2technologies.com"};
		String subject = "Congratulations! Your rebate claim has been accepted";
		
		Thread th = new Thread(new EmailGeneralUtil(to, null, bcc, subject, message));
		th.start();
	}

	@Override
	public void reject(RebateClaim claim) throws Twenty20Exception {
		// TODO Auto-generated method stub
		RebateClaim claim2 = dao.findById(claim.getId());
		claim2.setAccept(false);
		dao.merge(claim2);
		String message = REJECT_CLAIM;
		message = message.replace("&SUPPLIER&", claim.getResponse().getSupplier());
		message = message.replace("&BUYER&", claim.getResponse().getBuyer());
		message = message.replace("&REQUESTNAME&", claim.getResponse().getRequestName());
		message = message.replace("&INVOICENO&", claim.getInvoiceNo());
		message = message.replace("&INVOICEDATE&", claim.getInvDate());
		message = message.replace("&INVOICEAMOUNT&", new BigDecimal(claim.getInvoiceAmount()).toPlainString());
		message = message.replace("&NOTE&", claim.getNote()==null?"":claim.getNote());
		
		User b = userService.getUniqueUser(claim.getResponse().getBuyer());
		String to[] = {b.getEmail()};
		String bcc[] = {"jatin.sutaria@thev2technologies.com"};
		String subject = "Sorry! Your rebate claim has been rejected";
		
		Thread th = new Thread(new EmailGeneralUtil(to, null, bcc, subject, message));
		th.start();
	}
}
