package com.twenty20.jsf.charts.piechart.model;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.ItemSelectEvent;

public class PieChartBean {

	List<LabelValue> labelValues = new ArrayList<>();
	
	String title;
	
	private org.primefaces.model.chart.PieChartModel pieChartModel = new org.primefaces.model.chart.PieChartModel();
	
	public PieChartBean() {
		
	}
	
	public PieChartBean(String title) {
		this.title = title;
	}
	
	public void buildPieChart() {
		for(LabelValue labelValue : labelValues) {
			pieChartModel.set(labelValue.getLabel(), labelValue.getValue());
			
		}
		pieChartModel.setShowDataLabels(true);
		pieChartModel.setTitle(getTitle());
		pieChartModel.setLegendPosition("e");
		pieChartModel.setFill(false);
		//pieChartModel.set
	}
	
	public void pieItemSelect(ItemSelectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected", "Item Value: "
                + pieChartModel.getData().get(event.getItemIndex()));
        FacesContext.getCurrentInstance().addMessage(event.getComponent().getClientId(), msg);
    }
	
	public PieChartBean(List<LabelValue> labelValues) {
		this.labelValues = labelValues;
	}

	public List<LabelValue> getLabelValues() {
		return labelValues;
	}

	public void setLabelValues(List<LabelValue> labelValues) {
		this.labelValues = labelValues;
	}

	public org.primefaces.model.chart.PieChartModel getPieChartModel() {
		return pieChartModel;
	}

	public void setPieChartModel(org.primefaces.model.chart.PieChartModel pieChartModel) {
		this.pieChartModel = pieChartModel;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
	
}
