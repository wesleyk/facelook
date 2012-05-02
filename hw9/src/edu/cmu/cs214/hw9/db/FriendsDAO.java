package edu.cmu.cs214.hw9.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendsDAO extends SQLiteAdapter {
	public FriendsDAO() throws Exception{
		super();
	}

	/**
	 * Purpose: determine if email1 is friends with email2
	 * @param email1 Friend source
	 * @param email2 Friend destination
	 * @return Whether or not email1 is friends with email2
	 */
	public boolean isFriend(String email1, String email2) {
		PreparedStatement ps;
		ResultSet rs = null;
		String statement = "SELECT * FROM " + Constants.FRIENDS_TABLE + " WHERE email1=? AND email2=?;";
		try{
			ps = conn.prepareStatement(statement);
			ps.setString(1, email1);
			ps.setString(2, email2);
			ps.executeUpdate();
			rs = ps.executeQuery();
			return rs.isBeforeFirst();
		} catch(SQLException e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * emailAdding is sending a friend request to emailAdded
	 * @param emailAdding user sending the friend request
	 * @param emailAdded user who is receiving the friend request
	 * @return whether or not the query was successful
	 */
	public boolean addFriend(String emailAdding, String emailAdded) {
		//check if friend relationship is already there,
		// or if emailAdded has already friended emailAdding
		if(isFriend(emailAdding, emailAdded) ||
				isFriend(emailAdded, emailAdding)) {
			return false;
		}
		
		PreparedStatement ps;
		String statement = "INSERT INTO " + Constants.FRIENDS_TABLE + " (email1, email2) VALUES (?, ?)";
		try{
			ps = conn.prepareStatement(statement);
			ps.setString(1, emailAdding);
			ps.setString(2, emailAdded);
			ps.executeUpdate();
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * emailAccepting is accepting the friend request from emailAccepted
	 * @param emailAccepting user accepting the friend request
	 * @param emailAccepted user who sent the friend request
	 * @return whether or not the query was successful
	 */
	public boolean acceptFriend(String emailAccepting, String emailAccepted) {
		//check to make sure emailAccepted friended emailAccepting already,
		// but emailAccepting has not friended emailAccepted
		if(isFriend(emailAccepting, emailAccepted) ||
				!isFriend(emailAccepted, emailAccepting)) {
			return false;
		}
		
		PreparedStatement ps;
		String statement = "INSERT INTO " + Constants.FRIENDS_TABLE + " (email1, email2) VALUES (?, ?)";
		try{
			ps = conn.prepareStatement(statement);
			ps.setString(1, emailAccepting);
			ps.setString(2, emailAccepted);
			ps.executeUpdate();
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * emailRemoving is removing emailRemoved as a friend
	 * @param emailRemoving user doing the removing
	 * @param emailRemoved user being removed
	 * @return whether or not the query was successful
	 */
	public boolean removeFriend(String emailRemoving, String emailRemoved) {
		//check to make sure emailRemoving is even friends with emailRemoved
		if(!isFriend(emailRemoving, emailRemoved)) {
			return false;
		}
		
		PreparedStatement ps;
		String statement = "DELETE FROM " + Constants.FRIENDS_TABLE + "WHERE email1=? AND email2=?";
		try{
			ps = conn.prepareStatement(statement);
			ps.setString(1, emailRemoving);
			ps.setString(2, emailRemoved);
			ps.executeUpdate();
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Return list of friends of the given email
	 * @param email user id for user that list of friends relates to
	 * @return list of friends in CSV format, with five periods separating
	 * 			actual and pending friends
	 */
	public String listFriends(String email){
		
		ResultSet rs = null;
		String friendList = "";
		String pendingFriendList = "";
		try {
			String statement = "SELECT * FROM " + Constants.FRIENDS_TABLE + " WHERE email2=?;";
			PreparedStatement ps = conn.prepareStatement(statement);
			ps.setString(1, email);
			rs = ps.executeQuery();
			while(rs.next()){
				String friend = rs.getString("email1");
				
				// actual friend
				if(isFriend(friend, email)) {
					friendList += friend + ",";
				}
				
				// pending friend
				else {
					pendingFriendList += friend + ",";
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
            try{
            	if(rs != null){
            		rs.close();
            	}
            } catch (SQLException e){
            	e.printStackTrace();
            }
        }
		
		return friendList + "....." + pendingFriendList;
	}
}
