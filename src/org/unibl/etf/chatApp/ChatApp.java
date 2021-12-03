package org.unibl.etf.chatApp;

public class ChatApp {

	public static void main(String[] args) {
	
		if(DigitalCertificates.generateRoot()) {
			System.out.println("Root certificate is generated!");
		} else {
			System.out.println("Unsuccessful!");
		}
	}
}