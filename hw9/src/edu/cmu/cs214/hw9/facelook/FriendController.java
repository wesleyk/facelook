package edu.cmu.cs214.hw9.facelook;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.ArrayList;

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

			out.println("LISTFRIENDS " + email);//request the username by email
			
			String response = in.readLine();
			
			System.out.println("RESPONSE:\n" + response);
			
			//must tokenize response, place in arraylist
			String[] splitString = response.split(".....");
			
			if(splitString.length < 1) {
				return null;
			}
			
			String[] normalFriends = splitString[0].split(",");
			String[] pendingFriends = splitString[1].split(",");
			ArrayList<String> normalArrayList = new ArrayList<String>();
			ArrayList<String> pendingArrayList = new ArrayList<String>();
			ArrayList<ArrayList<String>> toReturn = new ArrayList<ArrayList<String>>();
			
			for(int i = 0; i < normalFriends.length; i++){
				System.out.println("Norm: " + normalFriends[i]);
				normalArrayList.add(normalFriends[i]);
				
			}
			for(int i = 0; i < pendingFriends.length; i++){
				System.out.println("Pend: " + pendingFriends[i]);
				normalArrayList.add(pendingFriends[i]);	
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
