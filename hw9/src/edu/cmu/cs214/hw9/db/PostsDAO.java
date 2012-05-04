package edu.cmu.cs214.hw9.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import json.JSONArray;
import json.JSONObject;

public class PostsDAO extends SQLiteAdapter {
	public PostsDAO(String dbName) throws Exception{
		super(dbName);
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
			System.out.println(statement);
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


