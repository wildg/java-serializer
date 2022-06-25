import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

import Solution.ObjectA;
import Solution.ObjectB;
import Solution.ObjectC;
import Solution.ObjectD;
import Solution.ObjectE;

public class UserInterface {
	
	// Create a new scanner object
	static Scanner in = new Scanner(System.in);
	
	// Create an array of ObjectB objects for circular reference
	private static List<ObjectB> bArray = new ArrayList<ObjectB>();
	
	// Check if ObjectB will have circular reference
	private static boolean circularReference = false;
	
	static String userInput() throws Exception {
    	System.out.println();
    	System.out.println("Please select which type of object you would like to send:");
    	System.out.println("\"Option 1\": Object with an integer (x) and a double (y)");
    	System.out.println("\"Option 2\": Object with a boolean (z) and a circular reference (other)");
    	System.out.println("\"Option 3\": Object with an array of ints (a)");
    	System.out.println("\"Option 4\": Object with an array of Option 1 objects (b)");
    	System.out.println("\"Option 5\": Object with an ArrayList of objects (c)");
    	
    	// Get the user's choice
    	String val = in.nextLine();
    	
    	// If the user chose option 1:
    	if ( (val.equals("Option 1")) || (val.equals("option 1")) || (val.equals("1")) ) {
    		System.out.println("You've selected Option 1");
    		System.out.println();
    		
    		return serializeReady( opt1() );
    	}
    	
    	else if ( (val.equals("Option 2")) || (val.equals("option 2")) || (val.equals("2")) ) {
    		System.out.println("You've selected Option 2");
    		System.out.println();
    		
    		return serializeReady( opt2() );
    	}
    	
    	else if ( (val.equals("Option 3")) || (val.equals("option 3")) || (val.equals("3")) ) {
    		System.out.println("You've selected Option 3");
    		System.out.println();
    		
    		return serializeReady( opt3() );
    	}
    	
    	else if ( (val.equals("Option 4")) || (val.equals("option 4")) || (val.equals("4")) ) {
    		System.out.println("You've selected Option 4");
    		System.out.println();
    		
    		return serializeReady( opt4() );
    	}
    	
    	else if ( (val.equals("Option 5")) || (val.equals("option 5")) || (val.equals("5")) ) {
    		System.out.println("You've selected Option 5");
    		System.out.println();
    		
    		return serializeReady( opt5() );
    	}
    	
    	else {
    		System.out.println("That is not an appropriate response...");
    		return userInput();
    	}
    }
    
    private static ObjectA opt1() {
    	
    	// Get the values needed for inputs x and y
    	int xVal = inputInt("X");
    	double yVal = inputDouble("Y");
    	
    	// Create a new instance of ObjectA
    	ObjectA obj = ObjectCreator.createObjectA(xVal, yVal);
    	
    	// Return the object
    	return obj;
    }
    
    private static int inputInt(String f) {
    	System.out.println("Please enter an INT to be stored in " + f + ":");
    	
    	// Get the user's input
    	String val = in.nextLine();
    	
    	try {
    		return Integer.valueOf(val);
    	} catch (Exception e) {
    		System.out.println("That is not an appropriate response...");
    		return inputInt(f);
    	}
    }
    
    private static double inputDouble(String f) {
    	System.out.println("Please enter a DOUBLE to be stored in " + f + ":");
    	
    	// Get the user's input
    	String val = in.nextLine();
    	
    	try {
    		return Double.valueOf(val);
    	} catch (Exception e) {
    		System.out.println("That is not an appropriate response...");
    		return inputDouble(f);
    	}
    }
    
    private static ObjectB opt2() {
    	
    	// Start the option 2 iterator and get main ObjectB
    	ObjectB main = opt2Iterator();
    	
    	// Check if there is a circular reference:
    	if (circularReference) {
    		
    		// Get the size of the array
    		int amountOfObjects = bArray.size();
    		
    		// If there was only one bObject stored
    		if (amountOfObjects == 1) {
    			
    			// Set main reference to itself
    			main.other = main;
    		}
    		
    		// Otherwise
    		else {
    			
    			// Get the last element in the array:
    			ObjectB lastObj = bArray.get(0);
        		
        		// Point other to the main object
        		lastObj.other = main;
    		}
    		
    	}
    	
    	// Reset circularReference to false
		circularReference = false;
    	
    	// Clear the list
    	bArray.clear();
    	
    	// Return the object
    	return main;
    }
    
