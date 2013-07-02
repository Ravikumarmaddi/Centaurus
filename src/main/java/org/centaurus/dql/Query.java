package org.centaurus.dql;

import java.util.List;

import org.centaurus.enums.Sorting;

/**
 * 
 * @author Vladislav Socolov
 */
public interface Query {

	public Query where(Expression expression);
	
	public Query where(Expression... expression);
	
	public Query offset(Long offset);
	
	public Query limit(Long limit);
	
	public Query sort(String field, Sorting sorting);
	
	public <T> List<T> list();
	
	public <T> T first();
	
	public <T> T last();
}
