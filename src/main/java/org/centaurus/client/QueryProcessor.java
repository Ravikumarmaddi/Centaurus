package org.centaurus.client;

import org.centaurus.dql.QueryData;

/**
 * 
 * @author Vladislav Socolov
 */
public interface QueryProcessor<T> {
	
	public T processWhereClause(QueryData queryData);

	public T processOffsetClause(QueryData queryData);

	public T processLimitClause(QueryData queryData);

	public T processSortClause(QueryData queryData);

}
