package edu.cmu.cs214.hw9.server;

import java.net.ServerSocket;
import java.util.Scanner;

import edu.cmu.cs214.hw9.server.ServerThread;
import edu.cmu.cs214.hw9.db.Constants;
import edu.cmu.cs214.hw9.db.FriendsDAO;
import edu.cmu.cs214.hw9.db.PostsDAO;
import edu.cmu.cs214.hw9.db.SubscriptionsDAO;
import edu.cmu.cs214.hw9.db.UserDAO;

public class Server {
	public static void main(String[] args){
		ServerSocket socket = null;
		UserDAO u = null;
		FriendsDAO f = null;
		SubscriptionsDAO s = null;
		PostsDAO p = null;
		
		try {		
			Scanner sc = new Scanner(System.in);
			System.out.println("Please enter a port number for this server: ");
			
			//ASSUME: As in readme, there will be 5 servers running
			//with port numbers ranging from 15210 to 15214.
			int portNum = sc.nextInt();
			System.out.println("Port number stored.");
			String dbName = portNum + ".db";
			
			u = new UserDAO(dbName);//initialize new database accessor.
			f = new FriendsDAO(dbName);
			s = new SubscriptionsDAO(dbName);
			p = new PostsDAO(dbName);

			u.createTables();//create the tables
			socket = new ServerSocket(portNum);
			while(true){
				//spawn a thread to do the work
				Thread connection = new ServerThread(socket.accept(), u, f, s, p);
				connection.start();
			}
		} catch (Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
	}
}