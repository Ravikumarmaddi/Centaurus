package org.centaurus;

import org.centaurus.client.DBClient;
import org.centaurus.configuration.CentaurusConfig;
import org.centaurus.exceptions.CentaurusException;

/**
 * 
 * @author Vladislav Socolov
 */
public final class SessionFactory {

	private static ScannerTemplate scanner;
	private static Session session;
	private static DBClient dbClient;
	
	private SessionFactory(){
		super();
	}
	
	public static Session openSession() throws CentaurusException {
		scanAndFetchDBClient();
		session = new SessionImpl(dbClient);
		return session;
	}

	public static Session getCurrentSession() throws CentaurusException {
		if(session != null){
			return session;
		}
		return null;
		//TODO method is not finished
	}

	private static void scanAndFetchDBClient() {
		if(scanner == null) {
			scanner = new Scanner();
			scanner.scanProject();	
		}
		
		if(dbClient == null){
			try {
				Class<?> clazz = Class.forName(CentaurusConfig.getInstance().getClient());
				dbClient = (DBClient) clazz.getConstructor().newInstance();
			} catch (Throwable e) {
				throw new CentaurusException("Cannot retrieve and process ClientDriver class", e);
			}
		}
	}
}
