package org.centaurus.client.mongodb;

import org.centaurus.Scanner;
import org.centaurus.ScannerTemplate;
import org.centaurus.Session;
import org.centaurus.SessionFactory;
import org.centaurus.SessionImpl;
import org.centaurus.client.DBClient;
import org.centaurus.exceptions.CentaurusException;

/**
 * 
 * @author Vladislav Socolov
 */
public class SessionFactoryImpl implements SessionFactory {
	private static final long serialVersionUID = 6295585436584281817L;
	
	private ScannerTemplate scanner;
	private Session session;
	private DBClient dbClient;
	
	public SessionFactoryImpl(){
		scanner = new Scanner();
		scanner.scanProject();
		dbClient = new MongoDBClient(new MongoDBMapper());
	}
	
	public Session openSession() throws CentaurusException {
		session = new SessionImpl(dbClient);
		return session;
	}

	public Session getCurrentSession() throws CentaurusException {
		if(session != null){
			return session;
		}
		return null;
		//TODO method is not finished
	}

}
