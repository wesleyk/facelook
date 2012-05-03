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
	 * Gets all posts by the given email sorted in descending order of date (most recent first)
	 * @param email
	 * @return
	 */
	public ArrayList<Post> allPostsByEmail (String email){
		ArrayList<Post> ret = new ArrayList<Post>();
		ResultSet rs = null;
		try {
			String statement = "SELECT * FROM " + Constants.POSTS_TABLE + " WHERE email=? ORDER BY date_added DESC;";
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

	public ArrayList<Post> allStatusPostsByEmail (String email){
		ArrayList<Post> ret = allPostsByEmail(email);
		if (ret.size() == 0){
			return new ArrayList<Post>();
		}
		
		ArrayList<Post> statusPosts = new ArrayList<Post>();
		
		for (Post p : ret){
			if (p.getIsStatus() == 1){
				statusPosts.add(p);
			}
		}
		return statusPosts;
	}
	
	public ArrayList<Post> allNotificationPostsByEmail (String email){
		ArrayList<Post> ret = allPostsByEmail(email);
		if (ret.size() == 0){
			return new ArrayList<Post>();
		}
		
		ArrayList<Post> nonStatusPosts = new ArrayList<Post>();
		
		for (Post p : ret){
			if (p.getIsStatus() == 0){
				nonStatusPosts.add(p);
			}
		}
		return nonStatusPosts;
	}
	
	public ArrayList<Post> topTenNotificationsByEmail (String email){
		ArrayList<Post> ret = allNotificationPostsByEmail(email);
		ArrayList<Post> topTen = new ArrayList<Post>(10);
		int i = 0;
		for (Post p : ret){
			topTen.add(p);
			i++;
			if (i == 10){
				break;
			}
		}
		return topTen;
	}
	
	public ArrayList<Post> topTenStatusesByEmail (String email){
		ArrayList<Post> ret = allStatusPostsByEmail(email);
		ArrayList<Post> topTen = new ArrayList<Post>(10);
		int i = 0;
		for (Post p : ret){
			topTen.add(p);
			i++;
			if (i == 10){
				break;
			}
		}
		return topTen;
	}
	
	public ArrayList<Post> topTenStatusPosts(){
		ArrayList<Post> ret = new ArrayList<Post>();
		ResultSet rs = null;
		try {
			String statement = "SELECT * FROM " + Constants.POSTS_TABLE + " WHERE is_status=1 ORDER BY date_added DESC;";
			PreparedStatement ps = conn.prepareStatement(statement);
			rs = ps.executeQuery();
			if(!rs.isBeforeFirst()) {
				System.out.println("no posts (in PostsDAO.java)");
				return ret;
			}
			int i = 0;
			while (rs.next() && i < 10){
				ret.add(new Post(rs.getString("email"), rs.getString("content"), rs.getInt("is_status"), rs.getInt("date_added")));
				i++;
			}
			
		}
		catch (Exception e){
			System.out.println("Error in postsDAO.java topTenStatusPosts method");
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
	
	public ArrayList<Post> topTenNotificationPosts(){
		ArrayList<Post> ret = new ArrayList<Post>();
		ResultSet rs = null;
		try {
			String statement = "SELECT * FROM " + Constants.POSTS_TABLE + " WHERE is_status=0 ORDER BY date_added DESC;";
			PreparedStatement ps = conn.prepareStatement(statement);
			rs = ps.executeQuery();
			if(!rs.isBeforeFirst()) {
				System.out.println("no posts (in PostsDAO.java)");
				return ret;
			}
			int i = 0;
			while (rs.next() && i < 10){
				ret.add(new Post(rs.getString("email"), rs.getString("content"), rs.getInt("is_status"), rs.getInt("date_added")));
				i++;
			}
			
		}
		catch (Exception e){
			System.out.println("Error in postsDAO.java topTenNotificationPosts method");
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


