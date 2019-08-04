package com.apptivedeals.store;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.apptivedeals.dao.BrandDao;
import com.apptivedeals.dao.CategoryDao;
import com.apptivedeals.dao.CollectionDao;
import com.apptivedeals.dao.GenderDao;
import com.apptivedeals.dao.ImageDao;
import com.apptivedeals.dao.ProductDao;
import com.apptivedeals.entity.Brand;
import com.apptivedeals.entity.Category;
import com.apptivedeals.entity.Collection;
import com.apptivedeals.entity.Gender;
import com.apptivedeals.entity.Image;
import com.apptivedeals.entity.Product;
import com.apptivedeals.service.InMemoryCache;

public abstract class AbstractCrawler {
	
	@Autowired
	private BrandDao brandDao;
	
	@Autowired
	private GenderDao genderDao;
	
	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private ImageDao imageDao;
	
	@Autowired
	private CollectionDao collectionDao;
	
	private static Long CACHE_DURATION = 3600000L;
	
	private InMemoryCache<String, Product> productCache = new InMemoryCache<String, Product>(CACHE_DURATION, CACHE_DURATION);
	private Map<String, Gender> genderCache = new HashMap<String, Gender>();
	private Map<String, Category> categoryCache = new HashMap<String, Category>();

	protected final Brand insertBrand(String brandName) {
		return brandDao.insert(brandName);
	}
	
	protected final Gender insertGender(String genderText) {
		if (genderCache.containsKey(genderText)) {
			return genderCache.get(genderText);
		}
		
		Gender gender = genderDao.insert(genderText);
		if (gender != null) {
			genderCache.put(genderText, gender);
		}
		
		return gender;
	}
	
	protected final Category insertCategory(String categoryText) {
		if (categoryCache.containsKey(categoryText)) {
			return categoryCache.get(categoryText);
		}
		
		Category category = categoryDao.insert(categoryText);
		if (category != null) {
			categoryCache.put(categoryText, category);
		}
		
		return category;
	}
	
	protected final Product get(String productId, Long brandId) {
		if (productCache.containsKey(productId)) {
			return productCache.get(productId);
		}
		
		Product product = productDao.get(productId, brandId);
		if (product != null) {
			productCache.put(productId, product);
		}
		
		return product;
	}
	
	protected final Product insert(String productId, String name, String url, Long brandId, Long genderId, Long categoryId) {
		if (productCache.containsKey(productId)) {
			return productCache.get(productId);
		}
		
		Product product = productDao.insert(productId, name, url, brandId, genderId, categoryId);
		productCache.put(productId, product);
		
		return product;
	}
	
	protected final Image insertImage(Long productId, String url, Boolean isPrimary) {
		return imageDao.insert(url, productId, isPrimary);
	}
	
	protected final Collection insertCollection(String collectionId, Long brandId, Long productId) {
		return collectionDao.insert(collectionId, brandId, productId);
	}
}
