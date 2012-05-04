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
	
	@Override
	public int hashCode() {
		return isFriend ? email.hashCode() + 1: email.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		CacheKey other = (CacheKey) o;
		return (email.equals(other.getEmail()) &&
				(isFriend == other.getIsFriend()));
	}
	
}
