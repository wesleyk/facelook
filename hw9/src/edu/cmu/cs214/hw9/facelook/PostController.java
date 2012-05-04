package edu.cmu.cs214.hw9.facelook;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import json.JSONArray;
import json.JSONObject;
import json.JSONTokener;
import json.JSONWriter;
import edu.cmu.cs214.hw9.db.Constants;
import edu.cmu.cs214.hw9.db.Post;

/**
 * This is the posts controller. It is used to make connections between
 * posts that come in from the gui, and need to interact with the backend
 * database. We have various operations we can do with posts, and each is
 * separated into different functions.
 * @author nikhil, wesley, jessica
 *
 */
public class PostController {

	private PostController() {
	}
	
	/**
	 * This function makes a post into the database. Depending on the is_status
	 * argument, we check if the post is to be a status or notification and 
	 * send the necessary protocol command for that. It is very similar
	 * in functionality to the login function in the login controller.
	 * @param email - email of the poster
	 * @param content - content of post
	 * @param is_status - status or notification
	 * @param date_added - date of creation of post
	 * @return
	 */
	public static boolean doPost(String email, String content, int is_status, long date_added){
		try{
			//hash email to determine server shard that we go to
			int serverPort = (email.hashCode() % Constants.NUM_SHARDS) + Constants.SERVER_PORT_BASE;
			
			// create socket for the needed port to connect to server
			Socket mySocket = new Socket("localhost", serverPort);
			PrintWriter out = new PrintWriter(mySocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));

			// create JSON writer objects to serialize information about
			// post, and then send that information
			StringWriter myWriter = new StringWriter();
			JSONWriter jsonW = new JSONWriter(myWriter);
			jsonW.object();//start object
			jsonW.key("email");//key
			jsonW.value(email);//value at key
			jsonW.key("content");//key
			jsonW.value(content);//value at key
			jsonW.key("is_status"); // key
			jsonW.value(is_status); //value
			jsonW.key("date_added"); // key
			jsonW.value(date_added); // value
			jsonW.endObject();//finish object
			String message = myWriter.toString();//creates a string serializing the object

			// Send the needed protocol command depending on the is_status argument
			if (is_status == 1){
				out.println("POST STATUS "+message);
			}
			else if (is_status == 0){
				out.println("POST NOTIF "+message);
			}
			
			// read in response from server
			String response = in.readLine();
			
			return response.contains("SUCCESS");
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * This function is used to get all the posts that must be shown somewhere
	 * on the app
	 * @param email1 - the email of the profile being viewed
	 * @param email2 - email of the user viewing profile of email1
	 * @return - array list of the posts to show
	 */
	public static ArrayList<Post> showPosts(String email1, String email2){
		try{
			//hash email to determine server shard that we go to
			int serverPort = (email1.hashCode() % Constants.NUM_SHARDS) + Constants.SERVER_PORT_BASE;
			
			// create socket with the server on the needed port
			Socket mySocket = new Socket("localhost", serverPort);
			PrintWriter out = new PrintWriter(mySocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			
			// create JSON writer object to send the 2 emails to the server
			StringWriter myWriter = new StringWriter();
			JSONWriter jsonW = new JSONWriter(myWriter);
			jsonW.object();//start object
			
			// friends see notifications and statuses, and subscribers see only statuses.
			if (FriendController.isActualFriends(email1, email2) || email1.equals(email2)){
				
				jsonW.key("email");//key
				jsonW.value(email1);//value at key
				jsonW.endObject();//finish object
				String message = myWriter.toString();//creates a string serializing the object
				
				// send necessary protocol command to the server
				out.println("GET STATUS AND NOTIF "+ message);
				
				// read in response from the server
				String response = in.readLine();

				// read in response from server in the form of string, then use JSON 
				// tokener to parse it, and get the array list of posts
				JSONArray o = new JSONArray(new JSONTokener(response.substring(8)));
				
				// parse the JSONarray to get individual posts
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
				// the two users are subscribers, show statuses only
				
				// add data needed to the JSONWriter
				jsonW.key("email");//key
				jsonW.value(email1);//value at key
				jsonW.endObject();//finish object
				String message = myWriter.toString();//creates a string serializing the object
				
				// get top ten statuses
				out.println("GET NOTIF "+ message);
				
				// read response from server
				String response = in.readLine();
				
				// read in response from server in the form of string, then use JSON 
				// tokener to parse it, and get the array list of posts
				JSONArray o = new JSONArray(new JSONTokener(response.substring(8)));
				
				// parse the JSON array to get individual posts
				ArrayList<Post> arr = new ArrayList<Post>();
				for (int i = 0; i < o.length(); i++){
					JSONObject j = o.getJSONObject(i);
					Post p = new Post(j.getString("email"), j.getString("content"), 
									  j.getInt("is_status"), j.getLong("date_added"));
					arr.add(p);
				}
				return arr;
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return new ArrayList<Post>();
	}
	
	public static ArrayList<Post> showNewsFeedPosts (String email){
		// viewing newsfeed for user with email: email
		try{
			
			/***********************************************/
			/***********************************************/
			/***** RETRIEVE FRIENDS AND SUBSCRIPTIONS ******/
			/***********************************************/
			/***********************************************/
			ArrayList<ArrayList<String>> allFriends =
									FriendController.listFriends(email);
			ArrayList<String> friends = new ArrayList<String>();
			
			if(allFriends != null) {
				friends = allFriends.get(0);
			}
			
			ArrayList<String> subscriptions = SubscriptionController.listSubscriptions(email);
			
			/***********************************************/
			/***********************************************/
			/********* RETRIEVE ALL RELATED POSTS **********/
			/***********************************************/
			/***********************************************/
			ArrayList<Post> posts = new ArrayList<Post>();
			
			//user should see his/her own posts on the newsfeed
			ArrayList<Post> ownPosts = showPosts(email,email);
			for(int i = 0; i < ownPosts.size(); i++) {
				posts.add(ownPosts.get(i));
			}
			
			for(int i = 0; i < friends.size(); i++) {
				ArrayList<Post> friendPosts = showPosts(friends.get(i), email);
				System.out.println(friends.get(i));
				for(int j = 0; j < friendPosts.size(); j++) {
					posts.add(friendPosts.get(j));
				}
			}
			
			for(int i = 0; i < subscriptions.size(); i++) {
				//don't double count if user is friends and subscribed to same person
				if(friends.contains(subscriptions.get(i))) {
					continue;
				}
				
				System.out.println(subscriptions.get(i));
				ArrayList<Post> subPosts = showPosts(subscriptions.get(i), email);
				for(int j = 0; j < subPosts.size(); j++) {
					posts.add(subPosts.get(j));
				}
			}
			
			/***********************************************/
			/***********************************************/
			/********* SORT POSTS AND GRAB ONLY TEN ********/
			/***********************************************/
			/***********************************************/
			Collections.sort(posts,new PostComparable());			
			
			ArrayList<Post> ret = new ArrayList<Post>(10);
			
			for(int i = 0; i < 10 && i < posts.size(); i++) {
				ret.add(posts.get(i));
			}
			
			return ret;
		}
		catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}