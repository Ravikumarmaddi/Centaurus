package org.centaurus.dql;

import org.centaurus.enums.Sorting;

/**
 * 
 * @author Vladislav Socolov
 */
public class SortOption {
	
	private String field;
	private Sorting sorting;

	public SortOption(String field, Sorting sorting) {
		this.setField(field);
		this.setSorting(sorting);
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Sorting getSorting() {
		return sorting;
	}

	public void setSorting(Sorting sorting) {
		this.sorting = sorting;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SortOption [field=");
		builder.append(field);
		builder.append(", sorting=");
		builder.append(sorting);
		builder.append("]");
		return builder.toString();
	}
}
