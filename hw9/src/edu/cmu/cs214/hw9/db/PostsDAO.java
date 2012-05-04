package edu.cmu.cs214.hw9.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import json.JSONArray;
import json.JSONObject;

public class PostsDAO extends SQLiteAdapter {
	public PostsDAO() throws Exception{
		super();
	}
	/**
	 * Gets posts by the given email sorted in descending order of date (most recent first)
	 * @param email
	 * @return
	 */
	public ArrayList<Post> topTenPostsByEmail (String email){
		ArrayList<Post> ret = new ArrayList<Post>(10);
		ResultSet rs = null;
		try {
			String statement = "SELECT * FROM " + Constants.POSTS_TABLE + " WHERE email=? ORDER BY date_added DESC LIMIT 10;";
			PreparedStatement ps = conn.prepareStatement(statement);
			ps.setString(1, email);
			rs = ps.executeQuery();
			if(!rs.isBeforeFirst()) {
				System.out.println("no posts by user with email " + email + "(in PostsDAO.java)");
				return ret;
			}
			while (rs.next()){
				ret.add(new Post(rs.getString("email"), rs.getString("content"), rs.getInt("is_status"), rs.getLong("date_added")));
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
		return ret;
	}
	
	
	public ArrayList<Post> topTenNotificationsByEmail (String email){
		ArrayList<Post> ret = new ArrayList<Post>(10);
		ResultSet rs = null;
		try {
			String statement = "SELECT * FROM " + Constants.POSTS_TABLE + " WHERE email=? AND is_status=0 ORDER BY date_added DESC LIMIT 10;";
			PreparedStatement ps = conn.prepareStatement(statement);
			ps.setString(1, email);
			rs = ps.executeQuery();
			if(!rs.isBeforeFirst()) {
				System.out.println("no posts by user with email " + email + "(in PostsDAO.java)");
				return ret;
			}
			while (rs.next()){
				ret.add(new Post(rs.getString("email"), rs.getString("content"), rs.getInt("is_status"), rs.getLong("date_added")));
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
		return ret;
	}
	
	public ArrayList<Post> topTenStatusesByEmail (String email){
		ArrayList<Post> ret = new ArrayList<Post>(10);
		ResultSet rs = null;
		try {
			String statement = "SELECT * FROM " + Constants.POSTS_TABLE + " WHERE email=? AND is_status=1 ORDER BY date_added DESC LIMIT 10;";
			PreparedStatement ps = conn.prepareStatement(statement);
			ps.setString(1, email);
			rs = ps.executeQuery();
			if(!rs.isBeforeFirst()) {
				System.out.println("no posts by user with email " + email + "(in PostsDAO.java)");
				return ret;
			}
			while (rs.next()){
				ret.add(new Post(rs.getString("email"), rs.getString("content"), rs.getInt("is_status"), rs.getLong("date_added")));
			}
			
		}
		catch (Exception e){
			System.out.println("Error in postsDAO.java allPostsByEmail method");
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
		return ret;
	}
	
	/**
	 * Return the newsfeed for a given user.
	 * The procedure is as follows:
	 * 1) Retrieve up to ten most recent notifications/statuses
	 * from the friends of the user and the own user
	 * 2) Retrieve up to ten most recent notifications from
	 * the user's subscriptions
	 * 3) Find aggregate ten most recent posts from the twenty
	 * @param email user that newsfeed will be found for
	 * @param friends list of friends
	 * @param subscriptions list of subscriptions
	 * @return ArrayList of Posts that represent the newsfeed
	 */
	public ArrayList<Post> getNewsFeed (String email, JSONArray friends, JSONArray subscriptions){
		ArrayList<Post> friendPosts = new ArrayList<Post>(10);
		ArrayList<Post> subPosts = new ArrayList<Post>(10);
		ArrayList<Post> ret = new ArrayList<Post>(10);
		
		ArrayList<String> friendList = new ArrayList<String>();
		
		ResultSet rs = null;
		PreparedStatement ps;
		
		//large sql statements for ten most recent friends/subscriptions posts
		String friendSQL = "SELECT * FROM " + Constants.POSTS_TABLE + " WHERE email = \"" + email + "\""; 
		String subSQL= "SELECT * FROM " + Constants.POSTS_TABLE + " WHERE is_status = 0";
		boolean firstSub = true;
		try {
			
			/*********************************************/
			/*********************************************/
			/***** GET FRIENDS AND SUBSCRIPTIONS *********/
			/********* AND SETUP SQL STATEMENTS **********/
			/*********************************************/
			/*********************************************/
			for (int i = 0; i < friends.length(); i++){
				JSONObject j = friends.getJSONObject(i);
				if(!j.getBoolean("pending")) {
					friendList.add(j.getString("friend"));
					friendSQL += " OR email = \"" + j.getString("friend") + "\"";
				}
			}
			
			friendSQL += " ORDER BY date_added DESC LIMIT 10;";
			
			for (int i = 0; i < subscriptions.length(); i++){
				JSONObject j = subscriptions.getJSONObject(i);
				String sub = j.getString("subscription");
				
				// only include in list of subscriptions if the user is
				// not already a friend
				if(!friendList.contains(sub)) {
					if(firstSub) {
						subSQL += " AND ";
						firstSub = false;
					}
					
					subSQL += "email = \"" + sub + "\" OR ";
				}
			}
			
			/***************************/
			/***************************/
			/***** EXECUTE SQL *********/
			/***************************/
			/***************************/
			ps = conn.prepareStatement(friendSQL);
			rs = ps.executeQuery();
			while(rs.next()) {
				friendPosts.add(new Post(rs.getString("email"), rs.getString("content"), rs.getInt("is_status"), rs.getLong("date_added")));
			}
			if(rs != null) {
				rs.close();
			}
			
			//only execute the subscription statement if the user actually has subscriptions
			if(!firstSub) {
				subSQL = subSQL.substring(0,subSQL.length()-3);
				subSQL += " ORDER BY date_added DESC LIMIT 10;";
				ps = conn.prepareStatement(subSQL);
				rs = ps.executeQuery();
				while(rs.next()) {
					subPosts.add(new Post(rs.getString("email"), rs.getString("content"), rs.getInt("is_status"), rs.getLong("date_added")));
				}
				if(rs != null) {
					rs.close();
				}
			}
			
			System.out.println(friendSQL);
			System.out.println(subSQL);
			
			
			/*******************************************/
			/*******************************************/
			/***** RETRIEVE TEN MOST RECENT POSTS ******/
			/*******************************************/
			/*******************************************/
			//perform a sort of "merge" to retrieve ten most recent posts
			int friendIndex = 0;
			int subIndex = 0;
			int fPostsSize = friendPosts.size();
			int sPostsSize = subPosts.size();
			
			while(ret.size() < 10 && (friendIndex < fPostsSize ||
									  subIndex < sPostsSize)) {
				if(friendIndex >= fPostsSize) {
					ret.add(subPosts.get(subIndex));
					subIndex++;
					continue;
				}
				
				else if(subIndex >= sPostsSize) {
					ret.add(friendPosts.get(friendIndex));
					friendIndex++;
					continue;
				}
				
				Post friendPost = friendPosts.get(friendIndex);
				Post subPost = subPosts.get(subIndex);
				
				//friend post should be added
				if(friendPost.getDateAdded() > subPost.getDateAdded()) {
					ret.add(friendPost);
					friendIndex++;
				}
				
				//subscription post should be added
				else {
					ret.add(subPost);
					subIndex++;
				}
			}
			
		}
		catch (Exception e){
			System.out.println("Error in postsDAO.java getNewsFeed method");
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
		return ret;
	}
	
	public boolean createPost (String email, String content, int is_status, long date_added){
		if (!(is_status == 0 || is_status ==1)){
			System.out.println("IS_STATUS CANNOT BE ANYTHING OTHER THAN 0 OR 1!");
			return false;
		}
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
		return true;
	}
	
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
	
	public JSONArray convertToJSONArray (ArrayList<Post> l){
		JSONArray a = new JSONArray();
		for (Post p : l){
			a.put(convertPostToJSON(p));
		}
		return a;
	}
}


