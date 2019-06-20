package com.apptivedeals.entity;

import java.util.Date;
import java.util.Objects;

public class Collection {
	public Long id;
	public String collectionId;
	public Long brandId;
	public Long productId;
	public Date createDate;
	public Date updateDate;
	
	@Override
	public String toString() {
		return "Collection ["
				+ "id=" + id + ", "
				+ "collectionId=" + collectionId + ", "
				+ "brandId=" + brandId + ", "
				+ "productId=" + productId + ", "
				+ "createDate=" + createDate + ", "
				+ "updateDate=" + updateDate
				+ "]";
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		
		if (o == this) {
			return true;
		}
		
		if (!o.getClass().equals(this.getClass())) {
			return false;
		}
		
		Collection other = (Collection) o;
		
		return Objects.equals(this.id, other.id)
				&& Objects.equals(this.collectionId, collectionId)
				&& Objects.equals(this.brandId, brandId)
				&& Objects.equals(this.productId, productId)
				&& Objects.equals(this.createDate, createDate)
				&& Objects.equals(this.updateDate, updateDate);
	} 
	
	@Override
	public int hashCode() {
		return Objects.hash(id, collectionId, brandId, productId, createDate, updateDate);
	}
}
