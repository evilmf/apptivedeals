package com.apptivedeals.entity;

import java.util.Date;
import java.util.Objects;

public abstract class AbstractClassificationEntity {
	public Long id;
	public String name;
	public Date createDate;
	public Date updateDate;
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " ["
				+ "id=" + id + ", "
				+ "name=" + name + ", "
				+ "createDate=" + createDate + ", "
				+ "updateDate=" + updateDate
				+ "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, name, createDate, updateDate);
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
		
		AbstractClassificationEntity other = (AbstractClassificationEntity) o;
		
		return Objects.equals(this.id, other.id)
				&& Objects.equals(this.name, other.name)
				&& Objects.equals(this.createDate, other.createDate)
				&& Objects.equals(this.updateDate, other.updateDate);
	}
}
