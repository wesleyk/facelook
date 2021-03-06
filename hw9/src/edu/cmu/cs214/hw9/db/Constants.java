package edu.cmu.cs214.hw9.db;

/**
 * Purpose: Constants file for whole project
 * @author Wesley, Jessica, Nikhil
 *
 */
public class Constants {
	public static final String JDBC_NAME = "org.sqlite.JDBC";//Java database connectivity driver for sqlite
	public static final int TIMEOUT = 30000;
	
	//table names
	public static final String USERS_TABLE = "Users";
	public static final String POSTS_TABLE = "Posts";
	public static final String FRIENDS_TABLE = "Friends";
	public static final String SUBSCRIPTIONS_TABLE = "Subscriptions";
	
	//cache capacity
	public static final int CACHE_SIZE = 10;

	public static final int MAX_POSTS = 10;
	
	//server constants
	public static final int SERVER_PORT_BASE = 15210;
	public static final int NUM_SHARDS = 5;
}