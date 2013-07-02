package org.centaurus.client;

import java.util.List;

import org.centaurus.dql.QueryData;


/**
 * 
 * @author Vladislav Socolov
 */
public interface DBClient {

	public <T> T insert(T document);

	public <T> T insertOrUpdate(T document);

	public <T> T update(T document);

	public <T> void delete(T document);

	public <T> List<T> list(Class<?> document);
	
	public <T> List<T> list(QueryData queryData);
	
	public <T> T first();
	
	public <T> T first(QueryData queryData);
	
	public <T> T last();
	
	public <T> T last(QueryData queryData);
	
	public void clear();

	public void close();

}
