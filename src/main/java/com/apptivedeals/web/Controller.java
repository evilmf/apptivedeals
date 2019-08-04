package com.apptivedeals.web;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apptivedeals.service.Monitor;

@RestController
public class Controller {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);
	
	@Autowired
	private Monitor monitor;
	
	@RequestMapping("/test")
	public void test() throws IOException {
		monitor.run();
	}
}
