package edu.cmu.cs214.hw9.facelook;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.ArrayList;

import json.JSONArray;
import json.JSONObject;
import json.JSONTokener;
import json.JSONWriter;

/**
 * A class for keeping track of subscriptions,
 * and sending subscription info back and forth from the server
 * @author Nikhil, Jessica, Wesley
 *
 */
public class SubscriptionController {

	private SubscriptionController() {
	}
	
	/**
	 * Checks the subscription relationship between 2 users
	 * 
	 * @param email1 - email of the subscribing user
	 * @param email2 - email of the user getting subscribed to
	 * @return true if email1 is subscribed to email2, false otherwise
	 */
	public static boolean isSubscribed(String email1, String email2) {
		try{
			//hash email to determine server shard that we go to
			int serverPort = (email1.hashCode() % 5) + 15210;
			
			Socket mySocket = new Socket("localhost", serverPort);
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
	
	/**
	 * Modifies the subscription relationship (add or remove)
	 * between 2 users, depending on the current relationship
	 * between the 2
	 * @param emailAdding - user that is adding a subscription
	 * @param emailAdded - user that is being subscribed to
	 * @return true if operation was successful, false otherwise
	 */
	public static boolean modifySubscription(String emailAdding, String emailAdded){
		
		try{
			//hash email to determine server shard that we go to
			int serverPort = (emailAdding.hashCode() % 5) + 15210;
			
			Socket mySocket = new Socket("localhost", serverPort);
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
	
	/**
	 * Lists all subscriptions of a certain user
	 * @param email - user that we are checking
	 * @return a String ArrayList of subscriptions of user 'email'
	 */
	public static ArrayList<String> listSubscriptions(String email){
		
		try{
			//hash email to determine server shard that we go to
			int serverPort = (email.hashCode() % 5) + 15210;
			
			Socket mySocket = new Socket("localhost", serverPort);
			PrintWriter out = new PrintWriter(mySocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));

			out.println("LISTSUBSCRIPTIONS " + email);//request the username by email
			
			String response = in.readLine();
			
			JSONArray o = new JSONArray(new JSONTokener(response));
			
			ArrayList<String> toReturn = new ArrayList<String>();
			for(int i = 0; i < o.length(); i++){
				JSONObject j = o.getJSONObject(i);
				toReturn.add(j.getString("subscription"));	
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
