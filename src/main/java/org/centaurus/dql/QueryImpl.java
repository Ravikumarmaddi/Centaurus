package org.centaurus.dql;

import java.util.Arrays;
import java.util.List;

import org.centaurus.client.DBClient;
import org.centaurus.enums.Sorting;

/**
 * 
 * @author Vladislav Socolov
 */
@SuppressWarnings("unchecked")
public class QueryImpl<T> extends QueryData implements Query<T> {

	private DBClient dbClient;
	
	public QueryImpl(Class<?> document, DBClient dbClient){
		super(document);
		this.dbClient = dbClient;
	}
	
	public Query<T> where(Expression expression) {
		getExpressionList().add(expression);
		return this;
	}

	public Query<T> where(Expression... expression) {
		getExpressionList().addAll(Arrays.asList(expression));
		return this;
	}

	public Query<T> offset(Number offset) {
		setOffset(offset);
		return this;
	}

	public Query<T> limit(Number limit) {
		setLimit(limit);
		return this;
	}

	public Query<T> sort(String field, Sorting sorting) {
		setSortOption(new SortOption(field, sorting));
		return this;
	}

	public List<T> list() {
		List<T> list = null;
		if(hasFilterOptions()){
			list = (List<T>) dbClient.list(getDocument(), this); //Send all filter conditions
		} else {
			list = (List<T>) dbClient.list(getDocument()); //Send only document class	
		}
		clearFilters();
		return list;
	}

	public T first() {
		T bean = null;
		if(hasFilterOptions()){
			bean = dbClient.first(this); //Send all filter conditions
		} else {
			bean = (T) dbClient.first(getDocument()); //Send only document class
		}
		clearFilters();
		return bean;
	}

	public T last() {
		T bean = null;
		if(hasFilterOptions()){
			bean = dbClient.last(this); //Send all filter conditions
		} else {
			bean = (T) dbClient.last(getDocument()); //Send only document class	
		}
		clearFilters();
		return bean;
	}

	public Number count() {
		Number count = null;
		if(hasFilterOptions()){
			count = dbClient.count(this); //Send all filter conditions
		} else {
			count = dbClient.count(getDocument()); //Send only document class
		}
		clearFilters();
		return count;
	}

}
