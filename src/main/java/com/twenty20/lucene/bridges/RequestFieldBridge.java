package com.twenty20.lucene.bridges;

import java.util.List;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

import com.twenty20.domain.RequestDescription;

public class RequestFieldBridge implements FieldBridge {

	@Override
	public void set(String name, Object value, Document doc, LuceneOptions options) {
		// TODO Auto-generated method stub
		List<RequestDescription> fields = (List<RequestDescription>) value;
		if(fields != null) {
			for(RequestDescription description : fields) {
				if(description.getSubProduct() != null && description.getSubProduct().trim().length() != 0) {
					options.addFieldToDocument("SUB_PRODUCT", description.getSubProduct().trim().replace(" ", ""), doc);
				}
				
			}
		}
		
		//Sets.in
	}

}
