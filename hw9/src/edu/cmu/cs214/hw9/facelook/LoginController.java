package edu.cmu.cs214.hw9.facelook;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;

import json.JSONWriter;

import edu.cmu.cs214.hw9.db.Constants;

// get fields from login panel, pass make calls to database
/**
 * This is the login controller. It is used to handle the connection between
 * the server and the login details during login from the app.
 * @author nikhil, wesley, jessica
 *
 */
public class LoginController {

	private LoginController() {
	}
	
	/**
	 * This methods tries to log in the user with the email and password.
	 * It uses a login protocol and sends the login information parsed as
	 * JSON data to the server, which sends back a response. It sends a
	 * success if the login was successful, and fail if it wasnt.
	 * @param email
	 * @param password
	 * @return
	 */
	public static boolean login(String email, String password){
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
			jsonW.endObject();//finish object
			String message = myWriter.toString();//creates a string serializing the object

			out.println("LOGIN "+message);//request the information for email
			
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
