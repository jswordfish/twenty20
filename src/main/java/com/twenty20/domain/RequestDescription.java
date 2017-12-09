package com.twenty20.domain;

import javax.persistence.Embeddable;

@Embeddable
public class RequestDescription {

	String subProduct = "";
	
	Double volumeRequired = 0d;
	
	String unit = "";

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
	
	
	
}
