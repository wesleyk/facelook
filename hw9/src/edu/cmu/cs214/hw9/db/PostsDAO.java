package edu.cmu.cs214.hw9.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import json.JSONArray;
import json.JSONObject;

/**
 * Purpose: Database accessor for friend related queries
 * @author Wesley, Jessica, Nikhil
 *
 */
public class PostsDAO extends SQLiteAdapter {
	
	//linked hash map that represents a cache.
	//a cachekey (email + indicator of whether the user is a friend)
	//maps to an arraylist of posts from that user
	protected Cache<CacheKey, ArrayList<Post>> cache;
	
	/**
	 * Constructor
	 * @param dbName name of the database that will be connected
	 * @throws Exception e
	 */
	public PostsDAO(String dbName) throws Exception{
		super(dbName);
		//creates cache with a capacity of 10 sets of posts
		cache = new Cache<CacheKey, ArrayList<Post>>(Constants.CACHE_SIZE);
	}
	/**
	 * Gets posts by the given email sorted in descending order of date (most recent first)
	 * @param email username
	 * @return arraylist of up to ten posts
	 */
	public ArrayList<Post> topTenPostsByEmail (String email){
		
		ArrayList<Post> ret = new ArrayList<Post>(10);
		
		/*****************************/
		/*****************************/
		/******** CACHE CHECK ********/
		/*****************************/
		/*****************************/
		CacheKey myKey = new CacheKey(email, true);
		ArrayList<Post> retPosts = cache.get(myKey);
		if(retPosts != null){
			return retPosts;
		}
		/*****************************/
		/*****************************/
		/****** END CACHE CHECK ******/
		/*****************************/
		/*****************************/
		
		ResultSet rs = null;
		try {
			String statement = "SELECT * FROM " + Constants.POSTS_TABLE + " WHERE email=? ORDER BY date_added DESC LIMIT 10;";
			PreparedStatement ps = conn.prepareStatement(statement);
			ps.setString(1, email);
			rs = ps.executeQuery();
			if(!rs.isBeforeFirst()) {
				return ret;
			}
			while (rs.next()){
				ret.add(new Post(rs.getString("email"), rs.getString("content"),
								rs.getInt("is_status"), rs.getLong("date_added")));
			}
			
		}
		catch (Exception e){
			System.out.println("Error in postsDAO.java topTenNotificationsByEmail method");
			e.printStackTrace();
		}
		finally {
            try{
            	if(rs != null){
            		rs.close();
            	}
            } catch (SQLException e){
            	System.out.println("error in closing resultset (in PostsDAO.java)");
            	e.printStackTrace();
            }
        }
		
		/*****************************/
		/*****************************/
		/******** CACHE STORE ********/
		/*****************************/
		/*****************************/
		//put posts in cache. The LinkedHashMap takes care of LRU policy.
		if(ret != null){
			cache.put(myKey, ret);
		}
		/*****************************/
		/*****************************/
		/****** END CACHE STORE ******/
		/*****************************/
		/*****************************/
		
		return ret;
	}
	
	/**
	 * Retrieve top ten notifications based on email
	 * @param email username
	 * @return arraylist of up to ten posts
	 */
	public ArrayList<Post> topTenNotificationsByEmail (String email){
		ArrayList<Post> ret = new ArrayList<Post>(10);
		
		
		/*****************************/
		/*****************************/
		/******** CACHE CHECK ********/
		/*****************************/
		/*****************************/
		CacheKey myKey = new CacheKey(email, false); //false = just get notifications
		ArrayList<Post> retPosts = cache.get(myKey);
		if(retPosts != null){
			return retPosts;
		}
		/*****************************/
		/*****************************/
		/****** END CACHE CHECK ******/
		/*****************************/
		/*****************************/
		
		ResultSet rs = null;
		try {
			String statement = "SELECT * FROM " + Constants.POSTS_TABLE + " WHERE email=? AND is_status=0 ORDER BY date_added DESC LIMIT 10;";
			PreparedStatement ps = conn.prepareStatement(statement);
			ps.setString(1, email);
			rs = ps.executeQuery();
			if(!rs.isBeforeFirst()) {
				return ret;
			}
			while (rs.next()){
				ret.add(new Post(rs.getString("email"), rs.getString("content"),
								rs.getInt("is_status"), rs.getLong("date_added")));
			}			
		}
		catch (Exception e){
			System.out.println("Error in postsDAO.java topTenNotificationsByEmail method");
			e.printStackTrace();
		}
		finally {
            try{
            	if(rs != null){
            		rs.close();
            	}
            } catch (SQLException e){
            	System.out.println("error in closing resultset (in PostsDAO.java)");
            	e.printStackTrace();
            }
        }
		
		/*****************************/
		/*****************************/
		/******** CACHE STORE ********/
		/*****************************/
		/*****************************/
		//put posts in cache. The LinkedHashMap takes care of LRU policy.
		if(ret != null){
			cache.put(myKey, ret);
		}
		/*****************************/
		/*****************************/
		/****** END CACHE STORE ******/
		/*****************************/
		/*****************************/
		
		return ret;
	}
	
	/**
	 * Store new post in the database and do necessary caching tasks
	 * @param email username
	 * @param content content of post
	 * @param is_status whether or not the post was a status
	 * @param date_added date post was made
	 * @return whether or not the store was successful
	 */
	public boolean createPost (String email, String content, int is_status, long date_added){
		if (!(is_status == 0 || is_status ==1)){
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
		
		PreparedStatement ps;
		String statement = "INSERT INTO " + Constants.POSTS_TABLE + " (email, content, is_status, date_added) VALUES (?, ?, ?, ?);";
		try{
			ps = conn.prepareStatement(statement);
			ps.setString(1, email);
			ps.setString(2, content);
			ps.setInt(3, is_status);
			ps.setLong(4, date_added);
			ps.executeUpdate();
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
		
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
	
	/**
	 * Purpose: convert post to JSON object
	 * @param p post
	 * @return post as jsonobject
	 */
	public JSONObject convertPostToJSON (Post p){
		JSONObject j = new JSONObject();
		try{
			j.put("email", p.getEmail());
			j.put("content", p.getContent());
			j.put("is_status", p.getIsStatus());
			j.put("date_added", p.getDateAdded());
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return j;
	}
	
	/**
	 * Purpose: convert list of posts to json array
	 * @param l list of posts
	 * @return list of posts as json array
	 */
	public JSONArray convertToJSONArray (ArrayList<Post> l){
		JSONArray a = new JSONArray();
		for (Post p : l){
			a.put(convertPostToJSON(p));
		}
		return a;
	}
}


