package edu.cmu.cs214.hw9.db;

/**
 * Purpose: Post object used to store a post
 * @author Wesley, Jessica, Nikhil
 *
 */
public class Post {
	private String content;
	private String email;
	private int is_status;
	private long date_added;
	
	/**
	 * Constructor
	 * @param email username
	 * @param content post content
	 * @param is_status whether or not the post was a status
	 * @param date_added date the post was submitted
	 */
	public Post(String email, String content, int is_status, long date_added){
		this.email = email;
		this.content = content;
		this.is_status = is_status;
		this.date_added = date_added;
	}
	
	/**
	 * Getter for content
	 * @return content
	 */
	public String getContent(){
		return content;
	}
	
	/**
	 * Getter for email
	 * @return email
	 */
	public String getEmail(){
		return email;
	}
	
	/**
	 * Getter for is_status
	 * @return is_status
	 */
	public int getIsStatus(){
		return is_status;
	}
	
	/**
	 * Getter for dateAdded
	 * @return dateAdded
	 */
	public long getDateAdded(){
		return date_added;
	}
}