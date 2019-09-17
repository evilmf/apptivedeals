package com.apptivedeals.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

	@Autowired
	private Monitor monitor;

	private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);

	// @Scheduled(fixedDelay = 360000, initialDelay = 15000)
	public void monitorRunner() {
		LOGGER.info("Scheduled monitor starts");
		monitor.run();
		LOGGER.info("Scheduled monitor finishes");
	}
}
