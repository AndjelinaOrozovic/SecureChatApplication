package org.unibl.etf.wrapper;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.unibl.etf.model.User;
import org.unibl.etf.util.DBUtil;

public class WrapperUser {
	
	private static final String SQL_SELECT_ALL = "SELECT * FROM user";
	private static final String SQL_INSERT = "INSERT INTO user (IdUser, Name, Surname, Username, Password, email) VALUES (null, ?, ?, ?, ?, ?)";
	private static final String SQL_SELECT_BY_USERNAME_AND_PASSWORD = "SELECT * FROM user WHERE Username=? AND Password=?";
	private static final String SQL_SELECT_USER_BY_USERNAME = "SELECT * FROM user WHERE Username = ?";
	private static final String SQL_SELECT_USER_BY_EMAIL = "SELECT * FROM user WHERE email = ?";
	private static final String SQL_SELECT_EMAIL_BY_USERNAME = "SELECT email FROM user WHERE Username = ?";
	private static final String SQL_SELECT_EMAIL_BY_ID = "SELECT email FROM user WHERE IdUser = ?";
	
	public static List<User> selectAll() {
		
		List<User> retVal = new ArrayList<User>();
		Connection c = null;
		Statement s = null;
		ResultSet rs = null;
		try {
			c = DBUtil.getConnection();
			s = c.createStatement();
			rs = s.executeQuery(SQL_SELECT_ALL);
			
			while(rs.next()) {
				retVal.add(new User(rs.getInt("IdUser"), rs.getString("Name"), rs.getString("Surname"), rs.getString("Username"), rs.getString("Password"), rs.getString("email")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs, s, c);
		}
		
		return retVal;
	}
	
	public static User selectByUsernameAndPassword(String username, String password) {
		User user = null;
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			c = DBUtil.getConnection();
			Object values[] = {username, sha256(password)};
			ps = DBUtil.prepareStatement(c, SQL_SELECT_BY_USERNAME_AND_PASSWORD, false, values);
			rs = ps.executeQuery();
			if(rs.next()) {
				user = new User(rs.getInt("IdUser"), rs.getString("Name"), rs.getString("Surname"), rs.getString("Username"), rs.getString("Password"), rs.getString("email"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs, ps, c);
		}
		
		return user;
	}
	
	public static boolean isUsernameUsed(String username) {
		boolean result = true;
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			c = DBUtil.getConnection();
			Object values[] = {username};
			ps = DBUtil.prepareStatement(c, SQL_SELECT_USER_BY_USERNAME, false, values);
			rs = ps.executeQuery();
			if(rs.next()) {
				result = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs, ps, c);
		}
		
		return result;
	}
	
	public static boolean isEmailUsed(String email) {
		boolean result = true;
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			c = DBUtil.getConnection();
			Object values[] = {email};
			ps = DBUtil.prepareStatement(c, SQL_SELECT_USER_BY_EMAIL, false, values);
			rs = ps.executeQuery();
			if(rs.next()) {
				result = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs, ps, c);
		}
		
		return result;
	}
	
	public static String getUserEmail(String username) {
		String result = null;
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			c = DBUtil.getConnection();
			Object values[] = {username};
			ps = DBUtil.prepareStatement(c, SQL_SELECT_EMAIL_BY_USERNAME, false, values);
			rs = ps.executeQuery();
			if(rs.next()) {
				result = rs.getString("email");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs, ps, c);
		}
		
		return result;
	}
	
	public static String getUserEmailById(int id) {
		String result = null;
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			c = DBUtil.getConnection();
			Object values[] = {id};
			ps = DBUtil.prepareStatement(c, SQL_SELECT_EMAIL_BY_ID, false, values);
			rs = ps.executeQuery();
			if(rs.next()) {
				result = rs.getString("email");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs, ps, c);
		}
		
		return result;
	}
	
	public static int insert(User u) {
		int retVal = 0;
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			c = DBUtil.getConnection();
			Object values[] = { u.getName(), u.getSurname(), u.getUsername(), sha256(u.getPassword()), u.getEmail() };
			ps = DBUtil.prepareStatement(c, SQL_INSERT, true, values);
			retVal = ps.executeUpdate();
			if(retVal != 0) {
				rs =ps.getGeneratedKeys();
				if(rs.next())
					u.setIdUser(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs, ps, c);
		}
		
		return retVal;
	}
	
	public static String sha256(String base) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(base.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
