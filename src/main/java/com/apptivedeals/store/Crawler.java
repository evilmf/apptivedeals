package com.apptivedeals.store;

import java.util.Map;

import com.apptivedeals.entity.Price;

public interface Crawler {
	
	public Map<Long, Price> getProducts();
}