    private static ObjectB opt2Iterator() {
    	
    	// Get the values needed for inputs z and other
    	boolean zVal = inputBool("X");
    	ObjectB otherVal = inputObjectB("Other");
    	
    	// Create a new instance of ObjectB
    	ObjectB obj = ObjectCreator.createObjectB(zVal, otherVal);
    	
    	// Add instance to array list
    	bArray.add(obj);
    	
    	// Return the object
    	return obj;
    }
    
    private static boolean inputBool(String f) {
    	System.out.println("Please enter a BOOLEAN to be stored in " + f + ":");
    	
    	try {
    		// Get the user's input
        	return Boolean.valueOf(in.nextLine());
    	} catch (Exception e) {
    		System.out.println("That is not an appropriate response...");
    		return inputBool(f);
    	}
    }
    
    private static ObjectB inputObjectB(String f) {
    	System.out.println("Please select an option for the OBJECT to be store in field from bellow:");
    	System.out.println("\"Option 1\": Circular reference to the first object");
    	System.out.println("\"Option 2\": Reference to a new object");
    	System.out.println("\"Option 3\": Null value");
    	
    	// Get the user's input
    	String val = in.nextLine();
    	
    	// Selected the first option: 
    	if ( (val.equals("Option 1")) || (val.equals("option 1")) || (val.equals("1")) ) {
    		
    		System.out.println("You've selected Option 1");
    		System.out.println();
    		
    		// Set circular reference to true
    		circularReference = true;
    		
    		return null;
    	}
    	
    	// Selected the second option:
    	else if ( (val.equals("Option 2")) || (val.equals("option 2")) || (val.equals("2")) ) {
    		System.out.println("You've selected Option 2");
    		System.out.println();
    		
    		// Create a new object
    		return opt2Iterator();
    	}
    	
    	// Selected the third option:
    	else if ( (val.equals("Option 3")) || (val.equals("option 3")) || (val.equals("3")) ) {
    		System.out.println("You've selected Option 3");
    		System.out.println();
    		
    		return null;
    	}
    	
    	// Otherwise
    	else {
    		System.out.println("That is not an appropriate response...");
    		
    		// Retry to get an answer
    		return inputObjectB(f);
    	}
    }
    
    private static ObjectC opt3() {
    	
    	// Get the values needed for inputs x and y
    	int[] aVal = inputIntArray("X");
    	
    	// Create a new instance of ObjectA
    	ObjectC obj = ObjectCreator.createObjectC(aVal);
    	
    	// Return the object
    	return obj;
    }
    
    private static int[] inputIntArray(String f) {
    	System.out.println("Please enter the INT for the length of the array:");
    	
    	try {
    		// Get the user's input
        	int ln = Integer.valueOf(in.nextLine());
        	
        	// Initialize the int array
        	int[] val = new int[ln];
        	
        	// Iterate over the length of the array
        	for (int i = 0; i < val.length; i++) {
        		val[i] = inputInt("index " + i + " ");
        	}
        	
        	// Return the array
        	return val;
        	
    	} catch(Exception e) {
    		System.out.println("That is not an appropriate response...");
    		return inputIntArray(f);
    	}
    }
    
    private static ObjectD opt4() {
    	
    	// Get the values needed for inputs x and y
    	ObjectA[] bVal = inputObjectAArray();
    	
    	// Create a new instance of ObjectA
    	ObjectD obj = ObjectCreator.createObjectD(bVal);
    	
    	// Return the object
    	return obj;
    }
    
    private static ObjectA[] inputObjectAArray() {
    	System.out.println("Please enter the INT for the length of the array:");
    	
    	try {
    		// Get the user's input
        	int ln = Integer.valueOf(in.nextLine());
        	
        	// Initialize the int array
        	ObjectA[] val = new ObjectA[ln];
        	
        	// Iterate over the length of the array
        	for (int i = 0; i < val.length; i++) {
        		val[i] = objectAArrayIterator(i);
        	}
        	
        	// Return the array
        	return val;
        	
    	} catch(Exception e) {
    		System.out.println("That is not an appropriate response...");
    		return inputObjectAArray();
    	}
    }
    
