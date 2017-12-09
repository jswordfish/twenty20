package com.twenty20.domain;

import javax.persistence.Embeddable;

@Embeddable
public class ResponseToRequestDescription {
	String subProduct = "";
	
	Double volumeRequired = 0d;
	
	String unit = "";
	
	String price;
	
	public ResponseToRequestDescription(String subProduct, Double volumeRequired, String unit, String price) {
		this.subProduct = subProduct;
		this.volumeRequired = volumeRequired;
		this.unit = unit;
		this.price = price;
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
	
	
	
}
