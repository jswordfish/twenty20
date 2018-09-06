package com.twenty20.util;

import com.twenty20.domain.Rebate;
import com.twenty20.domain.Response;
import com.twenty20.jsf.managedBeans.SpringUtil;
import com.twenty20.jsf.reports.model.RebateModel;
import com.twenty20.services.RebateService;

public class Util {

	
	public static RebateModel construct(Double exampleAmount, Response response) {
		RebateModel model = new RebateModel();
		model.setSupplierCompany(response.getSupplierCompany());
		model.setTransactionAmount(exampleAmount);
		if(response.getRebate() == null) {
			RebateService rebateService = SpringUtil.getService(RebateService.class);
			Rebate rebate = rebateService.getUniqueRebateByNameAndCompany(response.getRebateName(), response.getSupplierCompany());
			response.setRebate(rebate);
			
		}
		
		if(exampleAmount > response.getRebate().getBaseOfferSalesValue() && exampleAmount <= response.getRebate().getTier1OfferSalesValue()) {
			model.setSupplierRebateTierPercent(""+response.getRebate().getBaseOfferRebateOfferInPercent());
			model.setSavings(exampleAmount * response.getRebate().getBaseOfferRebateOfferInPercent() / 100);
			model.setSupplierRebateTierLevel("Base Tier");
		}
		else if(exampleAmount > response.getRebate().getTier1OfferSalesValue() && exampleAmount <= response.getRebate().getTier2OfferSalesValue()) {
			model.setSupplierRebateTierPercent(""+response.getRebate().getTier1OfferRebateOfferInPercent());
			model.setSavings(exampleAmount * response.getRebate().getTier1OfferRebateOfferInPercent() / 100);
			model.setSupplierRebateTierLevel("Tier 1");
		}
		else if(exampleAmount > response.getRebate().getTier2OfferSalesValue() && exampleAmount <= response.getRebate().getTier3OfferSalesValue()) {
			model.setSupplierRebateTierPercent(""+response.getRebate().getTier2OfferRebateOfferInPercent());
			model.setSavings(exampleAmount * response.getRebate().getTier2OfferRebateOfferInPercent() / 100);
			model.setSupplierRebateTierLevel("Tier 2");
		}
		else {
			model.setSupplierRebateTierPercent(""+response.getRebate().getTier3OfferRebateOfferInPercent());
			model.setSavings(exampleAmount * response.getRebate().getTier3OfferRebateOfferInPercent() / 100);
			model.setSupplierRebateTierLevel("Tier3");
		}
		
		
		return model;
	}
}
