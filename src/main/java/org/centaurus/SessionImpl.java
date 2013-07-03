package org.centaurus;

import org.centaurus.client.DBClient;
import org.centaurus.client.DBClientFactory;
import org.centaurus.configuration.CentaurusConfig;
import org.centaurus.dql.Query;
import org.centaurus.dql.QueryImpl;
import org.centaurus.enums.DBType;
import org.centaurus.exceptions.CentaurusException;

/**
 * 
 * @author Vladislav Socolov
 */
public class SessionImpl implements Session {
	private static final long serialVersionUID = -2723525933848519576L;

	private DBClient dbClient;

	SessionImpl() throws CentaurusException {
		DBType db = CentaurusConfig.getInstance().getDbTypeEnum();
		dbClient = DBClientFactory.buildDBClient(db);
	}

	public <T> T insert(T document) {
		return dbClient.insert(document);
	}

	public <T> T insertOrUpdate(T document) {
		return dbClient.insertOrUpdate(document);
	}

	public <T> T update(T document) {
		return dbClient.update(document);
	}

	public <T> void delete(T document) {
		dbClient.delete(document);
	}

	public void clear() {
		dbClient.clear();
	}

	public void close() throws CentaurusException {
		dbClient.close();
	}

	public <T> Query<T> createQuery(Class<T> document) {
		return new QueryImpl<T>(document, dbClient);
	}

}
