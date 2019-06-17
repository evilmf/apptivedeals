package com.apptivedeals.entity;

public class Brand extends AbstractClassificationEntity {
	
	@Override
	public boolean equals(Object o) {
		if (!super.equals(o)) {
			return false;
		}
		
		if (!o.getClass().equals(this.getClass())) {
			return false;
		}
		
		return true;
	}
	
}
