package com.apptivedeals.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apptivedeals.dao.ProductDao;
import com.apptivedeals.dao.SnapshotDao;
import com.apptivedeals.to.ProductSearchTo;
import com.apptivedeals.to.ProductSnapshot;

@Service
public class ProductSearchService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductSearchService.class);
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private SnapshotDao snapshotDao;
	
	public List<ProductSearchTo> search(String keyword) {
		return productDao.searchProduct(keyword);
	}
	
	public List<ProductSnapshot> getProductSnapshot(Long productId) {
		return snapshotDao.getProductSnapshots(productId);
	}
}
