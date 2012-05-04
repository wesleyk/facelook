package edu.cmu.cs214.hw9.facelook;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import edu.cmu.cs214.hw9.db.Constants;

public class NewsfeedController {

	private NewsfeedController() {
	}

	
	public static String getUserName(String email){
		
		try{
			//hash email to determine server shard that we go to
			int serverPort = (email.hashCode() % 5) + 15210;
			
			Socket mySocket = new Socket("localhost", serverPort);
			PrintWriter out = new PrintWriter(mySocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));

			out.println("GETUSERNAME " + email);//request the username by email
			
			String response = in.readLine();
			
			return response;
			
		}
		catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
		
		
}
	
