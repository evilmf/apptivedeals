package com.apptivedeals.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.apptivedeals.dao.ProductDao;
import com.apptivedeals.dao.SnapshotDao;
import com.apptivedeals.entity.Price;
import com.apptivedeals.entity.Snapshot.SnapshotProduct;
import com.apptivedeals.store.Crawler;

@Service
public class Monitor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Monitor.class);
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private SnapshotDao snapshotDao;
	
	@Autowired
	private ProductDao productDao;
	
	public void run() {
		Map<Long, Price> currentPrices = new HashMap<Long, Price>();
		Map<String, Crawler> beans = context.getBeansOfType(Crawler.class);
		for (String beanName : beans.keySet()) {
			currentPrices.putAll(beans.get(beanName).getProducts());
		}
		
		Map<Long, Price> previousPrices = snapshotDao.getLatestSnapshotPrices();
		Map<Long, Price> existingSnapshotProducts = new HashMap<Long, Price>();
		
		Map<Long, Price> newActiveProducts = new HashMap<Long, Price>();
		for (Long productId : currentPrices.keySet()) {
			if (!previousPrices.containsKey(productId)) {
				newActiveProducts.put(productId, currentPrices.get(productId));
			} else {
				existingSnapshotProducts.put(productId, currentPrices.get(productId));
			}
		}
		
		Map<Long, Price> newInactiveProducts = new HashMap<Long, Price>();
		for (Long productId : previousPrices.keySet()) {
			if (!currentPrices.containsKey(productId)) {
				newInactiveProducts.put(productId, previousPrices.get(productId));
			}
		}
		
		//Populating snapshot_detail and snapshot
		Long newSnapshotId = null;
		if (!newActiveProducts.isEmpty() || !newInactiveProducts.isEmpty()) {
			newSnapshotId = snapshotDao.insertSnapshotDetails(newActiveProducts, newInactiveProducts);
			
			generateSnapshot(newSnapshotId, newActiveProducts, existingSnapshotProducts);
		}
		
	}
	
	private void generateSnapshot(Long snapshotId, Map<Long, Price> newActiveProducts,
			Map<Long, Price> existingSnapshotProducts) {
		Map<Long, Map<Long, Map<Long, List<SnapshotProduct>>>> snapshotProducts = productDao
				.getSnapshotProduct(newActiveProducts, existingSnapshotProducts);
		
		int numberOfAffectedRow = snapshotDao.insertSnapshot(snapshotId, snapshotProducts);
		
		if (numberOfAffectedRow != 1) {
			LOGGER.error("1 snapshot insertion is expected but {} was inserted: {}", numberOfAffectedRow, snapshotProducts);
		}
	};
}
