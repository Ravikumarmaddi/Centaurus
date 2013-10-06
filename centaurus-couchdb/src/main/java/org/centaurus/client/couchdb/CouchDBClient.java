package org.centaurus.client.couchdb;

import java.util.List;

import org.apache.log4j.Logger;
import org.centaurus.client.DBClient;
import org.centaurus.dql.QueryData;

public class CouchDBClient implements DBClient{

	private static Logger log = Logger.getLogger(CouchDBClient.class);
	
	public <T> T insert(T document) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T insertOrUpdate(T document) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T update(T document) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> void delete(T document) {
		// TODO Auto-generated method stub
		
	}

	public <T> List<T> list(Class<T> document) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> List<T> list(Class<T> document, QueryData queryData) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T first(Class<T> document) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T first(Class<T> document, QueryData queryData) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T last(Class<T> document) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T last(Class<T> document, QueryData queryData) {
		// TODO Auto-generated method stub
		return null;
	}

	public Number count(Class<?> document) {
		// TODO Auto-generated method stub
		return null;
	}

	public Number count(Class<?> document, QueryData queryData) {
		// TODO Auto-generated method stub
		return null;
	}

	public void clear() {
		// TODO Auto-generated method stub
		
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}

}
