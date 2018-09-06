package com.twenty20.jsf.reports.model;

import java.util.Comparator;

public class SortByProductComparator implements Comparator<ResponseSummary>{

	@Override
	public int compare(ResponseSummary arg0, ResponseSummary arg1) {
		// TODO Auto-generated method stub
		return arg0.getSubProduct().hashCode() - arg1.getSubProduct().hashCode();
	}

}
