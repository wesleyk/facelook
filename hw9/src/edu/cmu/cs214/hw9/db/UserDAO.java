package edu.cmu.cs214.hw9.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Purpose: Database accessor for subscription related queries
 * @author Wesley, Jessica, Nikhil
 *
 */
public class UserDAO extends SQLiteAdapter {
	
	/**
	 * Constructor
	 * @param dbName name of the database that will be connected
	 * @throws Exception e
	 */
	public UserDAO(String dbName) throws Exception{
		super(dbName);
	}
	
	/**
	 * Return all the users
	 * @return list of users
	 */
	public ArrayList<User> allUsers(){
		String statement = "SELECT * FROM " + Constants.USERS_TABLE;
		ArrayList<User> ret = new ArrayList<User>();
		ResultSet rs = select(statement);
		
		try {
			while(rs.next()){
				ret.add(new User(rs.getString("email"), rs.getString("password"), rs.getString("name")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
            try{
                rs.close();
            } catch (SQLException e){
            }
        }
		return ret;
	}

	/**
	 * Return user based on username
	 * @param email email
	 * @return user associated to that username
	 */
	public User findUser(String email){
		User ret = null;
		ResultSet rs = null;
		try {
			String statement = "SELECT * FROM " + Constants.USERS_TABLE + " WHERE email=?;";
			PreparedStatement ps = conn.prepareStatement(statement);
			ps.setString(1, email);
			System.out.println(email + " in userDAO.java");
			rs = ps.executeQuery();
			if(!rs.isBeforeFirst()) {
				System.out.println("user not found in database (in User.DAO.java)");
				return null;
			}
			ret = new User(rs.getString("email"), rs.getString("password"), rs.getString("name"));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
            try{
            	if(rs != null){
            		rs.close();
            	}
            } catch (SQLException e){
            }
        }
		
		return ret;
	}
	
	/**
	 * Authenticate user based on email and passowrd
	 * @param email user email
	 * @param password user password
	 * @return whether or not the email and password link to the same user
	 */
	public boolean authenticateUser(String email, String password) {
		User lookup = findUser(email);
		if(lookup == null){
			return false;
		}
		
		return password.equals(lookup.getPassword());
	}
	
	/**
	 * Create a new user to the db
	 * @param email email
	 * @param password password
	 * @param name name
	 * @return whether or not the store was successful
	 */
	public boolean createUser(String email, String password, String name){
		User lookup = findUser(email);
		
		// user already exists!
		if(lookup != null){
			return false;
		}
		
		//User u = new User(email, password, andrewID, 0, false);
		PreparedStatement ps;
		String statement = "INSERT INTO " + Constants.USERS_TABLE + " (email, password, name) VALUES (?, ?, ?);";
		try{
			ps = conn.prepareStatement(statement);
			ps.setString(1, email);
			ps.setString(2, password);
			ps.setString(3, name);
			ps.executeUpdate();
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
		
		return true;
	}	
}
