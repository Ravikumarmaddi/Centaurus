package org.centaurus;

import org.centaurus.exceptions.CentaurusException;

/**
 * 
 * @author Vladislav Socolov
 */
public class SessionFactoryImpl implements SessionFactory{
	private static final long serialVersionUID = 6295585436584281817L;
	
	private ScannerTemplate scanner;
	private Session session;
	
	public SessionFactoryImpl(){
		scanner = new Scanner();
		scanner.scanProject();
	}
	
	public Session openSession() throws CentaurusException {
		session = new SessionImpl();
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
