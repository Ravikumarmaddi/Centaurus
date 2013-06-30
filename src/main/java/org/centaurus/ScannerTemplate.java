package org.centaurus;

import java.util.Set;

import org.centaurus.exceptions.ConfigurationException;
import org.centaurus.exceptions.CentaurusException;

/**
 * 
 * @author Vladislav Socolov
 */
public abstract class ScannerTemplate {

	public abstract Set<Class<?>> getMappedClasses();
	
	public abstract void setMappedClasses(Set<Class<?>> mappedClasses);
	
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
