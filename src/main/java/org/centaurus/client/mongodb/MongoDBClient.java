package org.centaurus.client.mongodb;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.centaurus.client.DBClient;
import org.centaurus.client.Mapper;
import org.centaurus.client.QueryProcessor;
import org.centaurus.configuration.CentaurusConfig;
import org.centaurus.dql.QueryData;
import org.centaurus.exceptions.CentaurusException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

/**
 * 
 * @author Vladislav Socolov
 */
public class MongoDBClient implements DBClient {
	
	private static Logger log = Logger.getLogger(MongoDBClient.class);
	
	private MongoClient mongoClient;
	private DB mongoDB;
	private Mapper mapper;
	QueryProcessor<DBObject> queryProcessor;
	
	public MongoDBClient(Mapper mapper) {
		this.mapper = mapper;
		queryProcessor = new MongoDBQueryProcessor<DBObject>();
		fetchClientProprietes();
	}
	
	public <T> T insert(T document) {
		DBObject dbObject = mapper.documentToDBObject(document);
		DBCollection collection = mongoDB.getCollection(mapper.getCollectionName(document.getClass()));
		collection.save(dbObject); 
		return document;
	}
	
	public <T> T insertOrUpdate(T document) {
		DBCollection collection = mongoDB.getCollection(mapper.getCollectionName(document.getClass()));
		BasicDBObject dbObject = mapper.documentToDBObject(document);
		collection.update(new BasicDBObject("_id", dbObject.get("_id")), dbObject, true, false);
		
		return document;
	}

	public <T> T update(T document) {
		DBCollection collection = mongoDB.getCollection(mapper.getCollectionName(document.getClass()));
		BasicDBObject dbObject = mapper.documentToDBObject(document);
		Object id = mapper.retrieveIdObject(document);
		collection.update(new BasicDBObject("_id", id), dbObject);
		
		return document;
	}

	public <T> void delete(T document) {
		DBCollection collection = mongoDB.getCollection(mapper.getCollectionName(document.getClass()));
		Object id = mapper.retrieveIdObject(document);
		collection.remove(new BasicDBObject("_id", new ObjectId(id.toString()))); 
	}

	public <T> List<T> list(Class<T> document) {
		List<T> list = new ArrayList<T>();
		DBCollection collection = mongoDB.getCollection(mapper.getCollectionName(document));
		DBCursor cursor = collection.find();
		for (DBObject dbObject : cursor) {
			list.add(document.cast(mapper.dbObjectToDocument(document, dbObject)));
		}
		
		return list;
	}

	public <T> List<T> list(Class<T> document, QueryData queryData) {	
		List<T> list = new ArrayList<T>();
		DBCursor cursor = processQueryClauses(document, queryData);
		for (DBObject dbObject : cursor) {
			list.add(document.cast(mapper.dbObjectToDocument(document, dbObject)));
		}
		
		return list;
	}

	public <T> T first(Class<T> document) {
		DBCollection collection = mongoDB.getCollection(mapper.getCollectionName(document));
		DBCursor limit = collection.find().limit(1);

		return document.cast(mapper.dbObjectToDocument(document, limit.next()));
	}

	public <T> T first(Class<T> document, QueryData queryData) {
		DBCursor cursor = processQueryClauses(document, queryData);
		return document.cast(mapper.dbObjectToDocument(document, cursor.next()));
	}

	public <T> T last(Class<T> document) {
		DBCollection collection = mongoDB.getCollection(mapper.getCollectionName(document));
		DBCursor limit = collection.find().sort(new BasicDBObject("_id", -1)).limit(1);

		return document.cast(mapper.dbObjectToDocument(document, limit.toArray(1).get(0)));
	}

	public <T> T last(Class<T> document, QueryData queryData) {
		DBCursor cursor = processQueryClauses(document, queryData).sort(new BasicDBObject("_id", -1)).limit(1);
		return document.cast(mapper.dbObjectToDocument(document, cursor.next()));
	}
	
	public Number count(Class<?> document) {
		return mongoDB.getCollection(mapper.getCollectionName(document)).count();
	}

	public Number count(Class<?> document, QueryData queryData) {
		return processQueryClauses(document, queryData).count();
	}
	
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	public void close() {
		mongoClient.close();
	}
	
	private void fetchClientProprietes() {
		String host = CentaurusConfig.getInstance().getDbHost();
		int port = Integer.valueOf(CentaurusConfig.getInstance().getDbPort());
		String dbName = CentaurusConfig.getInstance().getDbName();
		String user = CentaurusConfig.getInstance().getDbUsername();
		String password = CentaurusConfig.getInstance().getDbPassword();
		
		try {
			log.info(String.format("Connecting to database %s:%d %s", host, port, dbName));
			mongoClient = new MongoClient(new ServerAddress(host, port));
			mongoDB = mongoClient.getDB(dbName);
			if(user != null && password != null){
				boolean auth = mongoDB.authenticate(user, password.toCharArray());
				if(!auth){
					throw new CentaurusException("Database wrong username or password");
				}	
			}
			log.info(String.format("Connecting established to %s:%d %s", host, port, dbName));
		} catch (UnknownHostException e) {
			log.error(String.format("Cannot connect to %s:%d %s", host, port, dbName));
			throw new CentaurusException(String.format("Cannot connect to %s:%d %s", host, port, dbName), e);
		}
	}
 
	private DBCursor processQueryClauses(Class<?> document, QueryData queryData){
		DBCollection collection = mongoDB.getCollection(mapper.getCollectionName(document));
		DBCursor cursor = collection.find();
		
		if(!queryData.getExpressionList().isEmpty()){
			cursor = collection.find(queryProcessor.processWhereClause(queryData));
		}
		if(queryData.getOffset() != null){
			cursor.skip(queryData.getOffset().intValue());
		}
		if(queryData.getLimit() != null){
			cursor.limit(queryData.getLimit().intValue());
		}
		if(queryData.getSortOption() != null){
			cursor.sort(queryProcessor.processSortClause(queryData));
		}
		
		return cursor;
	}
}
