package edu.cmu.cs214.hw9.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PostsDAO extends SQLiteAdapter {
	public PostsDAO() throws Exception{
		super();
	}
	/**
	 * Gets posts by the given email sorted in descending order of date (most recent first)
	 * @param email
	 * @return
	 */
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
				ret.add(new Post(rs.getString("email"), rs.getString("content"), rs.getInt("is_status"), rs.getInt("date_added")));
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
				ret.add(new Post(rs.getString("email"), rs.getString("content"), rs.getInt("is_status"), rs.getInt("date_added")));
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
}


