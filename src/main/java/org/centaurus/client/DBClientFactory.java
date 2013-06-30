package org.centaurus.client;

import org.centaurus.client.mongodb.MongoDBClient;
import org.centaurus.client.mongodb.MongoDBMapper;
import org.centaurus.enums.DBType;

/**
 * 
 * @author Vladislav Socolov
 */
public class DBClientFactory {

	public static DBClient buildDBClient(DBType db) {
		DBClient client = null;
		switch (db) {
		case MongoDB:
			client = new MongoDBClient(new MongoDBMapper());
			break;
		case CouchDB:
			//TODO in future we'll add CouchDB
			break;
		}
		
		return client;
	} 
}
