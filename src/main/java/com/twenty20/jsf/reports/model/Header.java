package com.twenty20.jsf.reports.model;

import java.util.List;

public class Header {
	private String name;
    private List<SubCol> subColList;

    public Header(){
    }

    public Header(String name, List<SubCol> subColList) {
        this.name = name;
        this.subColList = subColList;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SubCol> getSubColList() {
		return subColList;
	}

	public void setSubColList(List<SubCol> subColList) {
		this.subColList = subColList;
	}
    
    
}
