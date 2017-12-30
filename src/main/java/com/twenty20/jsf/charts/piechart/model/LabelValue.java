package com.twenty20.jsf.charts.piechart.model;

public class LabelValue {
String label;

Double value;

public LabelValue() {
	
}

public LabelValue(String lab, Double val) {
	this.label = lab;
	this.value = val;
}

public String getLabel() {
	return label;
}

public void setLabel(String label) {
	this.label = label;
}

public Double getValue() {
	return value;
}

public void setValue(Double value) {
	this.value = value;
}



}
