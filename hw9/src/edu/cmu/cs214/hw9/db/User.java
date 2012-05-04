package edu.cmu.cs214.hw9.db;

/**
 * Purpose: User object used to store user info
 * @author Wesley, Nikhil, Jessica
 *
 */
public class User {
	private String email;
	private String password;
	private String name;
	
	/**
	 * Constructor
	 * @param email username
	 * @param password password associated to user
	 * @param name actual name of user
	 */
	public User(String email, String password, String name){
		this.email = email;
		this.password = password;
		this.name = name;
	}
		
	/**
	 * Getter for email
	 * @return email
	 */
	public String getEmail(){
		return email;
	}

	/**
	 * Getter for password
	 * @return password
	 */
	public String getPassword(){
		return password;
	}
	
	/**
	 * Getter for name
	 * @return name
	 */
	public String getName(){
		return name;
	}	
}
