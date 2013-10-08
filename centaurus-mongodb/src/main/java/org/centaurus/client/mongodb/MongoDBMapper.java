package org.centaurus.client.mongodb;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.bson.types.ObjectId;
import org.centaurus.annotations.Array;
import org.centaurus.annotations.Embedded;
import org.centaurus.annotations.Id;
import org.centaurus.client.Mapper;
import org.centaurus.exceptions.CentaurusMappingException;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


/**
 * 
 * @author Vladislav Socolov
 */
public class MongoDBMapper extends Mapper {
	
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
					if(value != null) {
						Annotation[] declaredAnnotations = field.getDeclaredAnnotations();
						for (Annotation annotation : declaredAnnotations) {
							String annotName = null;
							if(annotation instanceof org.centaurus.annotations.Field) {
								org.centaurus.annotations.Field annot = (org.centaurus.annotations.Field) annotation;
								annotName = annot.name().equals("") ? name : annot.name(); 
								dbObject.put(annotName, type.cast(value));
								break;
							} else if(annotation instanceof Id) {
								Id annot = (Id) annotation;
								if(annot.defaultId()) { //create basic mongo ObjectId
									dbObject.put(annot.name(), new ObjectId(type.cast(value).toString()));
								} else { 
									dbObject.put(annot.name(), type.cast(value));		
								}
								break;
							} else if(annotation instanceof Array) {
								Array annot = (Array) annotation;
								annotName = annot.name().equals("") ? name : annot.name();
								dbObject.put(annotName, documentListToDBList(value));
								break;
							} else if(annotation instanceof Embedded) {
								Embedded annot = (Embedded) annotation;
								annotName = annot.name().equals("") ? name : annot.name();
								dbObject.put(annotName, documentToDBObject(value));
								break;
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
		if(isMapped(document)){
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
							String annotName = null;
							if(annotation instanceof org.centaurus.annotations.Field) {
								org.centaurus.annotations.Field annot = (org.centaurus.annotations.Field) annotation;
								annotName = annot.name().equals("") ? field.getName() : annot.name();
								if(annotName.equals(key)){
									field.set(bean, parseDBTypesToJavaTypes(type, dbObj.get(key)));
								}
								break;
							} else if(annotation instanceof Id) {
								Id annot = (Id) annotation;
								annotName = annot.name().equals("") ? field.getName() : annot.name();
								if(annotName.equals(key)){
									field.set(bean, parseDBTypesToJavaTypes(type, dbObj.get(key)));	
								}
								break;
							} else if(annotation instanceof Array) {
								Array annot = (Array) annotation;
								annotName = annot.name().equals("") ? field.getName() : annot.name();
								if(annotName.equals(key)) {
									field.set(bean, dbObjectListToDocumentList(type, dbObj.get(key), field));
								}
								break;
							} else if(annotation instanceof Embedded) {
								Embedded annot = (Embedded) annotation;
								annotName = annot.name().equals("") ? field.getName() : annot.name();
								if(annotName.equals(key)) {
									Object embeddedObject = dbObjectToDocument(type, dbObj.get(key));
									field.set(bean, type.cast(embeddedObject));
								}
								break;
							}
						}
					}
				}
				return (T) bean;
			} catch (Throwable e) {
				throw new CentaurusMappingException("Cannot map dbObject to real object", e);
			}

		} else {
			throw new CentaurusMappingException(String.format("%s is not mapped", document.getName()));
		}
	}
	
	public Object retrieveIdObject(Object document) {
		Class<?> clazz = document.getClass();
		if(isMapped(clazz)) {
			try {
				Field[] fields = clazz.getDeclaredFields();
				for (Field field : fields) {
					field.setAccessible(true);
					Id annotation = field.getAnnotation(Id.class);
					if(annotation != null) {
						Object id = field.get(document);
						if(id != null) {
							return annotation.defaultId() == true ? new ObjectId(id.toString()) : id;
						}
						throw new CentaurusMappingException("Cannot delete document withod Id field specified.");
					}
				}
			} catch (Exception e) {
				throw new CentaurusMappingException(String.format("Cannot retireve Id object from document: %s", document.toString()));
			}
		}	
		throw new CentaurusMappingException(String.format("%s is not mapped", clazz.getName()));
	}
	
	@SuppressWarnings("unchecked")
	public <T> T documentListToDBList(Object documentList) {
		Class<? extends Object> clazz = documentList.getClass();
		BasicDBList basicDBList = new BasicDBList();

		List<Object> iterableList = clazz.isArray() ? Arrays.asList((Object[]) documentList) : (List<Object>) documentList;

		if(iterableList != null && !iterableList.isEmpty()){
			Class<?> componentType = iterableList.get(0).getClass();
			
			for (Object document : iterableList) {
				if (componentType.isPrimitive() || WRAPPER_TYPES.contains(componentType)) {
					basicDBList.add(document);
				} else {
					basicDBList.add(documentToDBObject(document));
				}
			}
		}
			
		return (T) (basicDBList.size() == 0 ? null : basicDBList);
	}

	@SuppressWarnings("unchecked")
	public <T> T dbObjectListToDocumentList(Class<T> type, Object dbObjectList, Field field) {
		BasicDBList dbList = (BasicDBList) dbObjectList;
		Class<?> componentType = null;
				
		if(type.isArray()){
			componentType = type.getComponentType();
			if(componentType.isPrimitive() || WRAPPER_TYPES.contains(componentType)){
				return type.cast(dbList.toArray((T[]) java.lang.reflect.Array.newInstance(componentType, 1)));
			} else {
				T[] arr = (T[]) java.lang.reflect.Array.newInstance(componentType, dbList.size());
				for (int i = 0; i < dbList.size(); i++) {
					arr[i] = (T) dbObjectToDocument(componentType, dbList.get(i));
				}
				return (T) arr;
			}
		} else {
			ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
			if(parameterizedType == null){
				componentType = type.getComponentType();	
			} else {
				componentType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
			}
			if(componentType.isPrimitive() || WRAPPER_TYPES.contains(componentType)){
				return type.cast(dbList);	
			} else {
				List<T> list = new ArrayList<T>();
				for (int i = 0; i < dbList.size(); i++) {
					list.add((T) dbObjectToDocument(componentType, dbList.get(i)));
				}
				return (T) list;
			}
		}
	}
	
	public <T> T parseDBTypesToJavaTypes(Class<T> type, Object value) {
		try {
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
			} else if(type.equals(Date.class)) {
				SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", Locale.ROOT);
				return type.cast(formatter.parse(value.toString()));
			} else {
				return type.cast(value.toString());
			}	
		} catch (Exception e) {
			throw new CentaurusMappingException(String.format("Cannot cast %s value to %s type", value.toString(), type.getName()));
		}
	}
	
}
