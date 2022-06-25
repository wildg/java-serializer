import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import javax.json.JsonObject;

import org.junit.jupiter.api.Test;

import Solution.ObjectA;
import Solution.ObjectB;
import Solution.ObjectC;
import Solution.ObjectD;
import Solution.ObjectE;

class SerializationTests {

	private JsonObject j1;
	private JsonObject j2;
	
	@Test
	void testOptionOne() throws Exception {
		
		// Create a new object a
		ObjectA a = new Solution.ObjectA();
		
		// Initialize the variables
		a.x = 1;
		a.y = 2.0;
		
		// Serialize the object
		j1 = Serializer.serializeObject(a);
		
		// Deserialize the object
		a = (ObjectA) Deserializer.deserializeObject(j1);
		
		// Assert that the fields are correct 
		assertEquals(a.x, 1);
		assertEquals(a.y, 2.0);
		
		// Re-serialize the Object A that we deserialized
		j2 = Serializer.serializeObject(a);
		
		// Assert that the serialized objects are the same
		assertEquals(j1, j2);
		
	}
	
	@Test
	void testOptionTwo() throws Exception {
		
		// Create two new objects b
		ObjectB b1 = new Solution.ObjectB();
		ObjectB b2 = new Solution.ObjectB();
		
		// Initialize the variables for the first object
		b1.z = true;
		b1.other = b2;
		
		// Initialize the variables for the first object
		b2.z = false;
		b2.other = b1;
		
		// Serialize the first object
		j1 = Serializer.serializeObject(b1);
		
		// Deserialize the object
		b1 = (ObjectB) Deserializer.deserializeObject(j1);
		
		// Assert that the fields are correct
		assertEquals(b1.z, true);
		assertEquals(b1.other.z, b2.z);
		assertEquals(b1.other.other, b1);
		
		// Re-serialize the Object B that we deserialized
		j2 = Serializer.serializeObject(b1);
		
		// Assert that the serialized objects are the same
		assertEquals(j1, j2);
	}
	
	@Test
	void testOptionThree() throws Exception {	
		
		// Create a new objects B
		ObjectC c = new Solution.ObjectC();
		
		// Initialize the variable
		int[] arr = new int[] {0,0,0,3,0};
		c.a = arr;
		
		// Serialize the object
		j1 = Serializer.serializeObject(c);
		
		// Deserialize the object
		c = (ObjectC) Deserializer.deserializeObject(j1);
		
		// Assert that the fields are correct
		for (int i = 0; i < c.a.length; i++) {
			assertEquals(c.a[i], arr[i]);
		}
		
		// Re-serialize the Object B that we deserialized
		j2 = Serializer.serializeObject(c);
		
		// Assert that the serialized objects are the same
		assertEquals(j1, j2);
	}
		
	@Test
	void testOptionFour() throws Exception {
		
		// Create a new object D and A
		ObjectD d = new Solution.ObjectD();
		ObjectA a = new Solution.ObjectA();
		
		// Initialize the variables for ObjectA
		a.x = 352;
		a.y = 2.023;
		
		// Initialize the variables for ObjectD
		ObjectA[] arr = new ObjectA[] {null, null, null, a, null};
		d.b = arr;
		
		// Serialize the object
		j1 = Serializer.serializeObject(d);
		
		// Deserialize the object
		d = (ObjectD) Deserializer.deserializeObject(j1);
		
		// Assert that the fields are correct
		for (int i = 0; i < d.b.length; i++) {
			if (i == 3) {
				assertEquals(d.b[i].x, 352);
				assertEquals(d.b[i].y, 2.023);
			}
			else {
				assertEquals(d.b[i], arr[i]);
			}
		}
		
		// Re-serialize the Object B that we deserialized
		j2 = Serializer.serializeObject(d);
		
		// Assert that the serialized objects are the same
		assertEquals(j1, j2);
	}
	
	@Test
	void testOptionFive() throws Exception {
		
		// Create a new object E and A
		ObjectE e = new ObjectE();
		ObjectA a = new Solution.ObjectA();
		
		// Initialize the variables for ObjectA
		a.x = 7023;
		a.y = 28083.03;
		
		// Initialize the variables for ObjectE
		ArrayList<Object> arr = new ArrayList<Object>(5);
		arr.add(a);
		e.c = new ArrayList<Object>(5);
		
		// Serialize the object
		j1 = Serializer.serializeObject(e);
		
		// Deserialize the object
		e = (ObjectE) Deserializer.deserializeObject(j1);
		
		// Assert that the fields are correct
		for (int i = 0; i < e.c.size(); i++) {
			if (i != 0) {
				assertEquals(e.c.get(0), a);
			}
			else {
				assertEquals(e.c.get(i), arr.get(i));
			}
		}
		
		// Re-serialize the Object B that we deserialized
		j2 = Serializer.serializeObject(e);
		
		// Assert that the serialized objects are the same
		assertEquals(j1, j2);
	}
}
