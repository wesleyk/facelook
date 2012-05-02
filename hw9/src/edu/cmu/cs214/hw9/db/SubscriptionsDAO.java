package edu.cmu.cs214.hw9.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SubscriptionsDAO extends SQLiteAdapter {
	public SubscriptionsDAO() throws Exception{
		super();
	}

	/**
	 * Purpose: determine if email1 is subscribed to email2
	 * @param email1 Friend source
	 * @param email2 Friend destination
	 * @return Whether or not email1 is friends with email2
	 */
	public boolean isSubscribed(String email1, String email2) {
		PreparedStatement ps;
		ResultSet rs = null;
		String statement = "SELECT * FROM " + Constants.SUBSCRIPTIONS_TABLE + " WHERE email1=? AND email2=?;";
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
	 * Add subscription to emailSubscribed for emailSubscriber
	 * @param emailSubscriber user doing the subscribing
	 * @param emailSubscribed user being subscribed
	 * @return whether or not the request was succesful
	 */
	public boolean addSubscription(String emailSubscriber, String emailSubscribed) {
		//check if emailSubscriber is already subscribed to emailSubscribed
		if(isSubscribed(emailSubscriber, emailSubscribed)) {
			return false;
		}
		
		PreparedStatement ps;
		String statement = "INSERT INTO " + Constants.SUBSCRIPTIONS_TABLE + " (email1, email2) VALUES (?, ?)";
		try{
			ps = conn.prepareStatement(statement);
			ps.setString(1, emailSubscriber);
			ps.setString(2, emailSubscribed);
			ps.executeUpdate();
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * emailRemoving is removing emailRemoved as a subscription
	 * @param emailRemoving user doing the removing
	 * @param emailRemoved user being removed
	 * @return whether or not the query was successful
	 */
	public boolean removeSubscription(String emailRemoving, String emailRemoved) {
		//check to make sure emailRemoving is even subscribed to emailRemoved
		if(!isSubscribed(emailRemoving, emailRemoved)) {
			return false;
		}
		
		PreparedStatement ps;
		String statement = "DELETE FROM " + Constants.SUBSCRIPTIONS_TABLE + "WHERE email1=? AND email2=?";
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
	 * Return list of subscriptions of the given email
	 * @param email user id for user that list of subscriptions relates to
	 * @return list of subscriptions in CSV format
	 */
	public String listSubscriptions(String email){
		
		ResultSet rs = null;
		String subscriptionList = "";
		try {
			String statement = "SELECT * FROM " + Constants.SUBSCRIPTIONS_TABLE + " WHERE email1=?;";
			PreparedStatement ps = conn.prepareStatement(statement);
			ps.setString(1, email);
			rs = ps.executeQuery();
			while(rs.next()){
				subscriptionList += rs.getString("email2") + ",";
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
		
		return subscriptionList;
	}
}
