package org.centaurus;

import org.centaurus.exceptions.CentaurusException;
import org.centaurus.exceptions.ConfigurationException;

/**
 * 
 * @author Vladislav Socolov
 */
public abstract class ScannerTemplate {
	
	protected abstract void scanConfigurationFile() throws ConfigurationException;
	
	protected abstract void scanPackages() throws ConfigurationException;
	
	protected abstract void scanClasses();
	
	public void scanProject(){
		try {
			scanConfigurationFile(); 
			scanPackages();
			scanClasses();
		} catch (ConfigurationException e) {
			throw new CentaurusException("Cannot perform scanning project.", e);
		}
	}
	
}
