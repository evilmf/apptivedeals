package com.apptivedeals.entity;

import java.util.Objects;

public class Price {
	
	public float priceRegular;
	public float priceDiscount;
	
	@Override
	public String toString() {
		return "Price ["
				+ "priceRegular=" + priceRegular + ", "
				+ "priceDiscount=" + priceDiscount 
				+ "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(priceRegular, priceDiscount);
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
		
		Price other = (Price) o;
		return Objects.equals(this.priceRegular, other.priceRegular)
				&& Objects.equals(this.priceDiscount, other.priceDiscount);
	}
}
