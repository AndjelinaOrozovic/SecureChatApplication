package org.unibl.etf.chatApp;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSendingClass {

	private static String username;
	private static String password;

	public MailSendingClass() {
		super();
	}

	public static void sendTokenWithMail(String emailOfUser, int token) {

		ResourceBundle bundle = PropertyResourceBundle.getBundle("org.unibl.etf.chatApp.credentialsForMail");
		username = bundle.getString("username");
		password = bundle.getString("password");
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username, "SecureChatApp"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailOfUser));
			message.setSubject("Token");
			message.setText(String.valueOf(token));

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendWarningWithMail(String emailOfUser, String warningMessage) {

		ResourceBundle bundle = PropertyResourceBundle.getBundle("org.unibl.etf.chatApp.credentialsForMail");
		username = bundle.getString("username");
		password = bundle.getString("password");
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username, "SecureChatApp"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailOfUser));
			message.setSubject("Token");
			message.setText(String.valueOf(warningMessage));

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static void sendCertificateWithMail(String emailOfUser, String certName, String certPath) {

		ResourceBundle bundle = PropertyResourceBundle.getBundle("org.unibl.etf.chatApp.credentialsForMail");
		username = bundle.getString("username");
		password = bundle.getString("password");
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username, "SecureChatApp"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailOfUser));
			message.setSubject("Digitalni sertifikat");
			
			BodyPart messageBodyPart1 = new MimeBodyPart();  
		    messageBodyPart1.setText("Ovo je Vas digitalni sertifikat za pristup aplikaciji ChatApplication!");  
		    
		    MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
		   
		    String filename = certPath;
		    
		    DataSource source = new FileDataSource(filename);  
		    messageBodyPart2.setDataHandler(new DataHandler(source));
		    messageBodyPart2.setFileName(certName);  
		   
		    Multipart multipart = new MimeMultipart();  
		    multipart.addBodyPart(messageBodyPart1);  
		    multipart.addBodyPart(messageBodyPart2);  
		   
		    message.setContent(multipart ); 
		    
		    Transport.send(message);  
		   
		    System.out.println("Done");

		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
