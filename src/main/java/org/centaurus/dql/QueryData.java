package org.centaurus.dql;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Vladislav Socolov
 */
public abstract class QueryData {
	
	private List<Expression> expressionList;
	private Number offset;
	private Number limit;
	private SortOption sortOption;
	private List<Projection> projectionList;
	
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

	public List<Projection> getProjectionList() {
		if(projectionList == null){
			projectionList = new ArrayList<Projection>();
		}
		return projectionList;
	}

	public void setProjectionList(List<Projection> projectionList) {
		this.projectionList = projectionList;
	}
	
	public boolean hasFilterOptions(){
		if(getExpressionList().size() != 0 || getOffset() != null || getLimit() != null 
				|| getSortOption() != null || getProjectionList().size() != 0){
			return true;
		}
		return false;
	}
	
	public void clearFilters(){
		getExpressionList().clear();
		setOffset(null);
		setLimit(null);
		setSortOption(null);
		getProjectionList().clear();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("QueryData [expressionList=");
		builder.append(expressionList);
		builder.append(", offset=");
		builder.append(offset);
		builder.append(", limit=");
		builder.append(limit);
		builder.append(", sortOption=");
		builder.append(sortOption);
		builder.append(", projectionList=");
		builder.append(projectionList);
		builder.append("]");
		return builder.toString();
	}

}
