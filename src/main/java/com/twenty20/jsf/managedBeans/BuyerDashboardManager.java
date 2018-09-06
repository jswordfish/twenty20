package com.twenty20.jsf.managedBeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.primefaces.context.RequestContext;
import org.springframework.stereotype.Service;

import com.twenty20.jsf.charts.barchart.model.BarChartBean;
import com.twenty20.jsf.charts.barchart.model.ChartSeriesData;
import com.twenty20.jsf.charts.piechart.model.LabelValue;
import com.twenty20.jsf.charts.piechart.model.PieChartBean;
import com.twenty20.jsf.reports.Report;

@ManagedBean(name = "buyerDashboard", eager = true)
@SessionScoped
@Service
public class BuyerDashboardManager {

	PieChartBean pieChartTotalSpendProfileByRegion ;
	
	PieChartBean pieChartTotalSpendProfileByOperation ;
	
	PieChartBean pieChartTotalSpendProfileByTieredCategory ;
	
	BarChartBean grossWellnessCount ;
	
	BarChartBean top10Vendors ;
	
	List<Report> reports = new ArrayList<Report>();
	
	 @ManagedProperty(value="#{tab}") 
	 TabManager tabManager;
	
	public String getFacelet() {
		return "buyerDashboard.xhtml?faces-redirect=true";
	}
	
	@PostConstruct
	public void init() {
		pieChartTotalSpendProfileByRegion = new PieChartBean("Total Spends By Region");
		pieChartTotalSpendProfileByOperation = new PieChartBean("Total Spends By Operation");
		pieChartTotalSpendProfileByTieredCategory = new PieChartBean("Total Spends By Tiers");
		grossWellnessCount = new BarChartBean(true);
		top10Vendors = new BarChartBean(false);
		
		LabelValue labelValue1 = new LabelValue("NORTH", 6.23);
		LabelValue labelValue2 = new LabelValue("SOUTH", 5.19);
		LabelValue labelValue3 = new LabelValue("CORPORATE", 3.29);
		List<LabelValue> regions = new ArrayList<>();
		regions.add(labelValue1);
		regions.add(labelValue2);
		regions.add(labelValue3);
		pieChartTotalSpendProfileByRegion.setLabelValues(regions);
		pieChartTotalSpendProfileByRegion.buildPieChart();
		
		LabelValue labelValue4 = new LabelValue("DRL", 1.18);
		LabelValue labelValue5 = new LabelValue("CMP", 5.63);
		LabelValue labelValue6 = new LabelValue("LOE", 1.05);
		LabelValue labelValue7 = new LabelValue("OTHERS", 3.62);
		
		List<LabelValue> operations = new ArrayList<>();
		operations.add(labelValue4);
		operations.add(labelValue5);
		operations.add(labelValue6);
		operations.add(labelValue7);
		pieChartTotalSpendProfileByOperation.setLabelValues(operations);
		pieChartTotalSpendProfileByOperation.buildPieChart();
		
		LabelValue labelValue8 = new LabelValue("TIER1", 3.12);
		LabelValue labelValue9 = new LabelValue("TIER2", 5.59);
		LabelValue labelValue10 = new LabelValue("TIER3", 5.80);
		LabelValue labelValue11 = new LabelValue("OTHERS", 1.46);
		List<LabelValue> tiers = new ArrayList<>();
		tiers.add(labelValue8);
		tiers.add(labelValue9);
		tiers.add(labelValue10);
		tiers.add(labelValue11);
		pieChartTotalSpendProfileByTieredCategory.setLabelValues(tiers);
		pieChartTotalSpendProfileByTieredCategory.buildPieChart();
		initBarCharts();
//		RequestContext.getCurrentInstance().update("buyerDashForm:pieChartRegion");
//		RequestContext.getCurrentInstance().update("buyerDashForm:pieChartOperations");
//		RequestContext.getCurrentInstance().update("buyerDashForm:pieChartTiers");
//		RequestContext.getCurrentInstance().update("buyerDashForm:wellness");
//		RequestContext.getCurrentInstance().update("buyerDashForm:top10");
		
		if(reports.size() == 0) {
			Report report1 = new Report();
			report1.setReportName("Total Spends By Region");
			report1.setImageLink("images/report1.jpg");
			reports.add(report1);
			
			Report report2 = new Report();
			report2.setReportName("Total Spend Profile By Operations");
			report2.setImageLink("images/report2.jpg");
			reports.add(report2);
			
			Report report3 = new Report();
			report3.setReportName("Total Spend Profile By Tier Caegory");
			report3.setImageLink("images/report3.jpg");
			reports.add(report3);
			
			Report report4	 = new Report();
			report4.setReportName("ISO Gross Wellness Count");
			report4.setImageLink("images/report3.jpg");
			reports.add(report4);
			
			Report report5	 = new Report();
			report5.setReportName("Top 10 Vendors");
			report5.setImageLink("images/report3.jpg");
			reports.add(report5);
		}
	}
	
