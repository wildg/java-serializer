import java.util.ArrayList;

import Solution.*;

public class ObjectCreator {
	
	/**
	 * Initializes a new ObjectA instance with inputed 'x' and 'y' values
	 * 
	 * @param xVal The new x value
	 * @param yVal The new y value
	 * @return The new instance of ObjectA
	 */
	public static ObjectA createObjectA(int xVal, double yVal) {
		
		// Create new instance of ObjectA
		ObjectA a = new ObjectA();
		
		// Give the object the appropriate values
		a.x = xVal;
		a.y = yVal;
		
		// Return the object
		return a;
	}
	
	/**
	 * Initializes a new ObjectB instance with inputed 'z' and 'other' values
	 * 
	 * @param zVal The new 'z' value
	 * @param otherVal The new 'other' value
	 * @return The new instance of ObjectB
	 */
	public static ObjectB createObjectB(boolean zVal, ObjectB otherVal) {
		
		// Create new instance of ObjectA
		ObjectB b = new ObjectB();
		
		// Give the object the appropriate values
		b.z = zVal;
		b.other = otherVal;
		
		// Return the object
		return b;
	}
	
	/**
	 * Initializes a new ObjectC instance with inputed 'a' value
	 * 
	 * @param aVal The new 'a' value
	 * @return The new instance of ObjectC
	 */
	public static ObjectC createObjectC(int[] aVal) {
		
		// Create new instance of ObjectA
		ObjectC c = new ObjectC();
		
		// Give the object the appropriate values
		c.a = aVal;
		
		// Return the object
		return c;
	}
	
	/**
	 * Initializes a new ObjectD instance with inputed 'a' value
	 * 
	 * @param bVal The new 'b' value
	 * @return The new instance of ObjectD
	 */
	public static ObjectD createObjectD(ObjectA[] bVal) {
		
		// Create new instance of ObjectA
		ObjectD d = new ObjectD();
		
		// Give the object the appropriate values
		d.b = bVal;
		
		// Return the object
		return d;
	}
	
	/**
	 * Initializes a new ObjectE instance with inputed 'c' value
	 * 
	 * @param cVal The new 'c' value
	 * @return The new instance of ObjectE
	 */
	public static ObjectE createObjectE(ArrayList<Object> cVal) {
		
		// Create new instance of ObjectA
		ObjectE e = new ObjectE();
		
		// Give the object the appropriate values
		e.c = cVal;
		
		// Return the object
		return e;
	}
}
