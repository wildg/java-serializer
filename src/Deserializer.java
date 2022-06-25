import java.lang.reflect.*;
import java.util.*;
import javax.json.*;

import Solution.ObjectA;

public class Deserializer {

    public static Object deserializeObject(JsonObject json_object) throws Exception {
    	
    	// Get the array found at objects
        JsonArray object_list = json_object.getJsonArray("objects");
        Map object_tracking_map = new HashMap();
        createInstances(object_tracking_map, object_list);
        assignFieldValues(object_tracking_map, object_list);
        return object_tracking_map.get("0");
    }

	private static void createInstances(Map trackingMap, JsonArray jsonArr) throws Exception {
		
		// Iterate over object list:
		for (int i = 0; i < jsonArr.size(); i++) {
			
			// Get the Json object at index i
			JsonObject jsonObj = jsonArr.getJsonObject(i);
			
			// Get the class of the object
			Class objClass = Class.forName(jsonObj.getString("class"));
			
			// Create an instance of the object
			Object obj = null;
			
			// If the object is an array:
			if (objClass.isArray()) {
				
				// Get the array type
				Class arrType = objClass.getComponentType();
				
				// Get the length of the array as string
				String lnString = jsonObj.getString("length");
				
				// Get the length as an integer
				int ln = Integer.valueOf(lnString);
				
				// If the array type is of integer:
				if (arrType.equals(int.class)) {
					
					// Create a new integer array of the appropriate length
					obj = new int[ln];
				}
				
				// If the array type is of ObjectA:
				else if (arrType.equals(ObjectA.class)) {
					
					// Create a new ObjectA array of the appropriate length
					obj = new ObjectA[ln];
				}
				
				// If the array type is of Object:
				else if (arrType.equals(Object.class)) {
					
					// Create a new Object array of the appropriate length
					obj = new Object[ln];
				}
			}
			
			// Otherwise:
			else {
				
				// Get the default constructor
				Constructor constructor = objClass.getDeclaredConstructor();
				
				// Set the constructor to accessible
				constructor.setAccessible(true);
				
				// Create a new instance of object with constructor
				obj = constructor.newInstance();
			}
			
			// Add object to tracking map
			trackingMap.put(jsonObj.getString("id"), obj);
		}
	}

    private static void assignFieldValues(Map object_tracking_map, JsonArray object_list) throws Exception {
    	
    	//Iterate over object list:
    	for (int i = 0; i < object_list.size(); i++) {
    		
    		// Get the Json object at index i
    		JsonObject jsonObj = object_list.getJsonObject(i);
    		
    		// Get the class of the object
    		Class objClass = Class.forName(jsonObj.getString("class"));
    		
    		// Get the instance of the object
    		Object obj = object_tracking_map.get(jsonObj.getString("id"));
    		
    		// If the object is an array:
    		if (objClass.isArray()) {
    			
    			// Get the entries of the json array
    			JsonArray jsonArr = jsonObj.getJsonArray("entries");
    			
    			// Get the array's component type
				Class arrCompType = objClass.getComponentType();
    			
    			// Iterate over json array:
    			for (int j = 0; j < jsonArr.size(); j++) {
    				
    				// Get the json object representation of the element at index j
    				JsonObject jsonElem = jsonArr.getJsonObject(j);
    				
    				// If the array is made up of primitives:
    				if (arrCompType.isPrimitive()) {
    					
    					// Get the value of the element
    					String valString = jsonElem.getString("value");
    					
    					// If the array is made up of integers:
        				if (arrCompType.equals(int.class)) {
        					
        					// Set the value of the element at index j accordingly
        					Array.set(obj, j, Integer.valueOf(valString));
        				}
    				}
    				
    				// Otherwise:
    				else {
    					
    					// Get the value of the element
    					String value = jsonElem.getString("reference");
    					
    					// initialize the value of the element at j to null
    					Array.set(obj, j, null);
    					
    					// If the value does not equal null:
    					if (!value.equals("null")) {
    						
    						// Set the value of the element at index j accordingly
    						Array.set(obj, j, object_tracking_map.get(value));
    					}
    				}
    			}
    		}
    		
    		// Otherwise:
    		else {
    			
    			// Get the fields of the json array
				JsonArray jsonArr = jsonObj.getJsonArray("fields");
				
				// Iterate over field array:
				for (int j = 0; j < jsonArr.size(); j++) {
					
					// Get the json object representation of the field at index j
					JsonObject fieldJson = jsonArr.getJsonObject(j);
					
					// Get the class name of the declaring class
					String declaringClassName = fieldJson.getString("declaring class");
					
					// Get the declaring class
					Class declaringClass = Class.forName(declaringClassName);
					
					// Get the name of the field
					String fieldName = fieldJson.getString("name");
					
					// Get the field from the declaring class
					Field f = declaringClass.getDeclaredField(fieldName);
					
					// Set the field to accessible
					f.setAccessible(true);
					
					// Get the field type
					Class fType = f.getType();
					
					// If the field type is primitive:
					if (fType.isPrimitive()) {
						
						// Get the value of the field:
						String value = fieldJson.getString("value");
						
						// If the field is an integer:
						if (fType.equals(int.class)) {
							
							// Set the value of the field to an integer
							f.set(obj, Integer.valueOf(value));
						}
						
						// If the field is an double:
						else if (fType.equals(double.class)) {
							
							// Set the value of the field to an double
							f.set(obj, Double.valueOf(value));
						}
						
						// If the field is an boolean:
						else if (fType.equals(boolean.class)) {
							
							// Set the value of the field to an boolean
							f.set(obj, Boolean.valueOf(value));
						}
					}
					
					// Otherwise:
					else {
						
						// Get the reference of the field:
						String value = fieldJson.getString("reference");
						
						// Set the field to the reference object
						f.set(obj, object_tracking_map.get(value));
					}
				}
    		}
    	}
    }

}