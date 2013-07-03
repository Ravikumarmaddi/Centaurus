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

	public Query<T> offset(Long offset) {
		setOffset(offset);
		return this;
	}

	public Query<T> limit(Long limit) {
		setLimit(limit);
		return this;
	}

	public Query<T> sort(String field, Sorting sorting) {
		setSortOption(new SortOption(field, sorting));
		return this;
	}

	public List<T> list() {
		if(hasFilterOptions()){
			return dbClient.list(this); //Send all filter conditions
		}
		return (List<T>) dbClient.list(getDocument()); //Send only document class
	}

	public T first() {
		if(hasFilterOptions()){
			return dbClient.first(this); //Send all filter conditions
		}
		return (T) dbClient.first(getDocument()); //Send only document class
	}

	public T last() {
		if(hasFilterOptions()){
			return dbClient.last(this); //Send all filter conditions
		}
		return (T) dbClient.last(getDocument()); //Send only document class
	}

	public Number count() {
		if(hasFilterOptions()){
			return dbClient.count(this); //Send all filter conditions
		}
		return dbClient.count(getDocument()); //Send only document class
	}

}
