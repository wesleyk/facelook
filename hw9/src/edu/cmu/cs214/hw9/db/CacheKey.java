package edu.cmu.cs214.hw9.db;

/**
 * Purpose: Key type for the Cache
 * @author Wesley, Jessica, Nikhil
 *
 */
public class CacheKey {

	//username
	private String email;
	
	//isFriend indicates whether posts are statuses and notifications,
	//or just statuses
	private boolean isFriend; 
	
	/**
	 * Constructor
	 * @param e email
	 * @param i isFriend
	 */
	public CacheKey(String e, boolean i){
		email = e;
		isFriend = i;
	}
	
	/**
	 * Getter method for email
	 * @return email
	 */
	public String getEmail(){
		
		return email;
		
	}
	
	/**
	 * Getter method for isFriend
	 * @return isFriend
	 */
	public boolean getIsFriend(){
		
		return isFriend;
		
	}
	
	/**
	 * override hashCode function for hashmap functions
	 */
	@Override
	public int hashCode() {
		return isFriend ? email.hashCode() + 1: email.hashCode();
	}
	
	/**
	 * override equals function for hashmap functions
	 */
	@Override
	public boolean equals(Object o) {
		CacheKey other = (CacheKey) o;
		return (email.equals(other.getEmail()) &&
				(isFriend == other.getIsFriend()));
	}
	
}
