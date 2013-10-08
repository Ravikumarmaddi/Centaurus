package org.centaurus.client.couchdb;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.centaurus.client.Mapper;
import org.centaurus.exceptions.CentaurusMappingException;

import com.google.gson.JsonObject;

/**
 * 
 * @author Vladislav Socolov
 */
public class CouchDBMapper extends Mapper {

	public <T> T documentToDBObject(Object document) {
		Class<? extends Object> clazz = document.getClass();
		if(isMapped(clazz)){
			JsonObject dbObject = new JsonObject();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				Class<?> type = field.getType();
				String name = field.getName();
				try {
					Object value = field.get(document);
					if(value != null) {
						Annotation[] declaredAnnotations = field.getDeclaredAnnotations();
						for (Annotation annotation : declaredAnnotations) {
							String annotName = null;
							if(annotation instanceof org.centaurus.annotations.Field) {
								org.centaurus.annotations.Field annot = (org.centaurus.annotations.Field) annotation;
								annotName = annot.name().equals("") ? name : annot.name(); 
								//dbObject.addProperty(annotName, (Object)type.cast(value));
								break;
							}
						}
					}
				} catch(Exception e){
					throw new CentaurusMappingException(String.format("Cannot map %s field of % class.", name, clazz.getName()));
				}
			}
		} else {
			throw new CentaurusMappingException(String.format("%s is not mapped", clazz.getName()));
		}
		return null;
	}

	public <T> T dbObjectToDocument(Class<T> document, Object dbObject) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isMapped(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getCollectionName(Class<?> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T parseDBTypesToJavaTypes(Class<T> type, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object retrieveIdObject(Object document) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T documentListToDBList(Object documentList) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T dbObjectListToDocumentList(Class<T> type, Object dbObjectList,
			Field field) {
		// TODO Auto-generated method stub
		return null;
	}

}
