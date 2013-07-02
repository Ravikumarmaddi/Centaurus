package org.centaurus.client.mongodb;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.centaurus.annotations.Document;
import org.centaurus.annotations.Id;
import org.centaurus.client.Mapper;
import org.centaurus.configuration.CentaurusConfig;
import org.centaurus.exceptions.CentaurusMappingException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


/**
 * 
 * @author Vladislav Socolov
 */
public class MongoDBMapper implements Mapper {
	
	@SuppressWarnings("unchecked")
	public <T> T documentToDBObject(Object document) {
		Class<? extends Object> clazz = document.getClass();
		if(isMapped(clazz)){
			BasicDBObject dbObject = new BasicDBObject();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				Class<?> type = field.getType();
				String name = field.getName();
				try {
					Object value = field.get(document);
					Annotation[] declaredAnnotations = field.getDeclaredAnnotations();
					for (Annotation annotation : declaredAnnotations) {
						if(annotation instanceof org.centaurus.annotations.Field){
							org.centaurus.annotations.Field fld = (org.centaurus.annotations.Field) annotation;
							String fieldAnnotationName = fld.name().equals("") ? name : fld.name(); 
							dbObject.put(fieldAnnotationName, type.cast(value));
							break;
						}else if(annotation instanceof Id){
							Id id = (Id) annotation;
							if(value != null){
								dbObject.put(id.name(), type.cast(value));	
							}
						}
					}
				} catch (Exception e) {
					throw new CentaurusMappingException(String.format("Cannot map %s field of % class.", name, clazz.getName()));
				}
			}
			
			return (T) dbObject;
		} else {
			throw new CentaurusMappingException(String.format("%s is not mapped", clazz.getName()));
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T dbObjectToDocument(Class<T> document, Object dbObject) {
		if(!isMapped(document)){
			try {
				Object bean = document.getConstructor().newInstance();
				DBObject dbObj = (DBObject) dbObject;
				for (String key : dbObj.keySet()) {
					Field[] fields = bean.getClass().getDeclaredFields();
					for (Field field : fields) {
						field.setAccessible(true);
						Class<?> type = field.getType();
						Annotation[] annotations = field.getAnnotations();
						for (Annotation annotation : annotations) {
							if(annotation instanceof org.centaurus.annotations.Field){
								org.centaurus.annotations.Field annot = (org.centaurus.annotations.Field) annotation;
								if(annot.name().equals(key)){
									field.set(bean, parseDBTypesToJavaTypes(type, dbObj.get(key)));	
								}
							} else if (annotation instanceof Id){
								Id annot = (Id) annotation;
								if(annot.name().equals(key)){
									field.set(bean, parseDBTypesToJavaTypes(type, dbObj.get(key)));	
								}
							}
						}
					}
				}
				return (T) bean;
			} catch (Throwable e) {
				throw new CentaurusMappingException("Cannot map dbObject to real object");
			}

		} else {
			throw new CentaurusMappingException(String.format("%s is not mapped", document.getName()));
		}
	}

	public boolean isMapped(Class<?> clazz) {
		if(CentaurusConfig.getInstance().getMappedClasses().contains(clazz)){
			return true;
		}
		return false;
	}

	public String getCollectionName(Class<?> clazz) {
		if(isMapped(clazz)) {
			Document annotation = clazz.getAnnotation(Document.class);
			return annotation.collection().equals("") ? clazz.getSimpleName() : annotation.collection();
		}	
		return null;
	}
	
	
	public <T> T parseDBTypesToJavaTypes(Class<T> type, Object value) {		
		if(type.equals(Integer.class)){
			return type.cast(((Double)Double.parseDouble(value.toString())).intValue());
		} else if(type.equals(Long.class)){
			return type.cast(((Double)Double.parseDouble(value.toString())).longValue());
		} else if (type.equals(Byte.class)) {
			return type.cast(((Double)Double.parseDouble(value.toString())).byteValue());
		} else if (type.equals(Double.class)) {
			return type.cast(Double.parseDouble(value.toString()));
		} else if (type.equals(Float.class)) {
			return type.cast(((Double)Double.parseDouble(value.toString())).floatValue());
		} else if (type.equals(Boolean.class)) {
			return type.cast(Boolean.parseBoolean(value.toString()));
		} else {
			return type.cast(value.toString());
		}
	}
}
