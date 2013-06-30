package org.centaurus;

import org.centaurus.client.DBClient;
import org.centaurus.client.DBClientFactory;
import org.centaurus.configuration.CentaurusConfig;
import org.centaurus.enums.DBType;
import org.centaurus.exceptions.CentaurusException;

/**
 * 
 * @author Vladislav Socolov
 */
public class SessionImpl implements Session {
	private static final long serialVersionUID = -2723525933848519576L;
	
	private DBClient dbClient;
	
	SessionImpl() throws CentaurusException{
		DBType db = CentaurusConfig.getInstance().getDbTypeEnum();
		dbClient = DBClientFactory.buildDBClient(db);
	}
	
	SessionImpl(ScannerTemplate scanner) throws CentaurusException{
		DBType db = CentaurusConfig.getInstance().getDbTypeEnum();
		dbClient = DBClientFactory.buildDBClient(db);
	}
	
	public <T> T insert(T document) {
		return dbClient.insert(document);
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
		dbClient.delete(document);
	}

	public void clear() {
		// TODO Auto-generated method stub
		
	}

	public void close() throws CentaurusException {
		// TODO Auto-generated method stub
		
	}

}
