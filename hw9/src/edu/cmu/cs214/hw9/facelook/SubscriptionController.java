package edu.cmu.cs214.hw9.facelook;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.ArrayList;

import json.JSONWriter;
import edu.cmu.cs214.hw9.db.Constants;

public class SubscriptionController {

	private SubscriptionController() {
	}
	
	public static boolean isSubscribed(String email1, String email2) {
		try{
			Socket mySocket = new Socket("localhost", Constants.SERVER_PORT);
			PrintWriter out = new PrintWriter(mySocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));

			StringWriter myWriter = new StringWriter();
			JSONWriter jsonW = new JSONWriter(myWriter);
			jsonW.object();//start object (refer to JSONWriter javadoc for a more in depth explanation of creation)
			jsonW.key("email1");//key
			jsonW.value(email1);//value at key
			jsonW.key("email2");//key
			jsonW.value(email2);//value at key
			jsonW.endObject();//finish object
			String message = myWriter.toString();//creates a string serializing the object
			
			out.println("ISSUBSCRIBED "+ message);//request the information for email
			
			String response = in.readLine();
			
			return response.contains("SUBSCRIBED");
			
		}
		catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;			
	}
	
	public static boolean modifySubscription(String emailAdding, String emailAdded){
		
		try{
			Socket mySocket = new Socket("localhost", Constants.SERVER_PORT);
			PrintWriter out = new PrintWriter(mySocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));

			StringWriter myWriter = new StringWriter();
			JSONWriter jsonW = new JSONWriter(myWriter);
			jsonW.object();//start object (refer to JSONWriter javadoc for a more in depth explanation of creation)
			jsonW.key("emailSubscriber");//key
			jsonW.value(emailAdding);//value at key
			jsonW.key("emailSubscribed");//key
			jsonW.value(emailAdded);//value at key
			jsonW.endObject();//finish object
			String message = myWriter.toString();//creates a string serializing the object
			
			out.println("MODIFYSUBSCRIPTION "+ message);//request the information for email
			
			String response = in.readLine();
			
			return response.contains("SUCCESS");
			
		}
		catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;	
	}
	
	public static ArrayList<String> listSubscriptions(String email){
		
		try{
			Socket mySocket = new Socket("localhost", Constants.SERVER_PORT);
			PrintWriter out = new PrintWriter(mySocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));

			out.println("LISTSUBSCRIPTIONS " + email);//request the username by email
			
			String response = in.readLine();
			//must tokenize response, place in arraylist
			String[] splitString = response.split(",");
			ArrayList<String> toReturn = new ArrayList<String>();
			for(int i = 0; i < splitString.length; i++){
				if(splitString[i].length() > 0)
					toReturn.add(splitString[i]);	
				
			}
			return toReturn;
			
		}
		catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
