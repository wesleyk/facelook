package edu.cmu.cs214.hw9.db;

public class CacheKey {

	private String email;
	private boolean isFriend; 
	//isFriend indicates whether posts are statuses and notifications,
	//or just statuses
	
	public CacheKey(String e, boolean i){
		
		email = e;
		isFriend = i;
		
	}
	
	public String getEmail(){
		
		return email;
		
	}
	
	public boolean getIsFriend(){
		
		return isFriend;
		
	}
	
	public boolean equals(CacheKey other){
		
		return(this.email.equals(other.getEmail()) &&
				(this.isFriend == other.getIsFriend()));
		
	}
	
}
