package org.centaurus.dql;

import org.centaurus.enums.Sorting;

/**
 * 
 * @author Vladislav Socolov
 */
public interface Query {

	public Query where(Filter filter);
	
	public Query where(Filter... filter);
	
	public Query offset(long offset);
	
	public Query limit(long limit);
	
	public Query sort(String field, Sorting sorting);
}
