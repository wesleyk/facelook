package edu.cmu.cs214.hw9.db;

public class Constants {
	public static final int SERVER_PORT = 15214;
	public static final String JDBC_NAME = "org.sqlite.JDBC";//Java database connectivity driver for sqlite
	public static final int TIMEOUT = 30000;
	public static final String USERS_TABLE = "Users";
	public static final String POSTS_TABLE = "Posts";
	public static final String FRIENDS_TABLE = "Friends";
	public static final String SUBSCRIPTIONS_TABLE = "Subscriptions";
	public static final int CACHE_SIZE=10;
}