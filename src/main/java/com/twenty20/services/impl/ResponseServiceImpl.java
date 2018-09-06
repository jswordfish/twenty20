package com.twenty20.services.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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

import org.apache.commons.io.FileUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import com.twenty20.common.Twenty20Exception;
import com.twenty20.dao.JPADAO;
import com.twenty20.dao.ResponseDao;
import com.twenty20.domain.Response;
import com.twenty20.domain.ResponseStatus;
import com.twenty20.domain.User;
import com.twenty20.services.RequestService;
import com.twenty20.services.ResponseService;
import com.twenty20.services.UserService;
import com.twenty20.util.ConfUtil;
import com.twenty20.util.EmailGeneralUtil;

@Service("responseService")
@org.springframework.transaction.annotation.Transactional(propagation= Propagation.REQUIRED, rollbackFor=Twenty20Exception.class)
public class ResponseServiceImpl extends BaseServiceImpl<Long, Response> implements ResponseService{
	@Autowired
    protected ResponseDao dao;
	
//	ValidatorFactor
	@Autowired
	ConfUtil confService;
	
	@Autowired
	RequestService requestService;
	
	@Autowired
	UserService userService;
	
	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	Validator validator = factory.getValidator();

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
	public void saveOrUpdate(Response response) throws Twenty20Exception {
		Set<ConstraintViolation<Response>> violations = validator.validate(response);
		if(violations.size() > 0){
			throw new Twenty20Exception("MISSING_MANDATORY_PARAMS");
		}
		
		response = saveAndGenerateAddDocUrl(response);
		Response response2 = getResponse(response.getRequestName(), response.getBuyer(), response.getBuyerCompany(), response.getSupplier(), response.getSupplierCompany());
		if(response2 == null) {
			//create
			response.setCreatedBy(response.getSupplier());
			response.setCreatedDate(new Date());
			response.setLastModifiedDate(new Date());
			dao.persist(response);
		}
		else {
			//update
			if(response.getCreatedDate() == null) {
				response.setCreatedDate(new Date());
			}
			
			response.setLastModifiedDate(new Date());
			
			response.setId(response2.getId());
			org.dozer.Mapper mapper = new DozerBeanMapper();
			mapper.map(response, response2);
			response2.setResponseToRequestDescriptions(response.getResponseToRequestDescriptions());
			dao.merge(response2);
		}
	}

	@Override
	public Response getResponse(String requestName, String buyer, String buyerCompany, String supplier,
			String supplierCompany) throws Twenty20Exception {
		//Company.getUniqueCompany
				Map<String, String> queryParams = new HashMap<String, String>();
				queryParams.put("buyerCompany", buyerCompany);
				queryParams.put("requestName", requestName);
				queryParams.put("buyer", buyer);
				queryParams.put("supplier", supplier);
				queryParams.put("supplierCompany", supplierCompany);
				
				List<Response> responses = findByNamedQueryAndNamedParams(
						"Response.getUniqueResponse", queryParams);
				if(responses.size() > 1){
					throw new Twenty20Exception("MORE_THAN_ONE_RESPONSES_FOR_SAME_REQUEST");
				}
				if(responses.size() == 0){
					return null;
				}
				return responses.get(0);
	}
	
