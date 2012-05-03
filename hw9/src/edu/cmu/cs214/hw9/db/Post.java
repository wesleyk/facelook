package edu.cmu.cs214.hw9.db;

public class Post {
	private String content;
	private String email;
	private int is_status;
	private int date_added;
	
	public Post(String email, String content, int is_status, int date_added){
		this.email = email;
		this.content = content;
		this.is_status = is_status;
		this.date_added = date_added;
	}
	
	public String getContent(){
		return content;
	}
	
	public String getEmail(){
		return email;
	}
	
	public int getIsStatus(){
		return is_status;
	}
	
	public int getDateAdded(){
		return date_added;
	}
}