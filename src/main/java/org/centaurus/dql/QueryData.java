package org.centaurus.dql;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Vladislav Socolov
 */
public abstract class QueryData {
	
	private Class<?> document;
	private List<Expression> expressionList;
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
	
	public List<Expression> getExpressionList() {
		if (expressionList == null) {
			expressionList = new ArrayList<Expression>();
		}
		return expressionList;
	}

	public void setExpressionList(List<Expression> expressionList) {
		this.expressionList = expressionList;
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
		if(getExpressionList().size() == 0 || getOffset() == null 
				 || getLimit() == null || getSortOption() == null){
			return false;
		}
		return true;
	}
}
