package org.centaurus.client;

import org.centaurus.dql.QueryData;

/**
 * 
 * @author Vladislav Socolov
 */
public interface QueryProcessor<T> {
	
	public T processWhereClause(QueryData queryData);

	public Number processOffsetClause(QueryData queryData);

	public Number processLimitClause(QueryData queryData);

	public T processSortClause(QueryData queryData);
	
	public T processProjectionClause(QueryData queryData);

}
