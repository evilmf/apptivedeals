package com.apptivedeals.store.af;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.jsoup.Connection.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.apptivedeals.entity.Brand;
import com.apptivedeals.entity.Category;
import com.apptivedeals.entity.Gender;
import com.apptivedeals.entity.Product;
import com.apptivedeals.entity.Price;
import com.apptivedeals.service.JsoupConnect;
import com.apptivedeals.store.Crawler;
import com.apptivedeals.store.af.ProductList.ProductCategory;
import com.fasterxml.jackson.databind.ObjectMapper;


public abstract class AbstractCrawler extends com.apptivedeals.store.AbstractCrawler implements Crawler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCrawler.class);
	
	@Autowired
	private JsoupConnect jsoupConnect;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Value("${min.discount}")
	private float MIN_DISCOUNT;
	
	private int ROWS = 240;
	
	private Brand brand;
	
	protected abstract String getBrandName();
	protected abstract String getImageUrlThumbnailTpl();
	protected abstract String getBaseUrl();
	protected abstract String getAjaxNavApiTpl();
	protected abstract String getProductInfoApiTpl();
	protected abstract List<Long> getCategoryIds();
	
	@PostConstruct
	public void init() {
		brand = insertBrand(getBrandName());
	}
	
	@Override
	public Map<Long, Price> getProducts() {
		LOGGER.info("Start getting products for brand: {}", brand);
		Map<Long, Price> productPriceList = new HashMap<Long, Price>();
		for (Long categoryId : getCategoryIds()) {
			LOGGER.info("Getting product for category ID {}", categoryId);
			int start = 0;
			while (true) {
				LOGGER.info("Getting products by category with params: categoryId-{}, start-{}, rows-{} }", categoryId, start, ROWS);
				String url = String.format(getAjaxNavApiTpl(), categoryId, ROWS, start);
				LOGGER.info("URL: {}", url);
				AjaxNavAPIResponseJSON res = get(url, AjaxNavAPIResponseJSON.class);
				if (res != null) {
					LOGGER.info("Stats: {} with params: categoryId-{}, start-{}, rows-{} }", res.stats, categoryId, start, ROWS);
					for (com.apptivedeals.store.af.Product p : res.products) {
						float discount = (p.highListPrice - p.lowPrice) / p.highListPrice;
						if (discount < MIN_DISCOUNT) {
							continue;
						}
						
						Product productEntity = load(p);
						if (productEntity != null) {
							Price price = new Price();
							price.priceRegular = p.highListPrice; 
							price.priceDiscount = p.lowPrice;
							
							productPriceList.put(productEntity.id, price);
						}
					}
					
					if ((res.stats.startNum + res.stats.count) >= res.stats.total) {
						break;
					}
				}
				
				start += ROWS;
			}
			LOGGER.info("Done getting product for category ID {}", categoryId);
		}
		
		LOGGER.info("Done getting products for brand: {}; Total number of products: {}", brand, productPriceList.size());
		
		return productPriceList;
	}
	
	private Product load(com.apptivedeals.store.af.Product product) {
		Product p = get(product.productId, brand.id);
		if (p != null) {
			//LOGGER.info("Product exists: {}", product);
			return p;
		}
		
		LOGGER.info("Product not yet exists: {}", product);
		ProductList productList = get(String.format(getProductInfoApiTpl(), product.productId),
				ProductList.class);
		if (productList != null) {
			ProductCategory productCategory = productList.products.get(0);
			Gender gender = insertGender(productCategory.productAttrs.gender);
			Category category = insertCategory(productCategory.breadcrumbCategoryHierarchy
					.get(productCategory.breadcrumbCategoryHierarchy.size() - 1).categoryNameEN);
			p = insert(product.productId, product.name, getBaseUrl() + product.productUrl, brand.id, gender.id,
					category.id);
			insertCollection(product.collection, brand.id, p.id);
			insertImage(p.id, generateImageUrl(product), true);
		}

		return p;
	}
	
	private String generateImageUrl(com.apptivedeals.store.af.Product product) {
		return String.format(getImageUrlThumbnailTpl(), product.productKIC, product.imageNameSuffix,
				product.imagePresets.imagePresetPrefix, product.imagePresets.imagePresetSuffix);
	}
	
	private <T> T get(String url, Class<T> clazz) {
		T resJson = null;
		try {
			Response response = jsoupConnect.get(url);
			if (response.statusCode() == 200) {
				String body = response.body();
				resJson = objectMapper.readValue(body, clazz); 
			} else {
				LOGGER.error("Error getting URL {}; HTTP Status Code: {}", url, response.statusCode()); 
			}
		} catch (IOException e) {
			LOGGER.error("Exception getting URL {}", url);
			LOGGER.error(e.getMessage(), e);
		}
		
		return resJson;
	}
}