	private void initBarCharts() {
		grossWellnessCount.setTitle("ISO Gross Wellness Count");
		grossWellnessCount.setLegendPosition("ne");
		grossWellnessCount.setxAxisLabel("Period");
		grossWellnessCount.setyAxisLabel("Amount");
		List<ChartSeriesData> chartSeriesDatas = new ArrayList<>();
		chartSeriesDatas.add(getSample1());
		chartSeriesDatas.add(getSample2());
		grossWellnessCount.setChartSeriesDatas(chartSeriesDatas);
		grossWellnessCount.init();
		
		top10Vendors.setTitle("Top 10 Vendors");
		top10Vendors.setLegendPosition("ne");
		top10Vendors.setxAxisLabel("Vendors");
		top10Vendors.setyAxisLabel("Purchases");
		List<ChartSeriesData> chartSeriesDatasVendor = new ArrayList<>();
		chartSeriesDatasVendor.add(getSample3());
		top10Vendors.setChartSeriesDatas(chartSeriesDatasVendor);
		top10Vendors.init();
	}
	
	private ChartSeriesData getSample3() {
		ChartSeriesData drl = new ChartSeriesData();
		drl.setLabel("Vendor Purchase Total");
		LabelValue labelValue1 = new LabelValue("Axar", 50.0);
		LabelValue labelValue2 = new LabelValue("Bhiwani", 48.0);
		LabelValue labelValue3 = new LabelValue("John Doe", 45.0);
		LabelValue labelValue4 = new LabelValue("A1", 44.0);
		LabelValue labelValue5 = new LabelValue("Ram & Co", 44.0);
		LabelValue labelValue6 = new LabelValue("Hind", 42.0);
		LabelValue labelValue7 = new LabelValue("Eng", 40.0);
		LabelValue labelValue8 = new LabelValue("Ger", 39.0);
		LabelValue labelValue9 = new LabelValue("Aus", 25.0);
		LabelValue labelValue10 = new LabelValue("Syr", 22.0);
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
		return drl;
	}
	
