package org.centaurus.client;

import org.centaurus.ScannerTemplate;

/**
 * 
 * @author Vladislav Socolov
 */
public interface Mapper {

	public <T> T documentToDBObject(Object document);
	
	public <T> T dbObjectToDocument(Object dbObject);
	
	public ScannerTemplate getScanner();

	public void setScanner(ScannerTemplate scanner);
	
	public boolean isMapped(Class<?> clazz);
	
	public String getCollectionName(Class<?> clazz);
	
}
