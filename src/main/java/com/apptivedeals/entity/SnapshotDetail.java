package com.apptivedeals.entity;

import java.util.Date;
import java.util.Objects;

public class SnapshotDetail extends Price {
	
	public Long id;
	public Long productId;
	public Long snapshotId;
	public Boolean isActive;
	public Date createDate;
	
	@Override
	public String toString() {
		return "SnapshotDetail ["
				+ "id=" + id + ", "
				+ "snapshotId=" + snapshotId + ", "
				+ "createDate=" + createDate + ", "
				+ "priceRegular=" + priceRegular + ", "
				+ "priceDiscount=" + priceDiscount + ", "
				+ "isActive=" + isActive + ", "
				+ "productId=" + productId
				+ "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, productId, snapshotId, priceDiscount, priceRegular, isActive, createDate);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		
		if (this == o) {
			return true;
		}
		
		if (!o.getClass().equals(this.getClass())) {
			return false;
		}
		
		SnapshotDetail other = (SnapshotDetail) o;
		return Objects.equals(this.id, other.id)
				&& Objects.equals(this.productId, other.productId)
				&& Objects.equals(this.snapshotId, other.snapshotId)
				&& Objects.equals(this.priceDiscount, other.priceDiscount)
				&& Objects.equals(this.priceRegular, other.priceRegular)
				&& Objects.equals(this.isActive, other.isActive)
				&& Objects.equals(this.createDate, other.createDate);
 	}
}
