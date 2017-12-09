package com.twenty20.services.impl;

import java.io.File;
import java.io.IOException;
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
import com.twenty20.services.ResponseService;
import com.twenty20.util.ConfUtil;

@Service("responseService")
@org.springframework.transaction.annotation.Transactional(propagation= Propagation.REQUIRED, rollbackFor=Twenty20Exception.class)
public class ResponseServiceImpl extends BaseServiceImpl<Long, Response> implements ResponseService{
	@Autowired
    protected ResponseDao dao;
	
//	ValidatorFactor
	@Autowired
	ConfUtil confService;
	
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
				response.setAdditionalDocumentUrl(confService.getDocumentsServerBaseUrl()+File.separator+"Responses"+File.separator+response.getSupplierCompany()+File.separator+response.getSupplier()+File.separator+"Response-"+response.getRequestName()+"."+response.getAdditionalDocumentExtension());
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

	
}
