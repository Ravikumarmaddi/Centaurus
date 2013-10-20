package org.centaurus.client.couchdb;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.centaurus.client.DBClient;
import org.centaurus.client.Mapper;
import org.centaurus.configuration.CentaurusConfig;
import org.centaurus.dql.QueryData;
import org.centaurus.exceptions.CentaurusException;
import org.lightcouch.CouchDbClient;
import org.lightcouch.DesignDocument.MapReduce;

import com.google.gson.JsonObject;

/**
 * 
 * @author Vladislav Socolov
 */
public class CouchDBClient implements DBClient {

	private static Logger log = Logger.getLogger(CouchDBClient.class);

	private CouchDbClient couchClient;
	private Mapper mapper;

	public CouchDBClient() {
		mapper = new CouchDBMapper();
		fetchClientProprietes();
	}

	public <T> T insert(T document) {
		JsonObject dbObject = mapper.documentToDBObject(document);
		couchClient.save(dbObject); 
		return document;
	}

	public <T> T insertOrUpdate(T document) {
		JsonObject dbObject = mapper.documentToDBObject(document);
		if(dbObject.get("_id") != null){
			couchClient.save(dbObject);
		} else {
			couchClient.update(dbObject);
		}
		return document;
	}

	public <T> T update(T document) {
		JsonObject dbObject = mapper.documentToDBObject(document);
		couchClient.update(dbObject);
		return document;
	}

	public <T> void delete(T document) {
		JsonObject dbObject = mapper.documentToDBObject(document);
		couchClient.remove(dbObject);
	}

	public <T> List<T> list(Class<T> document) {
		List<T> list = new ArrayList<T>();	
		String collectionName = mapper.getCollectionName(document);
		
		StringBuilder builder = new StringBuilder();
		builder.append("function(doc) {");
		builder.append(String.format("if(doc.collection_name == '%s') {", collectionName));
		builder.append("emit(doc._id, doc); } }");
		
		MapReduce mapReduce = new MapReduce();
		mapReduce.setMap(builder.toString());
		
		List<JsonObject> query = couchClient.view("_temp_view").tempView(mapReduce).query(JsonObject.class);
		for (JsonObject dbObject : query) {
			list.add(document.cast(mapper.dbObjectToDocument(document, dbObject)));
		}		
		return list;
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
		couchClient.shutdown();
	}

	private void fetchClientProprietes() {
		String host = CentaurusConfig.getInstance().getDbHost();
		int port = Integer.valueOf(CentaurusConfig.getInstance().getDbPort());
		String dbName = CentaurusConfig.getInstance().getDbName();
		String user = CentaurusConfig.getInstance().getDbUsername();
		String password = CentaurusConfig.getInstance().getDbPassword();

		try {
			log.info(String.format("Connecting to database %s:%d %s", host, port, dbName));
			couchClient = new CouchDbClient(dbName, false, "http", host, port, user, password);
			log.info(String.format("Connecting established to %s:%d %s", host, port, dbName));
		} catch (Exception e) {
			log.error(String.format("Cannot connect to %s:%d %s", host, port, dbName));
			throw new CentaurusException(String.format("Cannot connect to %s:%d %s", host, port, dbName), e);
		}
	}

}
