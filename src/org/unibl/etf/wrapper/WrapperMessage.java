package org.unibl.etf.wrapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.unibl.etf.model.Message;
import org.unibl.etf.util.DBUtil;

public class WrapperMessage {
	
	private static final String SQL_SELECT_ALL = "SELECT * FROM message";
	private static final String SQL_INSERT = "INSERT INTO message (IdMessage, Content, From_IdUser, To_IdUser) VALUES (null, ?, ?, ?)";
	private static final String SQL_SELECT_FOR_USER = "SELECT * FROM message WHERE From_IdUser = ? AND To_IdUser = ?";
	
	public static ArrayList<Message> selectAll() {
		ArrayList<Message> retVal = new ArrayList<Message>();
		Connection c = null;
		Statement s = null;
		ResultSet rs = null;
		try {
			c = DBUtil.getConnection();
			s = c.createStatement();
			rs = s.executeQuery(SQL_SELECT_ALL);
			
			while(rs.next()) {
				retVal.add(new Message(rs.getInt("IdMessage"), rs.getString("Content"), rs.getInt("From_IdUser"), rs.getInt("To_IdUser")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs, s, c);
		}
		
		return retVal;
 	}
	
	public static int insert(Message m) {
		int retVal = 0;
		
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			c = DBUtil.getConnection();
			ps = c.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, m.getContent());
			ps.setInt(2, m.getFrom_idUser());
			ps.setInt(3, m.getTo_idUser());
			retVal = ps.executeUpdate();
			if(retVal != 0) {
				rs = ps.getGeneratedKeys();
				if(rs.next())
					m.setIdMessage(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs, ps, c);
		}
		
		return retVal;
	}
	
	public static ArrayList<Message> selectForUser(Integer sender, Integer receiver) {
		ArrayList<Message> retVal = new ArrayList<Message>();
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			c = DBUtil.getConnection();
			Object values[] = {sender, receiver};
			ps = DBUtil.prepareStatement(c, SQL_SELECT_FOR_USER, false, values);
			rs = ps.executeQuery();
			while(rs.next()) {
				retVal.add(new Message(rs.getInt("IdMessage"), rs.getString("Content"), rs.getInt("From_IdUser"), rs.getInt("To_IdUser")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs, ps, c);
		}
		
		return retVal;
	}

}
