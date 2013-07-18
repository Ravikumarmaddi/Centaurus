package org.centaurus.client;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * 
 * @author Vladislav Socolov
 */
public interface Mapper {
	
	@SuppressWarnings("unchecked")
	public static final Set<Class<?>> WRAPPER_TYPES = new HashSet<Class<?>>(Arrays.asList(
		    Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class));
	
	public <T> T documentToDBObject(Object document);
	
	public <T> T dbObjectToDocument(Class<T> document, Object dbObject);
	
	public boolean isMapped(Class<?> clazz);
	
	public String getCollectionName(Class<?> clazz);
	
	public <T> T parseDBTypesToJavaTypes(Class<T> type, Object value);
	
	public Object retrieveIdObject(Object document);
	
}
