package com.apptivedeals.entity;

import java.util.Date;
import java.util.Objects;

public class Image {
	
	public Long id;
	public String url;
	public Long productId;
	public Boolean isPrimary;
	public Date createDate;
	public Date updateDate;
	
	@Override
	public String toString() {
		return "Image ["
				+ "id=" + id + ", "
				+ "url=" + url + ", "
				+ "productId=" + productId + ", "
				+ "isPrimary=" + isPrimary + ", "
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
		
		Image other = (Image) o;
		
		return Objects.equals(this.id, other.id)
				&& Objects.equals(this.url, other.url)
				&& Objects.equals(this.productId, other.productId)
				&& Objects.equals(this.isPrimary, other.isPrimary)
				&& Objects.equals(this.createDate, other.createDate)
				&& Objects.equals(this.updateDate, other.updateDate);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, url, productId, isPrimary, createDate, updateDate);
	}
}
