package edu.cmu.cs214.hw9.facelook;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;

import json.JSONWriter;

import edu.cmu.cs214.hw9.db.Constants;


/**
 * //A class for getting fields from registration panel, 
 * passes user info to server which gets info from database
 * and passes that back to the register controller.
 * @author Nikhil, Jessica, Wesley
 *
 */
public class RegisterController {

	private RegisterController() {
	}
	
	/**
	 * Registers a new user. Returns false if an error occurs
	 * 
	 * @param email - email of person that's registering
	 * @param password - user's password
	 * @param name - user's name
	 * @return true if registration succeeded, false otherwise
	 */
	public static boolean register(String email, String password, String name){
		try{
			
			//hash email to determine server shard that we go to
			int serverPort = (email.hashCode() % 5) + 15210;
			Socket mySocket = new Socket("localhost", serverPort);
			
			PrintWriter out = new PrintWriter(mySocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));

			StringWriter myWriter = new StringWriter();
			JSONWriter jsonW = new JSONWriter(myWriter);
			jsonW.object();//start object (refer to JSONWriter javadoc for a more in depth explanation of creation)
			jsonW.key("email");//key
			jsonW.value(email);//value at key
			jsonW.key("password");//key
			jsonW.value(password);//value at key
			jsonW.key("name");//key
			jsonW.value(name);//value at key
			jsonW.endObject();//finish object
			String message = myWriter.toString();//creates a string serializing the object

			out.println("REGISTER "+message);//request the information for email
			
			String response = in.readLine();
			
			return response.contains("SUCCESS");
		}
		catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
