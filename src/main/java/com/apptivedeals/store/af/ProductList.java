package com.apptivedeals.store.af;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductList {
	
	public List<ProductCategory> products;
	
	public static class ProductCategory {
		public String productId;
		public ProductAttrs productAttrs;
		public List<Category> breadcrumbCategoryHierarchy;
		
		@Override
		public String toString() {
			return "ProductCategory ["
					+ "productId=" + productId + ", "
					+ "productAttrs=" + productAttrs + ", "
					+ "breadcrumbCategoryHierarchy=" + breadcrumbCategoryHierarchy
					+ "]";
		}
		
		public static class Category {
			public Long categoryId;
			public String categoryName;
			@JsonProperty("categoryName_EN")
			public String categoryNameEN;
			
			@Override 
			public String toString() {
				return "Category ["
						+ "categoryId=" + categoryId + ", "
						+ "categoryName=" + categoryName + ", "
						+ "categoryNameEN=" + categoryNameEN
						+ "]";
			}
		}
		
		public static class ProductAttrs {
			public String gender;
			
			@Override
			public String toString() {
				return "ProductAttrs ["
						+ "gender=" + gender
						+ "]";
			}
		}
	}
}
