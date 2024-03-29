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

	public <T> List<T> list(Class<T> document);
	
	public <T> List<T> list(Class<T> document, QueryData queryData);
	
	public <T> T first(Class<T> document);
	
	public <T> T first(Class<T> document, QueryData queryData);
	
	public <T> T last(Class<T> document);
	
	public <T> T last(Class<T> document, QueryData queryData);
	
	public Number count(Class<?> document);
	
	public Number count(Class<?> document, QueryData queryData);
	
	public void clear();

	public void close();

}
