import java.lang.reflect.*;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public class Inspector {
	
	private Map<String, Boolean> trackingMap = new HashMap<String, Boolean>();

    public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();
        inspectClass(c, obj, recursive, 0);
    }
    
    // Return a string of tabs of appropriate size
    public String tabString(int depth) {
    	
    	// Get the amount of tabs we need by repeating "\t" according to depth
		String tabs = "";
		
		// Loop through our depth value:
		for (int i = 0; i < depth; i++) {
			
			// Add a tab for every layer of depth
			tabs += "\t";
		}
		
		// Return tabs
		return tabs;
    }
    
    // Run when the initial class inspected was an array
    public void initialClassArray(Class c, Object obj, boolean recursive, int depth) {
    	
    	// Get the arrays component type class
		Class componentTypeClass = c.getComponentType();
		
		// Print the array using print array method on the initial object
		printArray(obj, componentTypeClass, recursive, depth);
    };
    
    // Prints the component type of an array
    public void printArray(Object arr, Class componentType, boolean recursive, int depth) {
		
		// Print the component's name
		System.out.println(tabString(depth) + " Component type: " + componentType.getTypeName());
		
		// Iterate through the array using the iterate array method
    	iterateArray(arr, componentType, recursive, depth);
    }
    
    // Iterates through an array to print its values
    public void iterateArray(Object arr, Class componentType, boolean recursive, int depth) {
    	
    	// Get the array's length
		int arrayLength = Array.getLength(arr);
		
		// Print the array's length
		System.out.println(tabString(depth) + " Length: " + arrayLength);
		
		// Loop through the array:
		for (int i = 0; i < arrayLength; i++) {
			
			// Get the array item at index i
			Object arrayItem = Array.get(arr, i);
			
			// Run array logic method
			arrayLogic(arrayItem, componentType, recursive, depth);
		}
    }
    
    // Array logic that is run on every array item
    public void arrayLogic(Object arrayItem, Class componentType, boolean recursive, int depth) {
    	
    	// If the array item is null or the component type is primitive:
		if ((arrayItem == null) || (componentType.isPrimitive())) {
			
			// Print the value
			System.out.println(tabString(depth) + " Value: " + arrayItem);
		}
		
		// Otherwise:
		else {
				
			// Print the array item with the hash code
			System.out.println(tabString(depth) + " Value: " + arrayItem + "@" + arrayItem.hashCode());
			
			// If recursive is true:
			if (recursive) {
				
				// Inspect the array item's class
				inspectClass(arrayItem.getClass(), arrayItem, recursive, depth + 1);
			}
		}
    }
    
    // Prints super class information
    public void superClassSection(Class c, Object obj, boolean recursive, int depth) {
    	
    	// Get the super class of our current class c
		Class superClass = c.getSuperclass();
		
		// If there is a super class
		if (superClass != null) {
			
			// If the super class has no fields, just return
			if (superClass.getDeclaredFields().length == 0) {
				System.out.println(tabString(depth) + "SuperClass: " + superClass.getName() + " (No fields)");
				return;
			}
			
			// Print the super class' name
			System.out.println(tabString(depth) + "SuperClass: " + superClass.getName());
			
			// Inspect the super class now
			inspectClass(superClass, obj, recursive, depth + 1);
		}
		
		// Otherwise:
		else {
			System.out.println(tabString(depth) + "SuperClass: NONE");
		}
    }
    
    // Print all modifiers
    public void printModifiers(int modifierValue, int depth) {
		
		// Print the modifier's name
		System.out.println(tabString(depth) + " Modifiers: " + Modifier.toString(modifierValue));
    }
    
    // Prints all field information
    public void fieldsSection(Class c, Object obj, boolean recursive, int depth) {
    	
    	// Get the list of fields
		Field[] fieldList = c.getDeclaredFields();
		
		// If there are fields
		if (fieldList.length > 0) {
			
			// For every field in the list
			for (Field fieldClass : fieldList) {
				
				// Set the field access to true
				fieldClass.setAccessible(true);
				
				// Get the modifier values as an integer representation
				int modifierValue = fieldClass.getModifiers();
				
				// If the modifier is not final:
				if (!Modifier.isStatic(modifierValue)) {
					
					// Print the method's name
					System.out.println(tabString(depth) + "Field: " + fieldClass.getName());
					
					// Get the field's type class
					Class fieldTypeClass = fieldClass.getType();
					
					// Print the field's type
					System.out.println(tabString(depth) + " Type: " + fieldTypeClass.getTypeName());
					
					// Print the modifier value using the print modifiers method
					printModifiers(modifierValue, depth);
					
					// Get the value of field class
					Object val = getFieldVal(fieldClass, obj);
					
					// Check the field logic on the value
					fieldLogic(val, fieldTypeClass, obj, recursive, depth);
				}
			}
		}
		
		// Otherwise
		else {
			System.out.println(tabString(depth) + "Field: NONE");
		}
    }
    
    // Get the field value
    public Object getFieldVal(Field fieldClass, Object obj) {
    	
    	// Initializing the object val to null
		Object val = null;
		
    	// Set the value of val to the field class value 
		try {
			val = fieldClass.get(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		// Return the value
		return val;
    }
    
    // Check what to do depending on the value type
    public void fieldLogic(Object val, Class fieldTypeClass, Object obj, boolean recursive, int depth) {
		
		// If the field is an array:
		if (fieldTypeClass.isArray()) {
			
			// Get the arrays component type class
			Class componentTypeClass = fieldTypeClass.getComponentType();
			
			// Print the array using print array method
			printArray(val, componentTypeClass, recursive, depth);
		}
		
		// If the field class is not primitive and the value does not equal null
		else if ((!fieldTypeClass.isPrimitive()) && (val != null)) {
			
			// Print the array item with the hash code
			System.out.println(tabString(depth) + " Value: " + val + "@" + val.hashCode());
			
			// If recursive is true:
			if (recursive) {
				
				// Get the value's class
				Class valClass = val.getClass();
				
				// Inspect the value's class using the val object
				inspectClass(valClass, val, recursive, depth + 1);
			}
		}
		
		// Otherwise:
		else {
			
			// Print the value
			System.out.println(tabString(depth) + " Value: " + val);
		}
    }

    private void inspectClass(Class c, Object obj, boolean recursive, int depth) {
    	
    	// Create string to be stored in tracking map
		String checked = c + "@" + obj.hashCode();
		
		// If we have checked the object we're inspecting before, return
		if (trackingMap.get(checked) != null) {
			System.out.println(tabString(depth) + "Detecting circular reference");
			return;
		}
			
		// Save the checked object to tracking map
		trackingMap.put(checked, true);
		
		/*
		 * CLASS section begins here:
		 */  
		System.out.println("\n" + tabString(depth) + "CLASS");
		
		// Print class name
		System.out.println(tabString(depth) + "Class: " + checked);
		
		// If the class is an array:
		if (c.isArray()) {
			
			// Run initial class array and return
			initialClassArray(c, obj, recursive, depth);
		}
		
		// Otherwise:
		else {
			
			// Run regular class inspection
			regularClassInspection(c, obj, recursive, depth);
		}
    }
		
	// Do regular class inspection 
	public void regularClassInspection(Class c, Object obj, boolean recursive, int depth) {
		
		/*
		 * SUPERCLASS section begins here:
		 */
		System.out.println("\n" + tabString(depth) + "SUPERCLASS");
		
		// Run the super class section class
		superClassSection(c, obj, recursive, depth);
		
		/*
		 * FIELDS section begins here:
		 */
		System.out.println("\n" + tabString(depth) + "FIELDS");
		
		// Run the fields section class
		fieldsSection(c, obj, recursive, depth);
		
		// Print an empty line
		System.out.println();
    }

}