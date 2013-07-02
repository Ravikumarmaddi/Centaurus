package org.centaurus;

import java.io.Serializable;

import org.centaurus.dql.Query;
import org.centaurus.exceptions.CentaurusException;

/**
 * 
 * @author Vladislav Socolov
 */
public interface Session extends Serializable{

	public <T> T insert(T document);
	
	public <T> T insertOrUpdate(T document);
	
	public <T> T update(T document);
	
	public <T> void delete(T document);
	
	public Query createQuery(Class<?> document);
	
	public void clear();
	
	public void close() throws CentaurusException;
	
}
