package com.apptivedeals.store.af;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
	
	public String productId;
	public String name;
	public String collection;
	public String productKIC;
	public String longDesc;
	public float lowPrice;
	public float highListPrice;
	public String productUrl;
	public String imageNameSuffix;
	public String promoMessaging;
	public String imageId;
	public Boolean isSoldOut;
	public ImagePresets imagePresets;
	
	@Override
	public String toString() {
		return "Product ["
				+ "productId=" + productId + ", "
				+ "name=" + name + ", "
				+ "collection=" + collection + ", "
				+ "productKIC=" + productKIC + ", "
				+ "longDesc=" + longDesc + ", "
				+ "lowListPrice=" + lowPrice + ", "
				+ "highListPrice=" + highListPrice + ", "
				+ "productUrl=" + productUrl + ", "
				+ "imageNameSuffix=" + imageNameSuffix + ", "
				+ "promoMessaging=" + promoMessaging + ", "
				+ "imageId=" + imageId + ", "
				+ "isSoldOut=" + isSoldOut
 				+ "]";
	}
	
	public static class ImagePresets {
		public String imageClass;
		public String imagePresetPrefix;
		public String imagePresetSuffix;
		public String imageNameSuffix;
		public String productClass;
		public String modelImageNameSuffix;
		public String flatImageNameSuffix;
		
		@Override
		public String toString() {
			return "ImagePresets ["
					+ "imageClass=" + imageClass + ", "
					+ "imagePresetPrefix=" + imagePresetPrefix + ", "
					+ "imagePresetSuffix=" + imagePresetSuffix + ", "
					+ "imageNameSuffix=" + imageNameSuffix + ", "
					+ "productClass=" + productClass + ", "
					+ "modelImageNameSuffix=" + modelImageNameSuffix + ", "
					+ "flatImageNameSuffix=" + flatImageNameSuffix 
					+ "]";
		}
	}
	
}
