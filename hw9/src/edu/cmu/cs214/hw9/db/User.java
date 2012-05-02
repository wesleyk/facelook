package edu.cmu.cs214.hw9.db;

public class User {
	private int id;
	private String email;
	private String password;
	private String name;
	
	public User(int id, String email, String password, String name){
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
	}
		
	public String getEmail(){
		return email;
	}
	
	public String getPassword(){
		return password;
	}
	
	public int getID(){
		return id;
	}
	
	public String getName(){
		return name;
	}	
}
