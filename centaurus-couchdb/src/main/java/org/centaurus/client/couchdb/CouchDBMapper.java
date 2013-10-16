package org.centaurus.client.couchdb;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
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

	public <T> T dbObjectListToDocumentList(Class<T> type, Object dbObjectList, Field field) {
		// TODO Auto-generated method stub
		return null;
	}

}
