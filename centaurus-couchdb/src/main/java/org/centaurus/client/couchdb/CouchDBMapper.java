package org.centaurus.client.couchdb;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.centaurus.annotations.Array;
import org.centaurus.annotations.Embedded;
import org.centaurus.annotations.Id;
import org.centaurus.client.Mapper;
import org.centaurus.exceptions.CentaurusMappingException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
							} else if(annotation instanceof Array) {
								Array annot = (Array) annotation;
								annotName = annot.name().equals("") ? name : annot.name();
								dbObject.add(annotName, documentListToDBList(JsonElement.class, value));
								break;
							} else if(annotation instanceof Embedded) {
								Embedded annot = (Embedded) annotation;
								annotName = annot.name().equals("") ? name : annot.name();
								JsonObject jsonObject = (JsonObject) documentToDBObject(value);
								jsonObject.remove("collection_name");// remove collection_name field for embedded objects
								dbObject.add(annotName, jsonObject);
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

	@SuppressWarnings("unchecked")
	public <T> T dbObjectToDocument(Class<T> document, Object dbObject) {
		if(isMapped(document)){
			try {
				Object bean = document.getConstructor().newInstance();
				JsonObject dbObj = (JsonObject) dbObject;
				JsonObject value = (JsonElement) dbObj.get("id") != null ? (JsonObject) dbObj.get("value") : (JsonObject) dbObject;
				
				Field[] fields = document.getDeclaredFields();
				for (Field field : fields) {
					field.setAccessible(true);
					Class<?> type = field.getType();
					Annotation[] annotations = field.getAnnotations();
					for (Annotation annotation : annotations) {
						String annotName = null;
						if(annotation instanceof org.centaurus.annotations.Field) {
							org.centaurus.annotations.Field annot = (org.centaurus.annotations.Field) annotation;
							annotName = annot.name().equals("") ? field.getName() : annot.name();
							JsonElement jsonElement = value.get(annotName);
							if(jsonElement != null){
								field.set(bean, parseDBTypesToJavaTypes(type, jsonElement));
							}
							break;
						} else if(annotation instanceof Id) {
							Id annot = (Id) annotation;
							annotName = annot.name().equals("") ? field.getName() : annot.name();
							JsonElement jsonElement = value.get(annotName);
							if(jsonElement != null){
								field.set(bean, parseDBTypesToJavaTypes(type, jsonElement));
							}
							break;
						} else if(annotation instanceof Embedded) {
							Embedded annot = (Embedded) annotation;
							annotName = annot.name().equals("") ? field.getName() : annot.name();
							JsonElement jsonElement = value.get(annotName);
							if(jsonElement != null) {
								Object embeddedObject = dbObjectToDocument(type, jsonElement);
								field.set(bean, type.cast(embeddedObject));
							}
							break;
						} else if(annotation instanceof Array) {
							Array annot = (Array) annotation;
							annotName = annot.name().equals("") ? field.getName() : annot.name();
							JsonElement jsonElement = value.get(annotName);
							if(jsonElement != null) {
								field.set(bean, dbObjectListToDocumentList(type, jsonElement, field));
							}
							break;
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
							return id;
						}
						throw new CentaurusMappingException("Cannot retrieve id from object. Id is null.");
					}
				}
			} catch (Exception e) {
				throw new CentaurusMappingException(String.format("Cannot retireve Id object from document: %s", document.toString()));
			}
		}	
		throw new CentaurusMappingException(String.format("%s is not mapped", clazz.getName()));
	}

	@SuppressWarnings("unchecked")
	public <T> T documentListToDBList(Class<T> returnedType, Object documentList) {
		Class<? extends Object> documentListClazz = documentList.getClass();
		Gson gson = new Gson(); 
		
		List<Object> iterableList = documentListClazz.isArray() ? Arrays.asList((Object[]) documentList) : (List<Object>) documentList;

		if(iterableList != null && !iterableList.isEmpty()){
			Class<?> componentType = iterableList.get(0).getClass();
			if (componentType.isPrimitive() || WRAPPER_TYPES.contains(componentType)) {
				String json = gson.toJson(iterableList);
				JsonParser parser = new JsonParser();
				return (T) parser.parse(json);
			} else {
				JsonArray jsonArray = new JsonArray();
				for (Object document : iterableList) {
					JsonObject jsonObject = (JsonObject) documentToDBObject(document);
					jsonObject.remove("collection_name");// remove collection_name field for embedded objects
					jsonArray.add(jsonObject);
				}
				return (T) jsonArray;
			}
		}
		return null;
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
				return type.cast(formatter.parse(((JsonElement)value).getAsString()));
			} else {
				return type.cast(value.toString());
			}	
		} catch (Exception e) {
			throw new CentaurusMappingException(String.format("Cannot cast %s value to %s type", value.toString(), type.getName()));
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T dbObjectListToDocumentList(Class<T> type, Object dbObjectList, Field field) {
		JsonArray dbList = (JsonArray) dbObjectList;
		Class<?> componentType = null;
		Gson gson = new Gson();
		
		if(type.isArray()){
			componentType = type.getComponentType();
			if(componentType.isPrimitive() || WRAPPER_TYPES.contains(componentType)){
				return type.cast(gson.fromJson(dbList, type));
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
				return type.cast(gson.fromJson(dbList, type));	
			} else {
				List<T> list = new ArrayList<T>();
				for (int i = 0; i < dbList.size(); i++) {
					list.add((T) dbObjectToDocument(componentType, dbList.get(i)));
				}
				return (T) list;
			}
		}
	}

}
