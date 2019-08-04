package com.apptivedeals.store.af;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AjaxNavAPIResponseJSON {
	public Stats stats;
	public List<Product> products;
		
	@Override
	public String toString() {
		return "AjaxNavAPIResponseJSON ["
				+ "stats=" + stats + ", "
				+ "products=" + products 
				+ "]";
	}
}
