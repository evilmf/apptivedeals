package com.apptivedeals.to;

public class ProductSearchTo {
	
	public Long productId;
	public String productName;
	public String brand;
	public String gender;
	public String category;
	public String imageUrl;
	public float minPrice;
	public float maxPrice;
	
	@Override
	public String toString() {
		return "ProductSearchTo ["
				+ "productId=" + productId + ", "
				+ "productName=" + productName + ", "
				+ "brand=" + brand + ", "
				+ "gender=" + gender + ", "
				+ "category=" + category + ", "
				+ "imageUrl=" + imageUrl + ", "
				+ "minPrice=" + minPrice + ", "
				+ "maxPrice=" + maxPrice				
				+ "]";
	}
}
