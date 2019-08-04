package com.apptivedeals.store.af;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class AfCrawler extends AbstractCrawler {

	@Override
	protected String getBrandName() {
		return "A&F";
	}

	@Override
	protected String getImageUrlThumbnailTpl() {
		return "https://anf.scene7.com/is/image/anf/%s_%s?$%s%s$&wid=200&hei=250";
	}

	@Override
	protected String getBaseUrl() {
		return "https://www.abercrombie.com";
	}

	@Override
	protected String getAjaxNavApiTpl() {
		return "https://www.abercrombie.com/shop/AjaxNavAPIResponseJSON?catalogId=10901&categoryId=%s&langId=-1&requestType=category&rows=%s&sort=pricelow2high&start=%s&storeId=10051";
	}

	@Override
	protected String getProductInfoApiTpl() {
		return "https://www.abercrombie.com/api/ecomm/a-us/product/list?productIds=%s";
	}

	@Override
	protected List<Long> getCategoryIds() {
		return Arrays.asList(12204L, 12205L);
	}

}
