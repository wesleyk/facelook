package edu.cmu.cs214.hw9.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.ArrayList;

import json.JSONArray;
import json.JSONObject;
import json.JSONTokener;

import edu.cmu.cs214.hw9.db.FriendsDAO;
import edu.cmu.cs214.hw9.db.PostsDAO;
import edu.cmu.cs214.hw9.db.Post;
import edu.cmu.cs214.hw9.db.SubscriptionsDAO;
import edu.cmu.cs214.hw9.db.User;
import edu.cmu.cs214.hw9.db.UserDAO;

public class ServerThread extends Thread {
	private Socket mySocket;
	private UserDAO u;
	private FriendsDAO f;
	private SubscriptionsDAO s;
	private PostsDAO p;
	
	public ServerThread(Socket mySocket, UserDAO u,
					FriendsDAO f, SubscriptionsDAO s, PostsDAO p) throws Exception {
		if (s == null) {
			throw new NullPointerException();
		}
		this.mySocket = mySocket;
		this.mySocket.setSoTimeout(600000);
		this.u = u;
		this.f = f;
		this.s = s;
		this.p = p;
	}
	
	public void run(){
		try{
			PrintWriter out = new PrintWriter(mySocket.getOutputStream(),
					true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					mySocket.getInputStream()));
			while(true){//keep accepting messages until END command is given.
				String msg = in.readLine();
				JSONObject o;
				
				//on null input, do not do anything
				if(msg == null) {
					out.println("exit");
				}
				
				/***********************************/
				/***********************************/
				/***** LOGIN AND REGISTRATION ******/
				/***********************************/
				/***********************************/
				//LOGIN
				else if(msg.indexOf("LOGIN") == 0){
					o = new JSONObject(new JSONTokener(msg.substring(6)));
					boolean t = u.authenticateUser(o.getString("email"), o.getString("password"));
					if(t) {
						out.println("LOGIN SUCCESSFUL");
					}
					
					else {
						out.println("LOGIN FAILED");
					}
				}
				//REGISTER
				else if(msg.indexOf("REGISTER") == 0){
					o = new JSONObject(new JSONTokener(msg.substring(9)));
					boolean t = u.createUser(o.getString("email"), o.getString("password"), o.getString("name"));
					if(t) {
						out.println("REGISTRATION SUCCESSFUL");
					}
					
					else {
						out.println("REGISTRATION FAILED");
					}
				}
				
				/***********************************/
				/***********************************/
				/********* FRIEND ACTIONS **********/
				/***********************************/
				/***********************************/
				else if(msg.indexOf("ISACTUALFRIENDS") == 0){
					o = new JSONObject(new JSONTokener(msg.substring(16)));
					boolean t = f.isFriend(o.getString("email1"), o.getString("email2"))
									&&
								f.isFriend(o.getString("email2"), o.getString("email1"));
					if(t) {
						out.println("FRIENDS");
					}
					
					else {
						out.println("NOT");
					}
				}
				
				else if(msg.indexOf("MODIFYFRIEND") == 0){
					o = new JSONObject(new JSONTokener(msg.substring(13)));
					boolean t = f.modifyFriend(o.getString("emailModifying"), o.getString("emailModified"));
					if(t) {
						out.println("MODIFY SUCCESSFUL");
					}
					
					else {
						out.println("MODIFY FAILED");
					}
				}
				
				else if(msg.indexOf("LISTFRIENDS") == 0){
					JSONArray arr = f.listFriends(msg.substring(12));
					
					StringWriter myWriter = new StringWriter();
					arr.write(myWriter);
					String list = myWriter.toString();
					out.println(list);
				}
				
				/***********************************/
				/***********************************/
				/****** SUBSCRIPTION ACTIONS *******/
				/***********************************/
				/***********************************/
				else if(msg.indexOf("ISSUBSCRIBED") == 0){
					o = new JSONObject(new JSONTokener(msg.substring(13)));
					boolean t = s.isSubscribed(o.getString("email1"), o.getString("email2"));

					if(t) {
						out.println("SUBSCRIBED");
					}
					
					else {
						out.println("NOT");
					}
				}
				
				else if(msg.indexOf("MODIFYSUBSCRIPTION") == 0){
					o = new JSONObject(new JSONTokener(msg.substring(19)));
					boolean t = s.modifySubscription(o.getString("emailSubscriber"), o.getString("emailSubscribed"));

					if(t) {
						out.println("MODIFY SUCCESSFUL");
					}
					
					else {
						out.println("MODIFY FAILED");
					}
				}
				
				else if(msg.indexOf("LISTSUBSCRIPTIONS") == 0){
					JSONArray arr = s.listSubscriptions(msg.substring(18));
					
					StringWriter myWriter = new StringWriter();
					arr.write(myWriter);
					String list = myWriter.toString();
					out.println(list);
				}
				
				
				/***********************************/
				/***********************************/
				/**** MAKING AND VIEWING POSTS *****/
				/***********************************/
				/***********************************/
				else if(msg.indexOf("POST STATUS") == 0){
					o = new JSONObject(new JSONTokener(msg.substring(12)));
					boolean t = p.createPost(o.getString("email"), o.getString("content"), o.getInt("is_status"), o.getLong("date_added"));
					
					if (t){
						out.println("POST STATUS SUCCESSFUL");
					}
					else{
						out.println("POST STATUS FAILED");
					}
				}
				else if(msg.indexOf("POST NOTIF") == 0){
					o = new JSONObject(new JSONTokener(msg.substring(11)));
					boolean t = p.createPost(o.getString("email"), o.getString("content"), o.getInt("is_status"), o.getLong("date_added"));
					
					if (t){
						out.println("POST NOTIF SUCCESSFUL");
					}
					else{
						out.println("POST NOTIF FAILED");
					}
				}
				else if(msg.indexOf("GET STATUS AND NOTIF") == 0){
					o = new JSONObject(new JSONTokener(msg.substring(21)));
					ArrayList<Post> ret = p.topTenPostsByEmail(o.getString("email"));
					
					JSONArray arr = p.convertToJSONArray(ret);
					
					StringWriter myWriter = new StringWriter();
					arr.write(myWriter);
					String message = myWriter.toString();
					out.println("SUCCESS " + message);
				}
				else if(msg.indexOf("GET NOTIF") == 0){
					o = new JSONObject(new JSONTokener(msg.substring(10)));
					ArrayList<Post> ret = p.topTenNotificationsByEmail(o.getString("email"));
					
					JSONArray arr = p.convertToJSONArray(ret);
					
					StringWriter myWriter = new StringWriter();
					arr.write(myWriter);
					String message = myWriter.toString();
					out.println("SUCCESS " + message);
				}
				else if(msg.indexOf("SHOWNEWSFEED") == 0) {
					o = new JSONObject(new JSONTokener(msg.substring(13)));
					ArrayList<Post> ret = p.getNewsFeed(o.getString("email"));
					
					JSONArray arr = p.convertToJSONArray(ret);
					
					StringWriter myWriter = new StringWriter();
					arr.write(myWriter);
					String message = myWriter.toString();
					out.println("SUCCESS " + message);
				}
				
				/***********************************/
				/***********************************/
				/********** USER ACTIONS ***********/
				/***********************************/
				/***********************************/				
				//retrieve username given e-mail address
				else if(msg.indexOf("GETUSERNAME") == 0){
					User thisUser = u.findUser(msg.substring(12));
					if (thisUser == null){
						out.println("");
					}
					else{
						out.println(thisUser.getName());
					}
				}
				
				else if(msg.indexOf("END") == 0){//Break out of the loop, no longer accept messages
					out.println("closing connection");
					break;
				}
				else{
					out.println("Unrecognized command");
				}
			}
			out.close();
			in.close();
			mySocket.close();
		}catch(Exception e){
			e.printStackTrace();
		}

	}
}
