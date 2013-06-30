package org.centaurus.dql;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Vladislav Socolov
 */
public abstract class QueryData {
	
	private Class<?> document;
	private List<Filter> filterList;
	private Long offset;
	private Long limit;
	private SortOption sortOption;
	
	public QueryData(Class<?> document){
		this.document = document;
	}
	
	public Class<?> getDocument() {
		return document;
	}

	public void setDocument(Class<?> document) {
		this.document = document;
	}
	
	public List<Filter> getFilterList() {
		if (filterList == null) {
			filterList = new ArrayList<Filter>();
		}
		return filterList;
	}

	public void setFilterList(List<Filter> filterList) {
		this.filterList = filterList;
	}

	public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public Long getLimit() {
		return limit;
	}

	public void setLimit(Long limit) {
		this.limit = limit;
	}

	public SortOption getSortOption() {
		return sortOption;
	}

	public void setSortOption(SortOption sortOption) {
		this.sortOption = sortOption;
	}

	public boolean hasFilterOptions(){
		if(getFilterList().size() == 0 || getOffset() == null 
				 || getLimit() == null || getSortOption() == null){
			return false;
		}
		return true;
	}
}
