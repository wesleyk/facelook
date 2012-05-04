package edu.cmu.cs214.hw9.db;

import java.sql.*;

/**
 * Purpose: adapter used to establish DB connection and create tables
 * @author Wesley, Jessica, Nikhil
 *
 */
public class SQLiteAdapter {
	//connection variable used for the various DAO's to execute queries
	protected Connection conn;
	
	/**
	 * Constructor
	 * @param dbName name of db to be connected to
	 * @throws Exception e
	 */
	public SQLiteAdapter(String dbName) throws Exception {
		Class.forName(Constants.JDBC_NAME);//loads the driver
		conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);//connects to the database. 
		//214hw9.db is the name of the database in this case, can be changed to anything *.db
	}
	
	/**
	 * Create tables if they do not already exist
	 */
	public void createTables(){
		Statement stat;
		try {
			
			// Create the four tables: users, posts, friends, and subscriptions
			stat = conn.createStatement();
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS " + Constants.USERS_TABLE + " (email varchar(50) PRIMARY KEY, name varchar(50), password varchar(50));");
			
			stat = conn.createStatement();
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS " + Constants.POSTS_TABLE + " (email varchar(50), content varchar(200), is_status integer, date_added integer);");
			
			stat = conn.createStatement();
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS " + Constants.FRIENDS_TABLE + " (email1 varchar(50), email2 varchar(50));");
			
			stat = conn.createStatement();
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS " + Constants.SUBSCRIPTIONS_TABLE + " (email1 varchar(50), email2 varchar(50));");	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Generalized select db statement
	 * @param statement statement to be executed
	 * @return the resulting set
	 */
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