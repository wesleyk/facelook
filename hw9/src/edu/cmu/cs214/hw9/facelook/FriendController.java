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

import edu.cmu.cs214.hw9.db.Constants;

public class FriendController {

	private FriendController() {
	}

	public static boolean isActualFriends(String email1, String email2) {
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
			
			out.println("ISACTUALFRIENDS "+ message);//request the information for email
			
			String response = in.readLine();
			
			return response.contains("FRIENDS");
		}
		catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean modifyFriend(String emailModifying, String emailModified){
		
		try{
			Socket mySocket = new Socket("localhost", Constants.SERVER_PORT);
			PrintWriter out = new PrintWriter(mySocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));

			StringWriter myWriter = new StringWriter();
			JSONWriter jsonW = new JSONWriter(myWriter);
			jsonW.object();//start object (refer to JSONWriter javadoc for a more in depth explanation of creation)
			jsonW.key("emailModifying");//key
			jsonW.value(emailModifying);//value at key
			jsonW.key("emailModified");//key
			jsonW.value(emailModified);//value at key
			jsonW.endObject();//finish object
			String message = myWriter.toString();//creates a string serializing the object
			
			out.println("MODIFYFRIEND "+ message);//request the information for email
			
			String response = in.readLine();
			
			return response.contains("SUCCESS");
			
		}
		catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
	
	//want to get both friends and pending friends
	public static ArrayList<ArrayList<String>> listFriends(String email){
		
		try{
			Socket mySocket = new Socket("localhost", Constants.SERVER_PORT);
			PrintWriter out = new PrintWriter(mySocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));

			out.println("LISTFRIENDS " + email); //request the list by email
			
			String response = in.readLine();
			JSONArray o = new JSONArray(new JSONTokener(response));
			
			ArrayList<String> normalArrayList = new ArrayList<String>();
			ArrayList<String> pendingArrayList = new ArrayList<String>();
			ArrayList<ArrayList<String>> toReturn = new ArrayList<ArrayList<String>>();
			
			for (int i = 0; i < o.length(); i++){
				JSONObject j = o.getJSONObject(i);
				if(j.getBoolean("pending")) {
					pendingArrayList.add(j.getString("friend"));
				}
				
				else {
					normalArrayList.add(j.getString("friend"));
				}
			}
			
			toReturn.add(normalArrayList);
			toReturn.add(pendingArrayList); 
			
			return toReturn;
			
		}
		catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
