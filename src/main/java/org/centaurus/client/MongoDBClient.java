package org.centaurus.client;

import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.centaurus.configuration.CentaurusConfig;
import org.centaurus.exceptions.CentaurusException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

/**
 * 
 * @author Vladislav Socolov
 */
public class MongoDBClient implements DBClient {
	private static final long serialVersionUID = 2016785660109383037L;
	private static Logger log = Logger.getLogger(MongoDBClient.class);
	
	private MongoClient mongoClient;
	private DB mongoDB;
	private Mapper mapper;
	
	public MongoDBClient() {
		fetchClientProprietes();
	}
	
	public <T> T insert(T document) {
		DBObject dbObject = mapper.documentToDBObject(document);
		DBCollection collection = mongoDB.getCollection(mapper.getCollectionName(document.getClass()));
		collection.save(dbObject); 
		return document;
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
		DBObject dbObject = mapper.documentToDBObject(document);
		DBCollection collection = mongoDB.getCollection(mapper.getCollectionName(document.getClass()));
		collection.remove(dbObject); 
	}

	public void clear() {
		// TODO Auto-generated method stub
		
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}
	
	public Mapper getMapper() {
		return mapper;
	}

	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
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
 
}
