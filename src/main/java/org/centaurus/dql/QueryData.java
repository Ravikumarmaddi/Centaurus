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
	private Number offset;
	private Number limit;
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

	public Number getOffset() {
		return offset;
	}

	public void setOffset(Number offset) {
		this.offset = offset;
	}

	public Number getLimit() {
		return limit;
	}

	public void setLimit(Number limit) {
		this.limit = limit;
	}

	public SortOption getSortOption() {
		return sortOption;
	}

	public void setSortOption(SortOption sortOption) {
		this.sortOption = sortOption;
	}

	public boolean hasFilterOptions(){
		if(getExpressionList().size() != 0 || getOffset() != null 
				 || getLimit() != null || getSortOption() != null){
			return true;
		}
		return false;
	}
	
	public void clearFilters(){
		getExpressionList().clear();
		setOffset(null);
		setLimit(null);
		setSortOption(null);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("QueryData [document=");
		builder.append(document);
		builder.append(", expressionList=");
		builder.append(expressionList);
		builder.append(", offset=");
		builder.append(offset);
		builder.append(", limit=");
		builder.append(limit);
		builder.append(", sortOption=");
		builder.append(sortOption);
		builder.append("]");
		return builder.toString();
	}
}
