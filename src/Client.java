import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import javax.json.*;
import javax.json.stream.JsonParser;

import Solution.*;

public class Client {

    public static void main(String[] args) throws Exception {
        
    	// Create a new socket that goes to 0.0.0.0 at port 5000
        try (Socket s = new Socket("0.0.0.0", 5000)) {
        	
        	System.out.println("CLIENT: The client is connected to the server!");
        	
        	// Create a new buffered reader for the input
            BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
            
            // Iterate infinitely unit Server stops
            while (true) {
            	
            	// Wait for the server to send something
            	String serverResponse = input.readLine();
            	
            	// Print out what the client deserved
        		System.out.println("CLIENT: Received the following message:\n" + serverResponse);
        		
        		// Read the json object sent
        		JsonObject object = Json.createReader(new StringReader(serverResponse)).readObject();
            	
        		// Deserialize the json object
            	Object obj = Deserializer.deserializeObject(object);
            	
            	// Create a new inspector and inspect the object
            	Inspector i = new Inspector();
            	i.inspect(obj, true);
            }
        }
    }
}