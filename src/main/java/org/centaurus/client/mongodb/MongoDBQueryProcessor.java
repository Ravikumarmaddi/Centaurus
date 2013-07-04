package org.centaurus.client.mongodb;

import org.centaurus.client.QueryProcessor;
import org.centaurus.dql.QueryData;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 
 * @author Vladislav Socolov
 */
@SuppressWarnings("unchecked")
public class MongoDBQueryProcessor<T extends DBObject> implements QueryProcessor<T> {

	public T processWhereClause(QueryData queryData) {	
		// TODO Auto-generated method stub
		return null;
	}

	public T processOffsetClause(QueryData queryData) {
		// TODO Auto-generated method stub
		return null;
	}

	public T processLimitClause(QueryData queryData) {
		// TODO Auto-generated method stub
		return null;
	}

	public T processSortClause(QueryData queryData) {
		return (T) new BasicDBObject(queryData.getSortOption().getField(), queryData.getSortOption().getSorting().getValue());	
	}

}
