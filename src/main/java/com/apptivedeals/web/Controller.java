package com.apptivedeals.web;

import java.io.IOException;
import java.util.List;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Pattern.Flag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apptivedeals.entity.Snapshot;
import com.apptivedeals.service.Monitor;
import com.apptivedeals.service.ProductSearchService;
import com.apptivedeals.service.SnapshotService;
import com.apptivedeals.to.ProductSearchTo;
import com.apptivedeals.to.ProductSnapshot;

@RestController
public class Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

	@Autowired
	private Monitor monitor;
	
	@Autowired
	private SnapshotService snapshotService;
	
	@Autowired
	private ProductSearchService productSearchService;

	@GetMapping(value = "/monitor")
	public void test() throws IOException {
		monitor.run();
	}
	
	@GetMapping(value = "/snapshot/{id}")
	public Snapshot getSnapshot(@PathVariable Long id) {
		LOGGER.info("Calling getSnapshot({})", id);
		
		Snapshot snapshot = snapshotService.getSnapshot(id);
		
		LOGGER.info("Done calling getSnapshot({})", id);
		
		return snapshot;
	}
	
	@GetMapping(value = "/latestSnapshot")
	public Snapshot getLatestSnapshot() {
		LOGGER.info("Calling getLatestSnapshot()");
		
		Snapshot snapshot = snapshotService.getLatestSnapshot();
		
		LOGGER.info("Done calling getLatestSnapshot()");
		
		return snapshot;
	}
	
	@GetMapping(value = "/productSearchResult")
	public List<ProductSearchTo> productSearch(
			@RequestParam(name = "keyword", required = true) 
			@Pattern(regexp = ".*[0-9a-z]{2}.*", flags=Flag.CASE_INSENSITIVE) 
			String keyword) {
		LOGGER.info("Calling productSearch({})", keyword);
		
		List<ProductSearchTo> productSearchResult = productSearchService.search(keyword);
		
		LOGGER.info("Done calling productSearch({})", keyword);
		
		return productSearchResult;
	}
	
	@GetMapping(value = "/productSnapshots")
	public List<ProductSnapshot> getProductSnapshot(
			@RequestParam(name = "productId", required = true)
			Long productId) {
		LOGGER.info("Calling getProductSnapshot({})", productId);
		
		List<ProductSnapshot> productSnapshots = productSearchService.getProductSnapshot(productId);
		
		LOGGER.info("Done calling getProductSnapshot({})", productId);
		
		return productSnapshots;
	}
}









