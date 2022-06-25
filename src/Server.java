import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

import Solution.*;

public class Server {

	public static void main(String[] args) throws Exception {
    	
		UserInterface UI = new UserInterface();
		
    	// Create a new listener using port 5000
        try (ServerSocket listener = new ServerSocket(5000)) {
        	
            System.out.println("SERVER: The server is running (" + listener + ")");
            System.out.println("SERVER: The server's LOCAL ADDRESS is: " + listener.getLocalSocketAddress());
            System.out.println("SERVER: The server's PORT is: " + listener.getLocalPort());
            
            // Look to see if a client can be found:
            while (true) {
            	
            	// Try to find a client and accept connection:
                try (Socket client = listener.accept()) {
                	
                    System.out.println("SERVER: New connection established (" + client + ")");
                    
                    // Create a new method to send data to client
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                    
                    // Iterate infinitely until User input finishes
                    while (true) {
                    	
                    	// Get the user input
                    	String toSend = UI.userInput();
                    	
                    	// If the user input is null:
            			if (toSend == null) {
            				out.println("STOP");
            				break;
            			}
                    	
            			// Otherwise:
            			else {
            				System.out.println("SERVER: Sending the following message:\n" + toSend);
            				out.println(toSend);
            			}
                    }
                    
                    System.out.println("SERVER: Shutting down");
                    return;
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
	}
}