    private static ObjectA objectAArrayIterator(int i) {
    	System.out.println("Please select what to add at index " + Integer.toString(i));
		System.out.println("\"Option 1\": New object");
    	System.out.println("\"Option 2\": Null");
    	
    	// Get the user's input
    	String val = in.nextLine();
    	
    	// Selected the first option: 
    	if ( (val.equals("Option 1")) || (val.equals("option 1")) || (val.equals("1")) ) {
    		System.out.println("You've selected Option 2");
    		System.out.println();
    		
    		// Create a new object
    		return opt1();
    	}
    	
    	// Selected the second option: 
    	else if ( (val.equals("Option 2")) || (val.equals("option 2")) || (val.equals("2")) ) {
    		System.out.println("You've selected Option 2");
    		System.out.println();
    		
    		// Add null object
    		return null;
    	}
    	
    	else {
    		System.out.println("That is not an appropriate response...");
    		return objectAArrayIterator(i);
    	}
    }
    
    private static ObjectE opt5() {
    	
    	// Get the values needed for inputs x and y
    	ArrayList<Object> cVal = inputArrayList();
    	
    	// Create a new instance of ObjectA
    	ObjectE obj = ObjectCreator.createObjectE(cVal);
    	
    	// Return the object
    	return obj;
    }
    
    private static ArrayList<Object> inputArrayList() {
    	
    	System.out.println("Please enter the INT for the length of the array:");
    	
    	// Get the user's input
    	try {
    		int ln = Integer.valueOf(in.nextLine());
    		
    		// Initialize the array list
        	ArrayList<Object> val = new ArrayList<Object>(ln);
        	
        	// Iterate over the length of the array
        	for (int i = 0; i < ln; i++) {
        		val.add(i, arrayListIterator(i));
        	}
        	
        	return val;
    	} catch(Exception e) {
    		System.out.println("That is not an appropriate response...");
    		return inputArrayList();
    	}
    }
    
    private static Object arrayListIterator(int i) {
    	System.out.println();
    	System.out.println("Please select what to add at index " + Integer.toString(i));
    	System.out.println("\"Option 1\": Object with an integer (x) and a double (y)");
    	System.out.println("\"Option 2\": Object with a boolean (z) and a circular reference (other)");
    	System.out.println("\"Option 3\": Object with an array of ints (a)");
    	System.out.println("\"Option 4\": Object with an array of Option 1 objects (b)");
    	System.out.println("\"Option 5\": Object with an ArrayList of objects (c)");
    	System.out.println("\"Option 6\": Null");
    	
    	// Get the user's choice
    	String val = in.nextLine();
    	
    	// If the user chose option 1:
    	if ( (val.equals("Option 1")) || (val.equals("option 1")) || (val.equals("1")) ) {
    		System.out.println("You've selected Option 1");
    		System.out.println();
    		
    		return opt1();
    	}
    	
    	else if ( (val.equals("Option 2")) || (val.equals("option 2")) || (val.equals("2")) ) {
    		System.out.println("You've selected Option 2");
    		System.out.println();
    		
    		return opt2();
    	}
    	
    	else if ( (val.equals("Option 3")) || (val.equals("option 3")) || (val.equals("3")) ) {
    		System.out.println("You've selected Option 3");
    		System.out.println();
    		
    		return opt3();
    	}
    	
    	else if ( (val.equals("Option 4")) || (val.equals("option 4")) || (val.equals("4")) ) {
    		System.out.println("You've selected Option 4");
    		System.out.println();
    		
    		return opt4();
    	}
    	
    	else if ( (val.equals("Option 5")) || (val.equals("option 5")) || (val.equals("5")) ) {
    		System.out.println("You've selected Option 5");
    		System.out.println();
    		
    		return opt5();
    	}
    	
    	else if ( (val.equals("Option 6")) || (val.equals("option 6")) || (val.equals("6")) ) {
    		System.out.println("You've selected Option 5");
    		System.out.println();
    		
    		return null;
    	}
    	
    	else {
    		System.out.println("That is not an appropriate response...");
    		return arrayListIterator(i);
    	}
    }
    
    private static String serializeReady(Object obj) throws Exception {
    	
    	JsonObject json = Serializer.serializeObject(obj);
    	
    	// Print out the json
    	System.out.println(prettifyJson(json));
    	
    	// Serialize the given object
    	String jsonString = json.toString();
    	
    	return jsonString;
    }
    
    public static String prettifyJson(JsonObject j) {
		StringWriter writer = new StringWriter();
		Map<String, Object> settingMap = new HashMap<>();
		settingMap.put(JsonGenerator.PRETTY_PRINTING, true);
		JsonWriterFactory writerFactory = Json.createWriterFactory(settingMap);
		JsonWriter jsonWriter = writerFactory.createWriter(writer);
		jsonWriter.writeObject(j);
		jsonWriter.close();
		return writer.toString();
	}
}