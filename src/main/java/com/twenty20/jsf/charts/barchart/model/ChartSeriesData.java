package com.twenty20.jsf.charts.barchart.model;

import java.util.ArrayList;
import java.util.List;

import com.twenty20.jsf.charts.piechart.model.LabelValue;

public class ChartSeriesData {
	
	String label;
	
	List<LabelValue> labelValues = new ArrayList<>();

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<LabelValue> getLabelValues() {
		return labelValues;
	}

	public void setLabelValues(List<LabelValue> labelValues) {
		this.labelValues = labelValues;
	}
	
	

}
