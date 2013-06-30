package org.centaurus.client;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.centaurus.ScannerTemplate;
import org.centaurus.annotations.Document;
import org.centaurus.annotations.Id;
import org.centaurus.configuration.CentaurusConfig;
import org.centaurus.exceptions.CentaurusException;

import com.mongodb.BasicDBObject;


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
					throw new CentaurusException(String.format("Cannot map %s field of % class.", name, clazz.getName()));
				}
			}
			
			return (T) dbObject;
		} else {
			throw new CentaurusException(String.format("%s is not mapped", clazz.getName()));
		}
	}

	public <T> T dbObjectToDocument(Object dbObject) {
		// TODO Auto-generated method stub
		return null;
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
	
}
