package edu.cmu.cs214.hw9.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAO extends SQLiteAdapter {
	public UserDAO() throws Exception{
		super();
	}
	
	public ArrayList<User> allUsers(){
		String statement = "SELECT * FROM " + Constants.USERS_TABLE;
		ArrayList<User> ret = new ArrayList<User>();
		ResultSet rs = select(statement);
		
		try {
			while(rs.next()){
				ret.add(new User(rs.getInt("id"), rs.getString("email"), rs.getString("password"), rs.getString("name")));
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
			ret = new User(rs.getInt("id"), rs.getString("email"), rs.getString("password"), rs.getString("name"));
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
	
	public boolean authenticateUser(String email, String password) {
		User lookup = findUser(email);
		if(lookup == null){
			return false;
		}
		
		return password.equals(lookup.getPassword());
	}
	
	public boolean createUser(String email, String password, String name){
		User lookup = findUser(email);
		
		// user already exists!
		if(lookup != null){
			return false;
		}
		
		//User u = new User(email, password, andrewID, 0, false);
		PreparedStatement ps;
		String statement = "INSERT INTO " + Constants.USERS_TABLE + " (email, password, name) VALUES (?, ?, ?)";
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
