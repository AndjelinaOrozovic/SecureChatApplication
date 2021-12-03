package org.unibl.etf.model;

import java.io.Serializable;

public class User implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idUser;
	private String name;
	private String surname;
	private String password;
	private String username;
	private String email;
	
	public User() {
		super();
	}

	public User(String name, String surname, String username,  String password, String email) {
		super();
		this.name = name;
		this.surname = surname;
		this.password = password;
		this.username = username;
		this.email = email;
	}

	public User(Integer idUser, String name, String surname, String username, String password, String email) {
		super();
		this.idUser = idUser;
		this.name = name;
		this.surname = surname;
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public Integer getIdUser() {
		return idUser;
	}

	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "User [idUser=" + idUser + ", Name=" + name + ", Surname=" + surname + ", Password=" + password 	+ 
				", Username=" + username + ", email=" + email + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idUser == null) ? 0 : idUser.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (idUser == null) {
			if (other.idUser != null)
				return false;
		} else if (!idUser.equals(other.idUser))
			return false;
		return true;
	}
	
}
