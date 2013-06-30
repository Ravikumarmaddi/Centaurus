package org.centaurus;

import java.io.Serializable;

import org.centaurus.exceptions.CentaurusException;

/**
 * 
 * @author Vladislav Socolov
 */
public interface SessionFactory extends Serializable{
	
	public Session openSession() throws CentaurusException;
	
	public Session getCurrentSession() throws CentaurusException;
	
}
