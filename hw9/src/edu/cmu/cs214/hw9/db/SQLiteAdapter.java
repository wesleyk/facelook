package edu.cmu.cs214.hw9.db;

import java.sql.*;

public class SQLiteAdapter {
	protected Connection conn;
	
	public SQLiteAdapter() throws Exception {
		Class.forName(Constants.JDBC_NAME);//loads the driver
		conn = DriverManager.getConnection("jdbc:sqlite:214hw9.db");//connects to the database. 
		//214hw9.db is the name of the database in this case, can be changed to anything *.db
	}
	
	public void createTables(){
		Statement stat;
		try {
			stat = conn.createStatement();
			
			// Create the four tables: users, posts, friends, and subscriptions
			stat.executeUpdate("CREATE TABLE " + Constants.USERS_TABLE + " (email varchar(50) PRIMARY KEY, name varchar(50), password varchar(50));");
			stat.executeUpdate("CREATE TABLE " + Constants.POSTS_TABLE + " (email varchar(50) PRIMARY KEY, content varchar(200), is_post boolean, date_added date());");
			stat.executeUpdate("CREATE TABLE " + Constants.FRIENDS_TABLE + " (email1 varchar(50) PRIMARY KEY, email2 varchar(50));");
			stat.executeUpdate("CREATE TABLE " + Constants.SUBSCRIPTIONS_TABLE + " (email1 varchar(50) PRIMARY KEY, email2 varchar(50));");
			
			
			/*PreparedStatement ps;
			String statement = "INSERT INTO " + Constants.USERS_TABLE + " (email, password, name) VALUES (?, ?, ?)";
			try{
				ps = conn.prepareStatement(statement);
				ps.setString(1, "test@test.com");
				ps.setString(2, "testpass");
				ps.setString(3, "test");
				ps.executeUpdate();
			} catch(SQLException e){
				e.printStackTrace();
			}*/
			
			
			
		} catch (SQLException e) {
			return;
		}
	}
	
	public ResultSet select(String statement) {
		try {
			
			Statement st = conn.createStatement();
			return st.executeQuery(statement);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}	
		return null; 	
	}
}