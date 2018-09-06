package com.twenty20.jsf.reports.model;

public class ResponseSummary {
	String subProduct = "";
	
	Double volumeRequired = 0d;
	
	String unit = "";
	
	String price;
	
	String supplierCompany;
	
	public ResponseSummary() {
		
	}
	
	public ResponseSummary(String subProduct, Double volumeRequired, String unit, String price, String supplierCompany) {
		this.subProduct = subProduct;
		this.volumeRequired = volumeRequired;
		this.unit = unit;
		this.price = price;
		this.supplierCompany = supplierCompany;
		
	}

	public String getSubProduct() {
		return subProduct;
	}

	public void setSubProduct(String subProduct) {
		this.subProduct = subProduct;
	}

	public Double getVolumeRequired() {
		return volumeRequired;
	}

	public void setVolumeRequired(Double volumeRequired) {
		this.volumeRequired = volumeRequired;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSupplierCompany() {
		return supplierCompany;
	}

	public void setSupplierCompany(String supplierCompany) {
		this.supplierCompany = supplierCompany;
	}
	
	
	
}
