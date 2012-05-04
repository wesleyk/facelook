package edu.cmu.cs214.hw9.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class TestCache {
	
	static Cache<CacheKey, ArrayList<Post>> cache;
	
	//cache simulation
	public static void main(String[] args) {
		cache = new Cache<CacheKey, ArrayList<Post>>(4);
		
		createPost("1","1",0,2);
		createPost("1","1",1,2);
		createPost("3","3",0,3);
		createPost("3","3",1,3);
		createPost("2","2",0,2);
		createPost("2","2",1,2);
		
		Set<Entry<CacheKey,ArrayList<Post>>> entries = cache.entrySet();
		Iterator<Entry<CacheKey,ArrayList<Post>>> itr = entries.iterator();
		
		while(itr.hasNext()) {
			Entry<CacheKey,ArrayList<Post>> cur = itr.next(); 
			CacheKey curKey = cur.getKey();
			System.out.println("Key: " + curKey.getEmail() + " isFriend: " + curKey.getIsFriend());
			ArrayList<Post> curPosts = cur.getValue();
			for(int j = 0; j < curPosts.size(); j++) {
				Post post = curPosts.get(j);
				System.out.println("Values: Email: " + post.getEmail() + " User: " + post.getContent()
									+ " status: " + post.getIsStatus());
			}
			
			System.out.println();
		}
		
	}
	
	public static boolean createPost (String email, String content, int is_status, long date_added){
		if (!(is_status == 0 || is_status ==1)){
			System.out.println("IS_STATUS CANNOT BE ANYTHING OTHER THAN 0 OR 1!");
			return false;
		}
		
		/*****************************/
		/*****************************/
		/******** CACHE CHECK ********/
		/*****************************/
		/*****************************/
		CacheKey subKey = new CacheKey(email, false);
		CacheKey friendKey = new CacheKey(email, true);
		
		ArrayList<Post> subCache = null;
		ArrayList<Post> friendCache = null;
		
		subCache = cache.get(subKey);
		
		if(is_status == 1) {
			friendCache = cache.get(friendKey);
		}
		
		/*****************************/
		/*****************************/
		/****** END CACHE CHECK ******/
		/*****************************/
		/*****************************/
		
		Post newPost = new Post(email, content, is_status, date_added);
		
		/*****************************/
		/*****************************/
		/******* CACHE UPDATE ********/
		/*****************************/
		/*****************************/
		//update subscription aspect of cache
		
		//case where there was no entry to begin with
		if(subCache == null) {
			ArrayList<Post> store = new ArrayList<Post>();
			store.add(newPost);
			cache.put(subKey, store);
		}
		
		//otherwise, modify existing array list
		else {
			//if the list already has 10 elements,
			// then remove the last element
			if(subCache.size() == Constants.MAX_POSTS) {
				subCache.remove(Constants.MAX_POSTS-1);
			}
			
			//add the new element to the front of the list
			subCache.add(0,newPost);
			
			cache.put(subKey, subCache);
		}
		
		//update friend aspect of cache
		if(is_status == 1) {
			
			//case where there was no entry to begin with
			if(friendCache == null) {
				ArrayList<Post> store = new ArrayList<Post>();
				store.add(newPost);
				cache.put(friendKey, store);
			}
			
			//otherwise, modify existing array list
			else {
				//if the list already has 10 elements,
				// then remove the last element
				if(friendCache.size() == Constants.MAX_POSTS) {
					friendCache.remove(Constants.MAX_POSTS-1);
				}
				
				//add the new element to the front of the list
				friendCache.add(0,newPost);
				cache.put(friendKey, friendCache);
			}
		}
		
		
		/*****************************/
		/*****************************/
		/***** END CACHE UPDATE ******/
		/*****************************/
		/*****************************/
		
		return true;
	}
	
}


