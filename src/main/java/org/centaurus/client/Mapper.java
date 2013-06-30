package org.centaurus.client;


/**
 * 
 * @author Vladislav Socolov
 */
public interface Mapper {

	public <T> T documentToDBObject(Object document);
	
	public <T> T dbObjectToDocument(Object dbObject);
	
	public boolean isMapped(Class<?> clazz);
	
	public String getCollectionName(Class<?> clazz);
	
}
