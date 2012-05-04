package edu.cmu.cs214.hw9.facelook;

import java.util.Comparator;

import edu.cmu.cs214.hw9.db.Post;

//used for sorting posts
public class PostComparable implements Comparator<Post>{
	 
    @Override
    public int compare(Post p1, Post p2) {
    	long date1 = p1.getDateAdded();
    	long date2 = p2.getDateAdded();
    	
    	if(date1 > date2) {
    		return 1;
    	}
    	
    	else if(date2 > date1) {
    		return -1;
    	}
    	
    	else {
    		return 0;
    	}
    }
}