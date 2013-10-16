package org.centaurus.client;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.centaurus.annotations.Document;
import org.centaurus.configuration.CentaurusConfig;


/**
 * 
 * @author Vladislav Socolov
 */
public abstract class Mapper {
	
	@SuppressWarnings("unchecked")
	public static final Set<Class<?>> WRAPPER_TYPES = new HashSet<Class<?>>(Arrays.asList(
							    Boolean.class, Character.class, Byte.class, Short.class, Integer.class, 
							    Long.class, Float.class, Double.class, Void.class, String.class));
	
	public abstract <T> T documentToDBObject(Object document);
	
	public abstract <T> T dbObjectToDocument(Class<T> document, Object dbObject);
	
	public abstract <T> T parseDBTypesToJavaTypes(Class<T> type, Object value);
	
	public abstract Object retrieveIdObject(Object document);
	
	public abstract <T> T documentListToDBList(Class<T> returnedType, Object documentList);
	
	public abstract <T> T dbObjectListToDocumentList(Class<T> type, Object dbObjectList, Field field);
	
	public String getCollectionName(Class<?> clazz) {
		if(isMapped(clazz)) {
			Document annotation = clazz.getAnnotation(Document.class);
			return annotation.collection().equals("") ? clazz.getSimpleName() : annotation.collection();
		}	
		return null;
	}
	
	public boolean isMapped(Class<?> clazz) {
		if(CentaurusConfig.getInstance().getMappedClasses().contains(clazz)){
			return true;
		}
		return false;
	}
}
