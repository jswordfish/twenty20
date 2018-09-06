package com.twenty20.jsf.reports.model;

public class RebateModel {
	
	String supplierCompany;
	
	Double transactionAmount;
	
	Double savings;
	
	String supplierRebateTierLevel;
	
	String supplierRebateTierPercent;

	public String getSupplierCompany() {
		return supplierCompany;
	}

	public void setSupplierCompany(String supplierCompany) {
		this.supplierCompany = supplierCompany;
	}

	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public Double getSavings() {
		return savings;
	}

	public void setSavings(Double savings) {
		this.savings = savings;
	}

	public String getSupplierRebateTierLevel() {
		return supplierRebateTierLevel;
	}

	public void setSupplierRebateTierLevel(String supplierRebateTierLevel) {
		this.supplierRebateTierLevel = supplierRebateTierLevel;
	}

	public String getSupplierRebateTierPercent() {
		return supplierRebateTierPercent;
	}

	public void setSupplierRebateTierPercent(String supplierRebateTierPercent) {
		this.supplierRebateTierPercent = supplierRebateTierPercent;
	}
	
	
	
}
