package edu.cmu.cs214.hw9.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import json.JSONObject;
import json.JSONTokener;

import edu.cmu.cs214.hw9.db.FriendsDAO;
import edu.cmu.cs214.hw9.db.User;
import edu.cmu.cs214.hw9.db.UserDAO;

public class ServerThread extends Thread {
	private Socket mySocket;
	private UserDAO u;
	private FriendsDAO f;
	public ServerThread(Socket s, UserDAO u, FriendsDAO f) throws Exception {
		if (s == null) {
			throw new NullPointerException();
		}
		mySocket = s;
		mySocket.setSoTimeout(600000);
		this.u = u;
		this.f = f;
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
				else if(msg.indexOf("ADDFRIEND") == 0){
					o = new JSONObject(new JSONTokener(msg.substring(10)));
					boolean t = f.addFriend(o.getString("emailAdding"), o.getString("emailAdded"));
					if(t) {
						out.println("ADDING SUCCESSFUL");
					}
					
					else {
						out.println("ADDING FAILED");
					}
				}

				else if(msg.indexOf("ACCEPTFRIEND") == 0){
					o = new JSONObject(new JSONTokener(msg.substring(13)));
					boolean t = f.acceptFriend(o.getString("emailAccepting"), o.getString("emailAccepted"));
					if(t) {
						out.println("ACCEPTING SUCCESSFUL");
					}
					
					else {
						out.println("ACCEPTING FAILED");
					}
				}
				
				else if(msg.indexOf("LISTFRIENDS") == 0){
					String list = f.listFriends(msg.substring(12));
					out.println(list);
				}
				
				/***********************************/
				/***********************************/
				/****** SUBSCRIPTION ACTIONS *******/
				/***********************************/
				/***********************************/
				
				/***********************************/
				/***********************************/
				/**** MAKING AND VIEWING POSTS *****/
				/***********************************/
				/***********************************/
				
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
