package org.unibl.etf.beans;

import java.io.Serializable;
import java.util.ArrayList;

import org.unibl.etf.model.Message;
import org.unibl.etf.wrapper.WrapperMessage;

public class MessageBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ArrayList<Message> getAll() {
		return WrapperMessage.selectAll();	
	}
	
	public boolean save(Message m) {
		return WrapperMessage.insert(m) == 1 ? true : false;
	}
	
	public ArrayList<Message> getForUser(Integer sender, Integer receiver) {
		return WrapperMessage.selectForUser(sender, receiver);
	}
	
}
