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
import edu.cmu.cs214.hw9.db.Post;

public class PostController {

	private PostController() {
	}
	
	public static boolean doPost(String email, String content, int is_status, long date_added){
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
			jsonW.key("content");//key
			jsonW.value(content);//value at key
			jsonW.key("is_status");
			jsonW.value(is_status);
			jsonW.key("date_added");
			jsonW.value(date_added);
			jsonW.endObject();//finish object
			String message = myWriter.toString();//creates a string serializing the object

			if (is_status == 1){
				out.println("POST STATUS "+message);
			}
			else if (is_status == 0){
				out.println("POST NOTIF "+message);
			}
			
			String response = in.readLine();
			
			return response.contains("SUCCESS");
		}
		catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static ArrayList<Post> showPosts(String email1, String email2){
		try{
			//hash email to determine server shard that we go to
			int serverPort = (email1.hashCode() % 5) + 15210;
			
			Socket mySocket = new Socket("localhost", serverPort);
			PrintWriter out = new PrintWriter(mySocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			
			StringWriter myWriter = new StringWriter();
			JSONWriter jsonW = new JSONWriter(myWriter);
			jsonW.object();//start object (refer to JSONWriter javadoc for a more in depth explanation of creation)
			
			// friends see both notifications and statuses, and subscribers see only statuses.
			if (FriendController.isActualFriends(email1, email2) || email1.equals(email2)){
				jsonW.key("email");//key
				jsonW.value(email1);//value at key
				jsonW.endObject();//finish object
				String message = myWriter.toString();//creates a string serializing the object
				
				out.println("GET STATUS AND NOTIF "+ message);
				String response = in.readLine();
				System.out.println("response in GET STATUS AND NOTIF: " + response);
				// read in response from server in the form of string, then use JSON tokener to parse it, and get the array list of posts
				JSONArray o = new JSONArray(new JSONTokener(response.substring(8)));
				ArrayList<Post> arr = new ArrayList<Post>();
				for (int i = 0; i < o.length(); i++){
					JSONObject j = o.getJSONObject(i);
					Post p = new Post(j.getString("email"), j.getString("content"), 
									  j.getInt("is_status"), j.getLong("date_added"));
					arr.add(p);
				}
				return arr;
			}
			else{
				if (SubscriptionController.isSubscribed(email2, email1)){
					jsonW.key("email");//key
					jsonW.value(email1);//value at key
					jsonW.endObject();//finish object
					String message = myWriter.toString();//creates a string serializing the object
					
					// get top ten statuses
					out.println("GET NOTIF "+ message);
					
					String response = in.readLine();
					// read in response from server in the form of string, then use JSON tokener to parse it, and get the array list of posts
					JSONArray o = new JSONArray(new JSONTokener(response.substring(8)));
					ArrayList<Post> arr = new ArrayList<Post>();
					for (int i = 0; i < o.length(); i++){
						JSONObject j = o.getJSONObject(i);
						Post p = new Post(j.getString("email"), j.getString("content"), 
										  j.getInt("is_status"), j.getLong("date_added"));
						arr.add(p);
					}
					return arr;
				}
				else{
					// don't show anything
					return new ArrayList<Post>();
				}
			}
		}
		catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<Post>();
	}
	
	public static ArrayList<Post> showNewsFeedPosts (String email){
		// viewing newsfeed for user with email: email
		try{
			Socket mySocket = new Socket("localhost", Constants.SERVER_PORT);
			PrintWriter out = new PrintWriter(mySocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));

			out.println("SHOWNEWSFEED " + email); //request the list by email
			
			String response = in.readLine();
			JSONArray o = new JSONArray(new JSONTokener(response));
			ArrayList<Post> arr = new ArrayList<Post>();
			for (int i = 0; i < o.length(); i++){
				JSONObject j = o.getJSONObject(i);
				Post p = new Post(j.getString("email"), j.getString("content"), 
								  j.getInt("is_status"), j.getLong("date_added"));
				arr.add(p);
			}
			return arr;
		}
		catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
