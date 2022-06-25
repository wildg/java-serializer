

import java.util.*;
import javax.json.*;
import java.lang.reflect.*;

public class Serializer {

	/**
	 * Take an initialized object and return a serialized version of it as a JsonObject
	 * 
	 * @param object The object that will be serialized as a JsonObject
	 * @return The inputed object serialized as a JsonObject
	 * @throws Exception
	 */
    public static JsonObject serializeObject(Object object) throws Exception {
    	
    	// Create a Json array
        JsonArrayBuilder object_list = Json.createArrayBuilder();
        
        // Run serializeHelper on the object to update object_list
        serializeHelper(object, object_list, new IdentityHashMap());
        
        // Create a JsonObject builder to generate a Json object that will be returned
        JsonObjectBuilder json_base_object = Json.createObjectBuilder();
        
        // Add "objects" selection to Json object
        json_base_object.add("objects", object_list);
        
        // Build the jsonObject and return it
        return json_base_object.build();
    }

    /**
     * Recursive function that will serialize a given object and add it to an objectList
     * 
     * @param obj The object that will be serialized as a JsonObject 
     * @param objectList The JsonArray containing the object's data
     * @param trackingMap The map to track previously serialized items
     * @throws Exception
     */
    private static void serializeHelper(Object obj, JsonArrayBuilder objectList, Map trackingMap) throws Exception {
    	
    	// Get the object's id number as string
        String objectID = Integer.toString(trackingMap.size());
        
        // Add the object and objectID to tracking map
        trackingMap.put(obj, objectID);
        
        // Get the class of the object
        Class objClass = obj.getClass();
        
        // Create a JsonObject builder to store an object's data in Json form 
        JsonObjectBuilder object_info = Json.createObjectBuilder();
        
        // Add "class" selection to Json object
        object_info.add("class", objClass.getName());
        
        // Add "id" selection to Json object
        object_info.add("id", objectID);
        
        // If the object is an array:
        if (objClass.isArray()) {
        	
        	// Add "type" selection to Json object
        	object_info.add("type", "array");
        	
        	// Get the length of the object
        	int size = Array.getLength(obj);
        	
        	// Add "length" selection to Json object
        	object_info.add("length", Integer.toString(size));
        	
        	// Create an array of Json objects
        	JsonArrayBuilder jsonArr = Json.createArrayBuilder();
        	
        	// Create a jsonObject builder for each element in array
        	JsonObjectBuilder jsonElem = Json.createObjectBuilder();
        	
        	// Get the component type of the array
        	Class componentType = obj.getClass().getComponentType();
    		
        	// Loop through array entries
    		for (int i = 0; i < size; i++) {
    			
    			// Get the element found at index i
    			Object elem = Array.get(obj, i);
    			
    			// If the array item is null or the component type is primitive:
    			if (componentType.isPrimitive()) {
    				
    				// initialize the value to null
    				jsonElem.add("value", "null");
    				
    				// If the element is not null:
    				if (elem != null) {
    					
    					// Add the value
    					jsonElem.add("value", elem.toString());
    				}
    			}
    			
    			// Otherwise:
    			else {
    				
    				// Initialize the reference to null
    				jsonElem.add("reference", "null");
    				
    				// If the element is not null:
    				if (elem != null) {
    					
    					// Add the reference and iterate over the new object
            			jsonElem.add("reference", Integer.toString(trackingMap.size()));
            			serializeHelper(elem, objectList, trackingMap);
    				}
    			}
    			
    			// Add the element to the Json array
    			jsonArr.add(jsonElem);
    		}
    		
    		// Add Json array to "entries" selection on Json object
    		object_info.add("entries", jsonArr);
        }
        
        // Otherwise:
        else {
        	
        	// Add "type" selection to Json object
        	object_info.add("type", "object");
        	
        	// Create an array of Json objects
        	JsonArrayBuilder jsonArr = Json.createArrayBuilder();
        	
        	// Get array of declared fields
        	Field[] fields = objClass.getDeclaredFields();
        	
        	// Run the field iterator
        	fieldIterator(fields, obj, objectList, jsonArr, trackingMap);
            
            // If there is a super class:
            if (objClass.getSuperclass() != null) {
            	
            	// Get array of declared fields
            	fields = objClass.getSuperclass().getDeclaredFields();
            	
            	// Run the field iterator
            	fieldIterator(fields, obj, objectList, jsonArr, trackingMap);
            }
            
            // Add Json array to "fields" selection on Json object
            object_info.add("fields", jsonArr);
        }
        
        // Add json object to object list
        objectList.add(object_info);
    }
    
    // Iterate over the fields
    private static void fieldIterator(Field[] fields, Object obj, JsonArrayBuilder objectList, JsonArrayBuilder jsonArr, Map trackingMap) throws Exception {

        // Iterate over fields:
        for (Field f: fields) {
        	
        	// If the the modifier is not final:
        	if (!Modifier.isStatic(f.getModifiers())) {
        		
        		// Set the field to accessible
        		f.setAccessible(true);
        		
        		// Create a Json object for field information
                JsonObjectBuilder fieldJson = Json.createObjectBuilder();
                
                // Add "name" selection to Json object
                fieldJson.add("name", f.getName());
                
                // Add "declaring class" selection to Json object
                fieldJson.add("declaring class", f.getDeclaringClass().getName());
                
                // Get the value of the field
                Object val = f.get(obj);
                
                // Get the field type
                Class fType = f.getType();
                
                // If the field type is a primitive:
                if (fType.isPrimitive()) {
                	
                	// Add the value of the field
                	fieldJson.add("value", val.toString());
                }
                
                // Otherwise, the field type is not a primitive:
                else {
                	
                	// If the value is null:
                	if (val == null) {
                		fieldJson.add("reference", "null");
                	}
                	
                	// If the value is in the tracking map:
                	else if (trackingMap.get(val) != null) {
                		
                		// Only add the reference
            			fieldJson.add("reference", trackingMap.get(val).toString());
                	}
                	
                	// Otherwise:
                	else {
                		// Add the reference and iterate over the new object.
            			fieldJson.add("reference", Integer.toString(trackingMap.size()));
        				serializeHelper(val, objectList, trackingMap);
                	}
                }

                // Add the element to the Json array
                jsonArr.add(fieldJson);
        	}
        }
    }
}