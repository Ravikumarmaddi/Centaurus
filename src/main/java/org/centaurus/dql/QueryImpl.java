package org.centaurus.dql;

import java.util.Arrays;
import java.util.List;

import org.centaurus.client.DBClient;
import org.centaurus.enums.Sorting;

/**
 * 
 * @author Vladislav Socolov
 */
public class QueryImpl extends QueryData implements Query {

	private DBClient dbClient;
	
	public QueryImpl(Class<?> document, DBClient dbClient){
		super(document);
		this.dbClient = dbClient;
	}
	
	public Query where(Filter filter) {
		getFilterList().add(filter);
		return this;
	}

	public Query where(Filter... filter) {
		getFilterList().addAll(Arrays.asList(filter));
		return this;
	}

	public Query offset(Long offset) {
		setOffset(offset);
		return this;
	}

	public Query limit(Long limit) {
		setLimit(limit);
		return this;
	}

	public Query sort(String field, Sorting sorting) {
		setSortOption(new SortOption(field, sorting));
		return this;
	}

	public <T> List<T> list() {
		if(hasFilterOptions()){
			return dbClient.list(this); 
		}
		return dbClient.list();
	}

	public <T> T first() {
		if(hasFilterOptions()){
			return dbClient.first(this);
		}
		return dbClient.first();
	}

	public <T> T last() {
		if(hasFilterOptions()){
			return dbClient.last(this);
		}
		return dbClient.last();
	}

}
