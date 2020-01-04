package com.apptivedeals.to;

public class ProductSnapshotHistory {
	
	public Long snapshotId;
	public Long productId;
	public String productName;
	public String timeRange;
	public float priceDiscount;
	
	@Override
	public String toString() {
		return "ProductSnapshotHistory ["
				+ "snapshotId= " + snapshotId + ", " 
				+ "productId=" + productId + ", "
				+ "productName=" + productName + ", "
				+ "timeRange=" + timeRange + ", "
				+ "priceDiscount=" + priceDiscount 
				+ "]";
	}
}
