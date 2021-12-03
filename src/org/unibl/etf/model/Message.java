package org.unibl.etf.model;

import java.io.Serializable;

public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idMessage;
	private String content;
	private Integer from_idUser;
	private Integer to_idUser;
	
	public Message() {
		super();
	}

	public Message(String content, Integer from_idUser, Integer to_idUser) {
		super();
		this.content = content;
		this.from_idUser = from_idUser;
		this.to_idUser = to_idUser;
	}
	
	public Message(Integer idMessage, String content, Integer from_idUser, Integer to_idUser) {
		super();
		this.idMessage = idMessage;
		this.content = content;
		this.from_idUser = from_idUser;
		this.to_idUser = to_idUser;
	}
	
	public Integer getIdMessage() {
		return idMessage;
	}

	public void setIdMessage(Integer idMessage) {
		this.idMessage = idMessage;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getFrom_idUser() {
		return from_idUser;
	}

	public void setFrom_idUser(Integer from_idUser) {
		this.from_idUser = from_idUser;
	}

	public Integer getTo_idUser() {
		return to_idUser;
	}

	public void setTo_idUser(Integer to_idUser) {
		this.to_idUser = to_idUser;
	}

	@Override
	public String toString() {
		return "Message [idMessage=" + idMessage + ", content=" + content + ", from_idUser=" + from_idUser
				+ ", to_idUser=" + to_idUser + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((from_idUser == null) ? 0 : from_idUser.hashCode());
		result = prime * result + ((idMessage == null) ? 0 : idMessage.hashCode());
		result = prime * result + ((to_idUser == null) ? 0 : to_idUser.hashCode());
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
		Message other = (Message) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (from_idUser == null) {
			if (other.from_idUser != null)
				return false;
		} else if (!from_idUser.equals(other.from_idUser))
			return false;
		if (idMessage == null) {
			if (other.idMessage != null)
				return false;
		} else if (!idMessage.equals(other.idMessage))
			return false;
		if (to_idUser == null) {
			if (other.to_idUser != null)
				return false;
		} else if (!to_idUser.equals(other.to_idUser))
			return false;
		return true;
	}
	
}