	private Response saveAndGenerateAddDocUrl(Response	response){
    	try {
    		File responsesDir = new File(confService.getDocumentsLocation()+File.separator+"Responses");
    		if(!(responsesDir.exists() && responsesDir.isDirectory())) {
    			responsesDir.mkdir();
    		}
    		
    		File newResponseLoc = new File(responsesDir.getAbsolutePath()+File.separator+response.getSupplierCompany()+File.separator+response.getSupplier());
    		if(!(newResponseLoc.exists() && newResponseLoc.isDirectory())) {
    			newResponseLoc.mkdirs();
    		}
    		
			if(response.getAdditionalDocument() != null && response.getAdditionalDocumentExtension()!=  null && response.getAdditionalDocumentExtension().trim().length()!= 0){
				FileUtils.writeByteArrayToFile(new File(newResponseLoc.getAbsolutePath()+File.separator+"Response-"+response.getRequestName()+"."+response.getAdditionalDocumentExtension()), response.getAdditionalDocument());
				response.setAdditionalDocumentUrl(confService.getDocumentsServerBaseUrl()+"Responses/"+response.getSupplierCompany()+"/"+response.getSupplier()+"/"+"Response-"+response.getRequestName()+"."+response.getAdditionalDocumentExtension());
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new Twenty20Exception("CAN_NOT_CREATE_RESPONSE_FILE", e);
		}
    	return response;
    }

	@Override
	public void remove(long id) throws Twenty20Exception {
		// TODO Auto-generated method stub
		super.delete(id);
	}

	@Override
	public List<Response> getResponsesByCompany(String suplierCompany) throws Twenty20Exception {
		Map<String, String> queryParams = new HashMap<String, String>();
		
		queryParams.put("supplierCompany", suplierCompany);
		
		List<Response> responses = findByNamedQueryAndNamedParams(
				"Response.getResponseByCompany", queryParams);
		return responses;
	}

	@Override
	public List<Response> getResponsesBySupplierNameAndCompany(String supplierName, String suplierCompany)
			throws Twenty20Exception {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("supplier", suplierCompany);
		queryParams.put("supplierCompany", suplierCompany);
		
		List<Response> responses = findByNamedQueryAndNamedParams(
				"Response.getResponseBySupplierNameAndCompany", queryParams);
		return responses;
	}

	@Override
	public List<Response> getResponsesForRequentNameAndBuyerCompany(String requestName, String buyerCompany)
			throws Twenty20Exception {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("requestName", requestName);
		queryParams.put("buyerCompany", buyerCompany);
		
		List<Response> responses = findByNamedQueryAndNamedParams(
				"Response.getResponsesForRequentNameAndBuyerCompany", queryParams);
		return responses;
	}

	@Override
	public void acceptResponse(Response response) throws Twenty20Exception {
		// TODO Auto-generated method stub
		response.setResponseStatus(ResponseStatus.ACCEPT.getStatus());
		saveOrUpdate(response);
		List<Response> responses = getResponsesForRequentNameAndBuyerCompany(response.getRequestName(), response.getBuyerCompany());
		String subject = response.getRequestName()+" from "+response.getBuyerCompany()+" no longer available";
		String message = rejectHtml;
		String to[] = {"jatin.sutaria@gmail.com"};
		String bcc[] = new String[responses.size() -1];
		List<String> bccc = new ArrayList<>();
			for(int i=0;i<responses.size();i++) {
				Response res = responses.get(i);
					if(!res.getSupplierEmail().equalsIgnoreCase(response.getSupplierEmail())) {
						bccc.add(res.getSupplierEmail());
					}
			}
			bcc = bccc.toArray(bcc);
		Thread th = new Thread(new EmailGeneralUtil(to, null, bcc, subject, message));
		th.start();
		
		subject =  "Yor response to "+response.getRequestName()+" from "+response.getBuyerCompany()+" has been ACCEPTED";
		String to1[] = {"jatin.sutaria@gmail.com"};
		String bcc1[] = {response.getSupplierEmail()};
		Thread th1 = new Thread(new EmailGeneralUtil(to1, null, bcc1, subject, acceptHtml));
		th1.start();
		//requestService.g
	}

	private static String rejectHtml = "<p>Please note that the above referenced Buyer Request has been closed by the Buyer, and unfortunately you have not been successful at this time.  </p>\r\n" + 
			"<p>This could be because they Buyer has selected another offer, or because the requirement has changed. </p>\r\n" + 
			"<p>There are many Requests on the Platform that you can respond to, and we encourage you to keep responding (it is free) to grow your sales. </p>\r\n" + 
			"<p>Please note that if you want to - you can improve your Platform Offer [Master Rebate] to make it more attractive. So please continue using TWENTY20 - a better way to buy & sell.</p>\r\n" + 
			"<p>Thanks and Regards,</p>\r\n" + 
			"<p>Twenty20 Admin</p>\r\n" + 
			"<p>&nbsp;</p>\r\n" + 
			"<p>&nbsp;</p>\r\n" + 
			"<p>&nbsp;</p>\r\n" + 
			"<p>&nbsp;</p>";
	
	private static String acceptHtml = "<p>Congratulations! Your offer for the above referenced Buyer Request has been accepted by the Buyer. </p>\r\n" + 
			"<p>Please proceed to supply as per the requirements of the Buyer - the platform will capture supplies based on information which will be provided by the Buyer and allocated against your Platform Offer. </p>\r\n" + 
			"<p>Succesful execution of the deal will open up a wealth of oippurtunities on the platform</p>\r\n" + 
			"<p>Win more orders by responding to more Requests and grow your business with us. So please continue using TWENTY20 - a better way to buy & sell</p>\r\n" + 
			"<p>Thanks and Regards,</p>\r\n" + 
			"<p>Twenty20 Admin</p>\r\n" + 
			"<p>&nbsp;</p>\r\n" + 
			"<p>&nbsp;</p>\r\n" + 
			"<p>&nbsp;</p>\r\n" + 
			"<p>&nbsp;</p>";
	
	private static String negotiateHtml = "<p>Congratulations! The Buyer for the above referenced Buyer Request has short-listed you offer, and wants to negotiate to finalise a deal. </p>\r\n" + 
			"<p>You may expect the Buyer to contact you at any time to discuss further, or you may reach out to him through the following contact details [&BUYER_NAME& / &BUYER_EMAIL&] </p>\r\n" + 
			"<p>Succesful execution of the deal will open up a wealth of oippurtunities on the platform</p>\r\n" + 
			"<p>Best of luck! Don’t forget to keep responding to more Requests and grow your business with us. So please continue using TWENTY20 - a better way to buy & sell.</p>\r\n" + 
			"<p>Thanks and Regards,</p>\r\n" + 
			"<p>Twenty20 Admin</p>\r\n" + 
			"<p>&nbsp;</p>\r\n" + 
			"<p>&nbsp;</p>\r\n" + 
			"<p>&nbsp;</p>\r\n" + 
			"<p>&nbsp;</p>";
	
	public void negotiateResponse(Response response) {
		// TODO Auto-generated method stub
				response.setResponseStatus(ResponseStatus.NEGOTIATE.getStatus());
				saveOrUpdate(response);
				List<Response> responses = getResponsesForRequentNameAndBuyerCompany(response.getRequestName(), response.getBuyerCompany());
				String subject = "Selected for Negotiation for - "+response.getRequestName()+" from "+response.getBuyerCompany();
				
				User user = userService.getUniqueUser(response.getBuyer());
				String buyerEmail = user.getEmail();
				String buyerName = user.getFirstName()+" "+user.getLastName();
				String message = negotiateHtml;
				message = message.replace("&BUYER_NAME&", buyerName);
				message = message.replace("&BUYER_EMAIL&", buyerEmail);
				String to[] = {"jatin.sutaria@gmail.com"};
				String bcc[] = {response.getSupplierEmail()};
				
				Thread th = new Thread(new EmailGeneralUtil(to, null, bcc, subject, message));
				th.start();
			
	}
	
	//private static accept2 = ""

	@Override
	public void rejectResponse(Response response) throws Twenty20Exception {
		// TODO Auto-generated method stub
		response.setResponseStatus(ResponseStatus.DECLINE.getStatus());
		saveOrUpdate(response);
		
		String subject = response.getRequestName()+" from "+response.getBuyerCompany()+" no longer available";
		String message = rejectHtml;
		String to[] = {"jatin.sutaria@gmail.com"};
		String bcc[] = {response.getSupplierEmail()};
			
		Thread th = new Thread(new EmailGeneralUtil(to, null, bcc, subject, message));
		th.start();
	}

	@Override
	public List<Response> getAcceptedResponses(String buyerCompany) {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("buyerCompany", buyerCompany);
		
		List<Response> responses = findByNamedQueryAndNamedParams(
				"Response.getAcceptedResponsesForBuyer", queryParams);
		return responses;
	}
	
}
