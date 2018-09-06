package com.twenty20.jsf.reports.model;

public class DataTableColumn {
	 private String header;
	    private String property;
	    public DataTableColumn(String header, String property) {
	        this.header = header;
	        this.property = property;
	    }
	    public String getHeader() {
	        return header;
	    }
	    public String getProperty() {
	        return property;
	    } 
}
