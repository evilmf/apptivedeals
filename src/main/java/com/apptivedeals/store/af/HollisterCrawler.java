package com.apptivedeals.store.af;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class HollisterCrawler extends AbstractCrawler {

	@Override
	protected String getBrandName() {
		return "HOLLISTER";
	}

	@Override
	protected String getImageUrlThumbnailTpl() {
		return "https://anf.scene7.com/is/image/anf/%s_%s?$%s%s$&wid=200&hei=250";
	}

	@Override
	protected String getBaseUrl() {
		return "https://www.hollisterco.com";
	}

	@Override
	protected String getAjaxNavApiTpl() {
		return "https://www.hollisterco.com/shop/AjaxNavAPIResponseJSON?catalogId=10201&categoryId=%s&langId=-1&requestType=category&rows=%s&sort=pricelow2high&start=%s&storeId=10251";
	}

	@Override
	protected String getProductInfoApiTpl() {
		return "https://www.hollisterco.com/api/ecomm/h-us/product/list?productIds=%s";
	}

	@Override
	protected List<Long> getCategoryIds() {
		return Arrays.asList(12634L, 12635L);
	}

}