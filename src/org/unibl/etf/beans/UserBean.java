package org.unibl.etf.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.unibl.etf.chatApp.MailSendingClass;
import org.unibl.etf.model.User;
import org.unibl.etf.wrapper.WrapperUser;

public class UserBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user = new User();
	private boolean isLoggedIn = false;
	public static ArrayList<User> loggedUsers = new ArrayList<User>();
	public static int token;
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public boolean login(String username, String password) {
		if((user = WrapperUser.selectByUsernameAndPassword(username, password)) != null) {
			isLoggedIn = true;
			loggedUsers.add(user);
			token = (int) Math.floor(Math.random()*(9999-1000+1) + 1000);
			MailSendingClass.sendTokenWithMail(user.getEmail(), token); //ovo otkomentarisati
			return true;
		}
		return false;
	}
	
	public List<User> getAll() {
		List<User> retVal = new ArrayList<User>();
		retVal = WrapperUser.selectAll();
		return retVal;
	}
	
	public boolean isLoggedIn() {
		return isLoggedIn;
	}
	
	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
	
	public void logout() {
		user = new User();
		isLoggedIn = false;
	}
	
	public User getUser() {
		return user;
	}
	
	public boolean isUserNameAllowed(String username) {
		return WrapperUser.isUsernameUsed(username);
	}
	
	public boolean isEmailAllowed(String email) {
		return WrapperUser.isEmailUsed(email);
	}
	
	public boolean add(User u) {
		return WrapperUser.insert(u) == 1 ? true : false;
	}
	
	public User getUserByUserName(String username, String password) {
		return WrapperUser.selectByUsernameAndPassword(username, password);
	}
	
	public static String getUsersEmail(String username) {
		return WrapperUser.getUserEmail(username);
	}
	
	public static String getEmailById(int id) {
		return WrapperUser.getUserEmailById(id);
	}

}
