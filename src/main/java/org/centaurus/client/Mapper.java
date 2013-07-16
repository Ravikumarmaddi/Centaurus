package org.centaurus.client;


/**
 * 
 * @author Vladislav Socolov
 */
public interface Mapper {

	public <T> T documentToDBObject(Object document);
	
	public <T> T dbObjectToDocument(Class<T> document, Object dbObject);
	
	public boolean isMapped(Class<?> clazz);
	
	public String getCollectionName(Class<?> clazz);
	
	public <T> T parseDBTypesToJavaTypes(Class<T> type, Object value);
	
	public Object retrieveIdObject(Object document);
	
}