	private ChartSeriesData getSample1() {
		ChartSeriesData drl = new ChartSeriesData();
		drl.setLabel("DRL");
		LabelValue labelValue1 = new LabelValue("Jan 17", 42.0);
		LabelValue labelValue2 = new LabelValue("Feb 17", 40.0);
		LabelValue labelValue3 = new LabelValue("Mar 17", 45.0);
		LabelValue labelValue4 = new LabelValue("Apr 17", 49.0);
		LabelValue labelValue5 = new LabelValue("May 17", 47.0);
		LabelValue labelValue6 = new LabelValue("Jun 17", 42.0);
		LabelValue labelValue7 = new LabelValue("Jul 17", 40.0);
		LabelValue labelValue8 = new LabelValue("Aug 17", 42.0);
		LabelValue labelValue9 = new LabelValue("Sep 17", 44.0);
		LabelValue labelValue10 = new LabelValue("Oct 17", 38.0);
		LabelValue labelValue11 = new LabelValue("Nov 17", 41.0);
		LabelValue labelValue12 = new LabelValue("Dec 17", 42.0);
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
		drlValues.add(labelValue11);
		drlValues.add(labelValue12);
		drl.setLabelValues(drlValues);
		return drl;
	}
	private ChartSeriesData getSample2() {
		ChartSeriesData drl = new ChartSeriesData();
		drl.setLabel("CMP");
		LabelValue labelValue1 = new LabelValue("Jan 17", 48.0);
		LabelValue labelValue2 = new LabelValue("Feb 17", 39.0);
		LabelValue labelValue3 = new LabelValue("Mar 17", 42.0);
		LabelValue labelValue4 = new LabelValue("Apr 17", 45.0);
		LabelValue labelValue5 = new LabelValue("May 17", 41.0);
		LabelValue labelValue6 = new LabelValue("Jun 17", 32.0);
		LabelValue labelValue7 = new LabelValue("Jul 17", 20.0);
		LabelValue labelValue8 = new LabelValue("Aug 17", 12.0);
		LabelValue labelValue9 = new LabelValue("Sep 17", 54.0);
		LabelValue labelValue10 = new LabelValue("Oct 17", 38.0);
		LabelValue labelValue11 = new LabelValue("Nov 17", 41.0);
		LabelValue labelValue12 = new LabelValue("Dec 17", 32.0);
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
		drlValues.add(labelValue11);
		drlValues.add(labelValue12);
		drl.setLabelValues(drlValues);
		return drl;
	}

	public PieChartBean getPieChartTotalSpendProfileByRegion() {
		return pieChartTotalSpendProfileByRegion;
	}

	public void setPieChartTotalSpendProfileByRegion(PieChartBean pieChartTotalSpendProfileByRegion) {
		this.pieChartTotalSpendProfileByRegion = pieChartTotalSpendProfileByRegion;
	}

	public PieChartBean getPieChartTotalSpendProfileByOperation() {
		return pieChartTotalSpendProfileByOperation;
	}

	public void setPieChartTotalSpendProfileByOperation(PieChartBean pieChartTotalSpendProfileByOperation) {
		this.pieChartTotalSpendProfileByOperation = pieChartTotalSpendProfileByOperation;
	}

	public PieChartBean getPieChartTotalSpendProfileByTieredCategory() {
		return pieChartTotalSpendProfileByTieredCategory;
	}

	public void setPieChartTotalSpendProfileByTieredCategory(PieChartBean pieChartTotalSpendProfileByTieredCategory) {
		this.pieChartTotalSpendProfileByTieredCategory = pieChartTotalSpendProfileByTieredCategory;
	}

	public BarChartBean getGrossWellnessCount() {
		return grossWellnessCount;
	}

	public void setGrossWellnessCount(BarChartBean grossWellnessCount) {
		this.grossWellnessCount = grossWellnessCount;
	}

	public BarChartBean getTop10Vendors() {
		return top10Vendors;
	}

	public void setTop10Vendors(BarChartBean top10Vendors) {
		this.top10Vendors = top10Vendors;
	}
	
	public String goback() {
		tabManager.setDisplayTab("Projects");
		return "bootstrapTabs.xhtml?faces-redirect=true";
	}

	public TabManager getTabManager() {
		return tabManager;
	}

	public void setTabManager(TabManager tabManager) {
		this.tabManager = tabManager;
	}
	
	
	public void showReport() {
		Map<String,Object> options = new HashMap<String, Object>();
        options.put("resizable", false);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("contentWidth", 700);
        options.put("contentHeight", 700);

        
        Map<String, List<String>> params = new HashMap<String, List<String>>();

        RequestContext.getCurrentInstance().openDialog("buyerDashboard",options, params);
      
	}

	public List<Report> getReports() {
		return reports;
	}

	public void setReports(List<Report> reports) {
		this.reports = reports;
	}
	
	
}
