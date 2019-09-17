package com.apptivedeals.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Snapshot {
	
	public Long id;
	public Map<Long /* brand ID */, Map<Long/* gender ID */, Map<Long /* category ID */, List<SnapshotProduct>>>> snapshotProducts;
	public Date createDate;
	public Date updateDate;
	public Date snapshotDate;
	
	@Override
	public String toString() {
		return "Snapshot ["
				+ "id=" + id + ", "
				+ "snapshotProducts=" + snapshotProducts + ", "
				+ "createDate=" + createDate + ", "
				+ "updateDate=" + updateDate + ", "
				+ "snapshotDate=" + snapshotDate
				+ "]";
	}
	
	public static class SnapshotProduct {
		public Long productId;
		public String productName;
		public String productURL;
		public String imageURL;
		
		public Long brandId;
		public String brand;
		
		public Long genderId;
		public String gender;
		
		public Long categoryId;
		public String category;
		
		public float priceRegular;
		public float priceDiscount;
		public float discount;
		
		public Boolean isNew;
		
		@Override
		public String toString() {
			return "SnapshotProduct ["
					+ "productId=" + productId + ", "
					+ "productName=" + productName + ", "
					+ "productURL=" + productURL + ", "
					+ "imageURL=" + imageURL + ", "
					+ "brandId=" + brandId + ", "
					+ "brand=" + brand + ", "
					+ "genderId=" + genderId + ", "
					+ "gender=" + gender + ", "
					+ "categoryId=" + categoryId + ", "
					+ "category=" + category + ", "
					+ "priceRegular=" + priceRegular + ", "
					+ "priceDiscount=" + priceDiscount + ", "
					+ "discount=" + discount + ", "
					+ "isNew=" + isNew
					+ "]";
		}
	}
}
