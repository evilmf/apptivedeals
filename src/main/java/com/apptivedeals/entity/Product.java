package com.apptivedeals.entity;

import java.util.Date;
import java.util.Objects;

public class Product extends AbstractClassificationEntity {
	public Long id;
	public String productId;
	public String name;
	public String url;
	public Long brandId;
	public Long genderId;
	public Long categoryId;
	public Date createDate;
	public Date updateDate;
	
	@Override
	public String toString() {
		return "Product ["
				+ "id=" + id + ", "
				+ "productId=" + productId + ", "
				+ "name=" + name + ", "
				+ "url=" + url + ", "
				+ "brandId=" + brandId + ", "
				+ "genderId=" + genderId + ", "
				+ "categoryId=" + categoryId + ", "
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
		
		Product other = (Product) o;
		
		return Objects.equals(this.id, other.id)
				&& Objects.equals(this.productId, other.productId)
				&& Objects.equals(this.name, other.name)
				&& Objects.equals(this.url, other.url)
				&& Objects.equals(this.brandId, other.brandId)
				&& Objects.equals(this.genderId, other.genderId)
				&& Objects.equals(this.categoryId, other.categoryId)
				&& Objects.equals(this.createDate, other.createDate)
				&& Objects.equals(this.updateDate, other.updateDate);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, productId, name, url, brandId, genderId, categoryId, createDate, updateDate);
	}
}
