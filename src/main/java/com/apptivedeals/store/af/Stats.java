package com.apptivedeals.store.af;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Stats {
	public Integer total;
	public Integer count;
	public Integer startNum;
	
	@Override
	public String toString() {
		return "Stats ["
				+ "total=" + total + ", "
				+ "count=" + count + ", "
				+ "startNum=" + startNum 
				+ "]";
	}
}
