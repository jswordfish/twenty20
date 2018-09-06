package com.twenty20.jsf.reports.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.twenty20.domain.Response;
import com.twenty20.domain.ResponseToRequestDescription;
public class TableModel {
	
	private  List<DataTableColumn> dataTableColumns = new ArrayList<DataTableColumn>();
	
	private List<ResponseSummary> descriptions = new ArrayList<>();
	
	public void build(List<Response> responses) {
		dataTableColumns.add(new DataTableColumn("Supplier Company", "supplierCompany"));
		dataTableColumns.add(new DataTableColumn("Sub Product", "subProduct"));
		dataTableColumns.add(new DataTableColumn("Volume Required", "volumeRequired"));
		dataTableColumns.add(new DataTableColumn("Unit", "unit"));
		dataTableColumns.add(new DataTableColumn("Price", "price"));
		
		for(Response response : responses) {
				for(ResponseToRequestDescription description : response.getResponseToRequestDescriptions()) {
					ResponseSummary summary = new ResponseSummary(description.getSubProduct(), description.getVolumeRequired(), description.getUnit(),description.getPrice(), response.getSupplierCompany());
					descriptions.add(summary);
				}
			
		}
		
		Collections.sort(descriptions, new SortByProductComparator());
	}

	public List<DataTableColumn> getDataTableColumns() {
		return dataTableColumns;
	}

	public void setDataTableColumns(List<DataTableColumn> dataTableColumns) {
		this.dataTableColumns = dataTableColumns;
	}

	public List<ResponseSummary> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<ResponseSummary> descriptions) {
		this.descriptions = descriptions;
	}


	

}
