package org.centaurus.client.couchdb;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.centaurus.client.DBClient;
import org.centaurus.client.Mapper;
import org.centaurus.client.QueryProcessor;
import org.centaurus.configuration.CentaurusConfig;
import org.centaurus.dql.QueryData;
import org.centaurus.enums.Sorting;
import org.centaurus.exceptions.CentaurusException;
import org.lightcouch.CouchDbClient;
import org.lightcouch.DesignDocument.MapReduce;
import org.lightcouch.View;

import com.google.gson.JsonObject;

/**
 * 
 * @author Vladislav Socolov
 */
public class CouchDBClient implements DBClient {

	private static Logger log = Logger.getLogger(CouchDBClient.class);

	private CouchDbClient couchClient;
	private Mapper mapper;
	private QueryProcessor<String> queryProcessor;
	private String countReduce; //bad hack

	public CouchDBClient() {
		mapper = new CouchDBMapper();
		queryProcessor = new  CouchDBQueryProcessor<String>();
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
		
		MapReduce mapReduce = new MapReduce();
		mapReduce.setMap(baseTemporaryViewQuery(document).toString());
		
		List<JsonObject> query = couchClient.view("_temp_view").tempView(mapReduce).query(JsonObject.class);
		for (JsonObject dbObject : query) {
			list.add(document.cast(mapper.dbObjectToDocument(document, dbObject)));
		}		
		return list;
	}

	public <T> List<T> list(Class<T> document, QueryData queryData) {
		List<T> list = new ArrayList<T>();	
		
		List<JsonObject> query = processQueryClauses(document, queryData).query(JsonObject.class);
		for (JsonObject dbObject : query) {
			list.add(document.cast(mapper.dbObjectToDocument(document, dbObject)));
		}		
		return list;
	}

	public <T> T first(Class<T> document) {
		MapReduce mapReduce = new MapReduce();
		mapReduce.setMap(baseTemporaryViewQuery(document).toString());
		
		List<JsonObject> query = couchClient.view("_temp_view").tempView(mapReduce).limit(1).query(JsonObject.class);		
		return document.cast(mapper.dbObjectToDocument(document, query.get(0)));
	}

	public <T> T first(Class<T> document, QueryData queryData) {
		List<JsonObject> query = processQueryClauses(document, queryData).limit(1).query(JsonObject.class);
		return document.cast(mapper.dbObjectToDocument(document, query.get(0)));
	}

	public <T> T last(Class<T> document) {
		MapReduce mapReduce = new MapReduce();
		mapReduce.setMap(baseTemporaryViewQuery(document).toString());
		
		List<JsonObject> query = couchClient.view("_temp_view").tempView(mapReduce).descending(true).limit(1).query(JsonObject.class);		
		return document.cast(mapper.dbObjectToDocument(document, query.get(0)));
	}

	public <T> T last(Class<T> document, QueryData queryData) {
		List<JsonObject> query = processQueryClauses(document, queryData).limit(1).descending(true).query(JsonObject.class);
		return document.cast(mapper.dbObjectToDocument(document, query.get(0)));
	}

	public Number count(Class<?> document) {
		MapReduce mapReduce = new MapReduce();
		mapReduce.setMap(baseTemporaryViewQuery(document).toString());
		mapReduce.setReduce("_count");
		
		List<JsonObject> query = couchClient.view("_temp_view").tempView(mapReduce).query(JsonObject.class);
		JsonObject jsonObject = query.get(0);
		return jsonObject.get("value").getAsInt();
	}

	public Number count(Class<?> document, QueryData queryData) {
		countReduce = "_count";
		List<JsonObject> query = processQueryClauses(document, queryData).query(JsonObject.class);
		countReduce = null;
		JsonObject jsonObject = query.get(0);
		return jsonObject.get("value").getAsInt();
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
	
	private StringBuilder baseTemporaryViewQuery(Class<?> document){
		String collectionName = mapper.getCollectionName(document);
		
		StringBuilder builder = new StringBuilder();
		builder.append("function(doc) {");
		builder.append(String.format("if(doc.collection_name == '%s') {", collectionName));
		builder.append("emit(doc._id, doc); } }");
		
		return builder;
	}

	private View processQueryClauses(Class<?> document, QueryData queryData){
		String collectionName = mapper.getCollectionName(document);
		View view = couchClient.view("_temp_view");
		
		StringBuilder builder = new StringBuilder();
		builder.append("function(doc) {");
		builder.append(String.format("if(doc.collection_name == '%s') {", collectionName));
		
		if(!queryData.getExpressionList().isEmpty()){
			builder.append(queryProcessor.processWhereClause(queryData)).append("{");
			if(queryData.getSortOption() != null){
				builder.append(String.format("emit(doc.%s, doc);", queryData.getSortOption().getField()));
			} else {
				builder.append("emit(doc._id, doc);");	
			}
			builder.append("}}}");
		} 
		
		MapReduce mapReduce = new MapReduce();
		mapReduce.setMap(builder.toString());
		if(countReduce != null){
			mapReduce.setReduce(countReduce);
		}
		
		if(queryData.getOffset() != null) {
			view.skip(queryProcessor.processOffsetClause(queryData).intValue());
		}
		if(queryData.getLimit() != null) {
			view.limit(queryProcessor.processLimitClause(queryData).intValue());
		}
		if(queryData.getSortOption() != null) {
			view.descending(queryData.getSortOption().getSorting() == Sorting.DESC ? true : false);
		}
		
		return view.tempView(mapReduce);
	}
	
}
