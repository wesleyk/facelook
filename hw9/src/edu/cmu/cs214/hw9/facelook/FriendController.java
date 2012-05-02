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

	public static boolean modifyFriend(String emailModifying, String emailModified){
		
		try{
			Socket mySocket = new Socket("localhost", Constants.SERVER_PORT);
			PrintWriter out = new PrintWriter(mySocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));

			JSONWriter jsonW = new JSONWriter(out);
			jsonW.object();//start object (refer to JSONWriter javadoc for a more in depth explanation of creation)
			jsonW.key("emailModifying");//key
			jsonW.value(emailModifying);//value at key
			jsonW.key("emailModified");//key
			jsonW.value(emailModified);//value at key
			jsonW.endObject();//finish object
			String message = out.toString();//creates a string serializing the object
			
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

			StringWriter myWriter = new StringWriter();

			out.println("LISTFRIENDS " + email);//request the username by email
			
			String response = in.readLine();
			//must tokenize response, place in arraylist
			String[] splitString = response.split(".....");
			String[] normalFriends = splitString[0].split(",");
			String[] pendingFriends = splitString[1].split(",");
			ArrayList<String> normalArrayList = new ArrayList<String>();
			ArrayList<String> pendingArrayList = new ArrayList<String>();
			ArrayList<ArrayList<String>> toReturn = new ArrayList<ArrayList<String>>();
			
			for(int i = 0; i < splitString.length; i++){
				
				normalArrayList.add(normalFriends[i]);
				pendingArrayList.add(pendingFriends[i]);
				
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
