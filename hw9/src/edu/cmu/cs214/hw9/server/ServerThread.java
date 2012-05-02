package edu.cmu.cs214.hw9.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import json.JSONObject;
import json.JSONTokener;

import edu.cmu.cs214.hw9.db.User;
import edu.cmu.cs214.hw9.db.UserDAO;

public class ServerThread extends Thread {
	private Socket mySocket;
	private UserDAO u;
	public ServerThread(Socket s, UserDAO u2) throws Exception {
		if (s == null) {
			throw new NullPointerException();
		}
		mySocket = s;
		mySocket.setSoTimeout(600000);
		this.u = u2;
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
				if(msg == null) {
					out.println("exit");
				}
				else if(msg.indexOf("STORE") == 0){//Recognize the STORE command, store new data
					o = new JSONObject(new JSONTokener(msg.substring(6)));
					u.createUser(o.getString("email"), o.getString("password"), o.getString("name"));
					out.println("successful store");
				}
				else if(msg.indexOf("GETUSERNAME") == 0){//Recognize the GET command, get the data
					User thisUser = u.findUser(msg.substring(12));
					if (thisUser == null){
						out.println("ERROR: User does not exist!");
					}
					else{
						out.println(thisUser.getName());
					}
				}
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
				else if(msg.indexOf("REGISTER") == 0){
					o = new JSONObject(new JSONTokener(msg.substring(9)));
					boolean t = u.createUser(o.getString("email"), o.getString("password"), o.getString("name"));
					if(t) {
						System.out.println("REGISTRATION SUCCESSFUL");
						out.println("REGISTRATION SUCCESSFUL");
					}
					
					else {
						System.out.println("REGISTRATION FAILED");
						out.println("REGISTRATION FAILED");
					}
				}
				/*else if(msg.indexOf("CHANGE") == 0){//Recognize the CHANGE command, change the data
					o = new JSONObject(new JSONTokener(msg.substring(7)));
					d.changeAnimal(o.getString("username"), o.getString("animal"));
					out.println("successful change");
				}*/
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
