package com.twenty20.jsf.charts.barchart.model;

import java.util.ArrayList;
import java.util.List;

import com.twenty20.domain.Rebate;
import com.twenty20.domain.Response;import com.twenty20.domain.ResponseToRequestDescription;
import com.twenty20.jsf.charts.piechart.model.LabelValue;
import com.twenty20.jsf.managedBeans.SpringUtil;
import com.twenty20.jsf.reports.model.RebateModel;
import com.twenty20.services.RebateService;
import com.twenty20.util.Util;

public class RebatesChart {

	
	List<Response> responsesToRequest = new ArrayList<>();
	
	BarChartBean barChartBean;
	
	String title;

	public List<Response> getResponsesToRequest() {
		return responsesToRequest;
	}

	public void setResponsesToRequest(List<Response> responsesToRequest) {
		this.responsesToRequest = responsesToRequest;
	}
	
	public RebatesChart() {
		//init("Not Titled");
	}
	
	public RebatesChart(List<Response> responsesToRequest, String title) {
		this.responsesToRequest = responsesToRequest;
		init(title);
	}
	
	public void initForExampleSavings() {
		barChartBean = new BarChartBean();
		barChartBean.setTitle(title);
		barChartBean.setLegendPosition("ne");
		barChartBean.setxAxisLabel("Your Transaction Amounts(INR)");
		barChartBean.setyAxisLabel("Your Savings(INR)");
		
		List<ChartSeriesData> chartSeriesDatas = new ArrayList<>();
			for(Response response : responsesToRequest) {
				ChartSeriesData drl = new ChartSeriesData();
				drl.setLabel(response.getSupplierCompany());
				RebateModel model1 = Util.construct(1000000d, response);
				RebateModel model2 = Util.construct(3000000d, response);
				RebateModel model3 = Util.construct(7000000d, response);
				RebateModel model4 = Util.construct(10000000d, response);
				RebateModel model5 = Util.construct(15000000d, response);
				RebateModel model6 = Util.construct(19000000d, response);
				RebateModel model7 = Util.construct(25000000d, response);
				RebateModel model8 = Util.construct(30000000d, response);
				RebateModel model9 = Util.construct(40000000d, response);
				RebateModel model10 = Util.construct(50000000d, response);
				
				
				LabelValue labelValue1 = new LabelValue("10,00,000", model1.getSavings());
				LabelValue labelValue2 = new LabelValue("30,00,000", model2.getSavings());
				LabelValue labelValue3 = new LabelValue("70,00,000", model3.getSavings());
				LabelValue labelValue4 = new LabelValue("1,00,00,000", model4.getSavings());
				
				LabelValue labelValue5 = new LabelValue("1,50,00,000", model5.getSavings());
				LabelValue labelValue6 = new LabelValue("1,90,00,000", model6.getSavings());
				LabelValue labelValue7 = new LabelValue("2,50,00,000", model7.getSavings());
				LabelValue labelValue8 = new LabelValue("3,00,00,000", model8.getSavings());
				
				LabelValue labelValue9 = new LabelValue("4,00,00,000", model9.getSavings());
				LabelValue labelValue10 = new LabelValue("5,00,00,000", model10.getSavings());
				
				List<LabelValue> drlValues = new ArrayList<>();
				drlValues.add(labelValue1);
				drlValues.add(labelValue2);
				drlValues.add(labelValue3);
				drlValues.add(labelValue4);
				drlValues.add(labelValue5);
				drlValues.add(labelValue6);
				drlValues.add(labelValue7);
				drlValues.add(labelValue8);
				drlValues.add(labelValue9);
				drlValues.add(labelValue10);
				
				drl.setLabelValues(drlValues);
				chartSeriesDatas.add(drl);
			}
			barChartBean.setChartSeriesDatas(chartSeriesDatas);
			barChartBean.init();
	}
	
	public void initForValues() {
		barChartBean = new BarChartBean();
		barChartBean.setTitle(title);
		barChartBean.setLegendPosition("ne");
		barChartBean.setxAxisLabel("Tier Level");
		barChartBean.setyAxisLabel("Tier Amount Where Rebate Triggered");
		List<ChartSeriesData> chartSeriesDatas = new ArrayList<>();
		RebateService rebateService = SpringUtil.getService(RebateService.class);
		for(Response response : responsesToRequest) {
			ChartSeriesData drl = new ChartSeriesData();
			drl.setLabel(response.getSupplierCompany());
			Rebate rebate = rebateService.getUniqueRebateByNameAndCompany(response.getRebateName(), response.getSupplierCompany());
			LabelValue labelValue1 = new LabelValue("Base", rebate.getBaseOfferSalesValue());
			LabelValue labelValue2 = new LabelValue("Tier 1", rebate.getTier1OfferSalesValue());
			LabelValue labelValue3 = new LabelValue("Tier 2", rebate.getTier2OfferSalesValue());
			LabelValue labelValue4 = new LabelValue("Tier 3", rebate.getTier3OfferSalesValue());
			
			List<LabelValue> drlValues = new ArrayList<>();
			drlValues.add(labelValue1);
			drlValues.add(labelValue2);
			drlValues.add(labelValue3);
			drlValues.add(labelValue4);
			
			drl.setLabelValues(drlValues);
			chartSeriesDatas.add(drl);
		}
		barChartBean.setChartSeriesDatas(chartSeriesDatas);
		barChartBean.init();
	}
	
	private void init(String title) {
		barChartBean = new BarChartBean();
		barChartBean.setTitle(title);
		barChartBean.setLegendPosition("ne");
		barChartBean.setxAxisLabel("Tier Level");
		barChartBean.setyAxisLabel("Tier Percent");
		List<ChartSeriesData> chartSeriesDatas = new ArrayList<>();
		RebateService rebateService = SpringUtil.getService(RebateService.class);
		for(Response response : responsesToRequest) {
			ChartSeriesData drl = new ChartSeriesData();
			drl.setLabel(response.getSupplierCompany());
			Rebate rebate = rebateService.getUniqueRebateByNameAndCompany(response.getRebateName(), response.getSupplierCompany());
			LabelValue labelValue1 = new LabelValue("Base", rebate.getBaseOfferRebateOfferInPercent().doubleValue());
			LabelValue labelValue2 = new LabelValue("Tier 1", rebate.getTier1OfferRebateOfferInPercent().doubleValue());
			LabelValue labelValue3 = new LabelValue("Tier 2", rebate.getTier2OfferRebateOfferInPercent().doubleValue());
			LabelValue labelValue4 = new LabelValue("Tier 3", rebate.getTier3OfferRebateOfferInPercent().doubleValue());
			
			List<LabelValue> drlValues = new ArrayList<>();
			drlValues.add(labelValue1);
			drlValues.add(labelValue2);
			drlValues.add(labelValue3);
			drlValues.add(labelValue4);
			
			drl.setLabelValues(drlValues);
			chartSeriesDatas.add(drl);
		}
		barChartBean.setChartSeriesDatas(chartSeriesDatas);
		barChartBean.init();
	}

	public BarChartBean getBarChartBean() {
		return barChartBean;
	}

	public void setBarChartBean(BarChartBean barChartBean) {
		this.barChartBean = barChartBean;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
