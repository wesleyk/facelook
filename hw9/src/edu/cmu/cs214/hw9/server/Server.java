package edu.cmu.cs214.hw9.server;

import java.net.ServerSocket;

import edu.cmu.cs214.hw9.server.ServerThread;
import edu.cmu.cs214.hw9.db.Constants;
import edu.cmu.cs214.hw9.db.UserDAO;

public class Server {
	public static void main(String[] args){
		ServerSocket socket = null;
		UserDAO u = null;
		try {
			u = new UserDAO();//initialize new database accessor.
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {			
			u.createTables();//create the tables
			socket = new ServerSocket(Constants.SERVER_PORT);
			while(true){
				Thread connection = new ServerThread(socket.accept(), u);//spawn a thread to do the work
				connection.start();
			}
		} catch (Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
