package edu.cmu.cs214.hw9.server;

import java.net.ServerSocket;

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
			u = new UserDAO();//initialize new database accessor.
			f = new FriendsDAO();
			s = new SubscriptionsDAO();
			p = new PostsDAO();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {			
			u.createTables();//create the tables
			socket = new ServerSocket(Constants.SERVER_PORT);
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