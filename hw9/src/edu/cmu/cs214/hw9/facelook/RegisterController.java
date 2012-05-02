package edu.cmu.cs214.hw9.facelook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import json.JSONWriter;

import edu.cmu.cs214.hw9.db.Constants;

// get fields from login panel, pass make calls to database
public class RegisterController {

	private RegisterController() {
	}
	
	public static boolean register(String email, String password, String name){
		try{
			Socket mySocket = new Socket("localhost", Constants.SERVER_PORT);
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
