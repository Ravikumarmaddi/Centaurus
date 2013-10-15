package org.centaurus.client.couchdb;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.centaurus.annotations.Id;
import org.centaurus.client.Mapper;
import org.centaurus.exceptions.CentaurusMappingException;

import com.google.gson.JsonObject;

/**
 * 
 * @author Vladislav Socolov
 */
public class CouchDBMapper extends Mapper {

	@SuppressWarnings("unchecked")
	public <T> T documentToDBObject(Object document) {
		Class<? extends Object> clazz = document.getClass();
		if(isMapped(clazz)){
			JsonObject dbObject = new JsonObject();
			dbObject.addProperty("collection_name", getCollectionName(clazz));// define collection name
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
								if(type.equals(Integer.class)){
									dbObject.addProperty(annotName, (Integer)value);
								} else if(type.equals(Long.class)){
									dbObject.addProperty(annotName, (Long)value);
								} else if (type.equals(Byte.class)) {
									dbObject.addProperty(annotName, (Byte)value);
								} else if (type.equals(Double.class)) {
									dbObject.addProperty(annotName, (Double)value);
								} else if (type.equals(Float.class)) {
									dbObject.addProperty(annotName, (Float)value);
								} else if (type.equals(Boolean.class)) {
									dbObject.addProperty(annotName, (Boolean)value);
								} else if(type.equals(Date.class)) {
									SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", Locale.ROOT);
									dbObject.addProperty(annotName, formatter.format(value));
								} else {
									dbObject.addProperty(annotName, value.toString());
								}	
								break;
							} else if(annotation instanceof Id) {
								Id annot = (Id) annotation;
								if(!annot.defaultId()) { 
									if (type.equals(String.class)) {
										dbObject.addProperty(annot.name(), value.toString());
									} else {
										throw new CentaurusMappingException(String.format("%s document must have id of type String", clazz.getName()));
									}
								}
								break;
							} 
						}
					}
				} catch(Exception e){
					throw new CentaurusMappingException(String.format("Cannot map %s field of % class.", name, clazz.getName()));
				}
			}
			
			return (T) dbObject;
		} else {
			throw new CentaurusMappingException(String.format("%s is not mapped", clazz.getName()));
		}
	}

	public <T> T dbObjectToDocument(Class<T> document, Object dbObject) {
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
