package com.apptivedeals.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apptivedeals.dao.CategoryDao;

@Service
public class CategoryService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);
	
	@Autowired
	private CategoryDao categoryDao;
	
	private static Long CACHE_DURATION = 3600000L * 24L;
	private static String DEFAULT_CATEGORIES_KEY = "DEFAULT_CATEGORIES";
	
	private InMemoryCache<String, Map<Long, Map<Long, List<Long>>>> CACHE = 
			new InMemoryCache<String, Map<Long, Map<Long, List<Long>>>>(CACHE_DURATION, CACHE_DURATION);
	
	public Map<Long, Map<Long, List<Long>>> getDefaultCategories() {
		Map<Long, Map<Long, List<Long>>> defaultCategories = CACHE.get(DEFAULT_CATEGORIES_KEY);
		if (defaultCategories != null && !defaultCategories.isEmpty()) {
			LOGGER.info("Getting default category from cache.");
			return defaultCategories;
		}
		
		defaultCategories = categoryDao.getDefaultCategories();
		CACHE.put(DEFAULT_CATEGORIES_KEY, defaultCategories);
		
		return defaultCategories;
	}
}
